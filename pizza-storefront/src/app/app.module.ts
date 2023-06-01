import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { MainComponent } from './components/main.component';
import { OrdersComponentComponent } from './components/orders-component/orders-component.component';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { PizzaService } from './pizza.service';

const routes: Routes = [
  { path: '', component: MainComponent, title: 'ACE' },
  { path: 'orders/:email', component: OrdersComponentComponent },
  { path: '**', redirectTo: '/', pathMatch: 'full' },
];

@NgModule({
  declarations: [AppComponent, MainComponent, OrdersComponentComponent],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes),
    HttpClientModule,
    ReactiveFormsModule,
  ],

  providers: [PizzaService],
  bootstrap: [AppComponent],
})
export class AppModule {}
