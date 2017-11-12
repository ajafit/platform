import {Injectable} from '@angular/core';
import {Component, Input} from '@angular/core';
import {Router} from '@angular/router';
import {OnInit} from '@angular/core';

import {ItemService} from '../item/item.service';
import {AppComponent} from '../app.component';
import {CartComponent} from './cart.component';

import {Item} from '../item/item';
import {Cart} from '../cart/cart';
import {Region} from '../cart/region';

declare var jquery: any;
declare var $: any;

@Component({
  selector: 'app-region',
  templateUrl: './region.component.html',
  styleUrls: ['./region.component.css'],
})

@Injectable()
export class RegionComponent implements OnInit {
  constructor(private itemService: ItemService, private router: Router, private c: CartComponent) {}

   @Input()
   region: Region;
  ngOnInit(): void {
    if (!this.region) {
     this.itemService.getRegionTree(1).then(resp => this.setRegion(resp));
     console.log('carregando region component' + this.region);
    }
  }
  getRegion(): Region {
    console.log('region:' + this.region);
    if (!this.region) {
      console.log('carregando as regions');
     // this.itemService.getRegionTree(1).then(resp => this.setRegion(resp));
    }
    return this.region;
  }
  refresh(): void {
  //  this.router.navigate(['/cart']);
    this.c.ngOnInit();
    $('#region').modal('toggle');
  }
  setRegion(region: Region): Region {
    this.region = region;
    console.log('region settada com valor: ' + region);
    return region;
  }
  choose(region: Region): void {
    this.itemService.cartRegion(region).then(resp => this.refresh());
  }
}
