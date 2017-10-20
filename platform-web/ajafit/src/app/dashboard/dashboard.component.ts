import {Component} from '@angular/core';
import {ProfileService} from '../profile/profile.service';
import {OnInit} from '@angular/core';
import {Profile} from '../profile/profile';
import {Item} from '../item/item';
import {ItemService} from '../item/item.service';



@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  constructor(private profileService: ProfileService, private itemService: ItemService) {}
  profiles: Profile[];
  items: Item[];
  itemMap: Map<number, Item[]> = new Map<number, Item[]>();
  itemsCarousel: Item[];
  // itemsHighLight: Highlight[];
  itemsHighLight: Item[];
  fakeArray = new Array(1);
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
    this.itemsHighLight = this.items.filter(item => item.priority === 2);
  }
  getCSSClasses(index: number): string {
    return index === 0 ? 'carousel-item active' : 'carousel-item';
  }
  getRows(): number[] {
    const rows = Math.round(this.itemsHighLight.length / 3);
    console.log('rows:' + rows + ' total: ' + (this.itemsHighLight.length));
    return new Array(rows);
  }
}
