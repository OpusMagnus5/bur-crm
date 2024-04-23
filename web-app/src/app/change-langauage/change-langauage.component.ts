import {Component} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatSelect} from "@angular/material/select";

@Component({
  selector: 'change-langauage',
  standalone: true,
  imports: [
    MatFormField,
    MatSelect,
    MatLabel
  ],
  templateUrl: './change-langauage.component.html',
  styleUrl: './change-langauage.component.css'
})
export class ChangeLangauageComponent {

  private availableLanguages: string[] = ['pl', 'en'];

  private readonly form: FormGroup;
  private readonly languageControl: FormControl

  constructor() {
    this.languageControl = new FormControl(this.availableLanguages[0])
    this.form = new FormGroup({
      'language': this.languageControl
    })
  }

}
