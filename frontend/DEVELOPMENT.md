# Development Guide

This guide covers development setup, architecture, and best practices for the Social Network Frontend.

## 📚 Architecture Overview

### Layered Architecture

```
UI Layer (React Components)
    ↓
State Management Layer (Zustand + React Query)
    ↓
API Service Layer (Axios)
    ↓
Backend API (Spring Boot)
```

### Data Flow

1. **User Interaction** → Component Event Handler
2. **Component** → Custom Hook (useTimeline, useFriends, etc.)
3. **Custom Hook** → React Query (queries/mutations)
4. **React Query** → API Service (postService, friendshipService, etc.)
5. **API Service** → Axios Instance with Interceptors
6. **Axios** → Backend API

## 🏗️ Component Architecture

### Component Categories

#### 1. **Page Components** (`src/pages/`)
- Top-level route components
- Handle page layout and data fetching
- Connected to stores and hooks
- Example: `HomePage.tsx`, `UserProfilePage.tsx`

#### 2. **Layout Components** (`src/components/Layout/`)
- Provide page structure (Header, Sidebar)
- Shared across all pages
- Handle navigation

#### 3. **Feature Components** (`src/components/Posts/`, `src/components/Friends/`, etc.)
- Feature-specific components
- Combine multiple UI components
- Handle feature logic
- Example: `PostForm.tsx`, `FriendsList.tsx`

#### 4. **Reusable Components** (`src/components/Common/`)
- Generic UI components
- No feature-specific logic
- Used across the app
- Example: `LoadingSpinner.tsx`, `ErrorMessage.tsx`

## 🔄 State Management Pattern

### Zustand Stores

```typescript
// src/stores/authStore.ts
import { create } from 'zustand';

interface AuthState {
  currentUserId: number | null;
  isAuthenticated: boolean;
  setCurrentUser: (userId: number) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  // Initial state
  currentUserId: null,
  isAuthenticated: false,

  // Actions
  setCurrentUser: (userId) =>
    set({ currentUserId: userId, isAuthenticated: true }),
  logout: () =>
    set({ currentUserId: null, isAuthenticated: false }),
}));
```

### Using Stores in Components

```typescript
import { useAuthStore } from '../stores/authStore';

function MyComponent() {
  const { currentUserId, logout } = useAuthStore();
  
  return (
    <button onClick={logout}>
      Logout (User {currentUserId})
    </button>
  );
}
```

## 🎣 Custom Hooks Pattern

### Query Hooks

```typescript
// src/hooks/useTimeline.ts
export const useTimeline = (userId: number | null) => {
  return useQuery({
    queryKey: [QUERY_KEYS.TIMELINE, userId],
    queryFn: () => getTimeline(userId),
    enabled: !!userId,
    staleTime: 1000 * 60 * 5, // 5 minutes
  });
};
```

### Mutation Hooks

```typescript
// src/hooks/usePosts.ts
export const useCreatePost = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (postData: PostRequest) => createPost(postData),
    onSuccess: () => {
      // Invalidate related queries
      queryClient.invalidateQueries({
        queryKey: [QUERY_KEYS.POSTS],
      });
    },
  });
};
```

### Using Hooks in Components

```typescript
function TimelineComponent() {
  const { data: posts, isLoading, error } = useTimeline(userId);
  
  if (isLoading) return <LoadingSpinner />;
  if (error) return <ErrorMessage />;
  
  return <Timeline posts={posts} />;
}
```

## 📝 API Service Pattern

### Service Structure

```typescript
// src/services/postService.ts
import api from './api';
import { API_ENDPOINTS } from '../config/apiConfig';

export const createPost = async (postData: PostRequest): Promise<PostResponse> => {
  const response = await api.post<PostResponse>(
    API_ENDPOINTS.posts,
    postData
  );
  return response.data;
};
```

### Axios Configuration

```typescript
// src/services/api.ts
const api = axios.create({
  baseURL: API_CONFIG.baseURL,
  timeout: API_CONFIG.timeout,
});

// Request interceptor
api.interceptors.request.use((config) => {
  // Add auth token, modify headers, etc.
  return config;
});

// Response interceptor
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Handle errors globally
    return Promise.reject(error);
  }
);
```

## 🧪 Testing Pattern

### Testing Custom Hooks

```typescript
import { renderHook, waitFor } from '@testing-library/react';
import { useTimeline } from '../hooks/useTimeline';

test('useTimeline fetches posts', async () => {
  const { result } = renderHook(() => useTimeline(1));

  await waitFor(() => {
    expect(result.current.isSuccess).toBe(true);
  });

  expect(result.current.data).toEqual([...]);
});
```

### Testing Components

