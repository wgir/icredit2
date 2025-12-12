import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [ReactiveFormsModule],
    templateUrl: './login.component.html',
    styleUrl: './login.component.css'
})
export class LoginComponent {
    private authService = inject(AuthService);
    private router = inject(Router);
    private fb = inject(FormBuilder);

    loginForm: FormGroup;
    errorMessage = signal<string>('');
    isLoading = signal<boolean>(false);

    constructor() {
        this.loginForm = this.fb.group({
            username: ['', [Validators.required, Validators.minLength(3)]],
            password: ['', [Validators.required, Validators.minLength(4)]]
        });
    }

    onSubmit(): void {
        if (this.loginForm.valid) {
            this.isLoading.set(true);
            this.errorMessage.set('');

            const { username, password } = this.loginForm.value;

            // Simulate API call delay
            setTimeout(() => {
                const success = this.authService.login(username, password);

                if (success) {
                    this.router.navigate(['/dashboard']);
                } else {
                    this.errorMessage.set('Invalid credentials. Please try again.');
                }

                this.isLoading.set(false);
            }, 500);
        } else {
            this.errorMessage.set('Please fill in all required fields.');
        }
    }

    get username() {
        return this.loginForm.get('username');
    }

    get password() {
        return this.loginForm.get('password');
    }
}
