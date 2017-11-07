import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {Location} from '@angular/common';
import {Review} from './review';

import 'rxjs/add/operator/switchMap';

import {ItemService} from './item.service';
import {Item} from './item';
import {NutritionInfo} from './nutritionInfo';

@Component({
  selector: 'app-item-detail',
  templateUrl: './item-detail.component.html',
  styleUrls: ['./item-detail.component.css']
})
export class ItemDetailComponent implements OnInit {
  constructor(private itemService: ItemService, private route: ActivatedRoute, private location: Location) {}
  @Input() item: Item;
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
    return this.item.items;
  }
  getRates(): number[] {
    return new Array(10);
  }
  parse(str: string): number {
    console.log('isso:'+str);
   str = str.replace(',' , '.');
   const resp = Number(str);
    return resp;
  }
}
