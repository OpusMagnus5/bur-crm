import {Injectable} from "@angular/core";
import {UserHttpService} from "./service/user-http.service";
import {UserListResponseInterface} from "./model/user-list-response.interface";
import {Subject} from "rxjs";

@Injectable()
export class UsersListService {

  data: Subject<UserListResponseInterface> = new Subject<UserListResponseInterface>()

  constructor(private http: UserHttpService) {
    this.http.getUserPage(1, 10).subscribe(response =>
      this.data.next(response)
    );
  }
}
