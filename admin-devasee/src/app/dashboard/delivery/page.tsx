"use client"

import { useState, useEffect } from 'react';

// Type definitions
interface Product {
    id: number;
    name: string;
    unitPrice: number;
    quantity: number;
    deliveryFee: number;
    total: number;
}

interface Order {
    id: number;
    customerName: string;
    customerEmail: string;
    products: Product[];
    status: 'pending' | 'confirmed' | 'shipped' | 'delivered' | 'cancelled';
    orderDate: string;
    totalAmount: number;
    deliveryAddress: string;
}

interface DeliveryPartner {
    id: number;
    name: string;
    email: string;
    phone: string;
    trackingCode: string;
    trackingLink: string;
    vehicleType: string;
    status: 'available' | 'busy' | 'offline';
}

// Mock data
const initialProducts: Product[] = [
    { id: 1, name: 'Laptop', unitPrice: 999, quantity: 2, deliveryFee: 15, total: 2013 },
    { id: 2, name: 'Smartphone', unitPrice: 699, quantity: 1, deliveryFee: 10, total: 709 },
    { id: 3, name: 'Headphones', unitPrice: 199, quantity: 3, deliveryFee: 5, total: 602 },
];

const initialOrders: Order[] = [
    {
        id: 1,
        customerName: 'John Doe',
        customerEmail: 'john@example.com',
        products: [initialProducts[0]],
        status: 'shipped',
        orderDate: '2024-01-15',
        totalAmount: 2013,
        deliveryAddress: '123 Main St, New York, NY'
    },
    {
        id: 2,
        customerName: 'Jane Smith',
        customerEmail: 'jane@example.com',
        products: [initialProducts[1], initialProducts[2]],
        status: 'pending',
        orderDate: '2024-01-16',
        totalAmount: 1311,
        deliveryAddress: '456 Oak Ave, Los Angeles, CA'
    },
];

const initialDeliveryPartners: DeliveryPartner[] = [
    {
        id: 1,
        name: 'Mike Johnson',
        email: 'mike@delivery.com',
        phone: '+1-555-0101',
        trackingCode: 'TRK123456',
        trackingLink: 'https://tracking.example.com/TRK123456',
        vehicleType: 'Van',
        status: 'busy'
    },
    {
        id: 2,
        name: 'Sarah Wilson',
        email: 'sarah@delivery.com',
        phone: '+1-555-0102',
        trackingCode: 'TRK789012',
        trackingLink: 'https://tracking.example.com/TRK789012',
        vehicleType: 'Motorcycle',
        status: 'available'
    },
];

