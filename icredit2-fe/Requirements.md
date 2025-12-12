# 🚀 Frontend Application Prompt (Angular 21 + Bootstrap 5)

Build a complete **Angular 21** with SSR application using **Bootstrap 5** with the following requirements:

---

## 1. Public Layout

Create a public layout that includes:

- A header section with the **logo positioned on the left**.
- A horizontal navigation bar using Bootstrap 5 `navbar`.
- Navigation links:
  - **Home**
  - **Products**
  - **About Us**
  - **Login**
- The pages **Home**, **Products**, and **About Us** must be **public**, accessible without authentication.
- Each link should route to a dedicated Angular component and display its own content.

---

## 2. Login Page

Create a Login component that includes:

- Username and password input fields.
- A simple mock authentication service (no backend required).
- On successful login, redirect the user to the authenticated dashboard area.

---

## 3. Authenticated Layout (After Login)

After login, the system must display a new layout featuring:

- A top header bar.
- A **left-side vertical menu** (Bootstrap 5 sidebar) with links such as:
  - Dashboard
  - Profile
  - Logout
- A right-side main content area where routed child components will render.

---

## 4. Routing & Guards

Implement Angular routing with:

### Public Routes
- `/home`
- `/products`
- `/about`
- `/login`

### Protected Routes
- `/dashboard`
- `/profile`

Additional requirements:

- Use an **AuthGuard** to restrict access to protected routes.
- If the user is not authenticated, redirect them to `/login`.
- Logout should clear the authentication state and return the user to the public Home page.

---

## 5. Styling & Structure

Use **Bootstrap 5** for all UI layouts:

- Use `navbar`, grid system, spacing utilities, and responsive classes.
- Sidebar must be responsive and vertically aligned.
- Clean, modular Angular file structure:
  - `/components`
  - `/services`
  - `/guards`
  - `/layouts/public`
  - `/layouts/authenticated`

---

## 6. Deliverables

Provide:

- Full Angular 21 project structure.
- All component code (TypeScript, HTML, CSS).
- Routing module with public + protected routes.
- Auth service and AuthGuard implementation.
- Bootstrap 5 navigation bar (public) and sidebar (authenticated).
- Example placeholder content for all sections.

---

## 🎯 Final Goal

Build a fully functional **Angular 21 + Bootstrap 5** front-end application with:

- Public pages  
- Login functionality  
- Authenticated dashboard with vertical menu  
- Clean routing  
- Responsive UI  

