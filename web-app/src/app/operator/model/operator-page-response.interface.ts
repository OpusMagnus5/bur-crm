import {OperatorDataInterface} from "./operator-data.interface";

export interface OperatorPageResponseInterface {
  operators: OperatorDataInterface[],
  totalOperators: number
}
