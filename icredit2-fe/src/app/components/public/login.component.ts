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
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(4)]]
        });
    }

    onSubmit(): void {
        if (this.loginForm.valid) {
            this.isLoading.set(true);
            this.errorMessage.set('');

            const { email, password } = this.loginForm.value;

            this.authService.login(email, password).subscribe({
                next: () => {
                    this.isLoading.set(false);
                    this.router.navigate(['/dashboard']);
                },
                error: (err) => {
                    console.error('Login error', err);
                    this.errorMessage.set('Invalid credentials or server error. Please try again.');
                    this.isLoading.set(false);
                }
            });
        } else {
            this.errorMessage.set('Please fill in all required fields.');
        }
    }

    get email() {
        return this.loginForm.get('email');
    }

    get password() {
        return this.loginForm.get('password');
    }
}
