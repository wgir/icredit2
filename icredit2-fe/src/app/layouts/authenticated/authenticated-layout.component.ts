import { Component, inject } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-authenticated-layout',
    standalone: true,
    imports: [RouterOutlet, RouterLink, RouterLinkActive],
    templateUrl: './authenticated-layout.component.html',
    styleUrl: './authenticated-layout.component.css'
})
export class AuthenticatedLayoutComponent {
    authService = inject(AuthService);

    logout(): void {
        this.authService.logout();
    }
}
