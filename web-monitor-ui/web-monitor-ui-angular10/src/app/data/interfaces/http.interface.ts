import { HttpHeaders } from '@angular/common/http';

export interface BatchRequestParam {
    method: string;
    params: {
        url: string;
        params?: Object;
        body?: any;
        options?: HttpHeaders;
    };
};