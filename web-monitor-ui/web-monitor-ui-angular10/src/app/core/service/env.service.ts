import { Injectable } from '@angular/core';
import { environment } from '@env/environment';

@Injectable({
    providedIn: 'root'
})
export class EnvService {

    constructor() { }

    public getApiBasicUrl(): string {
        return environment.apiBasicUrl;
    }

    public getJsSdkAPIBasicUrl(): string {
        return environment.jsSdkAPIBasicUrl;
    }
}
