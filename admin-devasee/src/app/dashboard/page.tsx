'use client'

import { useState } from 'react';

// Type definitions
interface AnalyticsData {
    revenue: RevenueData[];
    userGrowth: UserGrowthData[];
    productPerformance: ProductPerformanceData[];
    orderStats: OrderStats;
    trafficSources: TrafficSource[];
    salesData: SalesData[];
}

interface RevenueData {
    month: string;
    revenue: number;
    profit: number;
    orders: number;
}

interface UserGrowthData {
    date: string;
    users: number;
    newUsers: number;
    activeUsers: number;
}

interface ProductPerformanceData {
    product: string;
    sales: number;
    revenue: number;
    conversion: number;
}

interface OrderStats {
    total: number;
    completed: number;
    pending: number;
    cancelled: number;
    averageOrderValue: number;
}

interface TrafficSource {
    source: string;
    visitors: number;
    conversion: number;
    revenue: number;
}

interface SalesData {
    hour: number;
    sales: number;
    visitors: number;
}

// Mock data
const mockAnalyticsData: AnalyticsData = {
    revenue: [
        { month: 'Jan', revenue: 12500, profit: 8500, orders: 234 },
        { month: 'Feb', revenue: 11800, profit: 7900, orders: 210 },
        { month: 'Mar', revenue: 14200, profit: 9200, orders: 256 },
        { month: 'Apr', revenue: 15600, profit: 10200, orders: 278 },
        { month: 'May', revenue: 16800, profit: 11000, orders: 295 },
        { month: 'Jun', revenue: 18900, profit: 12500, orders: 312 },
    ],
    userGrowth: [
        { date: 'Week 1', users: 1200, newUsers: 150, activeUsers: 890 },
        { date: 'Week 2', users: 1450, newUsers: 180, activeUsers: 1020 },
        { date: 'Week 3', users: 1670, newUsers: 165, activeUsers: 1150 },
        { date: 'Week 4', users: 1920, newUsers: 190, activeUsers: 1280 },
        { date: 'Week 5', users: 2180, newUsers: 210, activeUsers: 1420 },
    ],
    productPerformance: [
        { product: 'Laptops', sales: 156, revenue: 156000, conversion: 12.5 },
        { product: 'Smartphones', sales: 289, revenue: 202300, conversion: 18.2 },
        { product: 'Tablets', sales: 98, revenue: 58800, conversion: 8.7 },
        { product: 'Accessories', sales: 456, revenue: 45600, conversion: 22.1 },
        { product: 'Software', sales: 234, revenue: 70200, conversion: 15.6 },
    ],
    orderStats: {
        total: 1567,
        completed: 1345,
        pending: 156,
        cancelled: 66,
        averageOrderValue: 156.78,
    },
    trafficSources: [
        { source: 'Organic Search', visitors: 4560, conversion: 4.2, revenue: 68200 },
        { source: 'Social Media', visitors: 2340, conversion: 3.1, revenue: 34500 },
        { source: 'Direct', visitors: 1890, conversion: 5.6, revenue: 42300 },
        { source: 'Email', visitors: 1230, conversion: 7.8, revenue: 28900 },
        { source: 'Referral', visitors: 890, conversion: 6.2, revenue: 15600 },
    ],
    salesData: [
        { hour: 9, sales: 12, visitors: 156 },
        { hour: 10, sales: 18, visitors: 234 },
        { hour: 11, sales: 23, visitors: 289 },
        { hour: 12, sales: 28, visitors: 345 },
        { hour: 13, sales: 25, visitors: 312 },
        { hour: 14, sales: 30, visitors: 378 },
        { hour: 15, sales: 32, visitors: 401 },
        { hour: 16, sales: 29, visitors: 367 },
        { hour: 17, sales: 24, visitors: 298 },
    ],
};

