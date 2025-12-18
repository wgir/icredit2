class DashboardPage {
    visit() {
        cy.visit('/dashboard');
    }

    getWelcomeMessage() {
        return cy.contains('Welcome back');
    }
}
export default new DashboardPage();
