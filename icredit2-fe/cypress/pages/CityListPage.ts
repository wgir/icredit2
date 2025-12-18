class CityListPage {
    visit() {
        cy.visit('/cities');
    }

    goToCreateCity() {
        cy.get('a[href="/cities/new"]').click();
    }

    getCityRows() {
        return cy.get('table tbody tr');
    }
}
export default new CityListPage();
