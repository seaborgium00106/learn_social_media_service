import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // Allow connections from outside the container (Docker)
    host: '0.0.0.0',
    port: 3000,
    // Enable HMR for Docker development
    hmr: {
      host: process.env.VITE_HMR_HOST || 'localhost',
      port: process.env.VITE_HMR_PORT ? parseInt(process.env.VITE_HMR_PORT) : 3000,
      protocol: process.env.VITE_HMR_PROTOCOL || 'http',
    },
  },
  preview: {
    // For the serve preview in production
    host: '0.0.0.0',
    port: 3000,
  },
})
