import CityListPage from '../../pages/CityListPage';
import CityFormPage from '../../pages/CityFormPage';

describe('4.2 Authenticated layout - Configuration menu - Cities', () => {

    const user = { user_name: 'testuser', email: 'test@example.com' };

    beforeEach(() => {
        // Mock authentication for every test in this block
        cy.intercept('GET', '/v1/auth/me', {
            statusCode: 200,
            body: user
        }).as('authCheck');
    });

    it('4.2.2 Configuration menu - Cities CRUD', () => {
        const cities = [
            { id: '1', name: 'New York', active: true },
            { id: '2', name: 'London', active: false }
        ];

        cy.intercept('GET', '/v1/cities', { body: cities }).as('getCities');

        CityListPage.visit();
        cy.wait('@authCheck'); // Guard
        cy.wait('@getCities');
        CityListPage.getCityRows().should('have.length', 2);

        // 4.2.2.1.1 City create
        cy.intercept('POST', '/v1/cities', {
            statusCode: 201,
            body: { id: '3', name: 'Paris', active: true }
        }).as('createCity');

        // Mock getting list again after create if needed
        cy.intercept('GET', '/v1/cities', {
            body: [...cities, { id: '3', name: 'Paris', active: true }]
        }).as('getCitiesUpdated');

        CityListPage.goToCreateCity();
        CityFormPage.fillName('Paris');
        CityFormPage.submit();
        cy.wait('@createCity');

        // Should go back to list
        cy.location('pathname').should('eq', '/cities');
        cy.wait('@getCitiesUpdated');
        CityListPage.getCityRows().should('have.length', 3);

        // 4.2.2.1.2 City edit
        cy.intercept('GET', '/v1/cities/1', { body: cities[0] }).as('getCity1');
        cy.intercept('PUT', '/v1/cities/1', { body: { ...cities[0], active: false } }).as('updateCity');

        // Navigate to edit (assuming row 1 is ID 1)
        cy.get('a[href="/cities/1"]').click();
        cy.wait('@getCity1');

        CityFormPage.toggleActive(); // Uncheck active
        CityFormPage.submit();
        cy.wait('@updateCity');
        cy.location('pathname').should('eq', '/cities');
    });
});
