export interface HttpQueryFiltersInterface {
  [param: string]: string | number | boolean | readonly (string | number | boolean)[];
}
