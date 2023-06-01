import { Component, OnInit, inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { Order } from 'src/app/models';
import { PizzaService } from 'src/app/pizza.service';

@Component({
  selector: 'app-orders-component',
  templateUrl: './orders-component.component.html',
  styleUrls: ['./orders-component.component.css'],
})
export class OrdersComponentComponent implements OnInit {
  router = inject(Router);
  route = inject(ActivatedRoute);
  result$!: Observable<Order[]>;
  pizzaSvc = inject(PizzaService);
  email!: string;
  sub!: Subscription;

  ngOnInit(): void {
    this.email = this.route.snapshot.params['email'];
    console.log(this.email);
    this.result$ = this.pizzaSvc.getOrders(this.email);
  }

  delivered(orderId: string) {
    console.log(orderId);
    this.result$ = this.pizzaSvc.delivered(orderId);
    this.sub = this.result$.subscribe((result) => {
      alert(result);
    });
  }
}
