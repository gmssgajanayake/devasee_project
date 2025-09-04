// app/dashboard/layout.tsx
'use client';

import { useState } from 'react';
import { navItems } from '@/lib/dashboard-data';
import { Sidebar } from '@/components/dashboard/Sidebar';
import { Header } from '@/components/dashboard/Header';
import DashboardPage from './pages/DashboardPage';
import BooksPage from './pages/BooksPage';
import SettingsPage from './pages/SettingsPage';
import { UserButton, useUser } from "@clerk/nextjs";
import {redirect} from "next/navigation";

export default function DashboardLayout() {
    const [activeTab, setActiveTab] = useState('dashboard');
    const [sidebarOpen, setSidebarOpen] = useState(false);
    const [searchQuery, setSearchQuery] = useState('');

    const { isSignedIn, user } = useUser();

    if (!isSignedIn) {
        redirect("/sign-in");
    }


    const renderActivePage = () => {
        switch (activeTab) {
            case 'books':
                return <BooksPage searchQuery={searchQuery} />;
            case 'settings':
                return <SettingsPage />;
            case 'dashboard':
            default:
                return <DashboardPage searchQuery={searchQuery} />;
        }
    };

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

                <div className="p-2">
                    <UserButton afterSignOutUrl="/sign-in" />
                    <p className="mt-2 text-sm text-gray-600">
                        Logged in as {user?.emailAddresses[0]?.emailAddress}
                    </p>
                </div>

                {renderActivePage()}
            </div>
        </div>
    );
}