export default function DeliveryPage() {
    const [products, setProducts] = useState<Product[]>(() =>
        initialProducts.map(product => ({
            ...product,
            total: (product.unitPrice * product.quantity) + product.deliveryFee
        }))
    );
    const [orders, setOrders] = useState<Order[]>(initialOrders);
    const [deliveryPartners, setDeliveryPartners] = useState<DeliveryPartner[]>(initialDeliveryPartners);

    const [activeTab, setActiveTab] = useState<'products' | 'orders' | 'delivery-partners'>('orders');
    const [editingProduct, setEditingProduct] = useState<Product | null>(null);
    const [editingOrder, setEditingOrder] = useState<Order | null>(null);
    const [editingPartner, setEditingPartner] = useState<DeliveryPartner | null>(null);

    const [isProductModalOpen, setIsProductModalOpen] = useState(false);
    const [isOrderModalOpen, setIsOrderModalOpen] = useState(false);
    const [isPartnerModalOpen, setIsPartnerModalOpen] = useState(false);

    const [newProduct, setNewProduct] = useState<Omit<Product, 'id' | 'total'>>({
        name: '',
        unitPrice: 0,
        quantity: 1,
        deliveryFee: 0
    });

    const [newOrder, setNewOrder] = useState<Omit<Order, 'id' | 'totalAmount' | 'products'>>({
        customerName: '',
        customerEmail: '',
        status: 'pending',
        orderDate: new Date().toISOString().split('T')[0],
        deliveryAddress: ''
    });

    const [newPartner, setNewPartner] = useState<Omit<DeliveryPartner, 'id'>>({
        name: '',
        email: '',
        phone: '',
        trackingCode: '',
        trackingLink: '',
        vehicleType: '',
        status: 'available'
    });

    // Calculate total for a product
    const calculateProductTotal = (product: Omit<Product, 'id' | 'total'>) => {
        return (product.unitPrice * product.quantity) + product.deliveryFee;
    };

    // Product CRUD operations
    const handleAddProduct = () => {
        const productToAdd: Product = {
            ...newProduct,
            id: Math.max(0, ...products.map(p => p.id)) + 1,
            total: calculateProductTotal(newProduct)
        };
        setProducts([...products, productToAdd]);
        setIsProductModalOpen(false);
        setNewProduct({ name: '', unitPrice: 0, quantity: 1, deliveryFee: 0 });
    };

    const handleEditProduct = (product: Product) => {
        setEditingProduct(product);
        setIsProductModalOpen(true);
    };

    const handleUpdateProduct = () => {
        if (editingProduct) {
            const updatedProduct = {
                ...editingProduct,
                total: calculateProductTotal(editingProduct)
            };
            setProducts(products.map(p => p.id === updatedProduct.id ? updatedProduct : p));
            setEditingProduct(null);
            setIsProductModalOpen(false);
        }
    };

    const handleDeleteProduct = (productId: number) => {
        if (window.confirm('Are you sure you want to delete this product?')) {
            setProducts(products.filter(p => p.id !== productId));
        }
    };

    // Order CRUD operations
    const handleAddOrder = () => {
        const orderToAdd: Order = {
            ...newOrder,
            id: Math.max(0, ...orders.map(o => o.id)) + 1,
            products: [],
            totalAmount: 0
        };
        setOrders([...orders, orderToAdd]);
        setIsOrderModalOpen(false);
        setNewOrder({
            customerName: '',
            customerEmail: '',
            status: 'pending',
            orderDate: new Date().toISOString().split('T')[0],
            deliveryAddress: ''
        });
    };

    const handleEditOrder = (order: Order) => {
        setEditingOrder(order);
        setIsOrderModalOpen(true);
    };

    const handleUpdateOrder = () => {
        if (editingOrder) {
            setOrders(orders.map(o => o.id === editingOrder.id ? editingOrder : o));
            setEditingOrder(null);
            setIsOrderModalOpen(false);
        }
    };

    const handleDeleteOrder = (orderId: number) => {
        if (window.confirm('Are you sure you want to delete this order?')) {
            setOrders(orders.filter(o => o.id !== orderId));
        }
    };

    const handleOrderStatusChange = (orderId: number, newStatus: Order['status']) => {
        setOrders(orders.map(order =>
            order.id === orderId ? { ...order, status: newStatus } : order
        ));
    };

    // Delivery Partner CRUD operations
    const handleAddPartner = () => {
        const partnerToAdd: DeliveryPartner = {
            ...newPartner,
            id: Math.max(0, ...deliveryPartners.map(p => p.id)) + 1
        };
        setDeliveryPartners([...deliveryPartners, partnerToAdd]);
        setIsPartnerModalOpen(false);
        setNewPartner({
            name: '',
            email: '',
            phone: '',
            trackingCode: '',
            trackingLink: '',
            vehicleType: '',
            status: 'available'
        });
    };

    const handleEditPartner = (partner: DeliveryPartner) => {
        setEditingPartner(partner);
        setIsPartnerModalOpen(true);
    };

    const handleUpdatePartner = () => {
        if (editingPartner) {
            setDeliveryPartners(deliveryPartners.map(p => p.id === editingPartner.id ? editingPartner : p));
            setEditingPartner(null);
            setIsPartnerModalOpen(false);
        }
    };

    const handleDeletePartner = (partnerId: number) => {
        if (window.confirm('Are you sure you want to delete this delivery partner?')) {
            setDeliveryPartners(deliveryPartners.filter(p => p.id !== partnerId));
        }
    };

    // Status badge component
    const StatusBadge = ({ status }: { status: string }) => {
        const statusConfig = {
            pending: { color: 'bg-yellow-100 text-yellow-800', label: 'Pending' },
            confirmed: { color: 'bg-blue-100 text-blue-800', label: 'Confirmed' },
            shipped: { color: 'bg-purple-100 text-purple-800', label: 'Shipped' },
            delivered: { color: 'bg-green-100 text-green-800', label: 'Delivered' },
            cancelled: { color: 'bg-red-100 text-red-800', label: 'Cancelled' },
            available: { color: 'bg-green-100 text-green-800', label: 'Available' },
            busy: { color: 'bg-yellow-100 text-yellow-800', label: 'Busy' },
            offline: { color: 'bg-gray-100 text-gray-800', label: 'Offline' }
        };

        const config = statusConfig[status as keyof typeof statusConfig] || statusConfig.pending;

        return (
            <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${config.color}`}>
                {config.label}
            </span>
        );
    };

    return (
        <div className="min-h-screen bg-gray-50 p-6">
            <div className="max-w-7xl mx-auto">
                {/* Header */}
                <div className="mb-8">
                    <h2 className="text-2xl font-bold text-gray-900">📦 Delivery Management</h2>
                    <p className="text-gray-600">Manage products, orders, and delivery partners</p>
                </div>

                {/* Stats Cards */}
                <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
                    <div className="bg-white p-6 rounded-lg shadow">
                        <div className="flex items-center">
                            <div className="p-3 bg-blue-100 rounded-lg">
                                <span className="text-2xl">📦</span>
                            </div>
                            <div className="ml-4">
                                <p className="text-sm font-medium text-gray-600">Total Orders</p>
                                <p className="text-2xl font-bold">{orders.length}</p>
                            </div>
                        </div>
                    </div>
                    <div className="bg-white p-6 rounded-lg shadow">
                        <div className="flex items-center">
                            <div className="p-3 bg-green-100 rounded-lg">
                                <span className="text-2xl">🚚</span>
                            </div>
                            <div className="ml-4">
                                <p className="text-sm font-medium text-gray-600">Delivery Partners</p>
                                <p className="text-2xl font-bold">{deliveryPartners.length}</p>
                            </div>
                        </div>
                    </div>
                    <div className="bg-white p-6 rounded-lg shadow">
                        <div className="flex items-center">
                            <div className="p-3 bg-purple-100 rounded-lg">
                                <span className="text-2xl">💰</span>
                            </div>
                            <div className="ml-4">
                                <p className="text-sm font-medium text-gray-600">Total Revenue</p>
                                <p className="text-2xl font-bold">
                                    ${orders.reduce((sum, order) => sum + order.totalAmount, 0).toLocaleString()}
                                </p>
                            </div>
                        </div>
                    </div>
                    <div className="bg-white p-6 rounded-lg shadow">
                        <div className="flex items-center">
                            <div className="p-3 bg-orange-100 rounded-lg">
                                <span className="text-2xl">⏳</span>
                            </div>
                            <div className="ml-4">
                                <p className="text-sm font-medium text-gray-600">Pending Orders</p>
                                <p className="text-2xl font-bold">{orders.filter(o => o.status === 'pending').length}</p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Tabs */}
                <div className="bg-white rounded-lg shadow mb-6">
                    <div className="border-b border-gray-200">
                        <nav className="flex -mb-px">
                            {(['orders', 'products', 'delivery-partners'] as const).map((tab) => (
                                <button
                                    key={tab}
                                    className={`flex-1 py-4 px-6 text-center font-medium text-sm ${
                                        activeTab === tab
                                            ? 'border-b-2 border-blue-500 text-blue-600'
                                            : 'text-gray-500 hover:text-gray-700'
                                    }`}
                                    onClick={() => setActiveTab(tab)}
                                >
                                    {tab === 'orders' && '📋 Orders'}
                                    {tab === 'products' && '🛍️ Products'}
                                    {tab === 'delivery-partners' && '🚚 Delivery Partners'}
                                </button>
                            ))}
                        </nav>
                    </div>

                    <div className="p-6">
                        {/* Orders Tab */}
                        {activeTab === 'orders' && (
                            <div>
                                <div className="flex justify-between items-center mb-4">
                                    <h3 className="text-lg font-semibold">Order Management</h3>
                                    <button
                                        onClick={() => setIsOrderModalOpen(true)}
                                        className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
                                    >
                                        + Add Order
                                    </button>
                                </div>
                                <div className="overflow-x-auto">
                                    <table className="min-w-full divide-y divide-gray-200">
                                        <thead className="bg-gray-50">
                                        <tr>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Order ID</th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Customer</th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Date</th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Amount</th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                                        </tr>
                                        </thead>
                                        <tbody className="bg-white divide-y divide-gray-200">
                                        {orders.map((order) => (
                                            <tr key={order.id} className="hover:bg-gray-50">
                                                <td className="px-6 py-4 font-medium">#{order.id}</td>
                                                <td className="px-6 py-4">
                                                    <div>
                                                        <div className="font-medium">{order.customerName}</div>
                                                        <div className="text-sm text-gray-500">{order.customerEmail}</div>
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4">{new Date(order.orderDate).toLocaleDateString()}</td>
                                                <td className="px-6 py-4 font-medium">${order.totalAmount}</td>
                                                <td className="px-6 py-4">
                                                    <select
                                                        value={order.status}
                                                        onChange={(e) => handleOrderStatusChange(order.id, e.target.value as Order['status'])}
                                                        className="border border-gray-300 rounded px-2 py-1 text-sm"
                                                    >
                                                        <option value="pending">Pending</option>
                                                        <option value="confirmed">Confirmed</option>
                                                        <option value="shipped">Shipped</option>
                                                        <option value="delivered">Delivered</option>
                                                        <option value="cancelled">Cancelled</option>
                                                    </select>
                                                </td>
                                                <td className="px-6 py-4 space-x-2">
                                                    <button
                                                        onClick={() => handleEditOrder(order)}
                                                        className="text-blue-600 hover:text-blue-900 text-sm"
                                                    >
                                                        Edit
                                                    </button>
                                                    <button
                                                        onClick={() => handleDeleteOrder(order.id)}
                                                        className="text-red-600 hover:text-red-900 text-sm"
                                                    >
                                                        Delete
                                                    </button>
                                                </td>
                                            </tr>
                                        ))}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        )}

                        {/* Products Tab */}
                        {activeTab === 'products' && (
                            <div>
                                <div className="flex justify-between items-center mb-4">
                                    <h3 className="text-lg font-semibold">Product Management</h3>
                                    <button
                                        onClick={() => setIsProductModalOpen(true)}
                                        className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
                                    >
                                        + Add Product
                                    </button>
                                </div>
                                <div className="overflow-x-auto">
                                    <table className="min-w-full divide-y divide-gray-200">
                                        <thead className="bg-gray-50">
                                        <tr>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Product</th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Unit Price</th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Quantity</th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Delivery Fee</th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Total</th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
                                        </tr>
                                        </thead>
                                        <tbody className="bg-white divide-y divide-gray-200">
                                        {products.map((product) => (
                                            <tr key={product.id} className="hover:bg-gray-50">
                                                <td className="px-6 py-4 font-medium">{product.name}</td>
                                                <td className="px-6 py-4">${product.unitPrice}</td>
                                                <td className="px-6 py-4">{product.quantity}</td>
                                                <td className="px-6 py-4">${product.deliveryFee}</td>
                                                <td className="px-6 py-4 font-medium">${product.total}</td>
                                                <td className="px-6 py-4 space-x-2">
                                                    <button
                                                        onClick={() => handleEditProduct(product)}
                                                        className="text-blue-600 hover:text-blue-900 text-sm"
                                                    >
                                                        Edit
                                                    </button>
                                                    <button
                                                        onClick={() => handleDeleteProduct(product.id)}
                                                        className="text-red-600 hover:text-red-900 text-sm"
                                                    >
                                                        Delete
                                                    </button>
                                                </td>
                                            </tr>
                                        ))}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        )}

                        {/* Delivery Partners Tab */}
                        {activeTab === 'delivery-partners' && (
                            <div>
                                <div className="flex justify-between items-center mb-4">
                                    <h3 className="text-lg font-semibold">Delivery Partners</h3>
                                    <button
                                        onClick={() => setIsPartnerModalOpen(true)}
                                        className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700"
                                    >
                                        + Add Partner
                                    </button>
                                </div>
                                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                                    {deliveryPartners.map((partner) => (
                                        <div key={partner.id} className="bg-gray-50 rounded-lg p-6">
                                            <div className="flex justify-between items-start mb-4">
                                                <div>
                                                    <h4 className="font-semibold text-lg">{partner.name}</h4>
                                                    <p className="text-gray-600">{partner.vehicleType}</p>
                                                </div>
                                                <StatusBadge status={partner.status} />
                                            </div>
                                            <div className="space-y-2 text-sm">
                                                <div className="flex justify-between">
                                                    <span className="text-gray-600">Email:</span>
                                                    <span>{partner.email}</span>
                                                </div>
                                                <div className="flex justify-between">
                                                    <span className="text-gray-600">Phone:</span>
                                                    <span>{partner.phone}</span>
                                                </div>
                                                <div className="flex justify-between">
                                                    <span className="text-gray-600">Tracking Code:</span>
                                                    <span className="font-mono">{partner.trackingCode}</span>
                                                </div>
                                                <div className="flex justify-between">
                                                    <span className="text-gray-600">Tracking Link:</span>
                                                    <a href={partner.trackingLink} target="_blank" rel="noopener noreferrer" className="text-blue-600 hover:underline">
                                                        View
                                                    </a>
                                                </div>
                                            </div>
                                            <div className="flex space-x-2 mt-4">
                                                <button
                                                    onClick={() => handleEditPartner(partner)}
                                                    className="flex-1 bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 text-sm"
                                                >
                                                    Edit
                                                </button>
                                                <button
                                                    onClick={() => handleDeletePartner(partner.id)}
                                                    className="flex-1 bg-red-600 text-white py-2 rounded-lg hover:bg-red-700 text-sm"
                                                >
                                                    Delete
                                                </button>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        )}
                    </div>
                </div>

                {/* Modals */}
                {/* Product Modal */}
                {(isProductModalOpen || editingProduct) && (
                    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
                        <div className="bg-white rounded-lg shadow-xl max-w-md w-full">
                            <div className="p-6">
                                <h3 className="text-lg font-semibold mb-4">
                                    {editingProduct ? 'Edit Product' : 'Add New Product'}
                                </h3>
                                <div className="space-y-4">
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1">Product Name</label>
                                        <input
                                            type="text"
                                            className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                            value={editingProduct ? editingProduct.name : newProduct.name}
                                            onChange={(e) => editingProduct
                                                ? setEditingProduct({...editingProduct, name: e.target.value})
                                                : setNewProduct({...newProduct, name: e.target.value})
                                            }
                                        />
                                    </div>
                                    <div className="grid grid-cols-2 gap-4">
                                        <div>
                                            <label className="block text-sm font-medium text-gray-700 mb-1">Unit Price ($)</label>
                                            <input
                                                type="number"
                                                className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                                value={editingProduct ? editingProduct.unitPrice : newProduct.unitPrice}
                                                onChange={(e) => {
                                                    const value = parseFloat(e.target.value) || 0;
                                                    if (editingProduct) {
                                                        setEditingProduct({...editingProduct, unitPrice: value});
                                                    } else {
                                                        setNewProduct({...newProduct, unitPrice: value});
                                                    }
                                                }}
                                            />
                                        </div>
                                        <div>
                                            <label className="block text-sm font-medium text-gray-700 mb-1">Quantity</label>
                                            <input
                                                type="number"
                                                className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                                value={editingProduct ? editingProduct.quantity : newProduct.quantity}
                                                onChange={(e) => {
                                                    const value = parseInt(e.target.value) || 1;
                                                    if (editingProduct) {
                                                        setEditingProduct({...editingProduct, quantity: value});
                                                    } else {
                                                        setNewProduct({...newProduct, quantity: value});
                                                    }
                                                }}
                                            />
                                        </div>
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1">Delivery Fee ($)</label>
                                        <input
                                            type="number"
                                            className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                            value={editingProduct ? editingProduct.deliveryFee : newProduct.deliveryFee}
                                            onChange={(e) => {
                                                const value = parseFloat(e.target.value) || 0;
                                                if (editingProduct) {
                                                    setEditingProduct({...editingProduct, deliveryFee: value});
                                                } else {
                                                    setNewProduct({...newProduct, deliveryFee: value});
                                                }
                                            }}
                                        />
                                    </div>
                                    <div className="pt-2">
                                        <label className="block text-sm font-medium text-gray-700 mb-1">Total</label>
                                        <div className="text-lg font-semibold">
                                            ${editingProduct
                                            ? calculateProductTotal(editingProduct).toFixed(2)
                                            : calculateProductTotal(newProduct).toFixed(2)
                                        }
                                        </div>
                                    </div>
                                </div>
                                <div className="flex justify-end space-x-3 mt-6">
                                    <button
                                        onClick={() => {
                                            setIsProductModalOpen(false);
                                            setEditingProduct(null);
                                        }}
                                        className="px-4 py-2 text-gray-600 hover:text-gray-800"
                                    >
                                        Cancel
                                    </button>
                                    <button
                                        onClick={editingProduct ? handleUpdateProduct : handleAddProduct}
                                        className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
                                    >
                                        {editingProduct ? 'Update' : 'Add'} Product
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                )}

                {/* Order Modal */}
                {(isOrderModalOpen || editingOrder) && (
                    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
                        <div className="bg-white rounded-lg shadow-xl max-w-md w-full">
                            <div className="p-6">
                                <h3 className="text-lg font-semibold mb-4">
                                    {editingOrder ? 'Edit Order' : 'Add New Order'}
                                </h3>
                                <div className="space-y-4">
                                    <div className="grid grid-cols-2 gap-4">
                                        <div>
                                            <label className="block text-sm font-medium text-gray-700 mb-1">Customer Name</label>
                                            <input
                                                type="text"
                                                className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                                value={editingOrder ? editingOrder.customerName : newOrder.customerName}
                                                onChange={(e) => editingOrder
                                                    ? setEditingOrder({...editingOrder, customerName: e.target.value})
                                                    : setNewOrder({...newOrder, customerName: e.target.value})
                                                }
                                            />
                                        </div>
                                        <div>
                                            <label className="block text-sm font-medium text-gray-700 mb-1">Customer Email</label>
                                            <input
                                                type="email"
                                                className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                                value={editingOrder ? editingOrder.customerEmail : newOrder.customerEmail}
                                                onChange={(e) => editingOrder
                                                    ? setEditingOrder({...editingOrder, customerEmail: e.target.value})
                                                    : setNewOrder({...newOrder, customerEmail: e.target.value})
                                                }
                                            />
                                        </div>
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1">Delivery Address</label>
                                        <textarea
                                            className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                            rows={3}
                                            value={editingOrder ? editingOrder.deliveryAddress : newOrder.deliveryAddress}
                                            onChange={(e) => editingOrder
                                                ? setEditingOrder({...editingOrder, deliveryAddress: e.target.value})
                                                : setNewOrder({...newOrder, deliveryAddress: e.target.value})
                                            }
                                        />
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1">Status</label>
                                        <select
                                            className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                            value={editingOrder ? editingOrder.status : newOrder.status}
                                            onChange={(e) => editingOrder
                                                ? setEditingOrder({...editingOrder, status: e.target.value as Order['status']})
                                                : setNewOrder({...newOrder, status: e.target.value as Order['status']})
                                            }
                                        >
                                            <option value="pending">Pending</option>
                                            <option value="confirmed">Confirmed</option>
                                            <option value="shipped">Shipped</option>
                                            <option value="delivered">Delivered</option>
                                            <option value="cancelled">Cancelled</option>
                                        </select>
                                    </div>
                                </div>
                                <div className="flex justify-end space-x-3 mt-6">
                                    <button
                                        onClick={() => {
                                            setIsOrderModalOpen(false);
                                            setEditingOrder(null);
                                        }}
                                        className="px-4 py-2 text-gray-600 hover:text-gray-800"
                                    >
                                        Cancel
                                    </button>
                                    <button
                                        onClick={editingOrder ? handleUpdateOrder : handleAddOrder}
                                        className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
                                    >
                                        {editingOrder ? 'Update' : 'Add'} Order
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                )}

                {/* Delivery Partner Modal */}
                {(isPartnerModalOpen || editingPartner) && (
                    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
                        <div className="bg-white rounded-lg shadow-xl max-w-md w-full">
                            <div className="p-6">
                                <h3 className="text-lg font-semibold mb-4">
                                    {editingPartner ? 'Edit Delivery Partner' : 'Add New Delivery Partner'}
                                </h3>
                                <div className="space-y-4">
                                    <div className="grid grid-cols-2 gap-4">
                                        <div>
                                            <label className="block text-sm font-medium text-gray-700 mb-1">Name</label>
                                            <input
                                                type="text"
                                                className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                                value={editingPartner ? editingPartner.name : newPartner.name}
                                                onChange={(e) => editingPartner
                                                    ? setEditingPartner({...editingPartner, name: e.target.value})
                                                    : setNewPartner({...newPartner, name: e.target.value})
                                                }
                                            />
                                        </div>
                                        <div>
                                            <label className="block text-sm font-medium text-gray-700 mb-1">Vehicle Type</label>
                                            <input
                                                type="text"
                                                className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                                value={editingPartner ? editingPartner.vehicleType : newPartner.vehicleType}
                                                onChange={(e) => editingPartner
                                                    ? setEditingPartner({...editingPartner, vehicleType: e.target.value})
                                                    : setNewPartner({...newPartner, vehicleType: e.target.value})
                                                }
                                            />
                                        </div>
                                    </div>
                                    <div className="grid grid-cols-2 gap-4">
                                        <div>
                                            <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                                            <input
                                                type="email"
                                                className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                                value={editingPartner ? editingPartner.email : newPartner.email}
                                                onChange={(e) => editingPartner
                                                    ? setEditingPartner({...editingPartner, email: e.target.value})
                                                    : setNewPartner({...newPartner, email: e.target.value})
                                                }
                                            />
                                        </div>
                                        <div>
                                            <label className="block text-sm font-medium text-gray-700 mb-1">Phone</label>
                                            <input
                                                type="text"
                                                className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                                value={editingPartner ? editingPartner.phone : newPartner.phone}
                                                onChange={(e) => editingPartner
                                                    ? setEditingPartner({...editingPartner, phone: e.target.value})
                                                    : setNewPartner({...newPartner, phone: e.target.value})
                                                }
                                            />
                                        </div>
                                    </div>
                                    <div className="grid grid-cols-2 gap-4">
                                        <div>
                                            <label className="block text-sm font-medium text-gray-700 mb-1">Tracking Code</label>
                                            <input
                                                type="text"
                                                className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                                value={editingPartner ? editingPartner.trackingCode : newPartner.trackingCode}
                                                onChange={(e) => editingPartner
                                                    ? setEditingPartner({...editingPartner, trackingCode: e.target.value})
                                                    : setNewPartner({...newPartner, trackingCode: e.target.value})
                                                }
                                            />
                                        </div>
                                        <div>
                                            <label className="block text-sm font-medium text-gray-700 mb-1">Status</label>
                                            <select
                                                className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                                value={editingPartner ? editingPartner.status : newPartner.status}
                                                onChange={(e) => editingPartner
                                                    ? setEditingPartner({...editingPartner, status: e.target.value as DeliveryPartner['status']})
                                                    : setNewPartner({...newPartner, status: e.target.value as DeliveryPartner['status']})
                                                }
                                            >
                                                <option value="available">Available</option>
                                                <option value="busy">Busy</option>
                                                <option value="offline">Offline</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700 mb-1">Tracking Link</label>
                                        <input
                                            type="url"
                                            className="w-full px-3 py-2 border border-gray-300 rounded-lg"
                                            value={editingPartner ? editingPartner.trackingLink : newPartner.trackingLink}
                                            onChange={(e) => editingPartner
                                                ? setEditingPartner({...editingPartner, trackingLink: e.target.value})
                                                : setNewPartner({...newPartner, trackingLink: e.target.value})
                                            }
                                        />
                                    </div>
                                </div>
                                <div className="flex justify-end space-x-3 mt-6">
                                    <button
                                        onClick={() => {
                                            setIsPartnerModalOpen(false);
                                            setEditingPartner(null);
                                        }}
                                        className="px-4 py-2 text-gray-600 hover:text-gray-800"
                                    >
                                        Cancel
                                    </button>
                                    <button
                                        onClick={editingPartner ? handleUpdatePartner : handleAddPartner}
                                        className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
                                    >
                                        {editingPartner ? 'Update' : 'Add'} Partner
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}