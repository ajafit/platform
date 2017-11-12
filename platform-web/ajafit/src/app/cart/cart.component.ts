import {Injectable} from '@angular/core';
import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {OnInit} from '@angular/core';

import {ItemService} from '../item/item.service';
import {AppComponent} from '../app.component';
import {RegionComponent} from './region.component';

import {Item} from '../item/item';
import {Cart} from '../cart/cart';
import {Region} from '../cart/region';



@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})

@Injectable()
export class CartComponent implements OnInit {
  constructor(private itemService: ItemService, private router: Router, private a: AppComponent) {}
  items: Item[];
  cart: Cart;
  region: Region;
  setCart(cart: Cart): void {
    this.cart = cart;
    if (!cart.items) {
      this.router.navigate(['/']);
    }
  }
  loadItems(): void {
    this.a.ngOnInit();
    this.itemService.getCartItems().then(resp => this.setCart(resp));
}
  ngOnInit(): void {
    this.loadItems();
  }
  getAmountArray(): number[] {
    return new Array(99);
  }
  getDescriptions(str: string): string[] {
    return str.split('|');
  }
  remover(item: Item): void {
    this.itemService.removeItem(item).then(r => this.loadItems());
  }
  onChange(item: Item) {
    this.itemService.changeAmountItem(item).then(r => this.loadItems());
  }
}
