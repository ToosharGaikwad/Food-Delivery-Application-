import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { ToastService } from '../../../core/services/toast.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: '../auth.component.scss'
})
export class RegisterComponent {
  private readonly auth = inject(AuthService);
  private readonly toast = inject(ToastService);
  private readonly router = inject(Router);

  email = '';
  username = '';
  password = '';
  readonly loading = signal(false);

  submit() {
    if (!this.email || !this.username || !this.password) return;
    this.loading.set(true);
    this.auth.register({ email: this.email, username: this.username, password: this.password }).subscribe({
      next: () => {
        this.toast.success('Account created! Please sign in.');
        this.router.navigate(['/login']);
      },
      error: () => {
        this.toast.error('Registration failed');
        this.loading.set(false);
      }
    });
  }
}
