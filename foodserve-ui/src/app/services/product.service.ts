import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/api/products';

  constructor(private http: HttpClient) {}
  addProduct(product: any) {
    return this.http.post(`${this.apiUrl}/add`, product, {
      headers: {
        Authorization: 'Basic ' + btoa('admin:1234'),
      },
    });
  }
  // ✅ GET ALL (optional)
  getAllProducts() {
    return this.http.get(this.apiUrl);
  }

  // ✅ ADD THIS (IMPORTANT)
  getProductsByRestaurant(id: number) {
    return this.http.get(`${this.apiUrl}/${id}`);
  }
}
