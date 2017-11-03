import {Component} from '@angular/core';
import {ProfileService} from '../profile/profile.service';
import {OnInit} from '@angular/core';
import {Profile} from '../profile/profile';
import {Item} from '../item/item';
import {ItemService} from '../item/item.service';

import {Router} from '@angular/router';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  constructor(private profileService: ProfileService, private itemService: ItemService, private router: Router) {}
  profiles: Profile[];
  items: Item[];
  itemMap: Map<number, Item[]> = new Map<number, Item[]>();
  itemsCarousel: Item[];
  // itemsHighLight: Highlight[];
  itemsHighLight: Item[];
  fakeArray = new Array(1);
  selectedItem: Item;
  getProfiles(): void {
    this.profileService.getProfiles('e').then(profiles => this.setProfiles(profiles));
  }
  loadItems(): void {
    this.itemService.getItems('main').then(items => this.setItems(items));
  }
  ngOnInit(): void {
    this.loadItems();
  }
  setProfiles(profiles: Profile[]): void {
    this.profiles = profiles.slice(1);
  }
  setItems(items: Item[]): void {
    this.items = items;
    this.itemsCarousel = this.items.filter(item => item.priority === 1);
    this.itemsHighLight = this.items.filter(item => item.priority !== 1);
  }
  getCSSClasses(index: number): string {
    return index === 0 ? 'carousel-item active' : 'carousel-item';
  }
  getRows(): number[] {
    const lrows = (this.itemsHighLight == null) ? 0 : this.itemsHighLight.length;
    const rows = Math.round(lrows / 3) + 1;
    return new Array(rows);
  }
  goToDetail(item: Item): void {
    console.log('indo detalhar item' + item);
    this.router.navigate(['/item-detail', item.screenId, item.couponId]);
  }
}
