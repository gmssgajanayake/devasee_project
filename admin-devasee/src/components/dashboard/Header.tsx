// components/dashboard/Header.tsx

'use client';

import { Search, Menu } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';

interface HeaderProps {
    onMenuClick: () => void;
    searchQuery: string;
    setSearchQuery: (query: string) => void;
}

export function Header({ onMenuClick, searchQuery, setSearchQuery }: HeaderProps) {
    return (
        <header className="sticky top-0 z-30 flex h-14 items-center gap-4 border-b bg-background px-4 sm:px-6">
            <Button variant="ghost" size="icon" className="md:hidden" onClick={onMenuClick}>
                <Menu className="h-5 w-5" />
            </Button>
            <div className="relative ml-auto flex-1 md:grow-0">
                <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
                <Input
                    type="search"
                    placeholder="Search books..."
                    className="w-full rounded-lg bg-background pl-8 md:w-[200px] lg:w-[336px]"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                />
            </div>
        </header>
    );
}