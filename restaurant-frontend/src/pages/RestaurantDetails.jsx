
import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { restaurantService } from '../services/restaurantService';
import { useCart } from '../context/CartContext';
import { Plus } from 'lucide-react';

const RestaurantDetails = () => {
    const { id } = useParams();
    const { addToCart } = useCart();
    const [activeCategory, setActiveCategory] = useState(null);

    const { data: restaurant, isLoading: loadingRest } = useQuery({
        queryKey: ['restaurant', id],
        queryFn: () => restaurantService.getRestaurantById(id)
    });

    const { data: menus, isLoading: loadingMenus } = useQuery({
        queryKey: ['menus', id],
        queryFn: () => restaurantService.getMenuByRestaurant(id)
    });

    // Fetch plats for the first menu if activeCategory is unset types
    // This is a bit complex without a "get all plats for restaurant" endpoint
    // We will assume for now we list menus as categories and user expands them 
    // OR we fetch plats for specific menu.

    // Better UX: Fetch all plats for all menus (or backend endpoint should do it)
    // Since we don't have that, we will implement a component that fetches plats when expanded or loaded.

    if (loadingRest || loadingMenus) return <div className="p-8 text-center">Loading...</div>;

    return (
        <div>
            <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 mb-6">
                <h1 className="text-3xl font-bold mb-2">{restaurant?.name}</h1>
                <p className="text-gray-500">{restaurant?.address}</p>
            </div>

            <div className="flex flex-col gap-6">
                <h2 className="text-2xl font-bold">Menu</h2>

                {menus?.length === 0 ? (
                    <p>No menus available.</p>
                ) : (
                    <div className="space-y-8">
                        {menus?.map(menu => (
                            <MenuSection key={menu.id} menu={menu} restaurantId={id} addToCart={addToCart} />
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

const MenuSection = ({ menu, restaurantId, addToCart }) => {
    const { data: plats, isLoading } = useQuery({
        queryKey: ['plats', menu.id],
        queryFn: () => restaurantService.getPlatsByMenu(menu.id)
    });

    if (isLoading) return <div className="animate-pulse h-20 bg-gray-100 rounded-lg"></div>;

    return (
        <div className="bg-white rounded-xl border border-gray-100 overflow-hidden">
            <div className="bg-gray-50 px-6 py-3 border-b border-gray-100 font-bold text-lg">
                {menu.title}
            </div>
            <div className="divide-y divide-gray-100">
                {plats?.map(plat => (
                    <div key={plat.id} className="p-4 flex justify-between items-center hover:bg-gray-50 transition-colors">
                        <div>
                            <h4 className="font-bold">{plat.name}</h4>
                            <p className="text-gray-500 text-sm">{plat.description}</p>
                            <div className="font-medium mt-1">${plat.price}</div>
                        </div>
                        <button
                            onClick={() => addToCart(plat, restaurantId)}
                            className="bg-primary text-white p-2 rounded-full hover:bg-red-600 transition-colors"
                        >
                            <Plus className="w-5 h-5" />
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default RestaurantDetails;
