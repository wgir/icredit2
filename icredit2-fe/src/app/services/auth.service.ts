import { Injectable, signal, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';

export interface User {
    username: string;
    email: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private platformId = inject(PLATFORM_ID);
    private isBrowser = isPlatformBrowser(this.platformId);

    // Signal to track authentication state
    private isAuthenticatedSignal = signal<boolean>(false);
    private currentUserSignal = signal<User | null>(null);

    // Public readonly signals
    readonly isAuthenticated = this.isAuthenticatedSignal.asReadonly();
    readonly currentUser = this.currentUserSignal.asReadonly();

    constructor(private router: Router, private http: HttpClient) {
        // Check if user is already logged in (from localStorage)
        this.checkAuthStatus();
    }

    private checkAuthStatus(): void {
        if (!this.isBrowser) return;

        const token = localStorage.getItem('auth_token');
        const userStr = localStorage.getItem('current_user');

        if (token && userStr) {
            try {
                const user = JSON.parse(userStr);
                this.isAuthenticatedSignal.set(true);
                this.currentUserSignal.set(user);
            } catch (error) {
                this.logout();
            }
        }
    }

    login(email: string, password: string): Observable<any> {
        const url = '/v1/auth/login';
        const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
        const body = { email, password };

        return this.http.post<any>(url, body, { headers }).pipe(
            tap(response => {
                // Assuming the response contains a token and potentially user details
                // Adjust this based on actual backend response structure
                if (this.isBrowser && response.token) {
                    localStorage.setItem('auth_token', response.token);
                    if (response.user) {
                        localStorage.setItem('current_user', JSON.stringify(response.user));
                        this.currentUserSignal.set(response.user);
                    }
                }

                this.isAuthenticatedSignal.set(true);
            })
        );
    }

    logout(): void {
        // Clear auth data (only in browser)
        if (this.isBrowser) {
            localStorage.removeItem('auth_token');
            localStorage.removeItem('current_user');
        }

        // Update signals
        this.isAuthenticatedSignal.set(false);
        this.currentUserSignal.set(null);

        // Navigate to home
        this.router.navigate(['/home']);
    }

    getToken(): string | null {
        if (!this.isBrowser) return null;
        return localStorage.getItem('auth_token');
    }
}
