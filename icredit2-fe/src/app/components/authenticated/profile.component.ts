import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-profile',
    standalone: true,
    imports: [ReactiveFormsModule],
    templateUrl: './profile.component.html',
    styleUrl: './profile.component.css'
})
export class ProfileComponent {
    authService = inject(AuthService);
    private fb = inject(FormBuilder);

    profileForm: FormGroup;
    successMessage = signal<string>('');
    isEditing = signal<boolean>(false);

    constructor() {
        const user = this.authService.currentUser();
        this.profileForm = this.fb.group({
            username: [{ value: user?.username || '', disabled: true }],
            email: [user?.email || '', [Validators.required, Validators.email]],
            phone: ['', [Validators.pattern(/^\d{10}$/)]],
            address: ['']
        });
    }

    toggleEdit(): void {
        this.isEditing.set(!this.isEditing());
        if (this.isEditing()) {
            this.profileForm.get('email')?.enable();
            this.profileForm.get('phone')?.enable();
            this.profileForm.get('address')?.enable();
        } else {
            this.profileForm.get('email')?.disable();
            this.profileForm.get('phone')?.disable();
            this.profileForm.get('address')?.disable();
        }
    }

    onSubmit(): void {
        if (this.profileForm.valid) {
            // Simulate API call
            setTimeout(() => {
                this.successMessage.set('Profile updated successfully!');
                this.isEditing.set(false);
                this.profileForm.get('email')?.disable();
                this.profileForm.get('phone')?.disable();
                this.profileForm.get('address')?.disable();

                // Clear success message after 3 seconds
                setTimeout(() => this.successMessage.set(''), 3000);
            }, 500);
        }
    }

    cancelEdit(): void {
        this.isEditing.set(false);
        this.profileForm.reset();
        const user = this.authService.currentUser();
        this.profileForm.patchValue({
            username: user?.username || '',
            email: user?.email || ''
        });
        this.profileForm.get('email')?.disable();
        this.profileForm.get('phone')?.disable();
        this.profileForm.get('address')?.disable();
    }
}
