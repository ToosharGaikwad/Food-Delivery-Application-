import { Component, OnInit } from '@angular/core';
import { OrderService } from '../order-service.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css'],
})
export class OrderComponent implements OnInit {
  orders: any[] = [];

  newOrder = {
    userId: 1,
    restaurantId: 1,
    items: [
      {
        productId: 1,
        quantity: 1,
      },
    ],
  };

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  // ✅ Load all orders
  loadOrders() {
    this.orderService.getAllOrders().subscribe({
      next: (data: any) => {
        this.orders = data;
      },
      error: (err: any) => {
        console.error(err);
      },
    });
  }

  // ✅ Place order
  placeOrder() {
    this.orderService.placeOrder(this.newOrder).subscribe({
      next: (res: any) => {
        alert('Order Placed!');
        this.loadOrders();
      },
      error: (err: any) => {
        console.error(err);
      },
    });
  }
}
