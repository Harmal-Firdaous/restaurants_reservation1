
import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { useQuery, useMutation } from '@tanstack/react-query';
import { restaurantService } from '../services/restaurantService';
import { reservationService } from '../services/reservationService';
import { Star, MapPin, Calendar, Users, Clock } from 'lucide-react';
import { MapContainer, TileLayer, Marker } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import { format } from 'date-fns';

const RestaurantDetails = () => {
    const { id } = useParams();
    const [reservationData, setReservationData] = useState({
        dateTime: '',
        partySize: 2,
        userId: 1 // TODO: Get from auth context
    });
    const [showSuccess, setShowSuccess] = useState(false);

    const { data: restaurant, isLoading: loadingRest } = useQuery({
        queryKey: ['restaurant', id],
        queryFn: () => restaurantService.getRestaurantById(id)
    });

    const { data: reviews, isLoading: loadingReviews } = useQuery({
        queryKey: ['reviews', id],
        queryFn: () => restaurantService.getReviewsByRestaurant(id)
    });

    const createReservationMutation = useMutation({
        mutationFn: reservationService.createReservation,
        onSuccess: () => {
            setShowSuccess(true);
            setReservationData({ dateTime: '', partySize: 2, userId: 1 });
            setTimeout(() => setShowSuccess(false), 5000);
        }
    });

    const handleSubmit = (e) => {
        e.preventDefault();
        createReservationMutation.mutate({
            ...reservationData,
            restaurantId: parseInt(id),
            status: 'PENDING'
        });
    };

    if (loadingRest) return <div className="p-8 text-center">Loading...</div>;

    const center = restaurant?.latitude && restaurant?.longitude
        ? [restaurant.latitude, restaurant.longitude]
        : [33.5731, -7.5898];

    return (
        <div className="max-w-6xl mx-auto">
            {/* Restaurant Header */}
            <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 mb-6">
                <div className="flex justify-between items-start mb-4">
                    <div>
                        <h1 className="text-4xl font-bold mb-2">{restaurant?.name}</h1>
                        <div className="flex items-center gap-2 text-gray-600 mb-2">
                            <MapPin className="w-5 h-5" />
                            <p>{restaurant?.address}</p>
                        </div>
                        <div className="flex items-center gap-2">
                            <div className="flex items-center gap-1 bg-green-50 text-green-700 px-3 py-1 rounded-lg">
                                <Star className="w-4 h-4 fill-current" />
                                <span className="font-bold">{restaurant?.averageRating || 'New'}</span>
                            </div>
                            <span className="text-gray-500">({reviews?.length || 0} reviews)</span>
                        </div>
                    </div>
                    <div className="text-right">
                        <span className="inline-block bg-blue-50 text-blue-700 px-4 py-2 rounded-lg font-medium">
                            {restaurant?.cuisineType || 'Various Cuisine'}
                        </span>
                    </div>
                </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                {/* Left Column - Map and Reviews */}
                <div className="lg:col-span-2 space-y-6">
                    {/* Map */}
                    {restaurant?.latitude && restaurant?.longitude && (
                        <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                            <div className="p-4 border-b border-gray-100">
                                <h2 className="text-xl font-bold">Location</h2>
                            </div>
                            <div className="h-[300px]">
                                <MapContainer center={center} zoom={15} style={{ height: '100%', width: '100%' }}>
                                    <TileLayer
                                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                                    />
                                    <Marker position={center} />
                                </MapContainer>
                            </div>
                        </div>
                    )}

                    {/* Reviews */}
                    <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-6">
                        <h2 className="text-2xl font-bold mb-4">Reviews</h2>
                        {loadingReviews ? (
                            <div className="text-gray-500">Loading reviews...</div>
                        ) : reviews?.length === 0 ? (
                            <div className="text-gray-500">No reviews yet. Be the first to review!</div>
                        ) : (
                            <div className="space-y-4">
                                {reviews?.slice(0, 5).map((review) => (
                                    <div key={review.id} className="border-b border-gray-100 pb-4 last:border-0">
                                        <div className="flex items-center gap-2 mb-2">
                                            <div className="flex items-center gap-1">
                                                {[...Array(5)].map((_, i) => (
                                                    <Star
                                                        key={i}
                                                        className={`w-4 h-4 ${i < review.rating
                                                                ? 'fill-yellow-400 text-yellow-400'
                                                                : 'text-gray-300'
                                                            }`}
                                                    />
                                                ))}
                                            </div>
                                            <span className="text-sm text-gray-500">User #{review.userId}</span>
                                        </div>
                                        <p className="text-gray-700">{review.comment}</p>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                </div>

                {/* Right Column - Reservation Form */}
                <div className="lg:col-span-1">
                    <div className="bg-white rounded-xl shadow-lg border border-gray-100 p-6 sticky top-6">
                        <h2 className="text-2xl font-bold mb-6">Make a Reservation</h2>

                        {showSuccess && (
                            <div className="mb-4 p-4 bg-green-50 border border-green-200 rounded-lg text-green-700">
                                ✓ Reservation request submitted successfully!
                            </div>
                        )}

                        <form onSubmit={handleSubmit} className="space-y-4">
                            {/* Date and Time */}
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    <Calendar className="w-4 h-4 inline mr-1" />
                                    Date & Time
                                </label>
                                <input
                                    type="datetime-local"
                                    required
                                    value={reservationData.dateTime}
                                    onChange={(e) => setReservationData({ ...reservationData, dateTime: e.target.value })}
                                    min={new Date().toISOString().slice(0, 16)}
                                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                                />
                            </div>

                            {/* Party Size */}
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    <Users className="w-4 h-4 inline mr-1" />
                                    Party Size
                                </label>
                                <select
                                    value={reservationData.partySize}
                                    onChange={(e) => setReservationData({ ...reservationData, partySize: parseInt(e.target.value) })}
                                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary focus:border-transparent"
                                >
                                    {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map(num => (
                                        <option key={num} value={num}>{num} {num === 1 ? 'Person' : 'People'}</option>
                                    ))}
                                </select>
                            </div>

                            <button
                                type="submit"
                                disabled={createReservationMutation.isPending}
                                className="w-full bg-primary text-white py-3 rounded-lg font-bold hover:bg-red-600 transition-colors disabled:bg-gray-400 disabled:cursor-not-allowed"
                            >
                                {createReservationMutation.isPending ? 'Booking...' : 'Book Table'}
                            </button>
                        </form>

                        {createReservationMutation.isError && (
                            <div className="mt-4 p-4 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
                                Error: {createReservationMutation.error.message}
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default RestaurantDetails;
