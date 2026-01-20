# Social Network Frontend Application

A modern, responsive social network web application built with React, TypeScript, and Material-UI.

## 🚀 Technology Stack

### Core Technologies (Stable & Battle-Tested)
- **React 18+** - UI library
- **TypeScript 5.x** - Type safety
- **Vite 5.x** - Build tool and dev server
- **Material-UI (MUI) 5.x** - Component library
- **React Router v6** - Client-side routing
- **Zustand 4.x** - Client state management
- **React Query (TanStack Query) 5.x** - Server state management
- **Axios 1.x** - HTTP client
- **React Hook Form 7.x** - Form handling
- **Yup 1.x** - Form validation
- **date-fns 2.x** - Date formatting

## 📋 Prerequisites

Before running this application, ensure you have:

- **Node.js 18.x LTS or 20.x LTS** installed
- **npm 9.x or 10.x** (comes with Node.js)
- **Backend API running** on `http://localhost` (via NGINX load balancer) or `http://localhost:9091` (direct backend access)

## 🛠️ Installation & Setup

### 1. Install Dependencies

```bash
cd frontend
npm install
```

### 2. Configure Environment Variables

Create a `.env` file in the `frontend` directory:

```env
# Uses NGINX load balancer on port 80 (recommended for Docker Compose)
VITE_API_BASE_URL=http://localhost
VITE_API_TIMEOUT=10000
VITE_APP_NAME=Social Network
VITE_DEFAULT_PAGE_SIZE=10
```

**Note:** For direct backend access without the load balancer, use:
```env
VITE_API_BASE_URL=http://localhost:9091
```

### 3. Set Up User Authentication

For development, you need to set a user ID in localStorage. Open your browser console and run:

```javascript
localStorage.setItem('auth-storage', JSON.stringify({ 
  state: { currentUserId: 1, isAuthenticated: true }, 
  version: 0 
}));
```

Then refresh the page.

## 🏃 Running the Application

### Development Mode

Start the development server with hot module replacement:

```bash
npm run dev
```

The application will be available at `http://localhost:5173`

### Build for Production

```bash
npm run build
```

The build output will be in the `dist` directory.

### Preview Production Build

```bash
npm run preview
```

## 📁 Project Structure

```
frontend/
├── src/
│   ├── components/          # React components
│   │   ├── Common/          # Reusable UI components
│   │   ├── Layout/          # Layout components (Header, Sidebar)
│   │   ├── Posts/           # Post-related components
│   │   ├── Friends/         # Friend-related components
│   │   ├── Timeline/        # Timeline components
│   │   └── Users/           # User components
│   │
│   ├── pages/               # Page components (routes)
│   │   ├── HomePage.tsx
│   │   ├── FriendsListPage.tsx
│   │   ├── UserProfilePage.tsx
│   │   ├── SearchUsersPage.tsx
│   │   └── NotFoundPage.tsx
│   │
│   ├── services/            # API service layer
│   │   ├── api.ts           # Axios configuration
│   │   ├── userService.ts
│   │   ├── postService.ts
│   │   ├── friendshipService.ts
│   │   └── timelineService.ts
│   │
│   ├── stores/              # Zustand state stores
│   │   ├── authStore.ts     # Authentication state
│   │   ├── userStore.ts     # User profile state
│   │   └── uiStore.ts       # UI state (modals, notifications)
│   │
│   ├── hooks/               # Custom React hooks
│   │   ├── useTimeline.ts
│   │   ├── usePosts.ts
│   │   ├── useFriends.ts
│   │   └── useUsers.ts
│   │
│   ├── types/               # TypeScript type definitions
│   │   ├── models.ts        # Domain models
│   │   ├── api.ts           # API types
│   │   └── index.ts
│   │
│   ├── utils/               # Utility functions
│   │   ├── constants.ts
│   │   ├── dateFormatter.ts
│   │   └── validators.ts
│   │
│   ├── config/              # Configuration files
│   │   └── apiConfig.ts
│   │
│   ├── styles/              # Styling
│   │   ├── theme.ts         # MUI theme
│   │   └── globals.css      # Global CSS
│   │
│   ├── App.tsx              # Root component
│   └── main.tsx             # Entry point
│
├── public/                  # Static assets
├── .env                     # Environment variables
├── .env.example             # Environment template
├── index.html               # HTML template
├── vite.config.ts           # Vite configuration
├── tsconfig.json            # TypeScript configuration
└── package.json             # Dependencies
```

