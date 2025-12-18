import api from './api';

const SERVICE_PREFIX = '/restaurant-service/api';

export const restaurantService = {
    getAllRestaurants: async () => {
        const response = await api.get(`${SERVICE_PREFIX}/restaurants`);
        return response.data;
    },
    getRestaurantById: async (id) => {
        const response = await api.get(`${SERVICE_PREFIX}/restaurants/${id}`);
        return response.data;
    },
    getMenuByRestaurant: async (restaurantId) => {
        const response = await api.get(`${SERVICE_PREFIX}/menus/restaurant/${restaurantId}`);
        return response.data;
    },
    getPlatsByMenu: async (menuId) => {
        const response = await api.get(`${SERVICE_PREFIX}/plats/menu/${menuId}`);
        return response.data;
    }
};
