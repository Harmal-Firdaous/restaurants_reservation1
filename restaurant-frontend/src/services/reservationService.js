import api from './api';

const BASE_URL = '/reservation-service/api/reservations';

export const reservationService = {
    // Create a new reservation
    createReservation: async (reservationData) => {
        const response = await api.post(BASE_URL, reservationData);
        return response.data;
    },

    // Get all reservations for a user
    getUserReservations: async (userId) => {
        const response = await api.get(`${BASE_URL}/user/${userId}`);
        return response.data;
    },

    // Update a reservation
    updateReservation: async (id, reservationData) => {
        const response = await api.put(`${BASE_URL}/${id}`, reservationData);
        return response.data;
    },

    // Cancel a reservation
    cancelReservation: async (id) => {
        const response = await api.delete(`${BASE_URL}/${id}`);
        return response.data;
    }
};
