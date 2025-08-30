// app/dashboard/pages/BooksPage.tsx

import { useMemo } from 'react';
import { Plus, File, ListFilter, MoreHorizontal } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Badge } from '@/components/ui/badge';
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuCheckboxItem, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger } from '@/components/ui/dropdown-menu';
import { recentBooks as allBooks, Book } from '@/lib/dashboard-data';
import clsx from 'clsx';

const statusStyles = {
    available: 'bg-green-100 text-green-800',
    borrowed: 'bg-red-100 text-red-800',
    reserved: 'bg-yellow-100 text-yellow-800',
};

interface BooksPageProps {
    searchQuery: string;
}

export default function BooksPage({ searchQuery }: BooksPageProps) {
    const filteredBooks = useMemo(() => {
        return allBooks.filter(book =>
            book.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
            book.author.toLowerCase().includes(searchQuery.toLowerCase())
        );
    }, [searchQuery]);

    return (
        <main className="grid flex-1 items-start gap-4 p-4 sm:px-6 sm:py-0 md:gap-8 overflow-auto">
            <div className="flex items-center">
                <h1 className="text-2xl font-bold tracking-tight">Books</h1>
                <div className="ml-auto flex items-center gap-2">
                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <Button variant="outline" size="sm" className="h-8 gap-1">
                                <ListFilter className="h-3.5 w-3.5" />
                                <span className="sr-only sm:not-sr-only sm:whitespace-nowrap">Filter</span>
                            </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end">
                            <DropdownMenuLabel>Filter by status</DropdownMenuLabel>
                            <DropdownMenuSeparator />
                            <DropdownMenuCheckboxItem checked>Available</DropdownMenuCheckboxItem>
                            <DropdownMenuCheckboxItem>Borrowed</DropdownMenuCheckboxItem>
                            <DropdownMenuCheckboxItem>Reserved</DropdownMenuCheckboxItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                    <Button size="sm" variant="outline" className="h-8 gap-1">
                        <File className="h-3.5 w-3.5" />
                        <span className="sr-only sm:not-sr-only sm:whitespace-nowrap">Export</span>
                    </Button>
                    <Button size="sm" className="h-8 gap-1">
                        <Plus className="h-3.5 w-3.5" />
                        <span className="sr-only sm:not-sr-only sm:whitespace-nowrap">Add Book</span>
                    </Button>
                </div>
            </div>
            <Card>
                <CardHeader>
                    <CardTitle>Book Collection</CardTitle>
                    <CardDescription>Manage all the books in your library.</CardDescription>
                </CardHeader>
                <CardContent>
                    <Table>
                        <TableHeader>
                            <TableRow>
                                <TableHead>Title</TableHead>
                                <TableHead>Author</TableHead>
                                <TableHead>Status</TableHead>
                                <TableHead>
                                    <span className="sr-only">Actions</span>
                                </TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {filteredBooks.map((book: Book) => (
                                <TableRow key={book.id}>
                                    <TableCell className="font-medium">{book.title}</TableCell>
                                    <TableCell>{book.author}</TableCell>
                                    <TableCell>
                                        <Badge variant="outline" className={clsx('capitalize', statusStyles[book.status])}>
                                            {book.status}
                                        </Badge>
                                    </TableCell>
                                    <TableCell>
                                        <DropdownMenu>
                                            <DropdownMenuTrigger asChild>
                                                <Button aria-haspopup="true" size="icon" variant="ghost">
                                                    <MoreHorizontal className="h-4 w-4" />
                                                    <span className="sr-only">Toggle menu</span>
                                                </Button>
                                            </DropdownMenuTrigger>
                                            <DropdownMenuContent align="end">
                                                <DropdownMenuLabel>Actions</DropdownMenuLabel>
                                                <DropdownMenuItem>Edit</DropdownMenuItem>
                                                <DropdownMenuItem>Delete</DropdownMenuItem>
                                            </DropdownMenuContent>
                                        </DropdownMenu>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </CardContent>
                {/* You would add a CardFooter here for pagination controls */}
            </Card>
        </main>
    );
}