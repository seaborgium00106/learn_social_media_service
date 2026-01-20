# UI Development Plan - Social Network Application

## 1. Overview & Objectives

### Purpose
Create a single-page web application (SPA) that provides users with a social networking interface where they can:
- View their personalized timeline (aggregated posts from friends)
- Manage friendships (add/remove friends)
- Create new posts
- View their friend list
- Browse user profiles

### Target Users
Social network application users who want to interact with their network via a web browser.

---

## 2. Technology Stack Recommendations

### Frontend Framework Options

#### **Option 1: React.js** (Recommended)
**Pros:**
- Large ecosystem and community support
- Excellent component reusability
- Great performance with virtual DOM
- Abundant libraries for UI, routing, state management
- Strong developer tools and debugging

**Cons:**
- Steeper learning curve for beginners
- Requires build tools (webpack, etc.)

**Libraries to Use:**
- **React Query/TanStack Query** - API data fetching & caching
- **React Router** - Client-side routing
- **Redux or Zustand** - State management
- **Axios** - HTTP client
- **React Hook Form** - Form handling
- **Tailwind CSS or Material-UI** - Styling

---

#### **Option 2: Vue.js**
**Pros:**
- Gentler learning curve
- Excellent documentation
- Progressive framework (use as much or as little as needed)
- Great developer experience
- Smaller bundle size

**Cons:**
- Smaller ecosystem compared to React
- Less commercial adoption

**Libraries to Use:**
- **Vue 3** with Composition API
- **Vue Router** - Routing
- **Pinia** - State management
- **Axios** - HTTP client
- **Vee-Validate** - Form validation
- **Tailwind CSS** - Styling

---

#### **Option 3: Angular**
**Pros:**
- Full-featured framework out of the box
- Strong TypeScript support
- Great for enterprise applications
- Comprehensive CLI tooling

**Cons:**
- Steep learning curve
- More opinionated (less flexibility)
- Larger bundle size
- Overkill for this application's complexity

**Not Recommended** - Too heavyweight for a social SPA

---

### **Recommendation: React.js**
Use React with TypeScript for type safety and better developer experience.

### UI Component Library Options

#### **Option 1: Material-UI (MUI)**
**Pros:**
- Professional, polished components
- Extensive component library
- Great documentation
- Accessibility built-in
- Theme customization

**Cons:**
- Slightly larger bundle size
- Can feel "corporate"

---

#### **Option 2: Tailwind CSS + Headless Components**
**Pros:**
- Highly customizable
- Smaller bundle size
- Utility-first approach (fast development)
- Great flexibility
- Works well with shadcn/ui, Headless UI, Radix UI

**Cons:**
- More CSS knowledge required
- Need to build components from scratch or use component libraries

---

#### **Option 3: Bootstrap**
**Pros:**
- Familiar to many developers
- Quick prototyping
- Good mobile responsiveness

**Cons:**
- Less modern aesthetic
- Limited customization without overrides
- Heavier library

---

### **Recommendation: Tailwind CSS + shadcn/ui or Headless UI**
Modern, lightweight, and highly customizable. Alternatively, **Material-UI** for a more polished out-of-the-box experience.

### State Management

#### **Option 1: React Context API + useReducer**
**Pros:**
- Built into React
- No extra dependencies
- Good for small to medium apps

**Cons:**
- Can lead to prop drilling
- Not ideal for complex state

---

#### **Option 2: Redux Toolkit**
**Pros:**
- Industry standard
- Excellent debugging tools (Redux DevTools)
- Great for complex state management
- Predictable state flow

**Cons:**
- More boilerplate code
- Steeper learning curve

---

#### **Option 3: Zustand**
**Pros:**
- Minimal boilerplate
- Easy to learn
- Lightweight
- Good performance

**Cons:**
- Smaller ecosystem
- Less tooling compared to Redux

---

