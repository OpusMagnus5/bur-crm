import { Component} from '@angular/core';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatTabsModule } from '@angular/material/tabs';
import { NewUserComponent } from '../new-user/new-user.component';

@Component({
  selector: 'user-dashboard',
  templateUrl: './user.dashboard.component.html',
  styleUrl: './user.dashboard.component.css',
  standalone: true,
  imports: [
    MatGridListModule,
    MatTabsModule,
    NewUserComponent
  ]
})
export class UserDashboardComponent {

}
