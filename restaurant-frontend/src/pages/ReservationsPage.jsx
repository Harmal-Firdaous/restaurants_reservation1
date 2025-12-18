
import React from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { reservationService } from '../services/reservationService';
import { Link } from 'react-router-dom';
import { Calendar, Users, MapPin, Clock, X } from 'lucide-react';
import { format, parseISO, isPast } from 'date-fns';

const ReservationsPage = () => {
    const queryClient = useQueryClient();
    const userId = 1; // TODO: Get from auth context

    const { data: reservations, isLoading, error } = useQuery({
        queryKey: ['reservations', userId],
        queryFn: () => reservationService.getUserReservations(userId)
    });

    const cancelMutation = useMutation({
        mutationFn: reservationService.cancelReservation,
        onSuccess: () => {
            queryClient.invalidateQueries(['reservations', userId]);
        }
    });

    const handleCancel = (id) => {
        if (window.confirm('Are you sure you want to cancel this reservation?')) {
            cancelMutation.mutate(id);
        }
    };

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
                Error loading reservations. Please try again.
            </div>
        );
    }

    const upcomingReservations = reservations?.filter(r =>
        !isPast(parseISO(r.dateTime)) && r.status !== 'CANCELLED'
    ) || [];

    const pastReservations = reservations?.filter(r =>
        isPast(parseISO(r.dateTime)) || r.status === 'CANCELLED'
    ) || [];

    return (
        <div className="max-w-4xl mx-auto">
            <h1 className="text-3xl font-bold mb-8">My Reservations</h1>

            {/* Upcoming Reservations */}
            <div className="mb-12">
                <h2 className="text-2xl font-bold mb-4 flex items-center gap-2">
                    <Calendar className="w-6 h-6" />
                    Upcoming
                </h2>
                {upcomingReservations.length === 0 ? (
                    <div className="bg-white rounded-xl border border-gray-100 p-8 text-center">
                        <p className="text-gray-500 mb-4">No upcoming reservations</p>
                        <Link
                            to="/"
                            className="inline-block bg-primary text-white px-6 py-2 rounded-lg hover:bg-red-600 transition-colors"
                        >
                            Browse Restaurants
                        </Link>
                    </div>
                ) : (
                    <div className="space-y-4">
                        {upcomingReservations.map((reservation) => (
                            <ReservationCard
                                key={reservation.id}
                                reservation={reservation}
                                onCancel={handleCancel}
                                showCancel={true}
                            />
                        ))}
                    </div>
                )}
            </div>

            {/* Past Reservations */}
            {pastReservations.length > 0 && (
                <div>
                    <h2 className="text-2xl font-bold mb-4 text-gray-600">Past Reservations</h2>
                    <div className="space-y-4">
                        {pastReservations.map((reservation) => (
                            <ReservationCard
                                key={reservation.id}
                                reservation={reservation}
                                onCancel={handleCancel}
                                showCancel={false}
                            />
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

const ReservationCard = ({ reservation, onCancel, showCancel }) => {
    const statusColors = {
        PENDING: 'bg-yellow-50 text-yellow-700 border-yellow-200',
        CONFIRMED: 'bg-green-50 text-green-700 border-green-200',
        CANCELLED: 'bg-red-50 text-red-700 border-red-200'
    };

    return (
        <div className="bg-white rounded-xl border border-gray-100 p-6 hover:shadow-md transition-shadow">
            <div className="flex justify-between items-start">
                <div className="flex-1">
                    <div className="flex items-start justify-between mb-3">
                        <div>
                            <h3 className="text-xl font-bold mb-1">
                                {reservation.restaurant?.name || `Restaurant #${reservation.restaurantId}`}
                            </h3>
                            {reservation.restaurant?.address && (
                                <div className="flex items-center gap-1 text-gray-600 text-sm">
                                    <MapPin className="w-4 h-4" />
                                    <span>{reservation.restaurant.address}</span>
                                </div>
                            )}
                        </div>
                        <span className={`px-3 py-1 rounded-lg text-sm font-medium border ${statusColors[reservation.status] || 'bg-gray-50 text-gray-700'}`}>
                            {reservation.status}
                        </span>
                    </div>

                    <div className="grid grid-cols-2 gap-4 text-sm">
                        <div className="flex items-center gap-2 text-gray-700">
                            <Calendar className="w-4 h-4" />
                            <span className="font-medium">
                                {format(parseISO(reservation.dateTime), 'MMM dd, yyyy')}
                            </span>
                        </div>
                        <div className="flex items-center gap-2 text-gray-700">
                            <Clock className="w-4 h-4" />
                            <span className="font-medium">
                                {format(parseISO(reservation.dateTime), 'h:mm a')}
                            </span>
                        </div>
                        <div className="flex items-center gap-2 text-gray-700">
                            <Users className="w-4 h-4" />
                            <span className="font-medium">
                                {reservation.partySize} {reservation.partySize === 1 ? 'Person' : 'People'}
                            </span>
                        </div>
                    </div>
                </div>

                {showCancel && reservation.status !== 'CANCELLED' && (
                    <button
                        onClick={() => onCancel(reservation.id)}
                        className="ml-4 p-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                        title="Cancel reservation"
                    >
                        <X className="w-5 h-5" />
                    </button>
                )}
            </div>
        </div>
    );
};

export default ReservationsPage;
