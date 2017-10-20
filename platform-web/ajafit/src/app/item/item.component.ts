import {Component} from '@angular/core';
import {OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Item} from './item';
import {ItemDetailComponent} from './item-detail.component';
import {ItemService} from './item.service';


@Component({
  selector: 'app-item',
  templateUrl: './item.component.html',
  styleUrls: ['./item.component.css'],
  providers: [ItemService]
})
export class ItemComponent implements OnInit {
  constructor(private itemService: ItemService, private router: Router) {}
  filter: string;
  title = 'ajafit title';
  items: Item[];
  selectedItem: Item;
  onKey(event: any) {
    this.filter = event.target.value;
    console.log('filter:' + this.filter);
    this.getProfiles();
  }
  onSelect(pro: Item): void {
    this.selectedItem = pro;
  }
  getProfiles(): void {
    /*this.profileService.getProfiles().then(profiless => this.profiles = profiless);*/
    this.filter = this.filter == null ? '' : this.filter;
    console.log('filtrando:' + this.filter);
    this.itemService.getItems(this.filter).then(items => this.setItems(items));
  }
  ngOnInit(): void {
    this.getProfiles();
  }
  goToDetail(): void {
    this.router.navigate(['/profile-detail', this.selectedItem.id]);
  }
  setItems(items: Item[]) {
    console.log('items: ' + items);
    this.items = items;
  }
}
