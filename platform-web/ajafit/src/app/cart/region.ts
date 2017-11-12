export class Region {
  regionId: number;
  descriptions: string;
  outer: Region;
  inner: Region[];
  level: number;
}
