import { DecimalPipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RestaurantService } from '../../../core/services/restaurant.service';
import { ToastService } from '../../../core/services/toast.service';
import { Restaurant } from '../../../models';

@Component({
  selector: 'app-admin-restaurants',
  standalone: true,
  imports: [FormsModule, DecimalPipe],
  templateUrl: './admin-restaurants.component.html'
})
export class AdminRestaurantsComponent implements OnInit {
  private readonly restaurantService = inject(RestaurantService);
  private readonly toast = inject(ToastService);

  readonly restaurants = signal<Restaurant[]>([]);
  readonly editing = signal<number | null>(null);
  readonly showForm = signal(false);

  form: Partial<Restaurant> = this.emptyForm();

  ngOnInit() { this.load(); }

  load() {
    this.restaurantService.getAll().subscribe({
      next: data => this.restaurants.set(data)
    });
  }

  openCreate() {
    this.form = this.emptyForm();
    this.editing.set(null);
    this.showForm.set(true);
  }

  openEdit(r: Restaurant) {
    this.form = { ...r };
    this.editing.set(r.id);
    this.showForm.set(true);
  }

  save() {
    const editing = this.editing();
    const req = editing
      ? this.restaurantService.update(editing, this.form)
      : this.restaurantService.create(this.form);

    req.subscribe({
      next: () => {
        this.toast.success(editing ? 'Restaurant updated' : 'Restaurant created');
        this.showForm.set(false);
        this.load();
      },
      error: () => this.toast.error('Save failed')
    });
  }

  delete(id: number) {
    if (!confirm('Delete this restaurant?')) return;
    this.restaurantService.delete(id).subscribe({
      next: () => {
        this.toast.success('Restaurant deleted');
        this.load();
      },
      error: () => this.toast.error('Delete failed — admin role required')
    });
  }

  cancel() {
    this.showForm.set(false);
  }

  private emptyForm(): Partial<Restaurant> {
    return { name: '', address: '', rating: 4.0, open: true };
  }
}
