import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {NewUserComponent} from "./new-user.component";
import {UserHttpService} from "./service/user-http.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-edit-user',
  standalone: true,
  imports: [
    NewUserComponent
  ],
  templateUrl: './edit-user.component.html'
})
export class EditUserComponent implements AfterViewInit {

  @ViewChild(NewUserComponent) private readonly newUserComponent!: NewUserComponent;
  private readonly id: string;

  constructor(
    private userHttp: UserHttpService,
    private route: ActivatedRoute
  ) {
    this.id = this.route.snapshot.paramMap.get('id')!;
  }

  ngAfterViewInit(): void {
    this.userHttp.getUserDetails(this.id).subscribe(response => {
      this.newUserComponent.emailControl.setValue(response.email);
      this.newUserComponent.roleControl.setValue(response.roles[response.roles.length - 1].role);
      this.newUserComponent.firstNameControl.setValue(response.firstName);
      this.newUserComponent.lastNameControl.setValue(response.lastName);
      this.newUserComponent.userVersion = response.version;
    })
  }



}