## 🎯 Features

### ✅ Implemented Features

- **Timeline View** - View posts from all friends
- **Create Posts** - Share updates with your network
- **Friends Management** - Add and remove friends
- **User Search** - Find and connect with users
- **User Profiles** - View user information and posts
- **Pagination** - Navigate through timeline posts
- **Responsive Design** - Works on desktop, tablet, and mobile
- **Error Handling** - Graceful error messages and loading states
- **Type Safety** - Full TypeScript support

### 🎨 UI Components

- Material-UI components for consistent design
- Loading spinners and skeletons
- Error messages and empty states
- Confirmation dialogs
- Toast notifications (snackbars)

## 🔌 API Integration

The frontend connects to the backend API via NGINX load balancer on `http://localhost` (recommended for Docker Compose) or directly on `http://localhost:9091` for direct backend access.

### Backend Endpoints Used

```
GET  /api/v1/timeline/user/{userId}
GET  /api/v1/timeline/user/{userId}/filtered
GET  /api/v1/posts/user/{userId}
POST /api/v1/posts
DELETE /api/v1/posts/{id}
GET  /api/v1/friendships/user/{userId}
POST /api/v1/friendships
DELETE /api/v1/friendships
GET  /api/v1/users
GET  /api/v1/users/{userId}
```

## 🔧 Configuration

### API Configuration

Edit `src/config/apiConfig.ts` to change API endpoints or settings.

### Theme Customization

Edit `src/styles/theme.ts` to customize the Material-UI theme (colors, typography, etc.).

### Constants

Edit `src/utils/constants.ts` to change app-wide constants like routes, query keys, and messages.

## 🧪 Development Tips

### Change Current User

To switch users during development:

```javascript
// In browser console
localStorage.setItem('auth-storage', JSON.stringify({ 
  state: { currentUserId: 2, isAuthenticated: true }, 
  version: 0 
}));
// Then refresh the page
```

### Clear All Data

```javascript
localStorage.clear();
```

### Debug React Query

Install React Query Devtools for debugging:

```bash
npm install @tanstack/react-query-devtools
```

Add to `App.tsx`:

```typescript
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';

// Inside App component
<ReactQueryDevtools initialIsOpen={false} />
```

## 🚨 Troubleshooting

### "Network Error" when making API calls

- Ensure the backend is running on `http://localhost` (NGINX) or `http://localhost:9091` (direct backend)
- Check CORS configuration in the backend
- Verify `.env` file has correct `VITE_API_BASE_URL`

### "Please log in" message

- Set user ID in localStorage (see setup section)
- Refresh the page after setting localStorage

### TypeScript errors

```bash
npm run build
```

This will show all TypeScript errors.

## 📦 Build & Deployment

### Production Build

```bash
npm run build
```

### Deploy to Vercel

```bash
npm install -g vercel
vercel
```

### Deploy to Netlify

```bash
npm install -g netlify-cli
netlify deploy
```

## 🎓 Learning Resources

- [React Documentation](https://react.dev/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Material-UI Documentation](https://mui.com/)
- [React Router](https://reactrouter.com/)
- [React Query](https://tanstack.com/query/latest)
- [Zustand](https://github.com/pmndrs/zustand)

## 📝 License

This project is for educational purposes.

## 🤝 Contributing

1. Ensure backend is running
2. Test all features before committing
3. Follow TypeScript best practices
4. Use Material-UI components consistently

## 📧 Support

For issues or questions, please refer to the backend API documentation or check the browser console for errors.

---

**Built with ❤️ using stable, battle-tested technologies**
