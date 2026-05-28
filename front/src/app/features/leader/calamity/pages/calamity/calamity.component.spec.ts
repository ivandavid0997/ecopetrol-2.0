import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalamityComponent } from './calamity.component';

describe('CalamityComponent', () => {
  let component: CalamityComponent;
  let fixture: ComponentFixture<CalamityComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CalamityComponent]
    });
    fixture = TestBed.createComponent(CalamityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
