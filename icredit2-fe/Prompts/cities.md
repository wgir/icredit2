You are a frontend developer working with an existing Angular 21 application.

Context:
The backend provides the City CRUD endpoints for managing cities, scoped to a specific company. The frontend needs to consume these endpoints and display/manage cities through a UI. All endpoints require JWT authentication.

API Endpoints:

POST /api/cities - Create a new city

GET /api/cities - List all cities for the authenticated user’s company

GET /api/cities/{id} - Get city details by ID

PUT /api/cities/{id} - Update an existing city

DELETE /api/cities/{id} - Delete a city

Objective:

Integrate the City CRUD API into your Angular frontend to allow users to:

List cities

Create a new city

Edit a city

Delete a city

Ensure proper JWT handling for authentication and company isolation (cities should be visible/accessible only for the user's company).

Steps to Integrate City CRUD into Angular 21
1. Create a City Service to Consume the API

In your Angular app, create a service to interact with the City endpoints. The model of city is:

{
    id: string;  //UUID
    name: string;
}


2. Create a City Component for Displaying and Managing Cities

Generate a component to list cities and allow users to add, edit, and delete them.

3. Create a Component for Adding and Editing a City

4. Add Routing for the City Components

5. Conclusion

CityService handles API calls (with JWT tokens).

CityListComponent lists, deletes, and navigates to the edit form.

CityFormComponent handles adding and updating cities.

Routing ensures access to the city CRUD views.

This structure will enable you to fully integrate the City CRUD functionality into your Angular 21 app, using JWT for authentication and ensuring company-specific access.

