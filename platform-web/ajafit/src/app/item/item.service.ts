import {Injectable} from '@angular/core';
import {Item} from './item';
import {Cart} from '../cart/cart';
import {Region} from '../cart/region';

import {Http, Headers, Response, RequestOptions} from '@angular/http';
import {HttpHeaders} from '@angular/common/http';
import 'rxjs/add/operator/toPromise';

const headers = new Headers();
const opts = new RequestOptions();
opts.headers = headers;
opts.withCredentials = true;

@Injectable()
export class ItemService {
  itemsURL = 'http://www.ajafit.com.br/ajafit/platform/service/screen/list/';
  itemURL = 'http://www.ajafit.com.br/ajafit/platform/service/screen/detail/';
  itemAddURL = 'http://www.ajafit.com.br/ajafit/platform/service/cart/add';
  cartNumberURL = 'http://www.ajafit.com.br/ajafit/platform/service/cart/number';
  cartItemsURL = 'http://www.ajafit.com.br/ajafit/platform/service/cart/cart';
  cartRegionURL = 'http://www.ajafit.com.br/ajafit/platform/service/cart/region';
  regionTreeURL = 'http://www.ajafit.com.br/ajafit/platform/service/cart/region/tree/';
  removerItemURL = 'http://www.ajafit.com.br/ajafit/platform/service/cart/remove';
  changeAmountURL = 'http://www.ajafit.com.br/ajafit/platform/service/cart/amount';
  constructor(private http: Http) {}
  getItem(screenId: number, couponId: number): Promise<Item> {
    return this.http.get(this.itemURL + screenId + '/' + couponId, opts).toPromise().then(respo => this.parse(respo)).catch(this.error);
  }
  getItems(filter: string): Promise<Item[]> {
    return this.http.get(this.itemsURL + filter, opts).toPromise().then(respo => this.parse(respo)).catch(this.error);
  }
  parse(data: Response): Item[] {
    //  console.log('parsingg:' + data.json().items[0].nutritionInfo[0].key);
    return (data.json() as Item[]);
  }
  parse1(data: Response): Item {
    return (data.json() as Item);
  }
  parse2(data: Response): number {
    const x = (data.json() as Item);
    console.log(x);
    return x.count;
  }
  parse3(data: Response): Cart {
    return (data.json() as Cart);
  }
  parse4(data: Response): Region {
    return (data.json() as Region);
  }
  private error(error: any): Promise<any> {
    console.log('deu merda!!');
    return Promise.reject('>>' + error.status + '<<' || error);
  }
  addItem(item: Item): Promise<number> {
    return this.http.post(this.itemAddURL, item, opts).toPromise().then(respo => this.parse2(respo));
  }
  removeItem(item: Item): Promise<Boolean> {
    return this.http.put(this.removerItemURL, item, opts).toPromise().then(r => true);
  }
  changeAmountItem(item: Item): Promise<Boolean> {
    return this.http.put(this.changeAmountURL, item, opts).toPromise().then(r => true);
  }
  cartRegion(region: Region): Promise<Boolean> {
    return this.http.post(this.cartRegionURL, region, opts).toPromise().then(r => true);
  }
  getCartNumber(): Promise<number> {
    return this.http.get(this.cartNumberURL, opts).toPromise().then(respo => this.parse2(respo));
  }
  getCartItems(): Promise<Cart> {
    return this.http.get(this.cartItemsURL, opts).toPromise().then(respo => this.parse3(respo));
  }
  getRegionTree(regionId: number): Promise<Region> {
    return this.http.get(this.regionTreeURL + regionId, opts).toPromise().then(respo => this.parse4(respo));
  }
}
