import { Component } from '@angular/core';
import {MatFormFieldModule} from '@angular/material/form-field';
import { NewUser } from './model/newUserForm';

@Component({
  selector: 'new-user',
  standalone: true,
  imports: [MatFormFieldModule, NewUser],
  templateUrl: './new-user.component.html',
  styleUrl: './new-user.component.css'
})
export class NewUserComponent {

  private userForm = new NewUser();

}
