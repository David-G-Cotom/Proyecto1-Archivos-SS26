import { Component, inject, OnInit, signal } from '@angular/core';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { CompraResponse } from '../../models/compra.model';
import { Proveedor } from '../../models/proveedor.model';
import { Producto } from '../../models/producto.model';
import { CompraService } from '../../services/compra.service';
import { AuthService } from '../../services/auth.service';
import { ProductoService } from '../../services/producto.service';
import { ProveedorService } from '../../services/proveedor.service';
import { CurrencyPipe, DatePipe } from '@angular/common';

@Component({
  selector: 'app-compras',
  imports: [CurrencyPipe, DatePipe, ReactiveFormsModule],
  templateUrl: './compras.component.html',
  styleUrl: './compras.component.css',
})
export class ComprasComponent implements OnInit {

  protected readonly compras = signal<CompraResponse[]>([]);
  protected readonly proveedores = signal<Proveedor[]>([]);
  protected readonly productos = signal<Producto[]>([]);

  protected readonly cargando = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly mostrandoFormulario = signal(false);

  private readonly fb = inject(FormBuilder);
  private readonly compraService = inject(CompraService);
  private readonly proveedorService = inject(ProveedorService);
  private readonly productoService = inject(ProductoService)
  private readonly authService = inject(AuthService);

  protected readonly formulario = this.fb.group({
    idProveedor: [null as number | null, [Validators.required]],
    items: this.fb.array([this.crearLineaVacia()]),
  });

  ngOnInit(): void {
    this.cargarDatos();
  }

  private cargarDatos(): void {
    this.cargando.set(true);
    this.error.set(null);
    forkJoin({
      compras: this.compraService.listar(),
      proveedores: this.proveedorService.listar(true),
      productos: this.productoService.listar(true),
    }).subscribe({
      next: ({ compras, proveedores, productos }) => {
        const comprasOrdenadas: CompraResponse[] = [];
        for (const element of compras) {
          comprasOrdenadas.push(element);
        }
        // (Bubble Sort) de mayor a menor
        for (let i = 0; i < comprasOrdenadas.length - 1; i++) {
          for (let j = 0; j < comprasOrdenadas.length - 1 - i; j++) {
            if (comprasOrdenadas[j].fechaCompra.localeCompare(comprasOrdenadas[j + 1].fechaCompra) < 0) {
              const temporal = comprasOrdenadas[j];
              comprasOrdenadas[j] = comprasOrdenadas[j + 1];
              comprasOrdenadas[j + 1] = temporal;
            }
          }
        }
        // Mas reciente primero: mas util para revisar lo que se acaba de registrar.
        this.compras.set(comprasOrdenadas);
        this.proveedores.set(proveedores);
        this.productos.set(productos);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo conectar con el servidor. Intenta nuevamente mas tarde.');
        this.cargando.set(false);
      },
    });
  }

  private crearLineaVacia() {
    return this.fb.group({
      idProducto: [null as number | null, [Validators.required]],
      cantidad: [1, [Validators.required, Validators.min(1)]],
      precioUnitario: [0, [Validators.required, Validators.min(0)]],
    });
  }

  protected get items(): FormArray {
    return this.formulario.get('items') as FormArray;
  }

  protected agregarLinea(): void {
    this.items.push(this.crearLineaVacia());
  }

  protected quitarLinea(indice: number): void {
    if (this.items.length > 1) {
      this.items.removeAt(indice);
    }
  }

  protected totalCalculado(): number {
    return this.items.controls.reduce((acumulado, control) => {
      const { cantidad, precioUnitario } = control.value;
      return acumulado + (Number(cantidad) || 0) * (Number(precioUnitario) || 0);
    }, 0);
  }

  protected abrirFormulario(): void {
    this.formulario.reset({ idProveedor: null });
    this.items.clear();
    this.items.push(this.crearLineaVacia());
    this.mostrandoFormulario.set(true);
  }

  protected cancelarFormulario(): void {
    this.mostrandoFormulario.set(false);
  }

  protected registrar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    /**
     * Se vuelve a validar la sesion del usuario por si se acaba
     * su sesion para no enviar un username vacio al servidor.
     */
    const username = this.authService.usuarioActual()?.username;
    if (!username) {
      this.error.set('No hay una sesion activa. Vuelve a iniciar sesion.');
      return;
    }

    const valores = this.formulario.getRawValue();
    const itemsProcesados = [];
    for (const item of valores.items) {
      itemsProcesados.push({
        idProducto: item.idProducto!,
        cantidad: item.cantidad!,
        precioUnitario: item.precioUnitario!,
      });
    }
    const request = {
      idProveedor: valores.idProveedor!,
      username,
      items: itemsProcesados
    };

    this.compraService.registrar(request).subscribe({
      next: () => {
        this.mostrandoFormulario.set(false);
        this.cargarDatos();
      },
      error: (err) => {
        this.error.set(err?.error?.message ?? 'No se pudo registrar la compra.');
      },
    });
  }

}
