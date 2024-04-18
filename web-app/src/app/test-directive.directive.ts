import {Directive, ElementRef, HostBinding, HostListener, Input, OnInit, Renderer2} from '@angular/core';

@Directive({ //mozna stworzyc za pomoca CLI 'ng generate directive nazwa'
  selector: '[appTestDirective]', //atrybut ktory uzywamy w html
  standalone: true
})
export class TestDirectiveDirective implements OnInit{

  @Input('appTestDirective') defaultColor: string = 'red'; //jeśli nadam nazwe moge ustawiac bezposrednio z nazwy dyrektywy ale tylko wtedy jedli ma jedna propertke
  @HostBinding('style.backgroundColor') backgroundColor2: string = this.defaultColor;
  //drugi sposób aby przekazac kolor na jaki chcemy ustawic

  //podajemy ścieżkę z elementRefa do propertki elementu, możemu uzywac jesli chcemy zmienic tylko cos w elemencie zamiast Renderer2
  @HostBinding('style.backgroundColor') backgroundColor: string = 'blue';

  constructor(private elementRef: ElementRef, private renderer: Renderer2) { //wstrzykujemy element do konstruktora
    //pierwszy to referencja to elemntu html, drugi sluzy do operowania na nim
  }

  //i robimy coś z nim przy inicjalizacji
  ngOnInit() {
    this.elementRef.nativeElement.style.backgroundColor = 'red'; //tak nie należy robic
    this.renderer.setStyle(this.elementRef.nativeElement, 'background-color', 'red'); //tak jest poprawnie
    this.backgroundColor = 'red';
  }

  //reaguje na event zadeklarowany w nazwie na danym elemencie z uzyta dyrektywą
  @HostListener('mouseenter') mouserClick(event: Event) {
    this.renderer.setStyle(this.elementRef.nativeElement, 'background-color', 'red');
  }
}
