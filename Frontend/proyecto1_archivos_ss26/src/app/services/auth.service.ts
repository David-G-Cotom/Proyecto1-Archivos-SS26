import { computed, Injectable, signal } from '@angular/core';
import { UsuarioSesion } from '../models/usuario-sesion.model';
import { NombreRol } from '../models/nombre-rol';

const CLAVE_STORAGE = 'mini-erp-sesion';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private readonly usuariosSemilla: Record<string, Omit<UsuarioSesion, 'username'>> = {
    admin1: { nombreCompleto: 'Ana Administracion', rol: NombreRol.ADMINISTRACION },
    compras1: { nombreCompleto: 'Carlos Compras', rol: NombreRol.COMPRAS },
    inventario1: { nombreCompleto: 'Ines Inventario', rol: NombreRol.INVENTARIO },
    ventas1: { nombreCompleto: 'Vera Ventas', rol: NombreRol.VENTAS },
  };

  private readonly usuarioSesion = signal<UsuarioSesion | null>(this.leerSesionGuardada());

  public readonly usuarioActual = this.usuarioSesion.asReadonly();
  public readonly estaAutenticado = computed(() => this.usuarioSesion() !== null);

  public login(username: string, password: string): boolean {
    const datos = this.usuariosSemilla[username.trim().toLowerCase()];
    if (!datos || password.trim().length === 0) {
      return false;
    }
    const sesion: UsuarioSesion = { username: username.trim().toLowerCase(), ...datos };
    this.usuarioSesion.set(sesion);
    localStorage.setItem(CLAVE_STORAGE, JSON.stringify(sesion));
    return true;
  }

  public logout(): void {
    this.usuarioSesion.set(null);
    localStorage.removeItem(CLAVE_STORAGE);
  }

  private leerSesionGuardada(): UsuarioSesion | null {
    const usuarioGuardado = localStorage.getItem(CLAVE_STORAGE);
    if (!usuarioGuardado) {
      return null;
    }
    try {
      return JSON.parse(usuarioGuardado) as UsuarioSesion;
    } catch {
      return null;
    }
  }

}