```typescript
import { render, screen } from '@testing-library/react';
import PostForm from '../components/Posts/PostForm';

test('renders post form', () => {
  render(<PostForm />);
  expect(screen.getByPlaceholderText(/what's on your mind/i)).toBeInTheDocument();
});
```

## 🎨 Styling Guidelines

### Using Material-UI

```typescript
import { Box, Button, TextField } from '@mui/material';
import { useTheme } from '@mui/material/styles';

function MyComponent() {
  const theme = useTheme();

  return (
    <Box
      sx={{
        p: 2,
        backgroundColor: theme.palette.background.paper,
        borderRadius: theme.shape.borderRadius,
      }}
    >
      <TextField label="Name" />
      <Button variant="contained">Submit</Button>
    </Box>
  );
}
```

### Theme Customization

Edit `src/styles/theme.ts`:

```typescript
const theme = createTheme({
  palette: {
    primary: {
      main: '#007AFF',
    },
    secondary: {
      main: '#F3F3F3',
    },
  },
  typography: {
    fontFamily: 'Roboto',
  },
});
```

## 🔒 Type Safety

### Defining Types

```typescript
// src/types/models.ts
export interface Post {
  id: number;
  content: string;
  authorId: number;
  createdAt: string;
}

export interface TimelinePost extends Post {
  authorName: string;
  friendName: string;
}
```

### Using Types in Components

```typescript
interface PostCardProps {
  post: Post;
  onDelete?: (postId: number) => void;
}

const PostCard: React.FC<PostCardProps> = ({ post, onDelete }) => {
  // Component code
};
```

## 🔐 Authentication (Future Implementation)

### JWT Token Storage

```typescript
// Store token in localStorage
localStorage.setItem('authToken', response.data.token);

// In Axios interceptor
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

## ⚡ Performance Tips

### 1. Use React.memo for expensive components

```typescript
export const PostCard = React.memo(({ post }: PostCardProps) => {
  // Component code
});
```

### 2. Lazy load routes

```typescript
const HomePage = lazy(() => import('../pages/HomePage'));
const UserProfilePage = lazy(() => import('../pages/UserProfilePage'));

<Suspense fallback={<LoadingSpinner />}>
  <Routes>
    <Route path="/" element={<HomePage />} />
    <Route path="/profile/:userId" element={<UserProfilePage />} />
  </Routes>
</Suspense>
```

### 3. Optimize queries

```typescript
const { data, isLoading, error } = useQuery({
  queryKey: [QUERY_KEYS.POSTS, userId],
  queryFn: () => getPostsByUser(userId),
  staleTime: 1000 * 60 * 5, // Cache for 5 minutes
  gcTime: 1000 * 60 * 10,   // Keep in memory for 10 minutes
});
```

## 🐛 Debugging

### React DevTools

Install the React DevTools browser extension to inspect component props and state.

### React Query DevTools

```bash
npm install @tanstack/react-query-devtools
```

Add to `App.tsx`:

```typescript
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';

<ReactQueryDevtools initialIsOpen={false} />
```

### Console Logging

```typescript
// Debug store state
console.log(useAuthStore.getState());

// Debug component props
console.log('Props:', props);
```

## 📋 Common Development Tasks

### Adding a New Page

1. Create page component in `src/pages/`
2. Add route in `src/App.tsx`
3. Add navigation link in `src/components/Layout/Header.tsx` or `Sidebar.tsx`

### Adding a New API Service

1. Create service file in `src/services/`
2. Add API endpoints to `src/config/apiConfig.ts`
3. Create custom hook in `src/hooks/`
4. Use hook in components

### Adding a New Component

1. Create component file in appropriate folder
2. Define TypeScript interfaces
3. Export from barrel file (`index.ts`)
4. Use Material-UI components for styling

## 🚀 Code Quality

### ESLint

```bash
npm run lint
```

### TypeScript Check

```bash
npm run build  # Checks TypeScript before building
```

### Prettier Formatting

```bash
npx prettier --write src/
```

## 📚 Best Practices

1. **Always use TypeScript** - Define types for all props and state
2. **Separate concerns** - Services handle API, components handle UI
3. **Use custom hooks** - Encapsulate data fetching logic
4. **Error handling** - Always handle errors from API calls
5. **Loading states** - Show spinners while fetching data
6. **Accessibility** - Use semantic HTML and ARIA labels
7. **Performance** - Use React.memo for expensive components
8. **Clean code** - Follow naming conventions and organize code logically

## 🔗 Useful Resources

- [React Hooks Documentation](https://react.dev/reference/react)
- [React Query Documentation](https://tanstack.com/query/latest)
- [Material-UI API](https://mui.com/api/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Zustand GitHub](https://github.com/pmndrs/zustand)

---

Happy coding! 🎉
