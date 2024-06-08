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

  constructor(
    private serviceHttp: ServiceHttp,
    private route: ActivatedRoute
  ) {
    this.id = this.route.snapshot.paramMap.get('id')!;
  }

  ngAfterViewInit() {
    this.serviceHttp.getDetails(this.id).subscribe(response => {
      this.setFormControls(response);
    })
  }

  private setFormControls(response: GetServiceDetailsResponse) {
    this.createNewComponent.numberControl.setValue(response.number);
    this.createNewComponent.nameControl.setValue(response.name);
    this.createNewComponent.typeControl.setValue(response.type.value);
    this.createNewComponent.startDateControl.setValue(response.startDate);
    this.createNewComponent.endDateControl.setValue(response.endDate);
    this.createNewComponent.numberOfParticipantsControl.setValue(response.numberOfParticipants);
    this.createNewComponent.serviceProviderIdControl.setValue(response.serviceProvider);
    this.createNewComponent.programIdControl.setValue(response.program);
    this.createNewComponent.customerIdControl.setValue(response.customer);
    this.createNewComponent.coachIdsControl.setValue(response.coaches);
    this.createNewComponent.intermediaryIdControl.setValue(response.intermediary);
    this.createNewComponent.operatorControl.setValue(response.operator);
    this.createNewComponent.operatorControl.disable();
    this.createNewComponent.serviceVersion = response.version;
    this.createNewComponent.statusControl.setValue(response.status);
  }
}
