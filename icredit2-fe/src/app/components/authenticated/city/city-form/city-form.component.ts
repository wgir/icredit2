import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { CityService } from '../../../../services/city.service';

@Component({
    selector: 'app-city-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    templateUrl: './city-form.component.html',
    styles: []
})
export class CityFormComponent implements OnInit {
    private fb = inject(FormBuilder);
    private cityService = inject(CityService);
    private router = inject(Router);
    private route = inject(ActivatedRoute);

    cityForm = this.fb.group({
        name: ['', [Validators.required]],
        active: [true]
    });

    isEditMode = signal(false);
    cityId: string | null = null;

    ngOnInit(): void {
        this.cityId = this.route.snapshot.paramMap.get('id');
        if (this.cityId) {
            this.isEditMode.set(true);
            this.cityService.getById(this.cityId).subscribe({
                next: (city) => {
                    if (city) {
                        this.cityForm.patchValue({ name: city.name, active: city.active });
                    }
                },
                error: (err) => console.error('Error loading city', err)
            });
        }
    }

    onSubmit(): void {
        if (this.cityForm.invalid) return;

        const cityData = { name: this.cityForm.value.name!, active: this.cityForm.value.active! };

        if (this.isEditMode() && this.cityId) {
            this.cityService.update(this.cityId, cityData).subscribe({
                next: () => this.router.navigate(['/cities']),
                error: (err) => console.error('Error updating city', err)
            });
        } else {
            this.cityService.create(cityData).subscribe({
                next: () => this.router.navigate(['/cities']),
                error: (err) => console.error('Error creating city', err)
            });
        }
    }
}
