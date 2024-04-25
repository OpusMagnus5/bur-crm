import {UserListDataInterface} from "./user-list-data.interface";

export interface UserListResponseInterface {

  users: UserListDataInterface[],
  totalUsers: number
}
