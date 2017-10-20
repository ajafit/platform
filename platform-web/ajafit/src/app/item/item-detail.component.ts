import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {Location} from '@angular/common';


import 'rxjs/add/operator/switchMap';

import {ItemService} from './item.service';
import {Item} from './item';

@Component({
  selector: 'app-item-detail',
  templateUrl: './item-detail.component.html',
  styleUrls: ['./item-detail.component.css']
})
export class ItemDetailComponent implements OnInit {
  constructor(private itemService: ItemService, private route: ActivatedRoute, private location: Location) {}
  @Input() item: Item;
  ngOnInit(): void {
    this.route.paramMap.switchMap((params: ParamMap) => this.itemService.getItem(+params.get('id'))).subscribe(p => this.item = p);
  }
  goBack(): void {
    this.location.back();
  }
}
