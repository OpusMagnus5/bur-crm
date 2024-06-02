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
    if (this.serviceData) {
      this.createNewComponent.numberControl.setValue(this.serviceData.number);
      this.createNewComponent.nameControl.setValue(this.serviceData.name);
      this.createNewComponent.typeControl.setValue(this.serviceData.type.value);
      this.createNewComponent.startDateControl.setValue(this.serviceData.startDate);
      this.createNewComponent.endDateControl.setValue(this.serviceData.endDate);
      this.createNewComponent.numberOfParticipantsControl.setValue(this.serviceData.numberOfParticipants);
      this.createNewComponent.serviceProviderIdControl.setValue(this.serviceData.serviceProvider);
      this.createNewComponent.programIdControl.setValue(this.serviceData.program);
      this.createNewComponent.customerIdControl.setValue(this.serviceData.customer);
      this.createNewComponent.coachIdsControl.setValue(this.serviceData.coaches);
      this.createNewComponent.intermediaryIdControl.setValue(this.serviceData.intermediary);
    }
  }
}
