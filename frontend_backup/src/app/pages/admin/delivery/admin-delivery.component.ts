import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DeliveryService } from '../../../core/services/delivery.service';
import { ToastService } from '../../../core/services/toast.service';
import { DeliveryBoy } from '../../../models';

@Component({
  selector: 'app-admin-delivery',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './admin-delivery.component.html'
})
export class AdminDeliveryComponent {
  private readonly deliveryService = inject(DeliveryService);
  private readonly toast = inject(ToastService);

  readonly showForm = signal(false);
  form = { name: '', phone: '', available: true };
  readonly addedBoys = signal<DeliveryBoy[]>([]);

  openCreate() {
    this.form = { name: '', phone: '', available: true };
    this.showForm.set(true);
  }

  save() {
    this.deliveryService.addBoy(this.form).subscribe({
      next: boy => {
        this.toast.success(`Delivery boy added (ID: ${boy.id})`);
        this.addedBoys.update(list => [...list, boy]);
        this.showForm.set(false);
      },
      error: () => this.toast.error('Failed to add delivery boy')
    });
  }

  cancel() {
    this.showForm.set(false);
  }
}
