import api from './api';

const SERVICE_PREFIX = '/restaurant-service/api';

export const orderService = {
    createOrder: async (orderData) => {
        const response = await api.post(`${SERVICE_PREFIX}/orders`, orderData);
        return response.data;
    },
    getAllOrders: async () => {
        const response = await api.get(`${SERVICE_PREFIX}/orders`);
        return response.data;
    },
    getOrderById: async (id) => {
        const response = await api.get(`${SERVICE_PREFIX}/orders/${id}`);
        return response.data;
    }
};
