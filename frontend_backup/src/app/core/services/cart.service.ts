import { Injectable, signal, computed } from '@angular/core';
import { CartItem, CartState, Product } from '../../models';

const CART_KEY = 'fs_cart';

@Injectable({ providedIn: 'root' })
export class CartService {
  private readonly cartSignal = signal<CartState | null>(this.load());

  readonly cart = this.cartSignal.asReadonly();
  readonly itemCount = computed(() =>
    this.cartSignal()?.items.reduce((sum, i) => sum + i.quantity, 0) ?? 0
  );
  readonly total = computed(() =>
    this.cartSignal()?.items.reduce((sum, i) => sum + i.product.price * i.quantity, 0) ?? 0
  );
  readonly isEmpty = computed(() => !this.cartSignal()?.items.length);

  addItem(product: Product, restaurantId: number, restaurantName: string, quantity = 1): string | null {
    const current = this.cartSignal();
    if (current && current.restaurantId !== restaurantId) {
      return 'Your cart has items from another restaurant. Clear the cart first.';
    }
    const items = [...(current?.items ?? [])];
    const existing = items.find(i => i.product.id === product.id);
    if (existing) {
      existing.quantity += quantity;
    } else {
      items.push({ product, quantity });
    }
    const next: CartState = { restaurantId, restaurantName, items };
    this.save(next);
    return null;
  }

  updateQuantity(productId: number, quantity: number) {
    const current = this.cartSignal();
    if (!current) return;
    const items = current.items
      .map(i => i.product.id === productId ? { ...i, quantity } : i)
      .filter(i => i.quantity > 0);
    if (!items.length) {
      this.clear();
      return;
    }
    this.save({ ...current, items });
  }

  removeItem(productId: number) {
    this.updateQuantity(productId, 0);
  }

  clear() {
    localStorage.removeItem(CART_KEY);
    this.cartSignal.set(null);
  }

  private save(state: CartState) {
    localStorage.setItem(CART_KEY, JSON.stringify(state));
    this.cartSignal.set(state);
  }

  private load(): CartState | null {
    try {
      const raw = localStorage.getItem(CART_KEY);
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  }
}
