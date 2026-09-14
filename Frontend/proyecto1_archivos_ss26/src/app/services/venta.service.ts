import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';
import { VentaRequest, VentaResponse } from '../models/venta.model';

@Injectable({
  providedIn: 'root',
})
export class VentaService {

  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api/ventas`;

  listar(): Observable<VentaResponse[]> {
    return this.http.get<VentaResponse[]>(this.url);
  }

  listarPorCliente(idCliente: number): Observable<VentaResponse[]> {
    return this.http.get<VentaResponse[]>(`${this.url}/cliente/${idCliente}`);
  }

  registrar(request: VentaRequest): Observable<VentaResponse> {
    return this.http.post<VentaResponse>(this.url, request);
  }

}
