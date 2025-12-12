# 🚀 Quick Start Guide

## Getting Started in 3 Steps

### 1️⃣ Install Dependencies
```bash
npm install
```

### 2️⃣ Start Development Server
```bash
npm start
```

### 3️⃣ Open Browser
Navigate to: **http://localhost:4200/**

---

## 🎮 Try It Out!

### Explore Public Pages
1. **Home** - http://localhost:4200/home
2. **Products** - http://localhost:4200/products
3. **About Us** - http://localhost:4200/about

### Test Authentication
1. Click **Login** in the navigation
2. Enter any credentials:
   - Username: `demo` (min 3 characters)
   - Password: `demo` (min 4 characters)
3. Click **Sign In**
4. You'll be redirected to the **Dashboard**

### Access Protected Pages
After logging in:
- **Dashboard** - http://localhost:4200/dashboard
- **Profile** - http://localhost:4200/profile

### Logout
Click the **Logout** button in:
- The sidebar (left menu)
- The header (top right)

---

## 📁 Project Structure Overview

```
src/app/
├── components/
│   ├── public/          → Home, Products, About, Login
│   └── authenticated/   → Dashboard, Profile
├── layouts/
│   ├── public/          → Public navbar layout
│   └── authenticated/   → Sidebar layout
├── services/
│   └── auth.service.ts  → Authentication logic
├── guards/
│   └── auth.guard.ts    → Route protection
└── app.routes.ts        → All routes
```

---

## 🔑 Key Features

### ✨ Modern Angular 21
- Standalone Components
- Signals for state management
- New `@if` and `@for` syntax
- Functional guards
- Lazy loading

### 🎨 Beautiful UI
- Bootstrap 5
- Responsive design
- Smooth animations
- Modern color schemes
- Bootstrap Icons

### 🔐 Authentication
- Mock auth service
- Route guards
- LocalStorage persistence
- Protected routes

---

## 🛠️ Available Scripts

```bash
npm start              # Start dev server
npm run build          # Build for production
npm run watch          # Build with watch mode
npm test               # Run tests
```

---

## 📱 Responsive Design

Works perfectly on:
- 📱 Mobile phones
- 📱 Tablets
- 💻 Laptops
- 🖥️ Desktop monitors

---

## 🎯 What's Included

### Public Pages ✅
- [x] Home page with hero section
- [x] Products listing
- [x] About us page
- [x] Login form

### Authenticated Pages ✅
- [x] Dashboard with stats
- [x] User profile
- [x] Logout functionality

### Features ✅
- [x] Routing with guards
- [x] Form validation
- [x] State management with signals
- [x] Lazy loading
- [x] SSR support

---

## 💡 Tips

1. **Try the login** with any username/password (min lengths apply)
2. **Check the console** for any errors (there shouldn't be any!)
3. **Resize the browser** to see responsive design in action
4. **Navigate between pages** to see smooth routing
5. **Edit your profile** after logging in

---

## 🐛 Troubleshooting

### Port already in use?
```bash
# Kill the process on port 4200
# Then restart
npm start
```

### Dependencies issues?
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
```

---

## 📚 Learn More

- [Angular Documentation](https://angular.dev)
- [Bootstrap 5 Docs](https://getbootstrap.com/docs/5.3/)
- [Angular Signals](https://angular.dev/guide/signals)

---

## ✅ Checklist

- [ ] Installed dependencies
- [ ] Started dev server
- [ ] Opened http://localhost:4200
- [ ] Explored public pages
- [ ] Tested login
- [ ] Viewed dashboard
- [ ] Edited profile
- [ ] Tested logout

---

**Happy Coding! 🎉**
