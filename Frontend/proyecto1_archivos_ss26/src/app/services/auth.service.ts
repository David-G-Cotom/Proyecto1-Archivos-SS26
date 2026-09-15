import { computed, inject, Injectable, signal } from '@angular/core';
import { UsuarioSesion } from '../models/usuario-sesion.model';
import { NombreRol } from '../models/nombre-rol';
import { Observable, tap } from 'rxjs';
import { LoginResponse, SesionGuardada } from '../models/auth.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment.development';

const CLAVE_STORAGE = 'mini-erp-sesion';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private readonly sesionGuardada = this.leerSesionGuardada();

  private readonly usuarioSesion = signal<UsuarioSesion | null>(this.sesionGuardada?.usuario ?? null);
  private readonly token = signal<string | null>(this.sesionGuardada?.token ?? null);

  public readonly usuarioActual = this.usuarioSesion.asReadonly();

  private readonly http = inject(HttpClient);
  private url = `${environment.apiUrl}/api`;

  public estaAutenticado(): boolean {
    return this.usuarioSesion() !== null;
  }

  public getToken(): string | null {
    return this.token();
  }

  public login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.url}/auth/login`, { username, password }).pipe(
      tap((respuesta) => this.guardarSesion(respuesta)),
    );
  }

  public logout(): void {
    this.usuarioSesion.set(null);
    this.token.set(null);
    localStorage.removeItem(CLAVE_STORAGE);
  }

  private guardarSesion(respuesta: LoginResponse): void {
    const usuario: UsuarioSesion = {
      username: respuesta.username,
      nombreCompleto: respuesta.nombreCompleto,
      rol: respuesta.rol as NombreRol,
    };

    this.usuarioSesion.set(usuario);
    this.token.set(respuesta.token);

    const guardarStorage: SesionGuardada = { token: respuesta.token, usuario };
    localStorage.setItem(CLAVE_STORAGE, JSON.stringify(guardarStorage));
  }

  private leerSesionGuardada(): SesionGuardada | null {
    const usuarioGuardado = localStorage.getItem(CLAVE_STORAGE);
    if (!usuarioGuardado) {
      return null;
    }
    try {
      return JSON.parse(usuarioGuardado) as SesionGuardada;
    } catch {
      return null;
    }
  }

}
