import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';
import { CompraRequest, CompraResponse } from '../models/compra.model';

@Injectable({
  providedIn: 'root',
})
export class CompraService {

  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api/compras`;

  listar(): Observable<CompraResponse[]> {
    return this.http.get<CompraResponse[]>(this.url);
  }

  registrar(request: CompraRequest): Observable<CompraResponse> {
    return this.http.post<CompraResponse>(this.url, request);
  }

}
