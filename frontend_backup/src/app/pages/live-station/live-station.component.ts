import { UpperCasePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TrainService } from '../../core/services/delivery.service';
import { ToastService } from '../../core/services/toast.service';

@Component({
  selector: 'app-live-station',
  standalone: true,
  imports: [FormsModule, UpperCasePipe],
  templateUrl: './live-station.component.html',
  styleUrl: './live-station.component.scss'
})
export class LiveStationComponent {
  private readonly trainService = inject(TrainService);
  private readonly toast = inject(ToastService);

  stationCode = 'NDLS';
  hours = 2;
  readonly result = signal<string | null>(null);
  readonly loading = signal(false);

  search() {
    if (!this.stationCode.trim()) return;
    this.loading.set(true);
    this.result.set(null);
    this.trainService.getLiveStation(this.stationCode.trim().toUpperCase(), this.hours).subscribe({
      next: data => {
        this.result.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.toast.error('Failed to fetch live station data');
        this.loading.set(false);
      }
    });
  }
}
