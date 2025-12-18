
import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { ShoppingBag, Utensils, ClipboardList } from 'lucide-react';

const Layout = ({ children }) => {
    const { cartItems } = useCart();
    const location = useLocation();

    const cartCount = cartItems.reduce((sum, item) => sum + item.quantity, 0);

    const isActive = (path) => location.pathname === path ? 'text-primary' : 'text-gray-500 hover:text-primary';

    return (
        <div className="min-h-screen flex flex-col bg-gray-50">
            {/* Navbar */}
            <header className="bg-white shadow-sm sticky top-0 z-50">
                <div className="container mx-auto px-4 h-16 flex items-center justify-between">
                    <Link to="/" className="flex items-center gap-2 text-2xl font-bold text-primary">
                        <Utensils className="w-8 h-8" />
                        <span>FoodDelivery</span>
                    </Link>

                    <nav className="flex items-center gap-6">
                        <Link to="/" className={`flex items-center gap-1 font-medium transition-colors ${isActive('/')}`}>
                            Home
                        </Link>
                        <Link to="/orders" className={`flex items-center gap-1 font-medium transition-colors ${isActive('/orders')}`}>
                            <ClipboardList className="w-5 h-5" />
                            Orders
                        </Link>
                        <Link to="/cart" className={`flex items-center gap-1 font-medium transition-colors relative ${isActive('/cart')}`}>
                            <ShoppingBag className="w-5 h-5" />
                            <span>Cart</span>
                            {cartCount > 0 && (
                                <span className="absolute -top-2 -right-2 bg-primary text-white text-xs font-bold rounded-full w-5 h-5 flex items-center justify-center">
                                    {cartCount}
                                </span>
                            )}
                        </Link>
                    </nav>
                </div>
            </header>

            {/* Main Content */}
            <main className="flex-grow container mx-auto px-4 py-8">
                {children}
            </main>

            {/* Footer */}
            <footer className="bg-white border-t py-6 mt-auto">
                <div className="container mx-auto px-4 text-center text-gray-500 text-sm">
                    &copy; {new Date().getFullYear()} FoodDelivery App.
                </div>
            </footer>
        </div>
    );
};

export default Layout;
