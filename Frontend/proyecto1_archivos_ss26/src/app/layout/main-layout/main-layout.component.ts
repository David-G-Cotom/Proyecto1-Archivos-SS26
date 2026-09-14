import { Component, computed, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { RoleService } from '../../services/role.service';

@Component({
  selector: 'app-main-layout',
  imports: [RouterOutlet, RouterLink],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.css',
})
export class MainLayoutComponent {

  protected readonly opcionesMenuFull = [
    { ruta: 'dashboard', etiqueta: 'Inicio' },
    { ruta: 'productos', etiqueta: 'Productos' },
    { ruta: 'proveedores', etiqueta: 'Proveedores' },
    { ruta: 'compras', etiqueta: 'Compras' },
    { ruta: 'clientes', etiqueta: 'Clientes' },
    { ruta: 'ventas', etiqueta: 'Ventas' },
  ];

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly roleService = inject(RoleService);

  protected readonly sidebarAbierto = signal(true);

  protected readonly usuarioActual = this.authService.usuarioActual;

  protected readonly opcionesMenu = computed(() => {
    const usuario = this.usuarioActual();
    if (!usuario) {
      return [];
    }
    const opcionesPermitidas = [];
    for (const opcion of this.opcionesMenuFull) {
      if (this.roleService.rolPuedeAcceder(usuario.rol, opcion.ruta)) {
        opcionesPermitidas.push(opcion);
      }
    }
    return opcionesPermitidas;
  });

  protected alternarSidebar(): void {
    this.sidebarAbierto.update((valor) => !valor);
  }

  protected cerrarSesion(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login');
  }

}
