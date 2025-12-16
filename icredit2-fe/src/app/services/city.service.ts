import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { City } from '../models/city.model';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root'
})
export class CityService {
    private apiUrl = '/v1/cities';
    private http = inject(HttpClient);
    private authService = inject(AuthService);

    private getHeaders(): HttpHeaders {
        const token = this.authService.getToken();
        let headers = new HttpHeaders();
        if (token) {
            headers = headers.set('Authorization', `Bearer ${token}`);
        }
        return headers;
    }

    getAll(): Observable<City[]> {
        return this.http.get<City[]>(this.apiUrl, { headers: this.getHeaders() });
    }

    getById(id: string): Observable<City> {
        return this.http.get<City>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
    }

    create(city: Partial<City>): Observable<City> {
        return this.http.post<City>(this.apiUrl, city, { headers: this.getHeaders() });
    }

    update(id: string, city: Partial<City>): Observable<City> {
        return this.http.put<City>(`${this.apiUrl}/${id}`, city, { headers: this.getHeaders() });
    }

    delete(id: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() });
    }
}