### **Recommendation: React Query + Zustand**
- **React Query (TanStack Query)** for server state (API data)
- **Zustand** for client state (UI state, preferences)
- Cleaner separation of concerns

---

### HTTP Client

**Recommendation: Axios**
- Simple API
- Interceptors for authentication
- Request/response transformation
- Good error handling

---

### Form Handling

**Recommendation: React Hook Form**
- Lightweight
- Excellent performance
- Minimal re-renders
- Great validation integration (Zod, Yup)

---

### Validation

**Recommendation: Zod**
- TypeScript-first
- Type inference
- Lightweight
- Great error messages

---

### Build Tool & Package Manager

**Recommendation: Vite**
- Lightning-fast build times
- Fast HMR (Hot Module Replacement)
- Modern, lightweight
- Great development experience
- Faster than Create React App

**Package Manager: npm, yarn, or pnpm**
- Recommendation: **pnpm** (faster, efficient)

---

## 3. Application Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────┐
│           UI Layer (React Components)       │
│                                             │
│  ┌──────────────────────────────────────┐  │
│  │  Pages/Routes                        │  │
│  │  - HomePage/TimelineView             │  │
│  │  - UserProfilePage                   │  │
│  │  - FriendsListPage                   │  │
│  └──────────────────────────────────────┘  │
│                                             │
│  ┌──────────────────────────────────────┐  │
│  │  Components (Reusable)               │  │
│  │  - PostCard                          │  │
│  │  - FriendCard                        │  │
│  │  - Timeline                          │  │
│  │  - PostForm                          │  │
│  └──────────────────────────────────────┘  │
│                                             │
│  ┌──────────────────────────────────────┐  │
│  │  State Management Layer              │  │
│  │  - Zustand Stores (Client State)     │  │
│  │  - React Query (Server State)        │  │
│  └──────────────────────────────────────┘  │
└─────────────────────────────────────────────┘
             ↓
┌─────────────────────────────────────────────┐
│      API Service Layer                      │
│  - Axios Instances                          │
│  - API Request Methods                      │
│  - Error Handling & Interceptors            │
└─────────────────────────────────────────────┘
             ↓
