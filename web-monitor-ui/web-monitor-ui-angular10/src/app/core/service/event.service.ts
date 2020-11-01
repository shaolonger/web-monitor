import { Injectable, EventEmitter } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class EventService {

    public readonly eventEmitter: any;

    constructor() {
        this.eventEmitter = new EventEmitter();
    }
}
