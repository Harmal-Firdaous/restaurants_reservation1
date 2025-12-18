
import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Utensils, Calendar, Home } from 'lucide-react';

const Layout = ({ children }) => {
    const location = useLocation();

    const isActive = (path) => location.pathname === path ? 'text-primary' : 'text-gray-500 hover:text-primary';

    return (
        <div className="min-h-screen flex flex-col bg-gray-50">
            {/* Navbar */}
            <header className="bg-white shadow-sm sticky top-0 z-50">
                <div className="container mx-auto px-4 h-16 flex items-center justify-between">
                    <Link to="/" className="flex items-center gap-2 text-2xl font-bold text-primary">
                        <Utensils className="w-8 h-8" />
                        <span>RestaurantReserve</span>
                    </Link>

                    <nav className="flex items-center gap-6">
                        <Link to="/" className={`flex items-center gap-1 font-medium transition-colors ${isActive('/')}`}>
                            <Home className="w-5 h-5" />
                            Home
                        </Link>
                        <Link to="/reservations" className={`flex items-center gap-1 font-medium transition-colors ${isActive('/reservations')}`}>
                            <Calendar className="w-5 h-5" />
                            My Reservations
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
                    &copy; {new Date().getFullYear()} RestaurantReserve - Find and book your perfect dining experience.
                </div>
            </footer>
        </div>
    );
};

export default Layout;
