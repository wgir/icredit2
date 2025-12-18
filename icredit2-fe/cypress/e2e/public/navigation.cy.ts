
describe('4.1 Public layout: Navigation and Pages', () => {

    it('4.1.1 Home', () => {
        cy.visit('/home');
        // Just checking it loads without error, specific content check can be added
        cy.get('body').should('exist');
    });

    it('4.1.2 Products', () => {
        cy.visit('/products');
        cy.get('body').should('exist');
    });

    it('4.1.3 About', () => {
        cy.visit('/about');
        cy.get('body').should('exist');
    });
});
