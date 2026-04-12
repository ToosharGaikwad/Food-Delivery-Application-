import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Restaurant {
  id: number;
  name: string;
  address: string;
  rating: number;
  imageUrl?: string;
}

@Injectable({
  providedIn: 'root',
})
export class RestaurantService {
  private apiUrl = 'http://localhost:8080/res';

  constructor(private http: HttpClient) {}

  // ✅ Get all restaurants
  getAllRestaurants(): Observable<Restaurant[]> {
    return this.http.get<Restaurant[]>(`${this.apiUrl}/allRestaurant`);
  }

  // ✅ Get restaurant by ID
  getRestaurantById(id: number): Observable<Restaurant> {
    return this.http.get<Restaurant>(`${this.apiUrl}/${id}`);
  }

  // ✅ Add restaurant (ADMIN)
  addRestaurant(restaurant: Restaurant): Observable<Restaurant> {
    return this.http.post<Restaurant>(`${this.apiUrl}/add`, restaurant, {
      headers: this.getAuthHeaders(),
    });
  }

  // ✅ Delete restaurant (ADMIN)
  deleteRestaurant(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/id/${id}`, {
      headers: this.getAuthHeaders(),
    });
  }

  // ✅ Update restaurant (ADMIN)
  updateRestaurant(id: number, restaurant: Restaurant): Observable<Restaurant> {
    return this.http.put<Restaurant>(
      `${this.apiUrl}/update/${id}`,
      restaurant,
      {
        headers: this.getAuthHeaders(),
      },
    );
  }

  // 🔐 JWT Header
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');

    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }
}
