
import React, { createContext, useContext, useState, useEffect } from 'react';

const CartContext = createContext();

export const useCart = () => useContext(CartContext);

export const CartProvider = ({ children }) => {
    const [cartItems, setCartItems] = useState([]);
    const [restaurantId, setRestaurantId] = useState(null);

    // Load cart from local storage on init
    useEffect(() => {
        const savedCart = localStorage.getItem('cart');
        if (savedCart) {
            const { items, restId } = JSON.parse(savedCart);
            setCartItems(items);
            setRestaurantId(restId);
        }
    }, []);

    // Save cart to local storage whenever it changes
    useEffect(() => {
        localStorage.setItem('cart', JSON.stringify({ items: cartItems, restId: restaurantId }));
    }, [cartItems, restaurantId]);

    const addToCart = (item, restId) => {
        if (restaurantId && restaurantId !== restId) {
            if (!window.confirm("Start a new order? Adding items from a different restaurant will clear your current cart.")) {
                return;
            }
            setCartItems([]);
            setRestaurantId(restId);
        } else {
            setRestaurantId(restId);
        }

        setCartItems(prev => {
            const existing = prev.find(i => i.id === item.id);
            if (existing) {
                return prev.map(i => i.id === item.id ? { ...i, quantity: i.quantity + 1 } : i);
            }
            return [...prev, { ...item, quantity: 1 }];
        });
    };

    const removeFromCart = (itemId) => {
        setCartItems(prev => prev.filter(i => i.id !== itemId));
        if (cartItems.length <= 1) {
            setRestaurantId(null);
        }
    };

    const updateQuantity = (itemId, delta) => {
        setCartItems(prev => prev.map(i => {
            if (i.id === itemId) {
                return { ...i, quantity: Math.max(1, i.quantity + delta) };
            }
            return i;
        }));
    };

    const clearCart = () => {
        setCartItems([]);
        setRestaurantId(null);
    };

    const total = cartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);

    return (
        <CartContext.Provider value={{ cartItems, restaurantId, addToCart, removeFromCart, updateQuantity, clearCart, total }}>
            {children}
        </CartContext.Provider>
    );
};
