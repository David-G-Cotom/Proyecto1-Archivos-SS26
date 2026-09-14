import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';
import { Proveedor, ProveedorRequest } from '../models/proveedor.model';

@Injectable({
  providedIn: 'root',
})
export class ProveedorService {

  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api/proveedores`;

  listar(soloActivos = false): Observable<Proveedor[]> {
    return this.http.get<Proveedor[]>(this.url, { params: { soloActivos } });
  }

  crear(request: ProveedorRequest): Observable<Proveedor> {
    return this.http.post<Proveedor>(this.url, request);
  }

  actualizar(id: number, request: ProveedorRequest): Observable<Proveedor> {
    return this.http.put<Proveedor>(`${this.url}/${id}`, request);
  }

  desactivar(id: number): Observable<Proveedor> {
    return this.http.patch<Proveedor>(`${this.url}/${id}/desactivar`, {});
  }

  reactivar(id: number): Observable<Proveedor> {
    return this.http.patch<Proveedor>(`${this.url}/${id}/reactivar`, {});
  }

}
