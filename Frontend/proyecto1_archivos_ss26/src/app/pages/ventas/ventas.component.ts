import { Component, inject, OnInit, signal } from '@angular/core';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { VentaResponse } from '../../models/venta.model';
import { Cliente } from '../../models/cliente.model';
import { Producto } from '../../models/producto.model';
import { VentaService } from '../../services/venta.service';
import { ClienteService } from '../../services/cliente.service';
import { ProductoService } from '../../services/producto.service';
import { AuthService } from '../../services/auth.service';
import { CurrencyPipe, DatePipe } from '@angular/common';

@Component({
  selector: 'app-ventas',
  imports: [CurrencyPipe, DatePipe, ReactiveFormsModule],
  templateUrl: './ventas.component.html',
  styleUrl: './ventas.component.css',
})
export class VentasComponent implements OnInit {

  protected readonly ventas = signal<VentaResponse[]>([]);
  protected readonly clientes = signal<Cliente[]>([]);
  protected readonly productos = signal<Producto[]>([]);

  protected readonly cargando = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly mostrandoFormulario = signal(false);

  private readonly fb = inject(FormBuilder);
  private readonly ventaService = inject(VentaService);
  private readonly clienteService = inject(ClienteService)
  private readonly productoService = inject(ProductoService);
  private readonly authService = inject(AuthService);

  protected readonly formulario = this.fb.group({
    idCliente: [null as number | null, [Validators.required]],
    items: this.fb.array([this.crearLineaVacia()]),
  });

  ngOnInit(): void {
    this.cargarDatos();
  }

  private cargarDatos(): void {
    this.cargando.set(true);
    this.error.set(null);
    forkJoin({
      ventas: this.ventaService.listar(),
      clientes: this.clienteService.listar(true),
      productos: this.productoService.listar(true),
    }).subscribe({
      next: ({ ventas, clientes, productos }) => {
        const ventasOrdenadas: VentaResponse[] = [];
        for (const element of ventas) {
          ventasOrdenadas.push(element);
        }
        // (Bubble Sort) de mayor a menor
        for (let i = 0; i < ventasOrdenadas.length - 1; i++) {
          for (let j = 0; j < ventasOrdenadas.length - 1 - i; j++) {
            if (ventasOrdenadas[j].fechaVenta.localeCompare(ventasOrdenadas[j + 1].fechaVenta) < 0) {
              const temporal = ventasOrdenadas[j];
              ventasOrdenadas[j] = ventasOrdenadas[j + 1];
              ventasOrdenadas[j + 1] = temporal;
            }
          }
        }
        // Mas reciente primero: mas util para revisar lo que se acaba de registrar.
        this.ventas.set(ventasOrdenadas);
        this.clientes.set(clientes);
        this.productos.set(productos);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo conectar con el servidor. Intentalo mas tarde.');
        this.cargando.set(false);
      },
    });
  }

  private crearLineaVacia() {
    return this.fb.group({
      idProducto: [null as number | null, [Validators.required]],
      cantidad: [1, [Validators.required, Validators.min(1)]],
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

  protected precioVentaProducto(idProducto: number | null): number {
    if (idProducto === null) {
      return 0;
    }
    for (const producto of this.productos()) {
      if (producto.idProducto === idProducto) {
        return producto.precioVenta;
      }
    }
    return 0;
  }

  protected subtotalEstimado(): number {
    let subtotal = 0;
    for (const control of this.items.controls) {
      const precioVenta = this.precioVentaProducto(control.value.idProducto);
      const cantidad = Number(control.value.cantidad) || 0;
      subtotal += precioVenta * cantidad;
    }
    return subtotal;
  }

  protected abrirFormulario(): void {
    this.formulario.reset({ idCliente: null });
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
      });
    }
    const request = {
      idCliente: valores.idCliente!,
      username,
      items: itemsProcesados
    };

    this.ventaService.registrar(request).subscribe({
      next: () => {
        this.mostrandoFormulario.set(false);
        this.cargarDatos();
      },
      error: (err) => {
        this.error.set(err?.error?.message ?? 'No se pudo registrar la venta.');
      },
    });
  }

}
