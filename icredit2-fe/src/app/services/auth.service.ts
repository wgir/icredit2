import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';

export interface User {
    username: string;
    email: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    // Signal to track authentication state
    private isAuthenticatedSignal = signal<boolean>(false);
    private currentUserSignal = signal<User | null>(null);

    // Public readonly signals
    readonly isAuthenticated = this.isAuthenticatedSignal.asReadonly();
    readonly currentUser = this.currentUserSignal.asReadonly();

    constructor(private router: Router) {
        // Check if user is already logged in (from localStorage)
        this.checkAuthStatus();
    }

    private checkAuthStatus(): void {
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

    login(username: string, password: string): boolean {
        // Mock authentication - in real app, call backend API
        if (username && password) {
            const user: User = {
                username: username,
                email: `${username}@example.com`
            };

            // Store auth data
            localStorage.setItem('auth_token', 'mock-jwt-token-' + Date.now());
            localStorage.setItem('current_user', JSON.stringify(user));

            // Update signals
            this.isAuthenticatedSignal.set(true);
            this.currentUserSignal.set(user);

            return true;
        }
        return false;
    }

    logout(): void {
        // Clear auth data
        localStorage.removeItem('auth_token');
        localStorage.removeItem('current_user');

        // Update signals
        this.isAuthenticatedSignal.set(false);
        this.currentUserSignal.set(null);

        // Navigate to home
        this.router.navigate(['/home']);
    }

    getToken(): string | null {
        return localStorage.getItem('auth_token');
    }
}
