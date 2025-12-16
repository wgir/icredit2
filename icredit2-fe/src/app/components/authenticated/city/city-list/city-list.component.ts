import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CityService } from '../../../../services/city.service';
import { City } from '../../../../models/city.model';

@Component({
    selector: 'app-city-list',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './city-list.component.html',
    styles: []
})
export class CityListComponent implements OnInit {
    private cityService = inject(CityService);
    cities = signal<City[]>([]);

    ngOnInit(): void {
        this.loadCities();
    }

    loadCities(): void {
        this.cityService.getAll().subscribe({
            next: (data) => this.cities.set(data),
            error: (err) => console.error('Error loading cities', err)
        });
    }

    deleteCity(id: string): void {
        if (confirm('Are you sure you want to delete this city?')) {
            this.cityService.delete(id).subscribe({
                next: () => this.loadCities(),
                error: (err) => console.error('Error deleting city', err)
            });
        }
    }
}
