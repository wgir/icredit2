import { Injectable, signal, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap, of, map } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

export interface User {
    user_name: string;
    email: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private platformId = inject(PLATFORM_ID);
    private isBrowser = isPlatformBrowser(this.platformId);
    private http = inject(HttpClient);
    private router = inject(Router);

    // Signal to track authentication state
    private isAuthenticatedSignal = signal<boolean>(false);
    private currentUserSignal = signal<User | null>(null);

    // Public readonly signals
    readonly isAuthenticated = this.isAuthenticatedSignal.asReadonly();
    readonly currentUser = this.currentUserSignal.asReadonly();

    constructor() {
        // Initial check without blocking, useful for UI updates
        this.checkAuth().subscribe();
    }

    checkAuth(): Observable<boolean> {
        return this.http.get<User>('/v1/auth/me').pipe(
            tap(user => {
                this.isAuthenticatedSignal.set(true);
                this.currentUserSignal.set(user);
            }),
            map(() => true),
            catchError(() => {
                this.isAuthenticatedSignal.set(false);
                this.currentUserSignal.set(null);
                return of(false);
            })
        );
    }

    login(email: string, password: string): Observable<any> {
        const url = '/v1/auth/login';
        const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
        const body = { email, password };

        return this.http.post<any>(url, body, { headers }).pipe(
            tap(response => {
                this.isAuthenticatedSignal.set(true);
            })
        );
    }

    logout(): void {
        this.http.post('/v1/auth/logout', {}).subscribe(() => {
            this.isAuthenticatedSignal.set(false);
            this.currentUserSignal.set(null);
            this.router.navigate(['/home']);
        });
    }
}
