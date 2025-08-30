// lib/dashboard-data.ts

import { BookOpen, Users, BarChart3, Calendar, Home, BookMarked, User, Settings } from 'lucide-react';

// Define types for our data
export interface StatCard {
    title: string;
    value: string;
    icon: React.ElementType;
    change: string;
}

export interface Book {
    id: number;
    title: string;
    author: string;
    status: 'available' | 'borrowed' | 'reserved';
    added: string;
}

export interface Activity {
    id: number;
    user: string;
    action: string;
    book: string;
    time: string;
}

export interface NavItem {
    name: string;
    icon: React.ElementType;
    slug: string;
}

// Export mock data
export const stats: StatCard[] = [
    { title: 'Total Books', value: '1,248', icon: BookOpen, change: '+12% from last month' },
    { title: 'Active Readers', value: '573', icon: Users, change: '+8% from last month' },
    { title: 'Books Checked Out', value: '327', icon: BarChart3, change: '-3% from last month' },
    { title: 'Upcoming Returns', value: '42', icon: Calendar, change: '+5% from last month' },
];

export const recentBooks: Book[] = [
    { id: 1, title: 'The Midnight Library', author: 'Matt Haig', status: 'available', added: '2 days ago' },
    { id: 2, title: 'Project Hail Mary', author: 'Andy Weir', status: 'borrowed', added: '5 days ago' },
    { id: 3, title: 'Atomic Habits', author: 'James Clear', status: 'available', added: '1 week ago' },
    { id: 4, title: 'The Three-Body Problem', author: 'Liu Cixin', status: 'reserved', added: '1 week ago' },
    { id: 5, title: 'Dune', author: 'Frank Herbert', status: 'available', added: '2 weeks ago' },
];

export const recentActivity: Activity[] = [
    { id: 1, user: 'Sarah Johnson', action: 'checked out', book: 'The Silent Patient', time: '10 minutes ago' },
    { id: 2, user: 'Michael Chen', action: 'returned', book: 'Educated', time: '2 hours ago' },
    { id: 3, user: 'Emma Wright', action: 'reserved', book: 'Klara and the Sun', time: '5 hours ago' },
    { id: 4, user: 'David Miller', action: 'added to wishlist', book: 'The Thursday Murder Club', time: 'Yesterday' },
];

export const navItems: NavItem[] = [
    { name: 'Dashboard', icon: Home, slug: 'dashboard' },
    { name: 'Books', icon: BookMarked, slug: 'books' },
    { name: 'Members', icon: User, slug: 'members' },
    { name: 'Reports', icon: BarChart3, slug: 'reports' },
    { name: 'Settings', icon: Settings, slug: 'settings' },
];