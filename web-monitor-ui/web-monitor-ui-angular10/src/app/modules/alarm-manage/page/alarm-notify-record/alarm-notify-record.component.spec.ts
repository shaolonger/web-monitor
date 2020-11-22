import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AlarmNotifyRecordComponent } from './alarm-notify-record.component';

describe('AlarmNotifyRecordComponent', () => {
    let component: AlarmNotifyRecordComponent;
    let fixture: ComponentFixture<AlarmNotifyRecordComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [AlarmNotifyRecordComponent]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AlarmNotifyRecordComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
