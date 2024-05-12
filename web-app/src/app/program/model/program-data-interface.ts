import {OperatorDataInterface} from "../../operator/model/operator-data.interface";

export interface ProgramDataInterface {
  id: string
  name: string
  operator: OperatorDataInterface
}
