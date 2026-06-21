import { Routes } from '@angular/router';
import { authGuard, guestGuard, adminGuard, staffGuard } from './core/guards/auth.guard';
import { ShellComponent } from './layout/shell/shell.component';

export const routes: Routes = [
  {
    path: '',
    component: ShellComponent,
    children: [
      { path: '', loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent) },
      { path: 'restaurants', loadComponent: () => import('./pages/restaurants/restaurants.component').then(m => m.RestaurantsComponent) },
      { path: 'restaurants/:id', loadComponent: () => import('./pages/restaurant-detail/restaurant-detail.component').then(m => m.RestaurantDetailComponent) },
      { path: 'live-station', loadComponent: () => import('./pages/live-station/live-station.component').then(m => m.LiveStationComponent), canActivate: [authGuard] },
      { path: 'login', loadComponent: () => import('./pages/auth/login/login.component').then(m => m.LoginComponent), canActivate: [guestGuard] },
      { path: 'register', loadComponent: () => import('./pages/auth/register/register.component').then(m => m.RegisterComponent), canActivate: [guestGuard] },
      { path: 'checkout', loadComponent: () => import('./pages/checkout/checkout.component').then(m => m.CheckoutComponent), canActivate: [authGuard] },
      { path: 'orders', loadComponent: () => import('./pages/orders/my-orders/my-orders.component').then(m => m.MyOrdersComponent), canActivate: [authGuard] },
      { path: 'orders/:id', loadComponent: () => import('./pages/orders/order-detail/order-detail.component').then(m => m.OrderDetailComponent), canActivate: [authGuard] },
      {
        path: 'admin',
        loadComponent: () => import('./pages/admin/admin-shell/admin-shell.component').then(m => m.AdminShellComponent),
        canActivate: [staffGuard],
        children: [
          { path: '', redirectTo: 'orders', pathMatch: 'full' },
          { path: 'restaurants', loadComponent: () => import('./pages/admin/restaurants/admin-restaurants.component').then(m => m.AdminRestaurantsComponent), canActivate: [adminGuard] },
          { path: 'products', loadComponent: () => import('./pages/admin/products/admin-products.component').then(m => m.AdminProductsComponent), canActivate: [adminGuard] },
          { path: 'orders', loadComponent: () => import('./pages/admin/orders/admin-orders.component').then(m => m.AdminOrdersComponent) },
          { path: 'delivery', loadComponent: () => import('./pages/admin/delivery/admin-delivery.component').then(m => m.AdminDeliveryComponent), canActivate: [adminGuard] },
          { path: 'trains', loadComponent: () => import('./pages/admin/trains/admin-trains.component').then(m => m.AdminTrainsComponent), canActivate: [adminGuard] }
        ]
      }
    ]
  },
  { path: '**', redirectTo: '' }
];
