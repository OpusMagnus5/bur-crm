import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministrationUsersMenuComponent } from './administration-users-menu.component';

describe('AdministrationUsersMenuComponent', () => {
  let component: AdministrationUsersMenuComponent;
  let fixture: ComponentFixture<AdministrationUsersMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdministrationUsersMenuComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdministrationUsersMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
