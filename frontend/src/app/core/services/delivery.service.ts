import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { DeliveryBoy, Order, Train } from '../../models';

@Injectable({ providedIn: 'root' })
export class DeliveryService {
  private readonly base = `${environment.apiUrl}/Del`;

  constructor(private http: HttpClient) {}

  addBoy(boy: Partial<DeliveryBoy>) {
    return this.http.post<DeliveryBoy>(`${this.base}/addBoy`, boy);
  }

  assign(orderId: number, boyId: number) {
    const params = new HttpParams().set('orderId', orderId).set('boyId', boyId);
    return this.http.patch<Order>(`${this.base}/assign`, null, { params });
  }

  markDelivered(orderId: number) {
    return this.http.patch<Order>(`${this.base}/order/${orderId}/delivered`, null);
  }
}

@Injectable({ providedIn: 'root' })
export class TrainService {
  constructor(private http: HttpClient) {}

  getLiveStation(fromStationCode: string, hours: number) {
    const params = new HttpParams()
      .set('fromStationCode', fromStationCode)
      .set('hours', hours);
    return this.http.get(`${environment.apiUrl}/api/train/live-station`, {
      params,
      responseType: 'text'
    });
  }

  getAllTrains() {
    return this.http.get<Train[]>(`${environment.apiUrl}/api/trains`);
  }

  getTrain(trainNumber: string) {
    return this.http.get<Train>(`${environment.apiUrl}/api/trains/${trainNumber}`);
  }

  addTrain(train: Train) {
    return this.http.post<Train>(`${environment.apiUrl}/api/trains`, train);
  }

  updateStatus(trainNumber: string, status: string) {
    const params = new HttpParams().set('status', status);
    return this.http.put<Train>(
      `${environment.apiUrl}/api/trains/status/${trainNumber}`,
      null,
      { params }
    );
  }

  deleteTrain(trainNumber: string) {
    return this.http.delete<void>(`${environment.apiUrl}/api/trains/${trainNumber}`);
  }
}
