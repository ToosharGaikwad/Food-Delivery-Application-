import { Component, input } from '@angular/core';

@Component({
  selector: 'app-status-badge',
  standalone: true,
  template: `<span class="badge" [class]="statusClass()">{{ status() }}</span>`
})
export class StatusBadgeComponent {
  status = input.required<string>();

  statusClass() {
    const s = this.status().toLowerCase();
    if (['delivered', 'confirmed', 'paid'].includes(s)) return 'badge-success';
    if (['cancelled', 'failed'].includes(s)) return 'badge-danger';
    if (['pending'].includes(s)) return 'badge-warning';
    if (['preparing', 'out_for_delivery'].includes(s)) return 'badge-info';
    return 'badge-neutral';
  }
}
