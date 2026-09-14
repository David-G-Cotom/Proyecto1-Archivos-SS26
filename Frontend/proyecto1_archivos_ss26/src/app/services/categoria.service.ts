import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Categoria } from '../models/categoria.model';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class CategoriaService {

  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api`;

  listar(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(`${this.url}/categorias`);
  }

}
