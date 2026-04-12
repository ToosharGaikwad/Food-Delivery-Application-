import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RestaurantService } from '../services/restaurant.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
  restaurants: any[] = [];

  constructor(
    private restaurantService: RestaurantService,
    private router: Router,
  ) {}

  ngOnInit() {
    this.loadRestaurants();
  }

  viewProducts(id: number) {
    console.log('Clicked ID:', id);
    this.router.navigate(['/products', id]);
  }

  loadRestaurants() {
    this.restaurantService.getAllRestaurants().subscribe({
      next: (res: any) => {
        console.log('Restaurants:', res);
        this.restaurants = res;
      },
      error: (err: any) => {
        console.log(err);
      },
    });
  }
}
