class LoginPage {
    visit() {
        cy.visit('/login');
    }

    fillEmail(email: string) {
        cy.get('#email').clear().type(email);
    }

    fillPassword(password: string) {
        cy.get('#password').clear().type(password);
    }

    submit() {
        cy.get('button[type="submit"]').click();
    }

    getErrorMessage() {
        return cy.get('.alert-danger');
    }
}
export default new LoginPage();
