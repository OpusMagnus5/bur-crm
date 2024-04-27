import {HttpErrorDetailsInterface} from "./http-error-details.interface";

export interface HttpErrorResponseInterface {

  errorId: string,
  timestamp: Date,
  requestId: string,
  errors: HttpErrorDetailsInterface[]
}
