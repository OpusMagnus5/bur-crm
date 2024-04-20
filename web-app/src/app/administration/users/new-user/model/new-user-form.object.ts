import {FormControl} from "@angular/forms";

export class NewUser {

    email: FormControl = new FormControl();
    firstName: FormControl = new FormControl();
    lastName: FormControl = new FormControl();
}
