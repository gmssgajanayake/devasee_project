"use server";

import { Book } from "@/types/types";

// Describe the shape of the API response
interface ApiBook {
    id: string;
    title: string;
    author: string;
    publisher: string;
    category: string;
    description: string;
    language: string;
    price: number;
    stockQuantity: number;
    isbn: number;
    imgUrl: string;
}

interface ApiResponse {
    success: boolean;
    message: string;
    data: {
        content: ApiBook[];
        totalPages: number;
        totalElements: number;
        number: number;
        size: number;
    };
}

export async function getAllBooks(): Promise<Book[]> {
    try {
        const apiUrl = `http://api.devasee.lk/api/v1/product/book/public/books?page=0&size=20`;

        console.log(apiUrl)
        const res = await fetch(apiUrl, {
            cache: "no-store",
        });

        if (!res.ok) {
            throw new Error("Failed to fetch books");
        }

        const json: ApiResponse = await res.json();


        const books: Book[] = json.data.content.map((b) => ({
            id: b.id,
            title: b.title,
            author: b.author,
            price: b.price,
            stock: b.stockQuantity,
            image: b.imgUrl,
            type: b.category || "Books",
            brand: b.publisher || "Unknown",
            quantity:b.stockQuantity,
            description: b.description,
            language: b.language,
            publisher: b.publisher,
            isbn: String(b.isbn), // Convert number to string
        }));

        return books;
    } catch (err) {
        console.error("Error fetching books:", err);
        return [];
    }
}

