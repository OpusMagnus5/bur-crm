import {OperatorDataInterface} from "../../operator/model/operator-data.interface";

export interface ProgramDetailsResponseInterface {
  id: string,
  version: number,
  name: string,
  createdAt: Date,
  modifiedAt: Date,
  creatorFirstName: string,
  creatorLastName: string,
  modifierFirstName: string
  modifierLastName: string
  operator: OperatorDataInterface
}
