// app/dashboard/page.tsx

'use client';

import { useState, useMemo } from 'react';
import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import {
    recentBooks as allBooks,
    stats,
    recentActivity,
    navItems,
} from '@/lib/dashboard-data';
import { Sidebar } from '@/components/dashboard/Sidebar';
import { Header } from '@/components/dashboard/Header';
import { DashboardStats } from '@/components/dashboard/DashboardStats';
import { RecentBooksTable } from '@/components/dashboard/RecentBooksTable';
// You would also create a RecentActivityFeed component similarly

export default function BookDashboard() {
    const [activeTab, setActiveTab] = useState('dashboard');
    const [sidebarOpen, setSidebarOpen] = useState(false);
    const [searchQuery, setSearchQuery] = useState('');

    const filteredBooks = useMemo(() => {
        if (!searchQuery) return allBooks.slice(0, 5); // Show first 5 by default
        return allBooks.filter(book =>
            book.title.toLowerCase().includes(searchQuery.toLowerCase())
        );
    }, [searchQuery]);

    return (
        <div className="flex h-screen w-full bg-muted/40">
            <Sidebar
                isOpen={sidebarOpen}
                onClose={() => setSidebarOpen(false)}
                navItems={navItems}
                activeTab={activeTab}
                setActiveTab={setActiveTab}
            />

            <div className="flex-1 flex flex-col overflow-hidden">
                <Header
                    onMenuClick={() => setSidebarOpen(true)}
                    searchQuery={searchQuery}
                    setSearchQuery={setSearchQuery}
                />

                <main className="grid flex-1 items-start gap-4 p-4 sm:px-6 sm:py-0 md:gap-8 overflow-auto">
                    <div className="flex items-center">
                        <h1 className="text-2xl font-bold tracking-tight">Dashboard</h1>
                        <div className="ml-auto">
                            <Button size="sm" className="h-8 gap-1">
                                <Plus className="h-3.5 w-3.5" />
                                <span className="sm:whitespace-nowrap">Add New Book</span>
                            </Button>
                        </div>
                    </div>

                    <DashboardStats stats={stats} />

                    <div className="grid gap-4 md:gap-8 lg:grid-cols-2 xl:grid-cols-3">
                        <RecentBooksTable books={filteredBooks} />

                        {/* The RecentActivity card would also be its own component */}
                        {/* <RecentActivityFeed activities={recentActivity} /> */}
                    </div>
                </main>
            </div>
        </div>
    );
}