import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { JsErrorLogComponent } from './js-error-log.component';

describe('JsErrorLogComponent', () => {
  let component: JsErrorLogComponent;
  let fixture: ComponentFixture<JsErrorLogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ JsErrorLogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(JsErrorLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
