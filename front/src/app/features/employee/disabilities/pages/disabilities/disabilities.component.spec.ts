import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DisabilitiesComponent } from './disabilities.component';

describe('DisabilitiesComponent', () => {
  let component: DisabilitiesComponent;
  let fixture: ComponentFixture<DisabilitiesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DisabilitiesComponent]
    });
    fixture = TestBed.createComponent(DisabilitiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
