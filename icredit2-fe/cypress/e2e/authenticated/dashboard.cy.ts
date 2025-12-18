import DashboardPage from '../../pages/DashboardPage';

describe('4.2 Authenticated layout - Dashboard', () => {

    const user = { user_name: 'testuser', email: 'test@example.com' };

    beforeEach(() => {
        // Mock authentication for every test in this block
        cy.intercept('GET', '/v1/auth/me', {
            statusCode: 200,
            body: user
        }).as('authCheck');
    });

    it('4.2.1 Dashboard page', () => {
        DashboardPage.visit(); // This triggers AuthGuard -> authCheck
        cy.wait('@authCheck');
        DashboardPage.getWelcomeMessage().should('contain', 'testuser');
    });
});
