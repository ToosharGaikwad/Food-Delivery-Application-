import { CurrencyPipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CartService } from '../../core/services/cart.service';
import { OrderService } from '../../core/services/order.service';
import { PaymentService, RazorpayCheckoutService } from '../../core/services/payment.service';
import { AuthService } from '../../core/services/auth.service';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [RouterLink, CurrencyPipe, FormsModule],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.scss'
})
export class CheckoutComponent {
  readonly cart = inject(CartService);
  private readonly orderService = inject(OrderService);
  private readonly paymentService = inject(PaymentService);
  private readonly razorpay = inject(RazorpayCheckoutService);
  private readonly auth = inject(AuthService);
  private readonly toast = inject(ToastService);
  private readonly router = inject(Router);

  readonly cartState = this.cart.cart;
  paymentMode = 'RAZORPAY';
  readonly processing = signal(false);

  updateQty(productId: number, qty: number) {
    this.cart.updateQuantity(productId, qty);
  }

  placeOrder() {
    const state = this.cartState();
    if (!state?.items.length) return;

    this.processing.set(true);
    const orderRequest = {
      paymentMode: this.paymentMode,
      restaurantId: state.restaurantId,
      items: state.items.map(i => ({
        productId: i.product.id,
        quantity: i.quantity
      }))
    };

    this.orderService.create(orderRequest).subscribe({
      next: order => {
        if (this.paymentMode === 'RAZORPAY') {
          this.initiatePayment(order.orderId);
        } else {
          this.toast.success('Order placed successfully!');
          this.cart.clear();
          this.router.navigate(['/orders', order.orderId]);
          this.processing.set(false);
        }
      },
      error: err => {
        this.toast.error(err.error?.message ?? 'Failed to place order');
        this.processing.set(false);
      }
    });
  }

  private initiatePayment(orderId: number) {
    this.paymentService.create(orderId).subscribe({
      next: res => {
        this.razorpay.openCheckout(
          res.razorpayOrderId,
          res.amount,
          res.orderId,
          this.auth.email() ?? '',
          (paymentId, signature) => {
            this.paymentService.verify({
              razorpayOrderId: res.razorpayOrderId,
              razorpayPaymentId: paymentId,
              razorpaySignature: signature,
              orderId: res.orderId
            }).subscribe({
              next: () => {
                this.toast.success('Payment successful!');
                this.cart.clear();
                this.router.navigate(['/orders', res.orderId]);
                this.processing.set(false);
              },
              error: () => {
                this.toast.error('Payment verification failed');
                this.processing.set(false);
              }
            });
          },
          () => {
            this.toast.info('Payment cancelled. Order is pending.');
            this.router.navigate(['/orders', orderId]);
            this.processing.set(false);
          }
        );
      },
      error: () => {
        this.toast.error('Failed to initiate payment');
        this.processing.set(false);
      }
    });
  }
}
