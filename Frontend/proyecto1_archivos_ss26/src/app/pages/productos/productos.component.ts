import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { Producto } from '../../models/producto.model';
import { forkJoin } from 'rxjs';
import { InventarioService } from '../../services/inventario.service';
import { CategoriaService } from '../../services/categoria.service';
import { ProductoService } from '../../services/producto.service';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Categoria } from '../../models/categoria.model';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'app-productos',
  imports: [CurrencyPipe, ReactiveFormsModule],
  templateUrl: './productos.component.html',
  styleUrl: './productos.component.css',
})
export class ProductosComponent implements OnInit {

  protected readonly productos = signal<Producto[]>([]);
  protected readonly categorias = signal<Categoria[]>([]);
  protected readonly stockPorProducto = signal<Map<number, number>>(new Map());

  protected readonly cargando = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly mostrarSoloActivos = signal(true);
  protected readonly mostrandoFormulario = signal(false);
  protected readonly editandoId = signal<number | null>(null);

  protected readonly productosFiltrados = computed(() => {
    const lista = this.productos();
    if (!this.mostrarSoloActivos()) {
      return lista;
    }
    const filtrados = [];
    for (const item of lista) {
      if (item.activo) {
        filtrados.push(item);
      }
    }
    return filtrados;
  });

  private readonly fb = inject(FormBuilder);
  private readonly productoService = inject(ProductoService);
  private readonly categoriaService = inject(CategoriaService)
  private readonly inventarioService = inject(InventarioService);

  protected readonly formulario = this.fb.group({
    idCategoria: [null as number | null, [Validators.required]],
    nombre: ['', [Validators.required]],
    descripcion: [''],
    precioVenta: [0, [Validators.required, Validators.min(0)]],
    stockMinimo: [0, [Validators.required, Validators.min(0)]],
  });

  ngOnInit(): void {
    this.cargarDatos();
  }

  private cargarDatos(): void {
    this.cargando.set(true);
    this.error.set(null);

    forkJoin({
      productos: this.productoService.listar(),
      categorias: this.categoriaService.listar(),
      stock: this.inventarioService.listarStockActual(),
    }).subscribe({
      next: ({ productos, categorias, stock }) => {
        this.productos.set(productos);
        this.categorias.set(categorias);
        const mapaStock = new Map<number, number>();
        for (const s of stock) {
          mapaStock.set(s.idProducto, s.stockActual);
        }
        this.stockPorProducto.set(mapaStock);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo conectar con el servidor. Intentalo mas tarde.');
        this.cargando.set(false);
      },
    });
  }

  protected getStock(idProducto: number): number | null {
    return this.stockPorProducto().get(idProducto) ?? null;
  }

  protected abrirFormularioCrear(): void {
    this.editandoId.set(null);
    this.formulario.reset({ idCategoria: null, nombre: '', descripcion: '', precioVenta: 0, stockMinimo: 0 });
    this.mostrandoFormulario.set(true);
  }

  protected abrirFormularioEditar(producto: Producto): void {
    this.editandoId.set(producto.idProducto);
    this.formulario.setValue({
      idCategoria: producto.idCategoria,
      nombre: producto.nombre,
      descripcion: producto.descripcion ?? '',
      precioVenta: producto.precioVenta,
      stockMinimo: producto.stockMinimo,
    });
    this.mostrandoFormulario.set(true);
  }

  protected cancelarFormulario(): void {
    this.mostrandoFormulario.set(false);
    this.editandoId.set(null);
  }

  protected guardar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const valores = this.formulario.getRawValue();
    const request = {
      idCategoria: valores.idCategoria!,
      nombre: valores.nombre!,
      descripcion: valores.descripcion,
      precioVenta: valores.precioVenta!,
      stockMinimo: valores.stockMinimo!,
    };

    const idActual = this.editandoId();
    const peticion = idActual
      ? this.productoService.actualizar(idActual, request)
      : this.productoService.crear(request);

    peticion.subscribe({
      next: () => {
        this.mostrandoFormulario.set(false);
        this.cargarDatos();
      },
      error: () => this.error.set('No se pudo guardar el producto. Revisa los datos e intenta de nuevo.'),
    });
  }

  protected desactivar(producto: Producto): void {
    this.productoService.desactivar(producto.idProducto).subscribe({
      next: () => this.cargarDatos(),
      error: () => this.error.set('No se pudo desactivar el producto.'),
    });
  }

  protected reactivar(producto: Producto): void {
    this.productoService.reactivar(producto.idProducto).subscribe({
      next: () => this.cargarDatos(),
      error: () => this.error.set('No se pudo reactivar el producto.'),
    });
  }

}
