import { CurrencyPipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../../core/services/product.service';
import { RestaurantService } from '../../../core/services/restaurant.service';
import { ToastService } from '../../../core/services/toast.service';
import { Product, Restaurant } from '../../../models';

@Component({
  selector: 'app-admin-products',
  standalone: true,
  imports: [FormsModule, CurrencyPipe],
  templateUrl: './admin-products.component.html'
})
export class AdminProductsComponent implements OnInit {
  private readonly productService = inject(ProductService);
  private readonly restaurantService = inject(RestaurantService);
  private readonly toast = inject(ToastService);

  readonly products = signal<Product[]>([]);
  readonly restaurants = signal<Restaurant[]>([]);
  readonly showForm = signal(false);

  form = {
    name: '',
    category: '',
    price: 0,
    available: true,
    restaurantId: 0
  };

  ngOnInit() {
    this.load();
    this.restaurantService.getAll().subscribe({
      next: data => {
        this.restaurants.set(data);
        if (data.length) this.form.restaurantId = data[0].id;
      }
    });
  }

  load() {
    this.productService.getAll().subscribe({
      next: data => this.products.set(data)
    });
  }

  openCreate() {
    this.form = {
      name: '',
      category: '',
      price: 0,
      available: true,
      restaurantId: this.restaurants()[0]?.id ?? 0
    };
    this.showForm.set(true);
  }

  save() {
    this.productService.create({
      name: this.form.name,
      category: this.form.category,
      price: this.form.price,
      available: this.form.available,
      restaurant: { id: this.form.restaurantId } as Product['restaurant']
    }).subscribe({
      next: () => {
        this.toast.success('Product added');
        this.showForm.set(false);
        this.load();
      },
      error: () => this.toast.error('Failed — admin role required')
    });
  }

  delete(id: number) {
    if (!confirm('Delete this product?')) return;
    this.productService.delete(id).subscribe({
      next: () => {
        this.toast.success('Product deleted');
        this.load();
      },
      error: () => this.toast.error('Delete failed')
    });
  }

  cancel() {
    this.showForm.set(false);
  }
}
