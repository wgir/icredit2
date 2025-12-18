import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
    // Default redirect
    {
        path: '',
        redirectTo: '/home',
        pathMatch: 'full'
    },

    // Public routes with public layout
    {
        path: '',
        loadComponent: () => import('./layouts/public/public-layout.component').then(m => m.PublicLayoutComponent),
        children: [
            {
                path: 'home',
                loadComponent: () => import('./components/public/home.component').then(m => m.HomeComponent)
            },
            {
                path: 'products',
                loadComponent: () => import('./components/public/products.component').then(m => m.ProductsComponent)
            },
            {
                path: 'about',
                loadComponent: () => import('./components/public/about.component').then(m => m.AboutComponent)
            },
            {
                path: 'login',
                loadComponent: () => import('./components/public/login.component').then(m => m.LoginComponent)
            }
        ]
    },

    // Protected routes with authenticated layout
    {
        path: '',
        loadComponent: () => import('./layouts/authenticated/authenticated-layout.component').then(m => m.AuthenticatedLayoutComponent),
        canActivate: [authGuard],
        children: [
            {
                path: 'dashboard',
                loadComponent: () => import('./components/authenticated/dashboard/dashboard.component').then(m => m.DashboardComponent)
            },
            {
                path: 'cities',
                loadComponent: () => import('./components/authenticated/city/city-list/city-list.component').then(m => m.CityListComponent)
            },
            {
                path: 'cities/new',
                loadComponent: () => import('./components/authenticated/city/city-form/city-form.component').then(m => m.CityFormComponent)
            },
            {
                path: 'cities/:id',
                loadComponent: () => import('./components/authenticated/city/city-form/city-form.component').then(m => m.CityFormComponent)
            }
        ]
    },

    // Wildcard route - redirect to home
    {
        path: '**',
        redirectTo: '/home'
    }
];
