import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { PaymentCreateResponse, PaymentVerifyRequest } from '../../models';

@Injectable({ providedIn: 'root' })
export class PaymentService {
  private readonly base = `${environment.apiUrl}/payment`;

  constructor(private http: HttpClient) {}

  create(orderId: number) {
    return this.http.post<PaymentCreateResponse>(`${this.base}/create/${orderId}`, {});
  }

  verify(payload: PaymentVerifyRequest) {
    return this.http.post<{ message: string }>(`${this.base}/verify`, payload);
  }
}

declare global {
  interface Window {
    Razorpay: new (options: Record<string, unknown>) => { open: () => void };
  }
}

@Injectable({ providedIn: 'root' })
export class RazorpayCheckoutService {
  openCheckout(
    razorpayOrderId: string,
    amount: number,
    orderId: number,
    email: string,
    onSuccess: (paymentId: string, signature: string) => void,
    onDismiss?: () => void
  ) {
    const options: Record<string, unknown> = {
      key: environment.razorpayKey,
      amount: amount * 100,
      currency: 'INR',
      name: 'FoodServe',
      description: `Order #${orderId}`,
      order_id: razorpayOrderId,
      prefill: { email },
      theme: { color: '#FF6B35' },
      handler: (response: { razorpay_payment_id: string; razorpay_signature: string }) => {
        onSuccess(response.razorpay_payment_id, response.razorpay_signature);
      },
      modal: {
        ondismiss: () => onDismiss?.()
      }
    };
    new window.Razorpay(options).open();
  }
}
