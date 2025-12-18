# iCredit v2.0 - Frontend Documentation

## Overview
iCredit v2.0 is a modern Angular application designed to manage credit and city configurations. It features a public facing portal and a secured authenticated dashboard with role-based access control.

### Key Features
- **Public Layout**: Home, Products, About Us, and Login pages.
- **Authenticated Dashboard**: Protected area for logged-in users.
- **City Management**: CRUD operations for managing city data (Configuration Menu).
- **Authentication**: JWT-based auth with guards and interceptors.
- **Responsive Design**: Bootstrap 5 integration for mobile-first layouts.

## Prerequisities
- **Node.js**: v18.13.0 or higher
- **npm**: v8.0.0 or higher
- **Angular CLI**: v17.0.0 or higher

## Development Commands

### 1. Run Application
To start the development server, run:

```bash
npm start
```
*   **Description**: Compiles the application and starts a development server.
*   **Access**: Open your browser and navigate to `http://localhost:4200/`.
*   **Live Reload**: The app will automatically reload if you change any of the source files.

### 2. Build / Compile
To build the project for production, run:

```bash
npm run build
```
*   **Description**: The build artifacts will be stored in the `dist/` directory.
*   **Optimization**: This command uses the production build configuration (optimizes output bundles).

### 3. Running Unit Tests
To execute the unit tests via Karma/Jasmine:

```bash
npm test
```

## E2E Testing (Cypress)
This project uses Cypress for End-to-End testing. The tests are located in the `cypress/e2e` directory and follow the Page Object Model (POM) pattern.

### Directory Structure
*   `cypress/e2e/public/`: Tests for public pages (Login, Navigation).
*   `cypress/e2e/authenticated/`: Tests for protected features (Dashboard, City CRUD).
*   `cypress/pages/`: Page Object classes for reusability.

### Run Tests in Headless Mode
To run all tests in the command line (useful for CI/CD):

```bash
npx cypress run
```

### Run Tests in Interactive Mode
To open the Cypress Test Runner and watch tests execute in a browser:

```bash
npx cypress open
```
1.  Select **E2E Testing**.
2.  Choose a browser (e.g., Chrome).
3.  Click on the spec file you want to run (e.g., `authenticated/cities.cy.ts`).

## Project Structure
```text
src/
  app/
    components/      # UI Components (Public & Authenticated)
    guards/          # Route Guards (AuthGuard)
    interceptors/    # HTTP Interceptors (Token injection)
    layouts/         # Layout definitions (Public vs Authenticated)
    models/          # TypeScript Interfaces/Models
    services/        # API Services (Auth, City)
cypress/
  e2e/               # Test Specifications
  pages/             # Page Objects
```
