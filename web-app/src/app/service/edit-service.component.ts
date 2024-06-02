import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {CreateNewServiceComponent} from "./create-new-service.component";

@Component({
  selector: 'app-edit-service',
  standalone: true,
  imports: [
    CreateNewServiceComponent
  ],
  templateUrl: './edit-service.component.html'
})
export class EditServiceComponent implements AfterViewInit {

  @ViewChild(CreateNewServiceComponent) private readonly createNewComponent!: CreateNewServiceComponent;

  constructor(

  ) {

  }

  ngAfterViewInit() {
  }
}
