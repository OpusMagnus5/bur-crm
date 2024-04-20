import {
  Component,
  ContentChild,
  ElementRef,
  EventEmitter,
  inject,
  Input,
  OnDestroy,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild,
  ViewEncapsulation,
} from '@angular/core';
import {ActivatedRoute, Params, Router, RouterOutlet} from '@angular/router';
import {HeaderComponent} from './header/header.component';
import {AdministrationSideMenuComponent} from './administration/side-menu/administration-side-menu.component';
import {TestDirectiveDirective} from './test-directive.directive';
import {UnlessDirective} from './unless.directive';
import {NgSwitch, NgSwitchCase, NgSwitchDefault} from '@angular/common';
import {AppService} from './app.service';
import {interval, map, Observable, Subject, Subscription} from 'rxjs';
import {FormsModule, NgForm} from "@angular/forms";

@Component({
  //mozna stworzyc za pomoca CLI 'ng generate component nazwa'
  selector: 'app-root', //selektor ktory uzywamy w html, mozemy rowniez uzywac atrybutu lub klasy
  standalone: true, //jesli chcemy standalone
  imports: [
    RouterOutlet,
    HeaderComponent,
    AdministrationSideMenuComponent,
    TestDirectiveDirective,
    UnlessDirective,
    NgSwitch,
    NgSwitchCase,
    NgSwitchDefault,
    FormsModule,
  ], //tu dodajemy inne Componenty zamiast @NgModule || FormModule jest potrzebny do two-way binding
  templateUrl: './app.component.html', //tu html
  styleUrl: './app.component.css', //tu css
  encapsulation: ViewEncapsulation.None, //wyłącza enkapsulacje css, stają sie globalne, ShadowDom robi to co none ale za pomocą przeglądarki, nie wszystkie to obslugują
  providers: [AppService], //musimy rownież dodac tutaj wstrzykiwany serwis
})
export class AppComponent implements OnDestroy, OnInit {
  value: number = 10;
  title = 'web-app';
  isAllowed = true;
  serverNames = ['Testservet', 'Testservet2'];
  @Input() bindBesideComponent = 'bindBesideComponent'; //domyślnie zmienne w komponencie nie są dostępne w innych componentach nawet jesli są publiczne
  //Aby upublicznić taką zmienną w górę do rodziców używamy @Input() to znaczy ze oczekujemy zmiany tej wartosci, możemy podać inną nazwę w nawiasach, wtedy musimy się odwoływać po tej nazwie

  @Output() nameOfEvent = new EventEmitter<{ name: String; count: Number }>(); //Służy do emitowania eventów z okreslonymi danymi
  //musimy dodać równiez @Output dlatego że wysyłamy coś na zewnatrz, możemy również podać alias w nawiasie

  @ViewChild('referenceToP', { static: true }) viewReference: ElementRef; //tutaj tez mamy dostęp do refencji ale obiekt jest zwracany w innym typie
  //nie nalezy zmieniac wartosci referencji w skrypcie, lepiej uzywac tylko do pobierania wartosci
  //static używamy gdy chcemy uzyc tej wartosci w metodzie ngOnInit(), jeśli nie nie musimy ustawiac false

  @ContentChild('referenceToP', { static: true }) contentReference: ElementRef;
  //tutaj podobnie jak @ViewChild, tylko ze sluzy do pobrania contentu w ng-content

  private appService2?: AppService; //drugi sposób na wstrzykiwanie serviców

  constructor(
    private appService: AppService,
    private router: Router,
    private rout: ActivatedRoute
  ) {
    //dodajemy ActivatedRoute aby wskazac na jakiesj sciezce sie znajdujemy relatywnie
    //dodajmey router gdy chcemy w kodzie wyzwolic przjescie do innego komponentu
    //dodajemy serwis do kontruktora, te same instacje dostaną dzieci tego komponentu jeśli bedziemy wstrzykiwac
    //jeśli u dzieci podamy w providers otrzymają osobną instancje serwisu
    this.appService2 = inject(AppService); //drugi sposób na wstrzykiwanie serviców
  }

  onCreateClick() {
    this.isAllowed = false;
  }

  onPassLetter(event: Event) {
    console.log((<HTMLInputElement>event.target).value);
  }

  getColor() {
    return 'green';
  }

  emitEvent() {
    this.nameOfEvent.emit({ name: 'app-root', count: 1 }); // tutaj wywolujemy event  COMPONENT CUSTOM EVENT BINDING
  }

  setDataFromReference(reference: HTMLParagraphElement) {
    //LOCAL REFERENCE
    console.log(reference.innerText);
  }

