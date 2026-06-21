import { DecimalPipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RestaurantService } from '../../core/services/restaurant.service';
import { Restaurant } from '../../models';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, DecimalPipe],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  private readonly restaurantService = inject(RestaurantService);
  readonly restaurants = signal<Restaurant[]>([]);
  readonly loading = signal(true);

  ngOnInit() {
    this.restaurantService.getAll().subscribe({
      next: data => {
        this.restaurants.set(data.filter(r => r.open).slice(0, 6));
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }
}
