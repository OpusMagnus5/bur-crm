import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministrationSideMenuComponent } from './administration-side-menu.component';

describe('AdministrationSideMenuComponent', () => {
  let component: AdministrationSideMenuComponent;
  let fixture: ComponentFixture<AdministrationSideMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdministrationSideMenuComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdministrationSideMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
