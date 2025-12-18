import LoginPage from '../../pages/LoginPage';

describe('Login Page: Logic', () => {

    const user = { user_name: 'testuser', email: 'test@example.com' };

    it('4.1.4.1 With incorrect values', () => {
        cy.intercept('POST', '/v1/auth/login', {
            statusCode: 401,
            body: { message: 'Invalid credentials' }
        }).as('loginFail');

        LoginPage.visit();
        LoginPage.fillEmail('test@test.com');
        LoginPage.fillPassword('wrong');
        LoginPage.submit();
        cy.wait('@loginFail');
        LoginPage.getErrorMessage().should('exist');
    });

    it('4.1.4.1 With correct values', () => {
        // Correct values
        cy.intercept('POST', '/v1/auth/login', {
            statusCode: 200,
            body: { user }
        }).as('loginSuccess');

        // AuthGuard will call this when we navigate to dashboard
        cy.intercept('GET', '/v1/auth/me', {
            statusCode: 200,
            body: user
        }).as('getMe');

        LoginPage.visit();
        LoginPage.fillEmail('williamgustavo@gmail.com');
        LoginPage.fillPassword('12345');
        LoginPage.submit();
        cy.wait('@loginSuccess');

        // If login component redirects to /dashboard:
        cy.location('pathname').should('eq', '/dashboard');
        cy.wait('@getMe'); // Guard calls this
    });
});
