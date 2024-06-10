import {Component, signal, WritableSignal} from '@angular/core';
import {ServiceHttp} from "./service-http";
import {GetServiceDetailsResponse} from "./service-dtos";
import {ActivatedRoute} from "@angular/router";
import {TranslateModule} from "@ngx-translate/core";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {DocumentData} from "../document/document-dtos";

@Component({
  selector: 'app-service-details',
  standalone: true,
  imports: [
    TranslateModule,
    LocalizedDatePipe
  ],
  templateUrl: './service-details.component.html',
  styleUrl: './service-details.component.css'
})
export class ServiceDetailsComponent {

  protected serviceDetails: WritableSignal<GetServiceDetailsResponse> = signal({} as any);
  protected documents: WritableSignal<{ [type: string]: DocumentData[] }> = signal({})

  constructor(
    private serviceHttp: ServiceHttp,
    private route: ActivatedRoute
  ) {
    const id: string = this.route.snapshot.paramMap.get('id')!;
    this.serviceHttp.getDetails(id).subscribe(item => this.serviceDetails = signal(item))

  }

  protected getCoachesNames(): string {
    return this.serviceDetails().coaches.map(item => item.firstName + ' ' + item.lastName)
      .join(', ')
  }
}
