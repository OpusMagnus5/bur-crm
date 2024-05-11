import {ProgramDataInterface} from "./program-data-interface";

export interface OperatorDetailsResponseInterface {
  id: string,
  version: number
  name: string,
  notes: string,
  createdAt: Date,
  modifiedAt: Date,
  creatorFirstName: string,
  creatorLastName: string,
  modifierFirstName: string,
  modifierLastName: string,
  programs: ProgramDataInterface[]
}
