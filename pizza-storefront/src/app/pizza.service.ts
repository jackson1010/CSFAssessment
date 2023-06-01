import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable()
export class PizzaService {
  http = inject(HttpClient);
  // TODO: Task 3
  // You may add any parameters and return any type from placeOrder() method
  // Do not change the method name
  placeOrder(
    name: string,
    email: string,
    size: number,
    thickCrust: boolean,
    sauce: string,
    comments: string,
    selectedToppings: string[]
  ): Observable<any> {
    const headers = new HttpHeaders()
      .set('Content-Type', 'application/json')
      .set('Accept', 'application/json');

    const body = {
      name: name,
      email: email,
      sauce: sauce,
      size: size,
      thickCrust: thickCrust,
      toppings: selectedToppings,
      comments: comments,
    };
    console.log(name, email,size, thickCrust, sauce, comments, selectedToppings);

    return this.http.post<any>("api/order",body, {headers})
  }

  // TODO: Task 5
  // You may add any parameters and return any type from getOrders() method
  // Do not change the method name
  getOrders() {}

  // TODO: Task 7
  // You may add any parameters and return any type from delivered() method
  // Do not change the method name
  delivered() {}
}
