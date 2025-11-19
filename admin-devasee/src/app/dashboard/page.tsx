'use client'

import { useState, useEffect } from 'react';

// --- Types ---
interface RevenueData {
    month: string;
    revenue: number;
}

interface BookData {
    title: string;
    author: string;
    category: string;
    revenue: number;
    sales: number;
}

// --- Mock Data (Scaled Down for Small Shop) ---
const data = {
    revenue: [
        { month: 'Jan', revenue: 4500 },
        { month: 'Feb', revenue: 5200 },
        { month: 'Mar', revenue: 4800 },
        { month: 'Apr', revenue: 8500 },
        { month: 'May', revenue: 6200 },
        { month: 'Jun', revenue: 7500 },
    ],
    topBooks: [
        { title: 'The Seven Moons of Maali Almeida', author: 'Shehan Karunatilaka', category: 'Fiction', revenue: 28500, sales: 15 },
        { title: 'Atomic Habits', author: 'James Clear', category: 'Self-Help', revenue: 22000, sales: 12 },
        { title: 'Madol Doowa', author: 'Martin Wickramasinghe', category: 'Classic', revenue: 8500, sales: 18 },
        { title: 'Harry Potter', author: 'J.K. Rowling', category: 'Fantasy', revenue: 15000, sales: 5 },
    ],
    stats: {
        totalRevenue: 36700,
        booksSold: 142,
        avgOrderValue: 258,
    }
};

// --- Animation Components ---

// 1. Number Count Up Component
const CountUp = ({ end, prefix = '', duration = 1500 }: { end: number, prefix?: string, duration?: number }) => {
    const [count, setCount] = useState(0);

    useEffect(() => {
        let startTime: number;
        let animationFrame: number;

        const animate = (timestamp: number) => {
            if (!startTime) startTime = timestamp;
            const progress = timestamp - startTime;

            if (progress < duration) {
                // Ease-out cubic formula for smooth slowing down
                const nextCount = Math.min(end, Math.ceil((1 - Math.pow(1 - progress / duration, 3)) * end));
                setCount(nextCount);
                animationFrame = requestAnimationFrame(animate);
            } else {
                setCount(end);
            }
        };

        animationFrame = requestAnimationFrame(animate);
        return () => cancelAnimationFrame(animationFrame);
    }, [end, duration]);

    // Use Intl for formatting inside the component to ensure consistency
    const formatted = new Intl.NumberFormat('en-LK', {
        maximumFractionDigits: 0
    }).format(count);

    return <span>{prefix}{formatted}</span>;
};