  showViewReference() {
    console.log(this.viewReference.nativeElement.innerText);
  }

  //HOOKS LIFECYCLE
  //ngOnChanges jest wołany na starcie i za każdym razem gdy zmienia się property oznaczona @Input
  //ngOnInit kiedy komponent jest inicjalizowany, po konstrukotrze
  //ngDoCheck za każdym razem kiedy coś się zmieni w komponencie, oraz kliknięciu gdzieś na komponencie itp
  //ngAfterContentInit za po inicjalzacji ng-content jeśli został dodany
  //ngAfterContentChecked po każdej zmienie / reakcji w ng-content
  //ngAfterViewInit kiedy vidok komponentu i jego zależności został zainijalizowany
  //ngAfterViewChecked za każdym razem gdy coś sie zmieni w widoku komponentu lub dzieci
  //ngOnDestroy kiedy komponent jest usuwany z DOM np za pomocą ifa

  ngOnChanges(changes: SimpleChanges) {
    console.log(changes);
    this.appService; //tak wołamy metode na serwisie
  }

  subscription: Subscription;

  onLoad() {
    this.router.navigate(['/users'], {
      relativeTo: this.rout,
      queryParams: { name: 'Damian' },
      queryParamsHandling: 'preserve', // zachowuje parametry przy przejsciu, sa tej inne opcje do wyboru
      fragment: 'loading',
    }); //wskazujemy pojedyczne czesci naszej sciezki ale zawsze relatywnie, i do jakiej relatywnie
    // mozemy tez dodac query params oraz fragment

    this.rout.snapshot.params['id']; //wyciagamy id ze sciezki
    this.rout.snapshot.queryParams; //wyciagamy parametry
    this.rout.snapshot.fragment; //fragmenty
  }

  ngOnInit(): void {
    this.subscription = this.rout.params.subscribe((params: Params) => {
      //obserwator zmian parametrów, jesli znajdujemy się na komponencie na ktory chcemy przłeadowac z nowymi danymi musimu uzyc tego spososbu
      params['id'];
    });
    this.subscription = this.rout.queryParams.subscribe((params: Params) => {
      //obserwator zmian parametrów, jesli znajdujemy się na komponencie na ktory chcemy przłeadowac z nowymi danymi musimu uzyc tego spososbu
      params['id'];
    });
    this.subscription = this.rout.fragment.subscribe((fragment: string) => {
      //obserwator zmian parametrów, jesli znajdujemy się na komponencie na ktory chcemy przłeadowac z nowymi danymi musimu uzyc tego spososbu
      params['id'];
    });

    interval(1000).subscribe(count => { //observalble który emituje co 1s kolejną wartośc 1,2,3 itd,
      // one pozostają nawet po zniszczeniu komponentu więc trzeba pamiętać o ich zamykaniu metodą unsubscribe
      console.log(count);
    }, error => {
      console.log(error); //obsługa erroru
    }, () => {
      console.log('Complete'); //reakcja na zakończenie
    })

    const customIntervalObservable: Observable<any> = new Observable(observer => { //tworzymy observal to samo co wyżej tylko custom
      let count = 0;
      setInterval(() => {
        observer.next(count)
        observer.complete(); //konczenie observabla
        observer.error(new Error('count !!!')) //kiedy rzucimi błąd subscriber umiera, nie musimy niszczyć
        count++;
      }, 1000)
    })

    customIntervalObservable.pipe(map((data) => {
      return 'Round: ' + (data + 1); //operators, ten zmienia przed odebraniem go przez obserwatora
    }));

    customIntervalObservable.pipe(map((data) => {
      return 'Round: ' + (data + 1); //operators, ten zmienia przed odebraniem go przez obserwatora
    })).subscribe(); //i wtedy możemy subskrybowac dostosowane wiadomosci a kod oryginalny nie zostaje zmieniony
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe(); //usuwamy subskrypcje przy usuwaniu kompoenentu
  }

  newSubject = new Subject<boolean>();

  testSubject() {
    this.newSubject.next(true); //na subjectach mozemy recznie wypychac dane i go zasubskrybowac, nie nalezy stosować z @Output, słuzy do komunikacji miedzy componentami
    this.newSubject.subscribe()
  }

  onSubmit(form: NgForm) {
    console.log(form);
    /*propertki:
      dirty - okresla czy cos bylo zmienione w formie
      disabled gdy jest wylaczone
      valid jesli mamy walidatory zwraca czy jest ok
      touch - czy były klikane pola*/
  }


  
}
