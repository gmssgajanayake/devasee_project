"use client";

import { useState, useEffect } from "react";
import ItemCard from "@/components/ItemCard";
import { StaticImageData } from "next/image";
import SortOptions from "@/app/(router)/products/_components/SortOptions";

interface Book {
    image: StaticImageData;
    title: string;
    author: string;
    price: number;
    type?: string;
    brand?: string;
}

interface ContainerProps {
    books: Book[];
    sortBy: "title" | "author" | "price";
    setSortBy: (value: "title" | "author" | "price") => void;
}

const ITEMS_PER_PAGE = 20;

export default function Container({ books, sortBy, setSortBy }: ContainerProps) {
    const [currentPage, setCurrentPage] = useState(1);
    const [sortedBooks, setSortedBooks] = useState<Book[]>([]);

    useEffect(() => {
        const sorted = [...books];
        if (sortBy === "title") {
            sorted.sort((a, b) => a.title.localeCompare(b.title));
        } else if (sortBy === "author") {
            sorted.sort((a, b) => a.author.localeCompare(b.author));
        } else if (sortBy === "price") {
            sorted.sort((a, b) => a.price - b.price);
        }
        setSortedBooks(sorted);
        setCurrentPage(1);
    }, [books, sortBy]);

    const totalPages = Math.ceil(sortedBooks.length / ITEMS_PER_PAGE);
    const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
    const currentBooks = sortedBooks.slice(startIndex, startIndex + ITEMS_PER_PAGE);

    const goToPage = (page: number) => {
        if (page >= 1 && page <= totalPages) {
            setCurrentPage(page);
            window.scrollTo({ top: 0, behavior: "smooth" });
        }
    };

    return (
        <div className="w-full py-10 px-6 sm:px-12">
            <SortOptions sortBy={sortBy} setSortBy={setSortBy} />

            <div className="grid gap-3 grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
                {currentBooks.map((book, index) => (
                    <div key={index} className="w-full">
                        <ItemCard
                            isHovered={false}
                            imageUrl={book.image.src}
                            title={book.title}
                            author={book.author}
                            price={book.price}
                        />
                    </div>
                ))}
            </div>

            <div className="flex justify-center items-center gap-3 flex-wrap mt-10">
                <button
                    onClick={() => goToPage(currentPage - 1)}
                    disabled={currentPage === 1}
                    className="w-10 h-10 flex justify-center items-center bg-[#0000ff] text-white rounded-full disabled:bg-[#c7defe]"
                >
                    &#8592;
                </button>

                {[...Array(totalPages)].map((_, i) => (
                    <button
                        key={i}
                        onClick={() => goToPage(i + 1)}
                        className={`w-10 h-10 text-sm font-medium flex items-center justify-center ${
                            currentPage === i + 1
                                ? "bg-[#0000ff] text-white"
                                : "bg-white border border-gray-300 text-gray-700"
                        } rounded-full transition-all duration-200`}
                    >
                        {i + 1}
                    </button>
                ))}

                <button
                    onClick={() => goToPage(currentPage + 1)}
                    disabled={currentPage === totalPages}
                    className="w-10 h-10 flex justify-center items-center bg-[#0000ff] text-white rounded-full disabled:bg-[#c7defe]"
                >
                    &#8594;
                </button>
            </div>
        </div>
    );
}