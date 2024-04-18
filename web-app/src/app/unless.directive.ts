import {Directive, Input, TemplateRef, ViewContainerRef} from '@angular/core';

@Directive({
  selector: '[unless]',
  standalone: true
})
export class UnlessDirective {

  /*tutaj budujemy dyrektywe strukturalną, @Input używamy nadal jako property tylko że chcemy ustawić jednocześnie seter*/
  @Input('unless') set unless(condition: boolean) {
    if (!condition) {
      this.vcRef.createEmbeddedView(this.template);
    } else {
      this.vcRef.clear();
    }
  }

  /*Template Ref to referencja to templetu który ma się pojawic badz zniknąc, a view conatiner zarządza widokiem*/
  constructor(private template: TemplateRef<any>, private vcRef: ViewContainerRef) { }

}
