# iCredit Angular Application

A modern **Angular 21** application with **Server-Side Rendering (SSR)** and **Bootstrap 5**, featuring public and authenticated layouts with role-based routing.

## 🚀 Features

### Public Features
- **Home Page**: Landing page with hero section, features, and call-to-action
- **Products Page**: Display of financial products with detailed information
- **About Us Page**: Company information, mission, vision, and statistics
- **Login Page**: Authentication with form validation

### Authenticated Features
- **Dashboard**: Overview with statistics, recent activity, and quick actions
- **Profile**: User profile management with editable form
- **Secure Routes**: Protected by authentication guard

## 🏗️ Architecture

### Modern Angular 21 Features
- ✅ **Standalone Components**: No NgModules required
- ✅ **Signals**: Fine-grained reactivity for state management
- ✅ **New Control Flow**: `@if`, `@for`, `@switch` syntax
- ✅ **Functional Guards**: `CanActivateFn` with `inject()`
- ✅ **Lazy Loading**: Components loaded on demand
- ✅ **Reactive Forms**: Type-safe form handling
- ✅ **SSR Support**: Server-Side Rendering enabled

### Project Structure
```
src/app/
├── components/
│   ├── public/              # Public pages
│   │   ├── home.component.*
│   │   ├── products.component.*
│   │   ├── about.component.*
│   │   └── login.component.*
│   └── authenticated/       # Protected pages
│       ├── dashboard.component.*
│       └── profile.component.*
├── layouts/
│   ├── public/              # Public layout with navbar
│   │   └── public-layout.component.*
│   └── authenticated/       # Auth layout with sidebar
│       └── authenticated-layout.component.*
├── services/
│   └── auth.service.ts      # Authentication service
├── guards/
│   └── auth.guard.ts        # Route protection
├── app.routes.ts            # Routing configuration
└── app.ts                   # Root component
```

## 🛠️ Technologies

- **Angular**: 21.0.0
- **Bootstrap**: 5.3.8
- **TypeScript**: 5.9.2
- **RxJS**: 7.8.0
- **Node.js**: 20.19.4
- **npm**: 10.8.2

## 📦 Installation

1. **Clone the repository** (if applicable)
   ```bash
   git clone <repository-url>
   cd fe
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

## 🚀 Running the Application

### Development Server
```bash
npm start
```
Navigate to `http://localhost:4200/`. The application will automatically reload when you change source files.

### Build for Production
```bash
npm run build
```
The build artifacts will be stored in the `dist/` directory.

### Run SSR Server
```bash
npm run serve:ssr:icredit-app
```

## 🔐 Authentication

The application uses a **mock authentication service** for demonstration purposes.

### Demo Credentials
- **Username**: Any username (minimum 3 characters)
- **Password**: Any password (minimum 4 characters)

### How It Works
1. Enter any valid username and password on the login page
2. Upon successful login, you'll be redirected to the dashboard
3. Authentication state is stored in `localStorage`
4. Protected routes are guarded by `authGuard`

## 🗺️ Routing

### Public Routes
- `/home` - Home page
- `/products` - Products listing
- `/about` - About us
- `/login` - Login page

### Protected Routes (Requires Authentication)
- `/dashboard` - User dashboard
- `/profile` - User profile

### Route Guards
- **authGuard**: Protects authenticated routes
- Redirects to `/login` if not authenticated
- Stores return URL for post-login redirect

## 🎨 Styling

### Bootstrap 5
- Responsive grid system
- Pre-built components
- Utility classes
- Bootstrap Icons

### Custom Styles
- Smooth animations and transitions
- Hover effects
- Modern color schemes
- Responsive design

## 📱 Responsive Design

The application is fully responsive and works on:
- 📱 Mobile devices
- 📱 Tablets
- 💻 Desktops
- 🖥️ Large screens

## 🔧 Development

### Generate New Components
```bash
ng generate component components/component-name
```

### Generate Services
```bash
ng generate service services/service-name
```

### Generate Guards
```bash
ng generate guard guards/guard-name
```

## 📝 Code Quality

### TypeScript Strict Mode
The project uses TypeScript strict mode for better type safety.

### OnPush Change Detection
Components use OnPush change detection strategy for better performance.

### Smart/Dumb Component Pattern
- **Smart Components**: Handle business logic and state
- **Dumb Components**: Pure presentation components

## 🧪 Testing

```bash
npm test
```

## 📚 Key Concepts

### Signals
Used for reactive state management:
```typescript
const count = signal(0);
const doubled = computed(() => count() * 2);
```

### New Control Flow
```html
@if (condition) {
  <div>Content</div>
}

@for (item of items; track item.id) {
  <div>{{ item.name }}</div>
}
```

### Dependency Injection
```typescript
private authService = inject(AuthService);
```

## 🔄 State Management

- **Signals**: For component-level state
- **Services**: For shared state
- **LocalStorage**: For persistent authentication

## 🚧 Future Enhancements

- [ ] Connect to real backend API
- [ ] Add JWT token management
- [ ] Implement refresh token logic
- [ ] Add more pages and features
- [ ] Add unit and e2e tests
- [ ] Add internationalization (i18n)
- [ ] Add dark mode support
- [ ] Add PWA capabilities

## 📄 License

This project is for demonstration purposes.

## 👥 Contributing

Contributions are welcome! Please follow the Angular style guide and best practices.

## 📞 Support

For support, please contact the development team.

---

**Built with ❤️ using Angular 21 and Bootstrap 5**
