import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { OrderService } from '../../../core/services/order.service';
import { DeliveryService } from '../../../core/services/delivery.service';
import { ToastService } from '../../../core/services/toast.service';
import { AuthService } from '../../../core/services/auth.service';
import { StatusBadgeComponent } from '../../../shared/components/status-badge/status-badge.component';
import { ORDER_STATUSES, Order } from '../../../models';

@Component({
  selector: 'app-admin-orders',
  standalone: true,
  imports: [FormsModule, CurrencyPipe, DatePipe, StatusBadgeComponent],
  templateUrl: './admin-orders.component.html'
})
export class AdminOrdersComponent implements OnInit {
  private readonly orderService = inject(OrderService);
  private readonly deliveryService = inject(DeliveryService);
  private readonly toast = inject(ToastService);
  readonly auth = inject(AuthService);

  readonly orders = signal<Order[]>([]);
  readonly statuses = ORDER_STATUSES;
  readonly statusEdits = signal<Record<number, string>>({});
  readonly boyIds = signal<Record<number, number>>({});

  ngOnInit() {
    this.load();
  }

  load() {
    this.orderService.getAll().subscribe({
      next: data => {
        const sorted = data.sort((a, b) =>
          new Date(b.orderDate).getTime() - new Date(a.orderDate).getTime()
        );
        this.orders.set(sorted);
        const edits: Record<number, string> = {};
        sorted.forEach(o => edits[o.orderId] = o.orderStatus);
        this.statusEdits.set(edits);
      }
    });
  }

  updateStatus(orderId: number) {
    const status = this.statusEdits()[orderId];
    this.orderService.updateStatus(orderId, status).subscribe({
      next: updated => {
        this.toast.success('Status updated');
        this.orders.update(list =>
          list.map(o => o.orderId === orderId ? updated : o)
        );
      },
      error: () => this.toast.error('Status update failed')
    });
  }

  assignBoy(orderId: number) {
    const boyId = this.boyIds()[orderId];
    if (!boyId) {
      this.toast.error('Enter a delivery boy ID');
      return;
    }
    this.deliveryService.assign(orderId, boyId).subscribe({
      next: updated => {
        this.toast.success('Delivery boy assigned');
        this.orders.update(list =>
          list.map(o => o.orderId === orderId ? updated : o)
        );
      },
      error: err => this.toast.error(err.error?.message ?? 'Assignment failed')
    });
  }

  markDelivered(orderId: number) {
    this.deliveryService.markDelivered(orderId).subscribe({
      next: updated => {
        this.toast.success('Order marked delivered');
        this.orders.update(list =>
          list.map(o => o.orderId === orderId ? updated : o)
        );
        this.statusEdits.update(e => ({ ...e, [orderId]: updated.orderStatus }));
      },
      error: () => this.toast.error('Failed to mark delivered')
    });
  }

  setStatusEdit(orderId: number, status: string) {
    this.statusEdits.update(e => ({ ...e, [orderId]: status }));
  }

  setBoyId(orderId: number, value: string) {
    this.boyIds.update(b => ({ ...b, [orderId]: Number(value) }));
  }
}
