
import React, { useState, useEffect } from 'react';
import { useQuery } from '@tanstack/react-query';
import { restaurantService } from '../services/restaurantService';
import { geoService } from '../services/geoService';
import { Link } from 'react-router-dom';
import { Star, MapPin, Navigation } from 'lucide-react';
import { MapContainer, TileLayer, Marker, Popup, Circle } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';

// Fix for default marker icons in react-leaflet
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
    iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
    iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});

const HomePage = () => {
    const [viewMode, setViewMode] = useState('grid'); // 'grid' or 'map'
    const [userLocation, setUserLocation] = useState(null);
    const [distances, setDistances] = useState({});

    const { data: restaurants, isLoading, error } = useQuery({
        queryKey: ['restaurants'],
        queryFn: restaurantService.getAllRestaurants
    });

    // Get user's current location
    useEffect(() => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                (position) => {
                    setUserLocation({
                        lat: position.coords.latitude,
                        lng: position.coords.longitude
                    });
                },
                (error) => {
                    console.log('Location access denied or unavailable');
                }
            );
        }
    }, []);

    // Calculate distances using geo-service when user location and restaurants are available
    useEffect(() => {
        if (userLocation && restaurants) {
            const calculateDistances = async () => {
                const distancePromises = restaurants
                    .filter(r => r.latitude && r.longitude)
                    .map(async (restaurant) => {
                        try {
                            const distance = await geoService.calculateDistance(
                                userLocation.lat,
                                userLocation.lng,
                                restaurant.latitude,
                                restaurant.longitude
                            );
                            return { id: restaurant.id, distance };
                        } catch (err) {
                            console.error('Error calculating distance:', err);
                            return { id: restaurant.id, distance: null };
                        }
                    });

                const results = await Promise.all(distancePromises);
                const distanceMap = {};
                results.forEach(({ id, distance }) => {
                    distanceMap[id] = distance;
                });
                setDistances(distanceMap);
            };

            calculateDistances();
        }
    }, [userLocation, restaurants]);

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

    // Calculate center of map based on user location or first restaurant
    const center = userLocation
        ? [userLocation.lat, userLocation.lng]
        : restaurants?.length > 0 && restaurants[0].latitude && restaurants[0].longitude
            ? [restaurants[0].latitude, restaurants[0].longitude]
            : [33.5731, -7.5898]; // Default to Casablanca

    return (
        <div>
            <div className="flex justify-between items-center mb-8">
                <div>
                    <h1 className="text-3xl font-bold">Discover Restaurants</h1>
                    {userLocation && (
                        <p className="text-sm text-gray-600 mt-1 flex items-center gap-1">
                            <Navigation className="w-4 h-4" />
                            Showing distances from your location
                        </p>
                    )}
                </div>
                <div className="flex gap-2">
                    <button
                        onClick={() => setViewMode('grid')}
                        className={`px-4 py-2 rounded-lg transition-colors ${viewMode === 'grid'
                            ? 'bg-primary text-white'
                            : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                            }`}
                    >
                        Grid View
                    </button>
                    <button
                        onClick={() => setViewMode('map')}
                        className={`px-4 py-2 rounded-lg transition-colors ${viewMode === 'map'
                            ? 'bg-primary text-white'
                            : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                            }`}
                    >
                        Map View
                    </button>
                </div>
            </div>

            {restaurants?.length === 0 ? (
                <div className="text-center text-gray-500">No restaurants found.</div>
            ) : viewMode === 'map' ? (
                <div className="h-[600px] rounded-xl overflow-hidden shadow-lg border border-gray-200">
                    <MapContainer center={center} zoom={13} style={{ height: '100%', width: '100%' }}>
                        <TileLayer
                            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        />
                        {/* User location marker */}
                        {userLocation && (
                            <>
                                <Circle
                                    center={[userLocation.lat, userLocation.lng]}
                                    radius={50}
                                    pathOptions={{ color: 'blue', fillColor: 'blue', fillOpacity: 0.4 }}
                                />
                                <Marker position={[userLocation.lat, userLocation.lng]}>
                                    <Popup>
                                        <div className="p-2">
                                            <h3 className="font-bold">Your Location</h3>
                                        </div>
                                    </Popup>
                                </Marker>
                            </>
                        )}
                        {/* Restaurant markers */}
                        {restaurants?.filter(r => r.latitude && r.longitude).map((restaurant) => (
                            <Marker key={restaurant.id} position={[restaurant.latitude, restaurant.longitude]}>
                                <Popup>
                                    <div className="p-2">
                                        <h3 className="font-bold text-lg mb-1">{restaurant.name}</h3>
                                        <p className="text-sm text-gray-600 mb-2">{restaurant.address}</p>
                                        <div className="flex items-center gap-1 mb-2">
                                            <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                                            <span className="text-sm font-medium">{restaurant.averageRating || 'New'}</span>
                                        </div>
                                        {distances[restaurant.id] && (
                                            <p className="text-sm text-blue-600 mb-2">
                                                📍 {distances[restaurant.id].toFixed(1)} km away
                                            </p>
                                        )}
                                        <Link
                                            to={`/restaurant/${restaurant.id}`}
                                            className="inline-block bg-primary text-white px-3 py-1 rounded-md text-sm hover:bg-red-600 transition-colors"
                                        >
                                            View Details
                                        </Link>
                                    </div>
                                </Popup>
                            </Marker>
                        ))}
                    </MapContainer>
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {restaurants?.map((restaurant) => (
                        <Link key={restaurant.id} to={`/restaurant/${restaurant.id}`} className="block group">
                            <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden transition-transform group-hover:-translate-y-1">
                                <div className="h-48 bg-gradient-to-br from-orange-400 to-red-500 flex items-center justify-center">
                                    <span className="text-white text-6xl">🍽️</span>
                                </div>

                                <div className="p-4">
                                    <div className="flex justify-between items-start mb-2">
                                        <h3 className="text-xl font-bold text-gray-800">{restaurant.name}</h3>
                                        <div className="flex items-center gap-1 bg-green-50 text-green-700 px-2 py-1 rounded-md text-sm font-bold">
                                            <span>{restaurant.averageRating || 'New'}</span>
                                            <Star className="w-3 h-3 fill-current" />
                                        </div>
                                    </div>

                                    <div className="flex items-start gap-2 text-gray-500 text-sm mb-3">
                                        <MapPin className="w-4 h-4 mt-0.5 flex-shrink-0" />
                                        <p className="line-clamp-2">{restaurant.address}</p>
                                    </div>

                                    {distances[restaurant.id] && (
                                        <div className="flex items-center gap-1 text-blue-600 text-sm mb-3 font-medium">
                                            <Navigation className="w-4 h-4" />
                                            <span>{distances[restaurant.id].toFixed(1)} km away</span>
                                        </div>
                                    )}

                                    <div className="flex items-center justify-between text-sm">
                                        <span className="text-gray-600 font-medium">{restaurant.cuisineType || 'Various'}</span>
                                        <span className="text-primary font-bold">Book Now →</span>
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
