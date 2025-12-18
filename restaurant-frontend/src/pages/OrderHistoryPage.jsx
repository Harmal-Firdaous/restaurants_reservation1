
import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { orderService } from '../services/orderService';
import { Clock, CheckCircle, Package } from 'lucide-react';

const OrderHistoryPage = () => {
    const { data: orders, isLoading } = useQuery({
        queryKey: ['orders'],
        queryFn: orderService.getAllOrders
    });

    if (isLoading) return <div className="p-8 text-center">Loading orders...</div>;

    return (
        <div className="max-w-4xl mx-auto">
            <h1 className="text-3xl font-bold mb-8">My Orders</h1>

            {orders?.length === 0 ? (
                <div className="text-center py-10 bg-white rounded-xl border border-gray-100">
                    <Package className="w-16 h-16 text-gray-200 mx-auto mb-4" />
                    <p className="text-gray-500">No past orders found.</p>
                </div>
            ) : (
                <div className="space-y-4">
                    {orders?.slice().reverse().map(order => (
                        <div key={order.id} className="bg-white rounded-xl border border-gray-100 p-6 flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
                            <div>
                                <div className="flex items-center gap-2 mb-1">
                                    <span className="font-bold text-lg">Order #{order.id}</span>
                                    <StatusBadge status={order.status} />
                                </div>
                                <div className="text-gray-500 text-sm flex items-center gap-2">
                                    <Clock className="w-4 h-4" />
                                    {new Date(order.date).toLocaleString()}
                                </div>
                            </div>

                            <div className="flex items-center gap-6 w-full md:w-auto justify-between md:justify-end">
                                <div className="text-right">
                                    <div className="text-sm text-gray-400">Total</div>
                                    <div className="font-bold text-xl">${order.totalAmount?.toFixed(2)}</div>
                                </div>
                                {/* <button className="text-primary font-medium hover:underline text-sm">View Details</button> */}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

const StatusBadge = ({ status }) => {
    const styles = {
        PENDING: 'bg-yellow-100 text-yellow-700',
        PREPARING: 'bg-blue-100 text-blue-700',
        DELIVERED: 'bg-green-100 text-green-700',
        CANCELED: 'bg-red-100 text-red-700'
    };

    return (
        <span className={`px-2 py-0.5 rounded-full text-xs font-bold ${styles[status] || 'bg-gray-100'}`}>
            {status}
        </span>
    );
};

export default OrderHistoryPage;
