import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { Cliente } from '../../models/cliente.model';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { VentaResponse } from '../../models/venta.model';
import { ClienteService } from '../../services/cliente.service';
import { VentaService } from '../../services/venta.service';
import { CurrencyPipe, DatePipe } from '@angular/common';

@Component({
  selector: 'app-clientes',
  imports: [CurrencyPipe, DatePipe, ReactiveFormsModule],
  templateUrl: './clientes.component.html',
  styleUrl: './clientes.component.css',
})
export class ClientesComponent implements OnInit {

  protected readonly clientes = signal<Cliente[]>([]);
  protected readonly cargando = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly mostrarSoloActivos = signal(true);
  protected readonly mostrandoFormulario = signal(false);
  protected readonly editandoId = signal<number | null>(null);

  protected readonly clienteHistorial = signal<Cliente | null>(null);
  protected readonly ventasDelCliente = signal<VentaResponse[]>([]);
  protected readonly cargandoHistorial = signal(false);

  protected readonly clientesFiltrados = computed(() => {
    const lista = this.clientes();
    if (!this.mostrarSoloActivos()) {
      return lista;
    }
    const filtrados: Cliente[] = [];
    for (const element of lista) {
      if (element.activo) {
        filtrados.push(element);
      }
    }
    return filtrados;
  });

  private readonly fb = inject(FormBuilder);
  private readonly clienteService = inject(ClienteService)
  private readonly ventaService = inject(VentaService);

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
    this.clienteService.listar().subscribe({
      next: (clientes) => {
        this.clientes.set(clientes);
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

  protected abrirFormularioEditar(cliente: Cliente): void {
    this.editandoId.set(cliente.idCliente);
    this.formulario.setValue({
      nombre: cliente.nombre,
      nit: cliente.nit ?? '',
      telefono: cliente.telefono ?? '',
      email: cliente.email ?? '',
      direccion: cliente.direccion ?? '',
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
      nit: valores.nit || null,
      telefono: valores.telefono || null,
      email: valores.email || null,
      direccion: valores.direccion || null,
    };

    const idActual = this.editandoId();
    const peticion = idActual
      ? this.clienteService.actualizar(idActual, request)
      : this.clienteService.crear(request);

    peticion.subscribe({
      next: () => {
        this.mostrandoFormulario.set(false);
        this.cargarDatos();
      },
      error: () => this.error.set('No se pudo guardar el cliente. Revisa los datos e intenta de nuevo.'),
    });
  }

  protected desactivar(cliente: Cliente): void {
    this.clienteService.desactivar(cliente.idCliente).subscribe({
      next: () => this.cargarDatos(),
      error: () => this.error.set('No se pudo desactivar el cliente.'),
    });
  }

  protected reactivar(cliente: Cliente): void {
    this.clienteService.reactivar(cliente.idCliente).subscribe({
      next: () => this.cargarDatos(),
      error: () => this.error.set('No se pudo reactivar el cliente.'),
    });
  }

  protected verHistorial(cliente: Cliente): void {
    this.clienteHistorial.set(cliente);
    this.cargandoHistorial.set(true);
    this.ventaService.listarPorCliente(cliente.idCliente).subscribe({
      next: (ventas) => {
        this.ventasDelCliente.set(ventas);
        this.cargandoHistorial.set(false);
      },
      error: () => {
        this.error.set('No se pudo cargar el historial de ventas de este cliente.');
        this.cargandoHistorial.set(false);
      },
    });
  }

  protected cerrarHistorial(): void {
    this.clienteHistorial.set(null);
    this.ventasDelCliente.set([]);
  }

}
