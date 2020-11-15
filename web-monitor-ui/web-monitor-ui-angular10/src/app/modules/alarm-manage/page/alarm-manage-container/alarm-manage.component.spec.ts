import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AlarmManageComponent } from './alarm-manage.component';

describe('AlarmManageComponent', () => {
    let component: AlarmManageComponent;
    let fixture: ComponentFixture<AlarmManageComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [AlarmManageComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AlarmManageComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