┌─────────────────────────────────────────────┐
│    Backend API (Spring Boot - Existing)     │
│  - Timeline Endpoints                       │
│  - Post Endpoints                           │
│  - Friendship Endpoints                     │
│  - User Endpoints                           │
└─────────────────────────────────────────────┘
```

---

## 4. File Structure & Organization

```
social-network-ui/
├── public/
│   ├── index.html
│   └── favicon.ico
│
├── src/
│   ├── index.tsx
│   ├── App.tsx
│   ├── App.css
│   │
│   ├── pages/
│   │   ├── HomePage.tsx          # Main timeline view
│   │   ├── UserProfilePage.tsx   # User profile with posts
│   │   ├── FriendsListPage.tsx   # View all friends
│   │   ├── SearchUsersPage.tsx   # Find and add friends
│   │   └── NotFoundPage.tsx      # 404 page
│   │
│   ├── components/
│   │   ├── Layout/
│   │   │   ├── Header.tsx        # Navigation header
│   │   │   ├── Sidebar.tsx       # Sidebar with menu
│   │   │   └── MainLayout.tsx    # Main layout wrapper
│   │   │
│   │   ├── Timeline/
│   │   │   ├── Timeline.tsx      # Timeline container
│   │   │   ├── TimelinePost.tsx  # Individual post in timeline
│   │   │   └── TimelineFilter.tsx # Date range filter
│   │   │
│   │   ├── Posts/
│   │   │   ├── PostCard.tsx      # Post display component
│   │   │   ├── PostForm.tsx      # Create new post form
│   │   │   ├── PostActions.tsx   # Edit/Delete actions
│   │   │   └── PostList.tsx      # List of posts
│   │   │
│   │   ├── Friends/
│   │   │   ├── FriendCard.tsx    # Individual friend card
│   │   │   ├── FriendsList.tsx   # List of friends
│   │   │   ├── AddFriendButton.tsx
│   │   │   └── RemoveFriendButton.tsx
│   │   │
│   │   ├── Users/
│   │   │   ├── UserCard.tsx      # User profile card
│   │   │   ├── UserProfile.tsx   # User profile view
│   │   │   └── UserSearch.tsx    # Search users
│   │   │
│   │   ├── Common/
│   │   │   ├── Button.tsx        # Reusable button
│   │   │   ├── Modal.tsx         # Modal component
│   │   │   ├── LoadingSpinner.tsx
│   │   │   ├── ErrorMessage.tsx
│   │   │   └── ConfirmDialog.tsx
│   │   │
│   │   └── Pagination/
│   │       └── PaginationControls.tsx
│   │
│   ├── services/
│   │   ├── api.ts               # Axios instance configuration
│   │   ├── timelineService.ts   # Timeline API calls
│   │   ├── postService.ts       # Post API calls
│   │   ├── friendshipService.ts # Friendship API calls
│   │   └── userService.ts       # User API calls
│   │
│   ├── stores/
│   │   ├── authStore.ts         # Authentication state
│   │   ├── uiStore.ts           # UI state (modals, filters)
│   │   └── userStore.ts         # Current user info
│   │
│   ├── hooks/
│   │   ├── useTimeline.ts       # Timeline data fetching
│   │   ├── useFriends.ts        # Friends data fetching
│   │   ├── usePosts.ts          # Posts data fetching
│   │   └── useUsers.ts          # Users data fetching
│   │
│   ├── types/
│   │   ├── api.ts               # API response types
│   │   ├── models.ts            # Data models
│   │   └── index.ts             # Type exports
│   │
│   ├── utils/
│   │   ├── dateFormatter.ts     # Date formatting utilities
│   │   ├── validators.ts        # Form validation
│   │   └── constants.ts         # App constants
│   │
│   ├── styles/
│   │   ├── globals.css          # Global styles
│   │   ├── variables.css        # CSS variables
│   │   └── responsive.css       # Responsive utilities
│   │
│   └── config/
│       └── apiConfig.ts         # API configuration
│
├── .env.example
├── .gitignore
├── tsconfig.json
├── vite.config.ts
├── package.json
└── README.md
```

---

## 5. Core Pages & Features

### 5.1 HomePage / TimelineView
**Path:** `/`

**Features:**
- Display user's timeline (posts from all friends)
- "Create Post" button/form at the top
- Timeline sorted by newest posts first
- Pagination controls
- Date range filter
- Friend count display
- "Add Friend" quick link

**API Calls:**
- GET `/api/timeline/user/{userId}`
- GET `/api/timeline/user/{userId}/filtered?page=0&size=10&fromDate=...&toDate=...`

---

### 5.2 Create Post Form
**Component:** PostForm.tsx

**Features:**
- Text input for post content
- Character counter (optional)
- Submit button with loading state
- Success/error messages
- Form validation

**API Calls:**
- POST `/api/posts`

---

### 5.3 Friends Management
**Component:** FriendsPanel / FriendsListPage

**Features:**
- Display current user's friends
- Count of friends
- Friend cards with:
  - Friend's avatar/profile picture
  - Friend's name
  - "View Profile" link
  - "Remove Friend" button
- Pagination if many friends

**API Calls:**
- GET `/api/friendships/user/{userId}`
- DELETE `/api/friendships`

---

### 5.4 Add Friend Interface
**Component:** AddFriendButton / UserSearch

**Features:**
- Search/autocomplete to find users
- Display search results
- "Add Friend" button on user cards
- "Already friends" indicator
- "Pending request" indicator (if implementing friend requests later)

**API Calls:**
- GET `/api/users?search=...` (backend may need this endpoint)
- POST `/api/friendships`
- GET `/api/friendships/check?userId=X&friendId=Y`

---

### 5.5 User Profile Page
**Path:** `/users/{userId}`

**Features:**
- Display user information
- Show user's posts (not timeline)
- Friend/Remove Friend button
- View mutual friends (if implementing)
- User statistics (post count, friend count)

**API Calls:**
- GET `/api/users/{userId}`
- GET `/api/posts/user/{userId}`
- GET `/api/friendships/user/{userId}`

---

## 6. UI/UX Design Recommendations

### Layout Design

#### **Two-Column Layout** (Recommended)
```
┌─────────────────────────────────────────┐
│          Header/Navigation Bar          │
├────────────────┬────────────────────────┤
│                │                        │
│   Sidebar      │   Main Content Area    │
│                │                        │
│  - Home        │   - Timeline           │
│  - Friends     │   - Posts              │
│  - Profile     │   - Create Post Form   │
│  - Settings    │                        │
│                │                        │
└────────────────┴────────────────────────┘
```

#### **Three-Column Layout** (Alternative)
```
┌─────────────────────────────────────────────────────────┐
│               Header/Navigation Bar                     │
├───────────┬──────────────────────────┬─────────────────┤
│           │                          │                 │
│ Sidebar   │     Main Timeline        │   Right Sidebar │
│           │                          │                 │
│ - Home    │  - Create Post Form      │ - Friends List  │
│ - Friends │  - Timeline Posts        │ - Suggestions   │
│ - Profile │  - Pagination            │ - User Stats    │
│ - Logout  │                          │                 │
│           │                          │                 │
└───────────┴──────────────────────────┴─────────────────┘
```

---

### Color Scheme

**Light Theme:**
- Primary: Blue (#007AFF or similar)
- Secondary: Gray (#F3F3F3)
- Accent: Green (for CTAs like "Post", "Add Friend")
- Text: Dark Gray (#333333)
- Borders: Light Gray (#EEEEEE)

**Dark Theme (Optional):**
- Primary: Light Blue (#4A9EFF)
- Background: Dark Gray (#1F1F1F)
- Secondary: Darker Gray (#2A2A2A)
- Text: Light Gray (#EEEEEE)

---

### Key UI Components

1. **Post Card**
   - Author name
   - Timestamp
   - Post content
   - Friend indicator (small avatar/name)
   - Action menu (like, reply, delete if owner)

2. **Timeline Feed**
   - Infinite scroll OR pagination
   - Loading skeleton while fetching
   - Empty state message
   - Filter controls

3. **Friend Card**
   - Friend name
   - Avatar/Profile picture
   - "Remove Friend" button
   - "View Profile" link

4. **Create Post Form**
   - Text area with rich text options (optional)
   - Character count
   - Image upload (optional)
   - Submit button with loading state

---

## 7. Development Workflow

### Phase 1: Setup & Scaffolding (1-2 days)
1. Initialize Vite React project
2. Install dependencies (React Query, Zustand, Tailwind, etc.)
3. Set up TypeScript configuration
4. Create folder structure
5. Configure Axios with base URL and interceptors
6. Set up environment variables

### Phase 2: Core Infrastructure (2-3 days)
1. Create API service layer
2. Set up Zustand stores for state management
3. Create custom hooks for data fetching
4. Set up React Router configuration
5. Create reusable components (Button, Modal, Spinner, etc.)

### Phase 3: Layout & Navigation (1-2 days)
1. Build Header component
2. Build Sidebar navigation
3. Create MainLayout wrapper
4. Set up routing
5. Style with Tailwind CSS

### Phase 4: Core Features (3-5 days)
1. Build Timeline page and components
2. Implement post display with PostCard
3. Build PostForm for creating posts
4. Build Friends list display
5. Build Add/Remove Friend functionality
6. Implement pagination and filtering

### Phase 5: User Management (1-2 days)
1. Build User Search component
2. Build User Profile page
3. Build User Card component
4. Implement follow/unfollow

### Phase 6: Polish & Optimization (1-2 days)
1. Add loading states
2. Add error handling and error boundaries
3. Implement responsive design (mobile, tablet, desktop)
4. Add animations and transitions
5. Optimize bundle size

### Phase 7: Testing & Deployment (1-2 days)
1. Test all features manually
2. Browser compatibility testing
3. Performance testing
4. Deploy to hosting (Vercel, Netlify, GitHub Pages)

---

## 8. Technology Stack Summary

| Layer | Technology | Rationale |
|-------|-----------|-----------|
| **Framework** | React 18+ | Modern, widely-used |
| **Language** | TypeScript | Type safety, better DX |
| **Build Tool** | Vite | Fast builds and HMR |
| **Styling** | Tailwind CSS + shadcn/ui | Modern, customizable |
| **State Management** | Zustand + React Query | Simple, powerful |
| **HTTP Client** | Axios | Clean API, interceptors |
| **Form Handling** | React Hook Form + Zod | Minimal re-renders, validation |
| **Routing** | React Router v6 | Industry standard |
| **Component Library** | shadcn/ui (optional) | Accessible, customizable |

---

## 9. API Requirements Summary

### Endpoints Used
```
GET  /api/timeline/user/{userId}
GET  /api/timeline/user/{userId}/filtered
GET  /api/timeline/user/{userId}/count

