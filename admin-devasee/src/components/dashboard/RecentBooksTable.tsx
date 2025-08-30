// components/dashboard/RecentBooksTable.tsx

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { ArrowRight } from 'lucide-react';
import { Book } from '@/lib/dashboard-data';
import clsx from 'clsx';

const statusStyles = {
    available: 'bg-green-100 text-green-800',
    borrowed: 'bg-red-100 text-red-800',
    reserved: 'bg-yellow-100 text-yellow-800',
};

export function RecentBooksTable({ books }: { books: Book[] }) {
    return (
        <Card className="xl:col-span-2">
            <CardHeader className="flex flex-row items-center">
                <div className="grid gap-2">
                    <CardTitle>Recent Books</CardTitle>
                    <CardDescription>Recently added books to the collection.</CardDescription>
                </div>
                <Button asChild size="sm" className="ml-auto gap-1">
                    <a href="/books">
                        View All
                        <ArrowRight className="h-4 w-4" />
                    </a>
                </Button>
            </CardHeader>
            <CardContent>
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>Title</TableHead>
                            <TableHead>Author</TableHead>
                            <TableHead>Status</TableHead>
                            <TableHead className="text-right">Added</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {books.map((book) => (
                            <TableRow key={book.id}>
                                <TableCell className="font-medium">{book.title}</TableCell>
                                <TableCell>{book.author}</TableCell>
                                <TableCell>
                                    <Badge variant="outline" className={clsx('capitalize', statusStyles[book.status])}>
                                        {book.status}
                                    </Badge>
                                </TableCell>
                                <TableCell className="text-right">{book.added}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </CardContent>
        </Card>
    );
}