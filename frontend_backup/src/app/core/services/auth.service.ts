import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest } from '../../models';

const TOKEN_KEY = 'fs_token';
const ROLE_KEY = 'fs_role';
const EMAIL_KEY = 'fs_email';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly tokenSignal = signal<string | null>(this.read(TOKEN_KEY));
  private readonly roleSignal = signal<string | null>(this.read(ROLE_KEY));
  private readonly emailSignal = signal<string | null>(this.read(EMAIL_KEY));

  readonly isLoggedIn = computed(() => !!this.tokenSignal());
  readonly role = computed(() => this.roleSignal());
  readonly email = computed(() => this.emailSignal());
  readonly isAdmin = computed(() => this.roleSignal() === 'ADMIN');
  readonly isManager = computed(() => this.roleSignal() === 'restaurant_manager');
  readonly isStaff = computed(() => this.isAdmin() || this.isManager());

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: LoginRequest) {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/api/users/login`, credentials).pipe(
      tap(res => this.persistSession(res.token, res.role, credentials.email))
    );
  }

  register(data: RegisterRequest) {
    return this.http.post(`${environment.apiUrl}/api/users/register`, data, { responseType: 'text' });
  }

  logout() {
    const token = this.tokenSignal();
    if (token) {
      this.http.post(`${environment.apiUrl}/api/users/logout`, {}, {
        headers: { Authorization: `Bearer ${token}` }
      }).subscribe({ error: () => {} });
    }
    this.clearSession();
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return this.tokenSignal();
  }

  hasRole(...roles: string[]): boolean {
    const current = this.roleSignal();
    return !!current && roles.includes(current);
  }

  private persistSession(token: string, role: string, email: string) {
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(ROLE_KEY, role);
    localStorage.setItem(EMAIL_KEY, email);
    this.tokenSignal.set(token);
    this.roleSignal.set(role);
    this.emailSignal.set(email);
  }

  private clearSession() {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLE_KEY);
    localStorage.removeItem(EMAIL_KEY);
    this.tokenSignal.set(null);
    this.roleSignal.set(null);
    this.emailSignal.set(null);
  }

  private read(key: string): string | null {
    return localStorage.getItem(key);
  }
}
