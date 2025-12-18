import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { City } from '../models/city.model';


@Injectable({
    providedIn: 'root'
})
export class CityService {
    private apiUrl = '/v1/cities';
    private http = inject(HttpClient);

    getAll(): Observable<City[]> {
        return this.http.get<City[]>(this.apiUrl);
    }

    getById(id: string): Observable<City> {
        return this.http.get<City>(`${this.apiUrl}/${id}`);
    }

    create(city: Partial<City>): Observable<City> {
        return this.http.post<City>(this.apiUrl, city);
    }

    update(id: string, city: Partial<City>): Observable<City> {
        return this.http.put<City>(`${this.apiUrl}/${id}`, city);
    }

    delete(id: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/${id}`);
    }
}
