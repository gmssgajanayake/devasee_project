// app/dashboard/layout.tsx

'use client';

import { useState } from 'react';
import { navItems } from '@/lib/dashboard-data';
import { Sidebar } from '@/components/dashboard/Sidebar';
import { Header } from '@/components/dashboard/Header';

// Import the new pages we will create
import DashboardPage from './pages/DashboardPage';
import BooksPage from './pages/BooksPage';
import SettingsPage from './pages/SettingsPage';
import {UserButton} from "@clerk/nextjs";

export default function DashboardLayout() {
    const [activeTab, setActiveTab] = useState('dashboard');
    const [sidebarOpen, setSidebarOpen] = useState(false);
    const [searchQuery, setSearchQuery] = useState('');

    // A simple router to render the correct page based on the active tab
    const renderActivePage = () => {
        switch (activeTab) {
            case 'books':
                return <BooksPage searchQuery={searchQuery} />;
            case 'settings':
                return <SettingsPage />;
            // Add cases for 'members' and 'reports' here
            case 'dashboard':
            default:
                return <DashboardPage searchQuery={searchQuery} />;
        }
    };

    return (
        <div className="flex  h-screen w-full bg-muted/40">
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

                <UserButton
                    afterSignOutUrl="/sign-in"
                />

                {/* The main content area now renders the active page */}
                {renderActivePage()}
            </div>
        </div>
    );
}