import { Component, OnInit, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PizzaService } from '../pizza.service';
import { Observable, Subscription, firstValueFrom } from 'rxjs';

const SIZES: string[] = [
  'Personal - 6 inches',
  'Regular - 9 inches',
  'Large - 12 inches',
  'Extra Large - 15 inches',
];
const PIZZA_TOPPINGS: string[] = [
  'chicken',
  'seafood',
  'beef',
  'vegetables',
  'cheese',
  'arugula',
  'pineapple',
];

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
})
export class MainComponent implements OnInit {
  pizzaForm!: FormGroup;
  fb = inject(FormBuilder);
  router = inject(Router);
  pizzaSize = SIZES[0];
  toppings!: FormArray;
  pizzaSvc = inject(PizzaService);
  result$!: Observable<any>;
  subscription!: Subscription;
  email!: string;

  ngOnInit(): void {
    this.pizzaForm = this.createForm();
  }

  createForm() {
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      email: this.fb.control<string>('', [Validators.required]),
      size: this.fb.control<string>('', [Validators.required]),
      base: this.fb.control<string>('', [Validators.required]),
      sauce: this.fb.control<string>('', [Validators.required]),
      toppings: this.fb.array(
        PIZZA_TOPPINGS.map((topping) => this.fb.control(false)),
        Validators.required
      ),
      comments: this.fb.control<string>(''),
    });
  }

  constructor() {}

  updateSize(size: string) {
    this.pizzaSize = SIZES[parseInt(size)];
  }

  processForm() {
    console.log(this.pizzaForm.value);
    const name = this.pizzaForm.value['name'];
    const email = this.pizzaForm.value['email'];

    const size = this.pizzaForm.value['size'];
    //  const requiredSize=SIZES[size];

    const base = this.pizzaForm.value['base'];
    let thickCrust = false;
    if (base === 'thick') {
      thickCrust = true;
    }
    const sauce = this.pizzaForm.value['sauce'];
    const comments = this.pizzaForm.value['comments'];

    const Toppings: String[] = this.pizzaForm.value['toppings'];
    const selectedToppings: string[] = PIZZA_TOPPINGS.filter(
      (topping, index) => Toppings[index]
    );
    firstValueFrom(
      this.pizzaSvc.placeOrder(
        name,
        email,
        size,
        thickCrust,
        sauce,
        comments,
        selectedToppings
      )
    )
      .then((result) => {
        console.log(result);
        this.email = result.email;
        this.router.navigate(['/orders', this.email]);
      })
      .catch((error) => alert(JSON.stringify(error)));
  }
}
