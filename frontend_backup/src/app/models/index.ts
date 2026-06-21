export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  role: string;
}

export interface Restaurant {
  id: number;
  name: string;
  address: string;
  rating: number;
  open: boolean;
}

export interface Product {
  id: number;
  name: string;
  category: string;
  available: boolean;
  price: number;
  restaurant?: Restaurant;
}

export interface OrderItemRequest {
  productId: number;
  quantity: number;
}

export interface OrderRequest {
  userId?: number;
  restaurantId?: number;
  paymentMode: string;
  items: OrderItemRequest[];
  status?: string;
}

export interface OrderItem {
  orderItemId: number;
  quantity: number;
  price: number;
  product: Product;
}

export interface UserRef {
  id: number;
  email: string;
  username: string;
}

export interface DeliveryBoy {
  id: number;
  name: string;
  phone: string;
  available: boolean;
}

export interface Order {
  orderId: number;
  orderDate: string;
  orderStatus: string;
  totalAmount: number;
  paymentMode: string;
  paymentStatus: string;
  user?: UserRef;
  deliveryBoy?: DeliveryBoy;
  items?: OrderItem[];
}

export interface PaymentCreateResponse {
  razorpayOrderId: string;
  amount: number;
  orderId: number;
}

export interface PaymentVerifyRequest {
  razorpayOrderId: string;
  razorpayPaymentId: string;
  razorpaySignature: string;
  orderId: number;
}

export interface Train {
  trainNumber: string;
  trainName: string;
  source: string;
  destination: string;
  currentStation: string;
  status: string;
}

export interface CartItem {
  product: Product;
  quantity: number;
}

export interface CartState {
  restaurantId: number;
  restaurantName: string;
  items: CartItem[];
}

export const ORDER_STATUSES = [
  'PENDING', 'CONFIRMED', 'ACCEPTED', 'PLACED', 'PAID',
  'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED'
] as const;
