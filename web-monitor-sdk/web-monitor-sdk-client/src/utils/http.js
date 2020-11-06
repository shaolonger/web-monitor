import HttpClient from '../lib/httpClient';
import {
    baseUrl
} from '../config/index';

const httpClient = new HttpClient({
    baseUrl
});

export default httpClient;