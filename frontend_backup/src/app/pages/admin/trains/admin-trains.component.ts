import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TrainService } from '../../../core/services/delivery.service';
import { ToastService } from '../../../core/services/toast.service';
import { Train } from '../../../models';

@Component({
  selector: 'app-admin-trains',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './admin-trains.component.html'
})
export class AdminTrainsComponent implements OnInit {
  private readonly trainService = inject(TrainService);
  private readonly toast = inject(ToastService);

  readonly trains = signal<Train[]>([]);
  readonly showForm = signal(false);
  readonly statusEdits = signal<Record<string, string>>({});

  form: Train = {
    trainNumber: '',
    trainName: '',
    source: '',
    destination: '',
    currentStation: '',
    status: 'On Time'
  };

  ngOnInit() { this.load(); }

  load() {
    this.trainService.getAllTrains().subscribe({
      next: data => {
        this.trains.set(data);
        const edits: Record<string, string> = {};
        data.forEach(t => edits[t.trainNumber] = t.status);
        this.statusEdits.set(edits);
      },
      error: () => this.toast.error('Failed to load trains')
    });
  }

  openCreate() {
    this.form = {
      trainNumber: '', trainName: '', source: '',
      destination: '', currentStation: '', status: 'On Time'
    };
    this.showForm.set(true);
  }

  save() {
    this.trainService.addTrain(this.form).subscribe({
      next: () => {
        this.toast.success('Train added');
        this.showForm.set(false);
        this.load();
      },
      error: () => this.toast.error('Failed to add train')
    });
  }

  updateStatus(trainNumber: string) {
    const status = this.statusEdits()[trainNumber];
    this.trainService.updateStatus(trainNumber, status).subscribe({
      next: () => {
        this.toast.success('Status updated');
        this.load();
      },
      error: () => this.toast.error('Update failed')
    });
  }

  delete(trainNumber: string) {
    if (!confirm('Delete this train?')) return;
    this.trainService.deleteTrain(trainNumber).subscribe({
      next: () => {
        this.toast.success('Train deleted');
        this.load();
      },
      error: () => this.toast.error('Delete failed')
    });
  }

  setStatusEdit(trainNumber: string, status: string) {
    this.statusEdits.update(e => ({ ...e, [trainNumber]: status }));
  }

  cancel() {
    this.showForm.set(false);
  }
}
