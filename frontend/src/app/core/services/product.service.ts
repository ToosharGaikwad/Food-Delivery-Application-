import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Product } from '../../models';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly base = `${environment.apiUrl}/api/products`;

  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<Product[]>(this.base);
  }

  getByRestaurant(restaurantId: number) {
    return this.http.get<Product[]>(`${this.base}/${restaurantId}`);
  }

  create(product: Partial<Product>) {
    return this.http.post<Product>(`${this.base}/add`, product);
  }

  delete(id: number) {
    return this.http.delete<{ message: string }>(`${this.base}/${id}`);
  }
}
