import {ProgramDataInterface} from "./program-data-interface";

export interface ProgramPageResponseInterface {
  programs: ProgramDataInterface[],
  totalPrograms: number
}
