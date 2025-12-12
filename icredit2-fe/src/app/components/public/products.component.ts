import { Component, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

interface Product {
    id: number;
    name: string;
    description: string;
    rate: string;
    features: string[];
}

@Component({
    selector: 'app-products',
    standalone: true,
    imports: [RouterLink],
    templateUrl: './products.component.html',
    styleUrl: './products.component.css'
})
export class ProductsComponent {
    products = signal<Product[]>([
        {
            id: 1,
            name: 'Personal Loan',
            description: 'Quick and easy personal loans for your immediate needs',
            rate: '5.99% APR',
            features: ['Up to $50,000', 'Flexible terms', 'No prepayment penalty', 'Quick approval']
        },
        {
            id: 2,
            name: 'Business Credit',
            description: 'Grow your business with our flexible credit solutions',
            rate: '6.99% APR',
            features: ['Up to $500,000', 'Business rewards', 'Expense tracking', 'Dedicated support']
        },
        {
            id: 3,
            name: 'Credit Card',
            description: 'Premium credit cards with exclusive benefits',
            rate: '14.99% APR',
            features: ['Cash back rewards', 'Travel benefits', 'Zero annual fee', 'Fraud protection']
        },
        {
            id: 4,
            name: 'Auto Loan',
            description: 'Finance your dream car with competitive rates',
            rate: '4.99% APR',
            features: ['New & used cars', 'Up to 72 months', 'Pre-approval', 'Trade-in options']
        }
    ]);
}
