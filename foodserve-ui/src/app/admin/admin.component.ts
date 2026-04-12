import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.component.html',
})
export class AdminComponent implements OnInit {
  section: string = 'products';

  products: any[] = [];
  orders: any[] = [];
  restaurants: any[] = [];

  newProduct = {
    name: '',
    price: 0,
    restaurantId: 0,
    category: '',
  };

  private productApi = 'http://localhost:8080/api/products';
  private orderApi = 'http://localhost:8080/orders';
  private restaurantApi = 'http://localhost:8080/res/allRestaurant';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadProducts();
    this.loadOrders();
    this.loadRestaurants();
  }

  // ================= PRODUCTS =================

  loadProducts() {
    this.http.get<any[]>(this.productApi).subscribe({
      next: (res) => {
        this.products = res;
      },
      error: (err) => console.log(err),
    });
  }

  addProduct() {
    const payload = {
      name: this.newProduct.name,
      price: this.newProduct.price,
      category: this.newProduct.category,
      restaurant: {
        id: this.newProduct.restaurantId,
      },
    };

    this.http.post(this.productApi + '/add', payload).subscribe({
      next: () => {
        alert('Product added!');
        this.loadProducts();
      },
      error: (err) => console.log(err),
    });
  }

  deleteProduct(id: number) {
    this.http.delete(`${this.productApi}/${id}`).subscribe({
      next: () => {
        alert('Deleted!');
        this.loadProducts();
      },
      error: (err) => console.log(err),
    });
  }

  // ================= ORDERS =================

  loadOrders() {
    this.http.get<any[]>(this.orderApi).subscribe({
      next: (res) => {
        console.log('Orders loaded:', res);
        this.orders = res || [];
        console.log('Orders assigned:', this.orders);
      },
      error: (err) => {
        console.error('Error loading orders:', err);
        console.error('Status:', err.status);
        console.error('Message:', err.message);
        console.error('Response:', err.error);
      },
    });
  }

  updateStatus(id: number, status: string) {
    this.http.put(`${this.orderApi}/${id}/status`, { status }).subscribe({
      next: () => {
        alert('Status updated!');
        this.loadOrders();
      },
      error: (err) => {
        console.error('Error updating status:', err);
      },
    });
  }

  // ================= RESTAURANTS =================

  loadRestaurants() {
    this.http.get<any[]>(this.restaurantApi).subscribe({
      next: (res) => {
        this.restaurants = res;
      },
      error: (err) => console.log(err),
    });
  }

  deleteRestaurant(id: number) {
    this.http.delete(`${this.restaurantApi}/${id}`).subscribe({
      next: () => {
        alert('Restaurant deleted!');
        this.loadRestaurants();
      },
      error: (err) => console.log(err),
    });
  }
}
