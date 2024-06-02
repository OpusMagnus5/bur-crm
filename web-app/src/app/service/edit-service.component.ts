import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {CreateNewServiceComponent} from "./create-new-service.component";
import {ActivatedRoute} from "@angular/router";
import {ServiceHttp} from "./service-http";
import {GetServiceDetailsResponse} from "./service-dtos";

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
  private readonly id: string;
  private serviceData: GetServiceDetailsResponse | null = null;

  constructor(
    private serviceHttp: ServiceHttp,
    private route: ActivatedRoute
  ) {
    this.id = route.snapshot.paramMap.get('id')!;
    this.serviceHttp.getDetails(this.id)
      .subscribe(response => this.serviceData = response);
  }

  ngAfterViewInit() {
  }
}
