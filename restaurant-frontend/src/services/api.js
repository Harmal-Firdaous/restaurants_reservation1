import axios from 'axios';

// Default to 8888 which is common for Spring Cloud Gateway, but will confirm with checked file
const API_URL = 'http://localhost:8080';

const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export default api;
