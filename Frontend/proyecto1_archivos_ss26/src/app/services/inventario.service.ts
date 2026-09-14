import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';
import { StockActual } from '../models/stock-actual.model';

@Injectable({
  providedIn: 'root',
})
export class InventarioService {

  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api`;

  listarStockActual(): Observable<StockActual[]> {
    return this.http.get<StockActual[]>(`${this.url}/inventario/stock`);
  }

}
