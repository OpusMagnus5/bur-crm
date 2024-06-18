import {
  AfterViewInit,
  booleanAttribute,
  Directive,
  ElementRef,
  Input,
  OnChanges,
  Renderer2,
  SimpleChanges
} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";

@Directive({
  selector: '[showNoData]',
  standalone: true
})
export class ShowNoDataDirective implements AfterViewInit, OnChanges {

  @Input({ transform: booleanAttribute, alias: "showNoData", required: true }) showNoData!: boolean;
  @Input({ required: true }) data!: any

  constructor(
    private elementRef: ElementRef,
    private renderer: Renderer2,
    private translator: TranslateService
  ) {
  }

  ngAfterViewInit(): void {
    this.checkAndSetContent();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['showNoData']) {
      this.checkAndSetContent();
    }
  }

  private checkAndSetContent() {
    const paragraph = this.elementRef.nativeElement;
    if (this.showNoData) {
      this.translator.get('common.no-data').subscribe(text => {
        this.renderer.setProperty(paragraph, 'textContent', text);
      })
    } else {
      this.renderer.setProperty(paragraph, 'textContent', this.data);
    }
  }
}
