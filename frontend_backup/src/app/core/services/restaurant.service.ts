import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Restaurant } from '../../models';

@Injectable({ providedIn: 'root' })
export class RestaurantService {
  private readonly base = `${environment.apiUrl}/res`;

  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<Restaurant[]>(`${this.base}/allRestaurant`);
  }

  getById(id: number) {
    return this.http.get<Restaurant>(`${this.base}/${id}`);
  }

  create(restaurant: Partial<Restaurant>) {
    return this.http.post<Restaurant>(`${this.base}/addRestaurant`, restaurant);
  }

  update(id: number, restaurant: Partial<Restaurant>) {
    return this.http.put<Restaurant>(`${this.base}/update/${id}`, restaurant);
  }

  delete(id: number) {
    return this.http.delete<{ message: string }>(`${this.base}/id/${id}`);
  }
}
