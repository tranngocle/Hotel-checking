import { build, defineConfig, optimizeDeps } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  optimizeDeps: {
    include:['jwt-decode'],
  }
})