export default function BookShopAnalytics() {
    const [timeRange, setTimeRange] = useState('6M');
    const [isLoaded, setIsLoaded] = useState(false);

    // Trigger animations on mount
    useEffect(() => {
        setIsLoaded(true);
    }, []);

    // Helper: Format LKR Currency (Static)
    const formatLKR = (amount: number) => {
        return new Intl.NumberFormat('en-LK', {
            style: 'currency',
            currency: 'LKR',
            maximumFractionDigits: 0
        }).format(amount);
    };

    const maxRevenue = Math.max(...data.revenue.map(d => d.revenue));

    return (
        <div className="min-h-screen bg-slate-50 p-4 sm:p-8 font-sans text-slate-800">
            {/* Custom Styles for simple keyframe animations */}
            <style jsx global>{`
                @keyframes slideUpFade {
                    from { opacity: 0; transform: translateY(20px); }
                    to { opacity: 1; transform: translateY(0); }
                }
                .animate-entry {
                    animation: slideUpFade 0.6s cubic-bezier(0.16, 1, 0.3, 1) forwards;
                    opacity: 0; /* Start hidden */
                }
            `}</style>

            <div className="max-w-6xl mx-auto">

                {/* Header */}
                <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-8 gap-4 animate-entry" style={{ animationDelay: '0ms' }}>
                    <div>
                        <h1 className="text-2xl font-bold text-slate-900">📚 Bookstore Dashboard</h1>
                        <p className="text-slate-500 text-sm">Small Business Overview</p>
                    </div>
                    <select
                        value={timeRange}
                        onChange={(e) => setTimeRange(e.target.value)}
                        className="bg-white border border-slate-300 text-sm rounded-lg px-4 py-2 outline-none focus:ring-2 focus:ring-indigo-500 transition-shadow hover:shadow-sm"
                    >
                        <option value="7D">Last 7 Days</option>
                        <option value="30D">Last 30 Days</option>
                        <option value="6M">Last 6 Months</option>
                    </select>
                </div>

                {/* Key Metrics */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                    {/* Metric Card 1 */}
                    <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-100 animate-entry" style={{ animationDelay: '100ms' }}>
                        <p className="text-sm font-medium text-slate-500">Total Revenue</p>
                        <div className="flex items-baseline gap-2 mt-2">
                            <h3 className="text-2xl font-bold text-slate-900">
                                <CountUp end={data.stats.totalRevenue} prefix="LKR " />
                            </h3>
                            <span className="text-xs font-medium text-emerald-600 bg-emerald-100 px-2 py-0.5 rounded-full">+5%</span>
                        </div>
                    </div>

                    {/* Metric Card 2 */}
                    <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-100 animate-entry" style={{ animationDelay: '200ms' }}>
                        <p className="text-sm font-medium text-slate-500">Books Sold</p>
                        <div className="flex items-baseline gap-2 mt-2">
                            <h3 className="text-2xl font-bold text-slate-900">
                                <CountUp end={data.stats.booksSold} />
                            </h3>
                            <span className="text-xs font-medium text-indigo-600 bg-indigo-100 px-2 py-0.5 rounded-full">Active</span>
                        </div>
                    </div>

                    {/* Metric Card 3 */}
                    <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-100 animate-entry" style={{ animationDelay: '300ms' }}>
                        <p className="text-sm font-medium text-slate-500">Avg. Order Value</p>
                        <h3 className="text-2xl font-bold mt-2 text-slate-900">
                            <CountUp end={data.stats.avgOrderValue} prefix="LKR " />
                        </h3>
                    </div>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">

                    {/* REVENUE TREND CHART */}
                    <div className="lg:col-span-2 bg-white p-6 rounded-xl shadow-sm border border-slate-100 flex flex-col animate-entry" style={{ animationDelay: '400ms' }}>
                        <div className="flex justify-between items-center mb-2">
                            <h3 className="font-bold text-lg text-slate-800">Revenue Trend</h3>
                        </div>

                        {/* Chart Container */}
                        <div className="flex-1 flex items-end justify-between gap-3 sm:gap-6 min-h-[280px] w-full pt-8 pb-2">
                            {data.revenue.map((item, index) => {
                                const heightPercentage = (item.revenue / maxRevenue) * 100;
                                return (
                                    <div key={index} className="group relative flex flex-col items-center justify-end w-full h-full cursor-default">
                                        {/* Tooltip */}
                                        <div className="absolute bottom-full mb-2 opacity-0 group-hover:opacity-100 transition-all duration-200 transform translate-y-2 group-hover:translate-y-0 bg-slate-800 text-white text-xs py-1 px-2 rounded pointer-events-none z-10 whitespace-nowrap shadow-lg">
                                            {formatLKR(item.revenue)}
                                        </div>

                                        {/* Bar Wrapper */}
                                        <div className="relative w-full flex-1 flex items-end justify-center">
                                            <div
                                                className="w-full max-w-[40px] bg-indigo-500 rounded-t-sm group-hover:bg-indigo-600 relative shadow-sm"
                                                // Animation Logic:
                                                // 1. Use transition-all for smooth hover effects
                                                // 2. Use duration-1000 ease-out for the initial growth
                                                // 3. Use isLoaded state to toggle between height 0 and height X%
                                                style={{
                                                    height: isLoaded ? `${heightPercentage}%` : '0%',
                                                    transition: 'height 1s cubic-bezier(0.16, 1, 0.3, 1), background-color 0.2s'
                                                }}
                                            >
                                            </div>
                                        </div>

                                        {/* X-Axis Label */}
                                        <span className="text-xs text-slate-500 mt-3 font-medium">{item.month}</span>
                                    </div>
                                );
                            })}
                        </div>
                    </div>

                    {/* TRENDING BOOKS LIST */}
                    <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-100 animate-entry" style={{ animationDelay: '500ms' }}>
                        <h3 className="font-bold text-lg mb-6 text-slate-800">Top Sellers</h3>
                        <div className="space-y-6">
                            {data.topBooks.map((book, index) => (
                                <div
                                    key={index}
                                    className="flex items-start justify-between group opacity-0 animate-entry"
                                    // Stagger the list items individually
                                    style={{ animationDelay: `${600 + (index * 100)}ms`, animationFillMode: 'forwards' }}
                                >
                                    <div className="flex-1 pr-4 transition-transform duration-200 group-hover:translate-x-1">
                                        <div className="flex items-center gap-2 mb-1">
                                            <p className="text-sm font-semibold text-slate-900 line-clamp-1" title={book.title}>
                                                {book.title}
                                            </p>
                                        </div>
                                        <p className="text-xs text-slate-500 mb-1">by {book.author}</p>
                                        <span className="inline-block bg-slate-100 text-slate-600 text-[10px] px-2 py-0.5 rounded uppercase font-semibold tracking-wide group-hover:bg-indigo-50 group-hover:text-indigo-600 transition-colors">
                                            {book.category}
                                        </span>
                                    </div>
                                    <div className="text-right flex flex-col items-end">
                                        <p className="text-sm font-bold text-slate-900">{formatLKR(book.revenue)}</p>
                                        <p className="text-xs text-slate-500">{book.sales} copies</p>
                                    </div>
                                </div>
                            ))}
                        </div>

                        <button className="w-full mt-8 py-2.5 text-sm text-indigo-600 font-medium border border-indigo-100 bg-indigo-50 hover:bg-indigo-100 rounded-lg transition-all hover:scale-[1.02] active:scale-[0.98]">
                            View Inventory
                        </button>
                    </div>

                </div>
            </div>
        </div>
    );
}