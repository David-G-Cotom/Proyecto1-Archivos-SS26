import { Injectable } from '@angular/core';
import { NombreRol } from '../models/nombre-rol';

@Injectable({
  providedIn: 'root',
})
export class RoleService {

  private readonly RUTAS_PERMITIDAS_POR_ROL: Record<Exclude<NombreRol, NombreRol.ADMINISTRACION>, string[]> = {
    [NombreRol.COMPRAS]: ['dashboard', 'proveedores', 'compras'],
    [NombreRol.INVENTARIO]: ['dashboard', 'productos'],
    [NombreRol.VENTAS]: ['dashboard', 'clientes', 'ventas'],
  };

  public rolPuedeAcceder(rol: NombreRol, ruta: string): boolean {
    if (rol === NombreRol.ADMINISTRACION) {
      return true;
    }
    return this.RUTAS_PERMITIDAS_POR_ROL[rol].includes(ruta);
  }

}
