class CityFormPage {
    fillName(name: string) {
        cy.get('#name').clear().type(name);
    }

    toggleActive() {
        cy.get('#active').click();
    }

    submit() {
        cy.get('button[type="submit"]').click();
    }
}
export default new CityFormPage();
