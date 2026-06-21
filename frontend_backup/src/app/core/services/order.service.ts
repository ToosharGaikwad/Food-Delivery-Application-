import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Order, OrderRequest } from '../../models';

@Injectable({ providedIn: 'root' })
export class OrderService {
  private readonly base = `${environment.apiUrl}/orders`;

  constructor(private http: HttpClient) {}

  create(order: OrderRequest) {
    return this.http.post<Order>(this.base, order);
  }

  getAll() {
    return this.http.get<Order[]>(this.base);
  }

  getById(id: number) {
    return this.http.get<Order>(`${this.base}/${id}`);
  }

  getLatest() {
    return this.http.get<Order>(`${this.base}/latest`);
  }

  updateStatus(id: number, status: string) {
    return this.http.put<Order>(`${this.base}/${id}/status`, { status });
  }

  cancel(orderId: number) {
    return this.http.post<Order>(`${this.base}/fail/${orderId}`, {});
  }

  downloadReceipt(orderId: number) {
    return this.http.get(`${environment.apiUrl}/receipt/${orderId}`, {
      responseType: 'blob'
    });
  }
}
