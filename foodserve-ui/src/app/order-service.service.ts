import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface OrderRequestDTO {
  userId: number;
  restaurantId: number;
  items: {
    productId: number;
    quantity: number;
  }[];
}

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private baseUrl = 'http://localhost:8080/orders';

  constructor(private http: HttpClient) {}

  // ✅ Place Order
  placeOrder(order: OrderRequestDTO): Observable<any> {
    return this.http.post(this.baseUrl, order);
  }

  // ✅ Get Order by ID
  getOrder(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  // ✅ Get All Orders
  getAllOrders(): Observable<any> {
    return this.http.get(this.baseUrl);
  }
}
