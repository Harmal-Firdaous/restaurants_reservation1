
import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { restaurantService } from '../services/restaurantService';
import { Link } from 'react-router-dom';
import { Star, Clock } from 'lucide-react';

const HomePage = () => {
    const { data: restaurants, isLoading, error } = useQuery({
        queryKey: ['restaurants'],
        queryFn: restaurantService.getAllRestaurants
    });

    if (isLoading) {
        return (
            <div className="flex justify-center items-center h-64">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="text-center text-red-500 py-10">
                Error loading restaurants. Is the backend running?
            </div>
        );
    }

    return (
        <div>
            <h1 className="text-3xl font-bold mb-8">Popular Restaurants</h1>

            {restaurants?.length === 0 ? (
                <div className="text-center text-gray-500">No restaurants found.</div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {restaurants?.map((restaurant) => (
                        <Link key={restaurant.id} to={`/restaurant/${restaurant.id}`} className="block group">
                            <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden transition-transform group-hover:-translate-y-1">
                                {/* Placeholder Image since API might not return one yet */}
                                <div className="h-48 bg-gray-200 flex items-center justify-center">
                                    <span className="text-gray-400 text-4xl">🍽️</span>
                                </div>

                                <div className="p-4">
                                    <div className="flex justify-between items-start mb-2">
                                        <h3 className="text-xl font-bold text-gray-800">{restaurant.name}</h3>
                                        <div className="flex items-center gap-1 bg-green-50 text-green-700 px-2 py-1 rounded-md text-sm font-bold">
                                            <span>{restaurant.averageRating || 'New'}</span>
                                            <Star className="w-3 h-3 fill-current" />
                                        </div>
                                    </div>

                                    <p className="text-gray-500 text-sm mb-4 line-clamp-2">{restaurant.address}</p>

                                    <div className="flex items-center justify-between text-sm text-gray-400">
                                        <div className="flex items-center gap-1">
                                            <Clock className="w-4 h-4" />
                                            <span>30-45 min</span>
                                        </div>
                                        <span>Open</span>
                                    </div>
                                </div>
                            </div>
                        </Link>
                    ))}
                </div>
            )}
        </div>
    );
};

export default HomePage;
