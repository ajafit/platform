import {Injectable} from '@angular/core';
import {Item} from './item';

import {Http, Headers, Response, RequestOptions} from '@angular/http';
import {HttpHeaders} from '@angular/common/http';
import 'rxjs/add/operator/toPromise';

const headers = new Headers();
const opts = new RequestOptions();
opts.headers = headers;

@Injectable()
export class ItemService {
  itemsURL = 'http://localhost:8080/ajafit/platform/service/screen/list/';
  itemURL = 'http://localhost:8080/ajafit/platform/service/screen/detail/';
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
  } private error(error: any): Promise<any> {
    console.log('deu merda!!');
    return Promise.reject('>>' + error.status + '<<' || error);
  }
}
