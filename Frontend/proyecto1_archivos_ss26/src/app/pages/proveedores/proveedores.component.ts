import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { Proveedor } from '../../models/proveedor.model';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProveedorService } from '../../services/proveedor.service';

@Component({
  selector: 'app-proveedores',
  imports: [ReactiveFormsModule],
  templateUrl: './proveedores.component.html',
  styleUrl: './proveedores.component.css',
})
export class ProveedoresComponent implements OnInit {

  protected readonly proveedores = signal<Proveedor[]>([]);
  protected readonly cargando = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly mostrarSoloActivos = signal(true);
  protected readonly mostrandoFormulario = signal(false);
  protected readonly editandoId = signal<number | null>(null);

  protected readonly proveedoresFiltrados = computed(() => {
    const lista = this.proveedores();
    if (!this.mostrarSoloActivos()) {
      return lista;
    }
    const filtrados = []
    for (const element of lista) {
      if (element.activo) {
        filtrados.push(element);
      }
    }
    return filtrados;
  });

  private readonly fb = inject(FormBuilder);
  private readonly proveedorService = inject(ProveedorService);

  protected readonly formulario = this.fb.group({
    nombre: ['', [Validators.required]],
    nit: [''],
    telefono: [''],
    email: ['', [Validators.email]],
    direccion: [''],
  });

  ngOnInit(): void {
    this.cargarDatos();
  }

  private cargarDatos(): void {
    this.cargando.set(true);
    this.error.set(null);
    this.proveedorService.listar().subscribe({
      next: (proveedores) => {
        this.proveedores.set(proveedores);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set('No se pudo conectar con el servidor. Intenta de nuevo más tarde.');
        this.cargando.set(false);
      },
    });
  }

  protected abrirFormularioCrear(): void {
    this.editandoId.set(null);
    this.formulario.reset({ nombre: '', nit: '', telefono: '', email: '', direccion: '' });
    this.mostrandoFormulario.set(true);
  }

  protected abrirFormularioEditar(proveedor: Proveedor): void {
    this.editandoId.set(proveedor.idProveedor);
    this.formulario.setValue({
      nombre: proveedor.nombre,
      nit: proveedor.nit ?? '',
      telefono: proveedor.telefono ?? '',
      email: proveedor.email ?? '',
      direccion: proveedor.direccion ?? '',
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
      nombre: valores.nombre!,
      nit: valores.nit,
      telefono: valores.telefono,
      email: valores.email,
      direccion: valores.direccion,
    };

    const idActual = this.editandoId();
    const peticion = idActual
      ? this.proveedorService.actualizar(idActual, request)
      : this.proveedorService.crear(request);

    peticion.subscribe({
      next: () => {
        this.mostrandoFormulario.set(false);
        this.cargarDatos();
      },
      error: () => this.error.set('No se pudo guardar el proveedor. Revisa los datos e intenta de nuevo.'),
    });
  }

  protected desactivar(proveedor: Proveedor): void {
    this.proveedorService.desactivar(proveedor.idProveedor).subscribe({
      next: () => this.cargarDatos(),
      error: () => this.error.set('No se pudo desactivar el proveedor.'),
    });
  }

  protected reactivar(proveedor: Proveedor): void {
    this.proveedorService.reactivar(proveedor.idProveedor).subscribe({
      next: () => this.cargarDatos(),
      error: () => this.error.set('No se pudo reactivar el proveedor.'),
    });
  }

}
