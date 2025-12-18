import api from './api';

const BASE_URL = '/geo-service/api/geo';

export const geoService = {
    // Get place details by Google Place ID
    getPlaceDetails: async (placeId) => {
        const response = await api.get(`${BASE_URL}/place/${placeId}`);
        return response.data;
    },

    // Search for nearby places
    searchNearby: async (lat, lng, radius = 1000, keyword = null) => {
        const params = { lat, lng, radius };
        if (keyword) params.keyword = keyword;
        const response = await api.get(`${BASE_URL}/nearby`, { params });
        return response.data;
    },

    // Calculate distance between two points in kilometers
    calculateDistance: async (lat1, lon1, lat2, lon2) => {
        const response = await api.get(`${BASE_URL}/distance`, {
            params: { lat1, lon1, lat2, lon2 }
        });
        return response.data;
    }
};