export default function AnalyticsPage() {
    const [analyticsData, setAnalyticsData] = useState<AnalyticsData>(mockAnalyticsData);
    const [timeRange, setTimeRange] = useState<'7d' | '30d' | '90d' | '1y'>('30d');
    const [activeMetric, setActiveMetric] = useState<'revenue' | 'users' | 'conversion'>('revenue');

    // Simple bar chart component
    const BarChart = ({ data, width, height, color = '#3b82f6' }: {
        data: { label: string; value: number }[];
        width: number;
        height: number;
        color?: string;
    }) => {
        const maxValue = Math.max(...data.map(d => d.value));
        const barWidth = (width - 40) / data.length;

        return (
            <svg width={width} height={height} className="mt-4">
                {data.map((item, index) => {
                    const barHeight = (item.value / maxValue) * (height - 60);
                    return (
                        <g key={index} transform={`translate(${index * barWidth + 30}, ${height - barHeight - 30})`}>
                            <rect
                                width={barWidth - 10}
                                height={barHeight}
                                fill={color}
                                rx={4}
                                className="opacity-80 hover:opacity-100 transition-opacity"
                            />
                            <text
                                x={barWidth / 2 - 5}
                                y={barHeight + 20}
                                textAnchor="middle"
                                className="text-xs fill-gray-600"
                            >
                                {item.label}
                            </text>
                            <text
                                x={barWidth / 2 - 5}
                                y={-5}
                                textAnchor="middle"
                                className="text-xs fill-gray-800 font-medium"
                            >
                                {item.value}
                            </text>
                        </g>
                    );
                })}
            </svg>
        );
    };

    // Line chart component
    const LineChart = ({ data, width, height, color = '#10b981' }: {
        data: { label: string; value: number }[];
        width: number;
        height: number;
        color?: string;
    }) => {
        const maxValue = Math.max(...data.map(d => d.value));
        const pointWidth = (width - 40) / (data.length - 1);

        const points = data.map((item, index) => {
            const x = index * pointWidth + 30;
            const y = height - 30 - (item.value / maxValue) * (height - 60);
            return `${x},${y}`;
        }).join(' ');

        return (
            <svg width={width} height={height} className="mt-4">
                <polyline
                    fill="none"
                    stroke={color}
                    strokeWidth="2"
                    points={points}
                    className="opacity-80"
                />
                {data.map((item, index) => {
                    const x = index * pointWidth + 30;
                    const y = height - 30 - (item.value / maxValue) * (height - 60);
                    return (
                        <g key={index}>
                            <circle cx={x} cy={y} r="4" fill={color} className="opacity-80 hover:opacity-100" />
                            <text x={x} y={y - 10} textAnchor="middle" className="text-xs fill-gray-800 font-medium">
                                {item.value}
                            </text>
                            <text x={x} y={height - 10} textAnchor="middle" className="text-xs fill-gray-600">
                                {item.label}
                            </text>
                        </g>
                    );
                })}
            </svg>
        );
    };

    // Pie chart component
    const PieChart = ({ data, width, height }: {
        data: { label: string; value: number; color: string }[];
        width: number;
        height: number;
    }) => {
        const total = data.reduce((sum, item) => sum + item.value, 0);
        let currentAngle = 0;
        const radius = Math.min(width, height) / 2 - 20;
        const centerX = width / 2;
        const centerY = height / 2;

        return (
            <svg width={width} height={height} className="mt-4">
                {data.map((item, index) => {
                    const angle = (item.value / total) * 360;
                    const startAngle = currentAngle;
                    const endAngle = currentAngle + angle;
                    currentAngle += angle;

                    const x1 = centerX + radius * Math.cos((startAngle * Math.PI) / 180);
                    const y1 = centerY + radius * Math.sin((startAngle * Math.PI) / 180);
                    const x2 = centerX + radius * Math.cos((endAngle * Math.PI) / 180);
                    const y2 = centerY + radius * Math.sin((endAngle * Math.PI) / 180);

                    const largeArc = angle > 180 ? 1 : 0;

                    const pathData = [
                        `M ${centerX} ${centerY}`,
                        `L ${x1} ${y1}`,
                        `A ${radius} ${radius} 0 ${largeArc} 1 ${x2} ${y2}`,
                        'Z'
                    ].join(' ');

                    return (
                        <g key={index}>
                            <path d={pathData} fill={item.color} className="opacity-80 hover:opacity-100 transition-opacity" />
                            <text
                                x={centerX + (radius + 20) * Math.cos(((startAngle + angle / 2) * Math.PI) / 180)}
                                y={centerY + (radius + 20) * Math.sin(((startAngle + angle / 2) * Math.PI) / 180)}
                                textAnchor="middle"
                                className="text-xs fill-gray-800 font-medium"
                            >
                                {Math.round((item.value / total) * 100)}%
                            </text>
                        </g>
                    );
                })}
                <circle cx={centerX} cy={centerY} r={radius * 0.6} fill="white" />
                <text x={centerX} y={centerY} textAnchor="middle" className="text-lg font-bold fill-gray-800">
                    ${total.toLocaleString()}
                </text>
                <text x={centerX} y={centerY + 20} textAnchor="middle" className="text-xs fill-gray-600">
                    Total Revenue
                </text>
            </svg>
        );
    };

    // Calculate metrics
    const totalRevenue = analyticsData.revenue.reduce((sum, month) => sum + month.revenue, 0);
    const growthRate = ((analyticsData.revenue[analyticsData.revenue.length - 1].revenue -
            analyticsData.revenue[analyticsData.revenue.length - 2].revenue) /
        analyticsData.revenue[analyticsData.revenue.length - 2].revenue) * 100;
    const conversionRate = analyticsData.trafficSources.reduce((sum, source) => sum + source.conversion, 0) /
        analyticsData.trafficSources.length;

    return (
        <div className="min-h-screen bg-gray-50 p-6">
            <div className="max-w-7xl mx-auto">
                {/* Header */}
                <div className="mb-8">
                    <div className="flex justify-between items-center">
                        <div>
                            <h2 className="text-2xl font-bold text-gray-900">📊 Analytics Dashboard</h2>
                            <p className="text-gray-600">Monitor your business performance and key metrics</p>
                        </div>
                        <div className="flex space-x-2">
                            <select
                                value={timeRange}
                                onChange={(e) => setTimeRange(e.target.value as never)}
                                className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                            >
                                <option value="7d">Last 7 days</option>
                                <option value="30d">Last 30 days</option>
                                <option value="90d">Last 90 days</option>
                                <option value="1y">Last year</option>
                            </select>
                        </div>
                    </div>
                </div>

                {/* Key Metrics */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
                    <div className="bg-white p-6 rounded-lg shadow">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-600">Total Revenue</p>
                                <p className="text-2xl font-bold">${totalRevenue.toLocaleString()}</p>
                                <p className={`text-sm ${growthRate >= 0 ? 'text-green-600' : 'text-red-600'}`}>
                                    {growthRate >= 0 ? '↗' : '↘'} {Math.abs(growthRate).toFixed(1)}% from last period
                                </p>
                            </div>
                            <div className="p-3 bg-blue-100 rounded-lg">
                                <span className="text-2xl">💰</span>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white p-6 rounded-lg shadow">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-600">Total Orders</p>
                                <p className="text-2xl font-bold">{analyticsData.orderStats.total}</p>
                                <p className="text-sm text-gray-600">
                                    {analyticsData.orderStats.completed} completed • {analyticsData.orderStats.pending} pending
                                </p>
                            </div>
                            <div className="p-3 bg-green-100 rounded-lg">
                                <span className="text-2xl">📦</span>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white p-6 rounded-lg shadow">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-600">Conversion Rate</p>
                                <p className="text-2xl font-bold">{conversionRate.toFixed(1)}%</p>
                                <p className="text-sm text-gray-600">Average across all channels</p>
                            </div>
                            <div className="p-3 bg-purple-100 rounded-lg">
                                <span className="text-2xl">📈</span>
                            </div>
                        </div>
                    </div>

                    <div className="bg-white p-6 rounded-lg shadow">
                        <div className="flex items-center justify-between">
                            <div>
                                <p className="text-sm font-medium text-gray-600">Active Users</p>
                                <p className="text-2xl font-bold">
                                    {analyticsData.userGrowth[analyticsData.userGrowth.length - 1].activeUsers}
                                </p>
                                <p className="text-sm text-gray-600">
                                    +{analyticsData.userGrowth[analyticsData.userGrowth.length - 1].newUsers} new this week
                                </p>
                            </div>
                            <div className="p-3 bg-orange-100 rounded-lg">
                                <span className="text-2xl">👥</span>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Charts Grid */}
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
                    {/* Revenue Chart */}
                    <div className="bg-white p-6 rounded-lg shadow">
                        <div className="flex justify-between items-center mb-4">
                            <h3 className="text-lg font-semibold">Revenue Overview</h3>
                            <div className="flex space-x-2">
                                <button
                                    onClick={() => setActiveMetric('revenue')}
                                    className={`px-3 py-1 rounded text-sm ${
                                        activeMetric === 'revenue' ? 'bg-blue-100 text-blue-600' : 'text-gray-600'
                                    }`}
                                >
                                    Revenue
                                </button>
                                <button
                                    onClick={() => setActiveMetric('users')}
                                    className={`px-3 py-1 rounded text-sm ${
                                        activeMetric === 'users' ? 'bg-blue-100 text-blue-600' : 'text-gray-600'
                                    }`}
                                >
                                    Users
                                </button>
                            </div>
                        </div>
                        <BarChart
                            data={analyticsData.revenue.map(item => ({
                                label: item.month,
                                value: activeMetric === 'revenue' ? item.revenue : item.orders
                            }))}
                            width={500}
                            height={300}
                            color={activeMetric === 'revenue' ? '#3b82f6' : '#10b981'}
                        />
                    </div>

                    {/* User Growth Chart */}
                    <div className="bg-white p-6 rounded-lg shadow">
                        <h3 className="text-lg font-semibold mb-4">User Growth</h3>
                        <LineChart
                            data={analyticsData.userGrowth.map(item => ({
                                label: item.date,
                                value: item.activeUsers
                            }))}
                            width={500}
                            height={300}
                        />
                    </div>

                    {/* Traffic Sources */}
                    <div className="bg-white p-6 rounded-lg shadow">
                        <h3 className="text-lg font-semibold mb-4">Traffic Sources</h3>
                        <PieChart
                            data={analyticsData.trafficSources.map((source, index) => ({
                                label: source.source,
                                value: source.visitors,
                                color: ['#3b82f6', '#10b981', '#8b5cf6', '#f59e0b', '#ef4444'][index]
                            }))}
                            width={500}
                            height={300}
                        />
                        <div className="mt-4 space-y-2">
                            {analyticsData.trafficSources.map((source, index) => (
                                <div key={index} className="flex justify-between items-center text-sm">
                                    <div className="flex items-center">
                                        <div
                                            className="w-3 h-3 rounded-full mr-2"
                                            style={{ backgroundColor: ['#3b82f6', '#10b981', '#8b5cf6', '#f59e0b', '#ef4444'][index] }}
                                        ></div>
                                        <span>{source.source}</span>
                                    </div>
                                    <span className="font-medium">{source.conversion}% conversion</span>
                                </div>
                            ))}
                        </div>
                    </div>

                    {/* Product Performance */}
                    <div className="bg-white p-6 rounded-lg shadow">
                        <h3 className="text-lg font-semibold mb-4">Product Performance</h3>
                        <div className="space-y-4">
                            {analyticsData.productPerformance.map((product, index) => (
                                <div key={index} className="flex items-center justify-between">
                                    <div className="flex-1">
                                        <div className="flex justify-between mb-1">
                                            <span className="text-sm font-medium">{product.product}</span>
                                            <span className="text-sm text-gray-600">${product.revenue.toLocaleString()}</span>
                                        </div>
                                        <div className="w-full bg-gray-200 rounded-full h-2">
                                            <div
                                                className="bg-blue-600 h-2 rounded-full"
                                                style={{ width: `${(product.revenue / Math.max(...analyticsData.productPerformance.map(p => p.revenue))) * 100}%` }}
                                            ></div>
                                        </div>
                                        <div className="flex justify-between text-xs text-gray-500 mt-1">
                                            <span>{product.sales} sales</span>
                                            <span>{product.conversion}% conversion</span>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>

                {/* Additional Stats */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    {/* Sales by Hour */}
                    <div className="bg-white p-6 rounded-lg shadow">
                        <h3 className="text-lg font-semibold mb-4">Peak Sales Hours</h3>
                        <div className="space-y-3">
                            {analyticsData.salesData.map((hourData, index) => (
                                <div key={index} className="flex items-center justify-between">
                                    <span className="text-sm">{hourData.hour}:00</span>
                                    <div className="flex items-center space-x-2">
                                        <div className="w-32 bg-gray-200 rounded-full h-2">
                                            <div
                                                className="bg-green-500 h-2 rounded-full"
                                                style={{ width: `${(hourData.sales / Math.max(...analyticsData.salesData.map(h => h.sales))) * 100}%` }}
                                            ></div>
                                        </div>
                                        <span className="text-sm font-medium w-8">{hourData.sales}</span>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>

                    {/* Order Stats */}
                    <div className="bg-white p-6 rounded-lg shadow">
                        <h3 className="text-lg font-semibold mb-4">Order Statistics</h3>
                        <div className="space-y-4">
                            <div className="flex justify-between">
                                <span>Completed Orders</span>
                                <span className="font-medium text-green-600">{analyticsData.orderStats.completed}</span>
                            </div>
                            <div className="flex justify-between">
                                <span>Pending Orders</span>
                                <span className="font-medium text-yellow-600">{analyticsData.orderStats.pending}</span>
                            </div>
                            <div className="flex justify-between">
                                <span>Cancelled Orders</span>
                                <span className="font-medium text-red-600">{analyticsData.orderStats.cancelled}</span>
                            </div>
                            <div className="flex justify-between">
                                <span>Average Order Value</span>
                                <span className="font-medium">${analyticsData.orderStats.averageOrderValue}</span>
                            </div>
                            <div className="pt-4 border-t">
                                <div className="flex justify-between text-lg font-semibold">
                                    <span>Total Orders</span>
                                    <span>{analyticsData.orderStats.total}</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Quick Insights */}
                    <div className="bg-white p-6 rounded-lg shadow">
                        <h3 className="text-lg font-semibold mb-4">Quick Insights</h3>
                        <div className="space-y-3">
                            <div className="flex items-start space-x-2 p-3 bg-blue-50 rounded-lg">
                                <span>💡</span>
                                <div>
                                    <p className="font-medium">Best Performing</p>
                                    <p className="text-sm text-gray-600">Accessories have the highest conversion rate (22.1%)</p>
                                </div>
                            </div>
                            <div className="flex items-start space-x-2 p-3 bg-green-50 rounded-lg">
                                <span>🚀</span>
                                <div>
                                    <p className="font-medium">Growth Trend</p>
                                    <p className="text-sm text-gray-600">Revenue increased by {growthRate.toFixed(1)}% this month</p>
                                </div>
                            </div>
                            <div className="flex items-start space-x-2 p-3 bg-yellow-50 rounded-lg">
                                <span>⏰</span>
                                <div>
                                    <p className="font-medium">Peak Hours</p>
                                    <p className="text-sm text-gray-600">3 PM has the highest sales activity</p>
                                </div>
                            </div>
                            <div className="flex items-start space-x-2 p-3 bg-purple-50 rounded-lg">
                                <span>📱</span>
                                <div>
                                    <p className="font-medium">Top Channel</p>
                                    <p className="text-sm text-gray-600">Email marketing has the highest conversion rate (7.8%)</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}