// components/dashboard/Sidebar.tsx

'use client';

import { BookOpen, LogOut, User, X } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { NavItem } from '@/lib/dashboard-data';
import clsx from 'clsx';

interface SidebarProps {
    isOpen: boolean;
    onClose: () => void;
    navItems: NavItem[];
    activeTab: string;
    setActiveTab: (tab: string) => void;
}

export function Sidebar({ isOpen, onClose, navItems, activeTab, setActiveTab }: SidebarProps) {
    return (
        <aside
            className={clsx(
                'inset-y-0 left-0 z-10 bg-background shadow-md transition-all duration-300 ease-in-out fixed md:relative overflow-hidden',
                isOpen ? 'w-64' : 'w-0 md:w-20'
            )}
        >
            <div className="flex h-full flex-col">
                <div className="flex items-center justify-between p-4 border-b">
                    <div className={clsx('flex items-center gap-2 transition-opacity', isOpen ? 'opacity-100' : 'opacity-0 md:opacity-100')}>
                        <BookOpen className="h-8 w-8 text-primary" />
                        <span className="text-xl font-semibold">Devasee</span>
                    </div>
                    <Button variant="ghost" size="icon" className="md:hidden" onClick={onClose}>
                        <X className="h-5 w-5" />
                    </Button>
                </div>

                <nav className="flex-1 space-y-2 p-4">
                    {navItems.map((item) => (
                        <Button
                            key={item.name}
                            variant={activeTab === item.slug ? 'default' : 'ghost'}
                            className={clsx('w-full justify-start', isOpen ? 'px-4' : 'px-3')}
                            onClick={() => setActiveTab(item.slug)}
                        >
                            <item.icon className="h-5 w-5" />
                            {isOpen && <span className="ml-3">{item.name}</span>}
                        </Button>
                    ))}
                </nav>

                <div className="border-t p-4">
                    <div className={clsx('flex items-center gap-3 transition-opacity', isOpen ? 'opacity-100' : 'opacity-0 md:opacity-100')}>
                        <div className="h-10 w-10 rounded-full bg-primary flex items-center justify-center text-primary-foreground">
                            <User className="h-5 w-5" />
                        </div>
                        {isOpen && (
                            <div>
                                <p className="font-medium">Admin User</p>
                                <p className="text-sm text-muted-foreground">admin@devasee.com</p>
                            </div>
                        )}
                    </div>
                    <Button variant="ghost" className={clsx('w-full justify-start mt-4', isOpen ? 'px-4' : 'px-3')}>
                        <LogOut className="h-5 w-5" />
                        {isOpen && <span className="ml-3">Logout</span>}
                    </Button>
                </div>
            </div>
        </aside>
    );
}