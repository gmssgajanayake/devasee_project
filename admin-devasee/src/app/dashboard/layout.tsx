'use client';

import { useEffect, useState } from "react";
import { useClerk, UserButton, useUser } from "@clerk/nextjs";
import { useRouter, usePathname } from "next/navigation";
import Link from "next/link";
import Image from "next/image";
import Logo from "@/assets/logo.png";
import {
    LayoutDashboard,
    Users,
    Package,
    Truck,
    Menu,
    X,
    LogOut,
    Bell
} from "lucide-react"; // Using lucide-react for cleaner icons

// --- Data-driven navigation items for better maintainability ---
const NAV_ITEMS = [
    { href: "/dashboard", label: "Analytics", icon: LayoutDashboard },
    { href: "/dashboard/users", label: "Users", icon: Users },
    { href: "/dashboard/inventory", label: "Inventory", icon: Package },
    { href: "/dashboard/delivery", label: "Delivery", icon: Truck },
];

// --- Refactored NavItem with <Link> for better performance and accessibility ---
const NavItem = ({ href, label, icon: Icon, active }: { href: string; label: string; icon: React.ElementType; active: boolean }) => {
    return (
        <Link
            href={href}
            className={`w-full text-left py-2.5 px-4 rounded-lg transition-all duration-200 flex items-center gap-3 ${
                active
                    ? "bg-blue-600 text-white shadow-md"
                    : "text-gray-600 hover:bg-blue-50 hover:text-blue-700"
            }`}
        >
            <Icon className="h-5 w-5" />
            <span className="font-medium">{label}</span>
        </Link>
    );
};

// --- Reusable Sidebar Component ---
const Sidebar = ({ sidebarOpen, setSidebarOpen }: { sidebarOpen: boolean; setSidebarOpen: (open: boolean) => void; }) => {
    const pathname = usePathname();
    const { signOut } = useClerk();
    const router = useRouter();
    const { user } = useUser();

    return (
        <aside
            className={`w-72 h-full bg-white shadow-lg fixed top-0 left-0 z-40 transform transition-transform duration-300 ease-in-out md:relative md:translate-x-0 ${
                sidebarOpen ? 'translate-x-0' : '-translate-x-full'
            }`}
        >
            <div className="flex flex-col h-full">
                {/* Sidebar Header */}
                <div className="flex items-center justify-between p-4 border-b">
                    <div className="flex items-center gap-3">
                        <Image src={Logo} alt="Logo" width={40} height={40} />
                        <h1 className="font-bold text-xl text-gray-800/90">Devasee&nbsp;Admin</h1>
                    </div>
                    <button
                        onClick={() => setSidebarOpen(false)}
                        className="p-2 rounded-lg text-gray-500 hover:bg-gray-100 md:hidden"
                        aria-label="Close menu"
                    >
                        <X className="h-6 w-6" />
                    </button>
                </div>

                {/* Navigation */}
                <nav className="flex-grow p-4 space-y-2 mt-4">
                    {NAV_ITEMS.map((item) => (
                        <NavItem
                            key={item.href}
                            href={item.href}
                            label={item.label}
                            icon={item.icon}
                            active={pathname === item.href}
                        />
                    ))}
                </nav>

                {/* Sidebar Footer with consolidated User Profile */}
                <div className="p-4 border-t">
                    <div className="flex items-center justify-between p-3 bg-blue-50 rounded-lg">
                        <div className="flex items-center gap-3">
                            <UserButton afterSignOutUrl="/sign-in" />
                            <div className="flex flex-col">
                                <span className="text-sm font-semibold text-blue-900 leading-tight">
                                    {user?.fullName || "Admin User"}
                                </span>
                                <span className="text-xs text-gray-500 leading-tight">
                                    {user?.primaryEmailAddress?.emailAddress}
                                </span>
                            </div>
                        </div>
                        <button
                            onClick={() => signOut(() => router.replace("/sign-in"))}
                            className="text-blue-600 hover:text-blue-800 p-2 rounded-md hover:bg-blue-100 transition-colors"
                            aria-label="Log out"
                        >
                            <LogOut className="h-5 w-5" />
                        </button>
                    </div>
                </div>
            </div>
        </aside>
    );
};

// --- Main Header Component ---
const Header = ({ onMenuToggle }: { onMenuToggle: () => void; }) => {
    return (
        <header className="flex items-center justify-between md:justify-end h-18 p-4 bg-white border-b shadow-sm sticky top-0 z-20">
            {/* Mobile Menu Button */}
            <button
                onClick={onMenuToggle}
                className="p-2 rounded-lg text-gray-600 hover:bg-gray-100 md:hidden"
                aria-label="Open menu"
            >
                <Menu className="h-6 w-6" />
            </button>
            {/* Desktop Header Content */}
            <div className="flex items-center gap-4">
                <button className="text-gray-500 hover:text-blue-600 p-2 rounded-full hover:bg-blue-50 transition-colors">
                    <Bell className="h-6 w-6" />
                </button>
                {/* UserButton is now only in the sidebar for a cleaner header */}
            </div>
        </header>
    )
}

export default function DashboardLayout({ children }: { children: React.ReactNode }) {
    const { isSignedIn, isLoaded } = useUser();
    const router = useRouter();
    const [sidebarOpen, setSidebarOpen] = useState(false);

    useEffect(() => {
        if (isLoaded && !isSignedIn) {
            router.replace("/sign-in");
        }
    }, [isLoaded, isSignedIn, router]);

    if (!isLoaded) {
        return (
            <div className="flex items-center justify-center h-screen bg-gray-50">
                <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    if (!isSignedIn) {
        return null; // Return null while redirecting
    }

    return (
        <div className="flex h-screen w-screen bg-gray-50 overflow-hidden">
            {/* Overlay for mobile view */}
            {sidebarOpen && (
                <div
                    onClick={() => setSidebarOpen(false)}
                    className="fixed inset-0 bg-black bg-opacity-40 z-30 md:hidden"
                    aria-hidden="true"
                />
            )}

            <Sidebar sidebarOpen={sidebarOpen} setSidebarOpen={setSidebarOpen} />

            <div className="flex flex-col flex-1 w-full overflow-hidden">
                <Header onMenuToggle={() => setSidebarOpen(true)} />
                <main className="flex-1 overflow-y-auto p-4 md:p-6">
                    {children}
                </main>
            </div>
        </div>
    );
}