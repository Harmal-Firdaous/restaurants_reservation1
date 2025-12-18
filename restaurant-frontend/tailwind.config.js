/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: '#FF6B6B', // Vibrant Red/Pink
        secondary: '#4ECDC4', // Teal
        dark: '#2D3436',
        light: '#F7F7F7'
      }
    },
  },
  plugins: [],
}
