import {Injectable} from '@angular/core';
import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {OnInit} from '@angular/core';

import {ItemService} from '../item/item.service';
import {AppComponent} from '../app.component';

import {Item} from '../item/item';




@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})

@Injectable()
export class CartComponent implements OnInit {
  constructor(private itemService: ItemService, private router: Router, private a: AppComponent) {}
  items: Item[];
  selected: number;
  setItems(items: Item[]): void {
    this.items = items;
    if (items.length === 0) {
      this.router.navigate(['/']);
    }
  }
  loadItems(): void {
    this.a.ngOnInit();
    this.itemService.getCartItems().then(resp => this.setItems(resp));
  }
  ngOnInit(): void {
    this.loadItems();
    this.selected = 10;
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