GET  /api/posts/user/{userId}
POST /api/posts
PUT  /api/posts/{id}
DELETE /api/posts/{id}

GET  /api/friendships/user/{userId}
GET  /api/friendships/check
POST /api/friendships
DELETE /api/friendships

GET  /api/users/{userId}
GET  /api/users (search - may need to be added to backend)
```

### Additional Backend Endpoints (Optional)
- `GET /api/users/search?q=...` - User search functionality

---

## 10. Performance Considerations

### Caching Strategy
- **React Query** will automatically cache API responses
- Configure stale time: 5 minutes for timeline
- Configure cache time: 10 minutes for timelines
- Refetch on window focus (configurable)

### Pagination
- Implement pagination instead of infinite scroll initially
- Default: 10 posts per page
- Allow user to change page size

### Image Optimization
- Use lazy loading for images
- Compress images on the backend or use CDN

### Bundle Size
- Tree-shake unused code
- Lazy load pages using React.lazy()
- Minify and compress

---

## 11. Browser Support

- Chrome (latest 2 versions)
- Firefox (latest 2 versions)
- Safari (latest 2 versions)
- Edge (latest 2 versions)
- Mobile browsers (Chrome Mobile, Safari iOS)

---

## 12. Hosting & Deployment Options

### Option 1: Vercel (Recommended)
- Built for Next.js and React
- Easy deployment with git integration
- Free tier available
- Fast CDN
- Serverless functions support

### Option 2: Netlify
- Similar to Vercel
- Good for static React sites
- Easy CI/CD
- Free tier

### Option 3: GitHub Pages
- Free
- Simple for static sites
- Limited features
- Good for learning

### Option 4: AWS S3 + CloudFront
- More control
- Better for production at scale
- Costs minimal
- Complexity higher

---

## 13. Development Tools & Environment

### IDE
- **Visual Studio Code** (Recommended)
- Extensions: ES7+ React/Redux/React-Native snippets, Tailwind CSS IntelliSense, Prettier

### Dev Tools
- **React DevTools** - Browser extension for debugging
- **Redux DevTools** - For state management debugging (if using Redux)
- **Vite DevTools** - Fast build inspection
- **Postman or Insomnia** - API testing

### Version Control
- Git & GitHub

---

## 14. Security Considerations

### Authentication
- Implement JWT token storage in localStorage or sessionStorage
- Add Authorization header to all API requests via Axios interceptor
- Implement logout functionality

### Validation
- Validate all form inputs on client-side
- Trust backend validation (never trust client-only validation)
- Sanitize user input before displaying (prevent XSS)

### CORS
- Configure CORS properly on backend to accept requests from frontend domain
- Use relative paths in API calls if frontend and backend are on same domain

### Environment Variables
- Never commit sensitive data
- Use `.env` files for local development
- Use `.env.example` as template

---

## 15. Testing Strategy

### Unit Testing
- Use Jest for unit tests
- Test utilities, validators, API helpers
- Test individual components in isolation

### Integration Testing
- Test components with their hooks
- Test API integrations
- Use React Testing Library

### E2E Testing (Optional)
- Use Cypress or Playwright
- Test full user workflows
- Test across browsers

---

## 16. Documentation

### Code Documentation
- Add JSDoc comments to functions
- Document complex logic
- README.md for setup instructions

### API Documentation
- Keep OpenAPI/Swagger docs for backend API
- Document expected request/response formats
- Document error codes and messages

---

## 17. Repository Setup

### Repository Structure
```
social-network-ui/                    # New repository
├── README.md
├── DEVELOPMENT.md                    # Development setup
├── package.json
├── tsconfig.json
├── vite.config.ts
├── src/
├── public/
├── .env.example
└── .gitignore
```

### Separate from Backend
- **Keep separate repositories** for better management
- Use monorepo pattern (optional) only if coordinating releases
- Document API contracts clearly

---

## 18. Recommended Tech Stack for Implementation

### Frontend Stack (Final Recommendation)
```json
{
  "core": {
    "react": "18.x",
    "typescript": "5.x",
    "vite": "5.x"
  },
  "styling": {
    "tailwindcss": "3.x",
    "shadcn/ui": "latest"
  },
  "state-management": {
    "zustand": "4.x",
    "@tanstack/react-query": "5.x"
  },
  "forms": {
    "react-hook-form": "7.x",
    "zod": "3.x"
  },
  "routing": {
    "react-router": "6.x"
  },
  "http": {
    "axios": "1.x"
  },
  "utilities": {
    "date-fns": "2.x",
    "clsx": "2.x"
  },
  "dev-tools": {
    "prettier": "3.x",
    "eslint": "8.x"
  }
}
```

---

## 19. Key Deliverables

1. ✅ Repository created (separate from backend)
2. ✅ Project structure set up
3. ✅ Home page with timeline display
4. ✅ Create post functionality
5. ✅ Friends list display
6. ✅ Add/remove friends feature
7. ✅ User search and profile pages
8. ✅ Pagination and filtering
9. ✅ Responsive design (mobile, tablet, desktop)
10. ✅ Error handling and loading states
11. ✅ Deployment to hosting platform

---

## 20. Timeline Estimate

| Phase | Duration | Tasks |
|-------|----------|-------|
| Setup & Scaffolding | 1-2 days | Initialize project, install dependencies |
| Infrastructure | 2-3 days | API services, stores, hooks, components |
| Layout | 1-2 days | Header, sidebar, main layout |
| Core Features | 3-5 days | Timeline, posts, friends, search |
| Polish | 1-2 days | UI refinements, animations, responsive |
| Testing & Deploy | 1-2 days | Testing, optimization, deployment |
| **Total** | **9-16 days** | **Complete MVP** |

---

## 21. Future Enhancements (Out of Scope for MVP)

- Real-time notifications using WebSockets
- Direct messaging between users
- User search with advanced filters
- Like/comment functionality on posts
- Image upload and galleries
- User profiles with bio and profile pictures
- Notifications system
- Follow/Unfollow instead of bidirectional friends
- Trending posts
- Dark mode theme switcher
- Mobile app (React Native)

---

## Conclusion

This plan outlines a modern, scalable approach to building the UI for the social network application. The recommended tech stack (React + TypeScript + Vite + Tailwind + Zustand + React Query) provides:

- **Fast development** with hot module replacement
- **Type safety** with TypeScript
- **Clean code** with modern patterns
- **Good performance** with optimized bundle and caching
- **Scalability** for future features
- **Maintainability** with clear architecture

The phased approach ensures steady progress and allows for testing and feedback at each stage.
