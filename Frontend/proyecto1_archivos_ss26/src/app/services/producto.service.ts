import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';
import { Producto, ProductoRequest } from '../models/producto.model';

@Injectable({
  providedIn: 'root',
})
export class ProductoService {

  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api/productos`;

  listar(soloActivos = false): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.url, { params: { soloActivos } });
  }

  obtenerPorId(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.url}/${id}`);
  }

  crear(request: ProductoRequest): Observable<Producto> {
    return this.http.post<Producto>(this.url, request);
  }

  actualizar(id: number, request: ProductoRequest): Observable<Producto> {
    return this.http.put<Producto>(`${this.url}/${id}`, request);
  }

  desactivar(id: number): Observable<Producto> {
    return this.http.patch<Producto>(`${this.url}/${id}/desactivar`, {});
  }

  reactivar(id: number): Observable<Producto> {
    return this.http.patch<Producto>(`${this.url}/${id}/reactivar`, {});
  }

}
