import { DecimalPipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RestaurantService } from '../../core/services/restaurant.service';
import { Restaurant } from '../../models';

@Component({
  selector: 'app-restaurants',
  standalone: true,
  imports: [RouterLink, FormsModule, DecimalPipe],
  templateUrl: './restaurants.component.html',
  styleUrl: './restaurants.component.scss'
})
export class RestaurantsComponent implements OnInit {
  private readonly restaurantService = inject(RestaurantService);
  readonly restaurants = signal<Restaurant[]>([]);
  readonly filtered = signal<Restaurant[]>([]);
  readonly loading = signal(true);
  searchQuery = '';

  ngOnInit() {
    this.restaurantService.getAll().subscribe({
      next: data => {
        this.restaurants.set(data);
        this.filtered.set(data);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  onSearch() {
    const q = this.searchQuery.toLowerCase().trim();
    if (!q) {
      this.filtered.set(this.restaurants());
      return;
    }
    this.filtered.set(
      this.restaurants().filter(r =>
        r.name.toLowerCase().includes(q) || r.address.toLowerCase().includes(q)
      )
    );
  }
}
