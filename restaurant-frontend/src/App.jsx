
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { CartProvider } from './context/CartContext';
import Layout from './components/Layout';
// Placeholder pages - created next
import HomePage from './pages/HomePage';
import RestaurantDetails from './pages/RestaurantDetails';
import CartPage from './pages/CartPage';
import OrderHistoryPage from './pages/OrderHistoryPage';

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <CartProvider>
        <Router>
          <Layout>
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/restaurant/:id" element={<RestaurantDetails />} />
              <Route path="/cart" element={<CartPage />} />
              <Route path="/orders" element={<OrderHistoryPage />} />
            </Routes>
          </Layout>
        </Router>
      </CartProvider>
    </QueryClientProvider>
  );
}

export default App;
