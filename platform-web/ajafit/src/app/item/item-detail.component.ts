import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {Location} from '@angular/common';
import {Review} from './review';

import 'rxjs/add/operator/switchMap';

import {ItemService} from './item.service';
import {Item} from './item';
import {NutritionInfo} from './nutritionInfo';

import {AppComponent} from '../app.component';

declare var jquery: any;
declare var $: any;

@Component({
  selector: 'app-item-detail',
  templateUrl: './item-detail.component.html',
  styleUrls: ['./item-detail.component.css']
})
export class ItemDetailComponent implements OnInit {
  constructor(private itemService: ItemService, private route: ActivatedRoute, private location: Location, private a: AppComponent) {}
  @Input() item: Item;
  message: string;
  ngOnInit(): void {
    const a = this.itemService;
    const b = this.route.paramMap;
    b.switchMap((params: ParamMap) => a.getItem(+params.get('screen'), +params.get('coupon'))).subscribe(p => this.item = p);
    console.log('navegou ate aqui!!');
  }
  goBack(): void {
    this.location.back();
  }
  getNutritionInfo(): NutritionInfo[] {
    return this.item.items[0].nutritionInfo;
  }
  getItems(): Item[] {
    if (this.item) {
      return this.item !== null ? this.item.items : null;
    }
    return null;
  }
  getRates(): number[] {
    return new Array(10);
  }
  parse(str: string): number {
    str = str.replace(',', '.');
    const resp = Number(str);
    return resp;
  }
  getDescriptions(str: string): string[] {
    return str.split('|');
  }
  addItem(item: Item): Boolean {
    console.log('add item' + item.couponId + ' - ' + item.screenId);
    this.itemService.addItem(item).then(resp => this.setItem(resp));
    /*this.message = 'agora vai ' + (Math.floor(Math.random() * 10) + 1);*/
//    this.message = 'item adicionado a carrinho!';
    return true;
  }
  setItem(count: number): void {
    this.message = 'foda-se';
    $('#added').modal('toggle');
    //$('#added').modal('show');
    this.a.setCartNumber(count);
  }
  getMessage(): string {
    const i = this.message;
    //this.message = null;
    return i;
  }
}
