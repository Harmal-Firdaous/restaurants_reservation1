
import React, { useState } from 'react';
import { useCart } from '../context/CartContext';
import { orderService } from '../services/orderService';
import { Trash2, ArrowLeft } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';

const CartPage = () => {
    const { cartItems, removeFromCart, updateQuantity, total, clearCart, restaurantId } = useCart();
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handlePlaceOrder = async () => {
        if (cartItems.length === 0) return;

        setLoading(true);
        try {
            const orderData = {
                totalAmount: total,
                status: 'PENDING',
                customerName: 'Guest User', // Mock user
                customerAddress: '123 Main St', // Mock address
                orderItems: cartItems.map(item => ({
                    quantity: item.quantity,
                    price: item.price,
                    plat: { id: item.id }
                }))
            };

            await orderService.createOrder(orderData);
            clearCart();
            alert('Order Placed Successfully!');
            navigate('/orders');
        } catch (error) {
            console.error(error);
            alert('Failed to place order.');
        } finally {
            setLoading(false);
        }
    };

    if (cartItems.length === 0) {
        return (
            <div className="text-center py-20">
                <h2 className="text-2xl font-bold mb-4">Your cart is empty</h2>
                <Link to="/" className="text-primary hover:underline">Browse Restaurants</Link>
            </div>
        );
    }

    return (
        <div className="max-w-3xl mx-auto">
            <Link to={`/restaurant/${restaurantId}`} className="block mb-6 text-gray-500 hover:text-dark flex items-center gap-1">
                <ArrowLeft className="w-4 h-4" /> Back to Menu
            </Link>

            <h1 className="text-3xl font-bold mb-8">Your Order</h1>

            <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden mb-8">
                <div className="divide-y divide-gray-100">
                    {cartItems.map(item => (
                        <div key={item.id} className="p-4 flex items-center justify-between">
                            <div className="flex-1">
                                <h4 className="font-bold text-lg">{item.name}</h4>
                                <div className="text-gray-500">${item.price} each</div>
                            </div>

                            <div className="flex items-center gap-4">
                                <div className="flex items-center gap-2 bg-gray-50 rounded-lg p-1">
                                    <button onClick={() => updateQuantity(item.id, -1)} className="w-8 h-8 flex items-center justify-center font-bold hover:bg-gray-200 rounded-md">-</button>
                                    <span className="w-8 text-center font-bold">{item.quantity}</span>
                                    <button onClick={() => updateQuantity(item.id, 1)} className="w-8 h-8 flex items-center justify-center font-bold hover:bg-gray-200 rounded-md">+</button>
                                </div>

                                <div className="font-bold w-16 text-right">${(item.price * item.quantity).toFixed(2)}</div>

                                <button onClick={() => removeFromCart(item.id)} className="text-red-400 hover:text-red-600 p-2">
                                    <Trash2 className="w-5 h-5" />
                                </button>
                            </div>
                        </div>
                    ))}
                </div>

                <div className="bg-gray-50 p-6 flex justify-between items-center border-t border-gray-100">
                    <span className="text-xl font-bold text-gray-700">Total</span>
                    <span className="text-3xl font-bold text-primary">${total.toFixed(2)}</span>
                </div>
            </div>

            <button
                onClick={handlePlaceOrder}
                disabled={loading}
                className="w-full bg-primary text-white py-4 rounded-xl font-bold text-xl hover:bg-red-600 transition-colors disabled:opacity-70"
            >
                {loading ? 'Placing Order...' : 'Place Order Now'}
            </button>
        </div>
    );
};

export default CartPage;
