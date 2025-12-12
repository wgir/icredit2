import { Component, inject, signal, computed } from '@angular/core';
import { AuthService } from '../../services/auth.service';

interface DashboardStats {
    totalLoans: number;
    activeLoans: number;
    creditScore: number;
    availableCredit: number;
}

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [],
    templateUrl: './dashboard.component.html',
    styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
    authService = inject(AuthService);

    stats = signal<DashboardStats>({
        totalLoans: 3,
        activeLoans: 2,
        creditScore: 750,
        availableCredit: 25000
    });

    // Computed signal example
    creditScoreStatus = computed(() => {
        const score = this.stats().creditScore;
        if (score >= 750) return { label: 'Excellent', class: 'success' };
        if (score >= 700) return { label: 'Good', class: 'info' };
        if (score >= 650) return { label: 'Fair', class: 'warning' };
        return { label: 'Poor', class: 'danger' };
    });
}
