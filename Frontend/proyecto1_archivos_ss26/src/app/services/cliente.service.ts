import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';
import { Cliente, ClienteRequest } from '../models/cliente.model';

@Injectable({
  providedIn: 'root',
})
export class ClienteService {

  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api/clientes`;

  listar(soloActivos = false): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.url, { params: { soloActivos } });
  }

  crear(request: ClienteRequest): Observable<Cliente> {
    return this.http.post<Cliente>(this.url, request);
  }

  actualizar(id: number, request: ClienteRequest): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.url}/${id}`, request);
  }

  desactivar(id: number): Observable<Cliente> {
    return this.http.patch<Cliente>(`${this.url}/${id}/desactivar`, {});
  }

  reactivar(id: number): Observable<Cliente> {
    return this.http.patch<Cliente>(`${this.url}/${id}/reactivar`, {});
  }

}
