import {Directive, ElementRef, Input, Renderer2} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";

@Directive({
  selector: '[noDataIfEmpty]',
  standalone: true
})
export class NoDataIfEmptyDirective {

  @Input('noDataIfEmpty') testData: any = null;

  constructor(
    private elementRef: ElementRef,
    private renderer: Renderer2,
    private translator: TranslateService
  ) {
    this.checkAndSetContent();
  }

  private checkAndSetContent() {
    const paragraph = this.elementRef.nativeElement;
    if (!this.testData) {
      this.translator.get('common.no-data').subscribe(text => {
        this.renderer.setProperty(paragraph, 'textContent', text);
      })
    }
  }
}
