// app/dashboard/pages/DashboardPage.tsx

import { useMemo } from 'react';
import { Plus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { recentBooks as allBooks, stats } from '@/lib/dashboard-data';
import { DashboardStats } from '@/components/dashboard/DashboardStats';
import { RecentBooksTable } from '@/components/dashboard/RecentBooksTable';
// Assume RecentActivityFeed is another component you've created
// import { RecentActivityFeed } from '@/components/dashboard/RecentActivityFeed';

interface DashboardPageProps {
    searchQuery: string;
}

export default function DashboardPage({ searchQuery }: DashboardPageProps) {
    const filteredBooks = useMemo(() => {
        if (!searchQuery) return allBooks.slice(0, 5); // Show first 5 on dashboard
        return allBooks.filter(book =>
            book.title.toLowerCase().includes(searchQuery.toLowerCase())
        );
    }, [searchQuery]);

    return (
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
                {/* <RecentActivityFeed /> */}
            </div>
        </main>
    );
}