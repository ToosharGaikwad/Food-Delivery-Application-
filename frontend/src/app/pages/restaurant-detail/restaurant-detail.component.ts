import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { RestaurantService } from '../../core/services/restaurant.service';
import { ProductService } from '../../core/services/product.service';
import { CartService } from '../../core/services/cart.service';
import { ToastService } from '../../core/services/toast.service';
import { AuthService } from '../../core/services/auth.service';
import { Product, Restaurant } from '../../models';

@Component({
  selector: 'app-restaurant-detail',
  standalone: true,
  imports: [RouterLink, CurrencyPipe, DecimalPipe],
  templateUrl: './restaurant-detail.component.html',
  styleUrl: './restaurant-detail.component.scss'
})
export class RestaurantDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly restaurantService = inject(RestaurantService);
  private readonly productService = inject(ProductService);
  readonly cart = inject(CartService);
  readonly auth = inject(AuthService);
  private readonly toast = inject(ToastService);

  readonly restaurant = signal<Restaurant | null>(null);
  readonly products = signal<Product[]>([]);
  readonly loading = signal(true);
  readonly quantities = signal<Record<number, number>>({});

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.restaurantService.getById(id).subscribe({
      next: r => {
        this.restaurant.set(r);
        this.productService.getByRestaurant(id).subscribe({
          next: p => {
            this.products.set(p.filter(x => x.available));
            const qty: Record<number, number> = {};
            p.forEach(x => qty[x.id] = 1);
            this.quantities.set(qty);
            this.loading.set(false);
          },
          error: () => this.loading.set(false)
        });
      },
      error: () => this.loading.set(false)
    });
  }

  setQty(productId: number, delta: number) {
    this.quantities.update(q => ({
      ...q,
      [productId]: Math.max(1, (q[productId] ?? 1) + delta)
    }));
  }

  addToCart(product: Product) {
    if (!this.auth.isLoggedIn()) {
      this.toast.info('Please login to add items to cart');
      return;
    }
    const r = this.restaurant();
    if (!r) return;
    const err = this.cart.addItem(product, r.id, r.name, this.quantities()[product.id] ?? 1);
    if (err) {
      this.toast.error(err);
    } else {
      this.toast.success(`${product.name} added to cart`);
    }
  }
}
