'use client';

import AddBookForm from "@/app/(router)/books/AddBookForm";

export default function BookDashboard() {
    return (
        <div className="flex justify-center items-center h-screen w-full bg-muted/40">
           <AddBookForm/>
        </div>
    );
}