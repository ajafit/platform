import {Item} from '../item/item';
export class Cart {
  cartId: number;
  items: Cart[];
  subTotalValue: string;
  estimatedShipping: string;
  region: string;
}
