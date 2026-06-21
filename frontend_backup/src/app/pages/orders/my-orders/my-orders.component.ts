import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { OrderService } from '../../../core/services/order.service';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';
import { StatusBadgeComponent } from '../../../shared/components/status-badge/status-badge.component';
import { Order } from '../../../models';

@Component({
  selector: 'app-my-orders',
  standalone: true,
  imports: [RouterLink, CurrencyPipe, DatePipe, StatusBadgeComponent],
  templateUrl: './my-orders.component.html',
  styleUrl: './my-orders.component.scss'
})
export class MyOrdersComponent implements OnInit {
  private readonly orderService = inject(OrderService);
  private readonly auth = inject(AuthService);
  private readonly toast = inject(ToastService);

  readonly orders = signal<Order[]>([]);
  readonly latestOrder = signal<Order | null>(null);
  readonly loading = signal(true);

  ngOnInit() {
    this.orderService.getAll().subscribe({
      next: all => {
        const email = this.auth.email();
        const mine = email
          ? all.filter(o => o.user?.email === email)
          : all;
        this.orders.set(mine.sort((a, b) =>
          new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime()
        ));
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });

    this.orderService.getLatest().subscribe({
      next: o => this.latestOrder.set(o),
      error: () => {}
    });
  }

  downloadReceipt(orderId: number) {
    this.orderService.downloadReceipt(orderId).subscribe({
      next: blob => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `receipt-${orderId}.pdf`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => this.toast.error('Could not download receipt')
    });
  }

  cancelOrder(orderId: number) {
    this.orderService.cancel(orderId).subscribe({
      next: updated => {
        this.toast.success('Order cancelled');
        this.orders.update(list =>
          list.map(o => o.orderId === orderId ? updated : o)
        );
      },
      error: err => this.toast.error(err.error?.message ?? 'Cannot cancel order')
    });
  }
}
