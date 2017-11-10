import {NutritionInfo} from './nutritionInfo';
import {Review} from './review';
export class Item {
  itemId: number;
  couponId: number;
  screenId: number;
  name: string;
  descriptions: string;
  imageLarge: string;
  image1: string;
  image2: string;
  image3: string;
  video: string;
  value: string;
  valueFinal: string;
  priority: number;
  items: Item[];
  nutritionInfo: NutritionInfo[];
  reviews: Review[];
  related: Item[];
  rate: string;
  totaReviews: number;
  count: number;
}
