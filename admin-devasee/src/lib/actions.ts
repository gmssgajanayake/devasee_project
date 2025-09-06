// src/lib/actions.ts
"use server";

import { auth } from "@clerk/nextjs/server";

export async function getToken() {
    const { getToken } = await auth();
    const token = await getToken({ template: "devasee_user_token" });
    return token;
}

export async function addBook(formData: FormData) {
    try {
        const token = getToken();

        if (!token) {
            throw new Error("Missing authentication token");
        }

        // Build FormData properly
        const body = new FormData();
        body.append(
            "book",
            JSON.stringify({
                title: "Introduction to Algorithms II",
                author:
                    "Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, Clifford Stein",
                publisher: "MIT Press",
                category: "Computer Science",
                description:
                    "Comprehensive textbook covering a broad range of algorithms in depth, widely used in universities.",
                language: "English",
                price: 65.0,
                stockQuantity: 40,
                isbn: 9729033848,
                imgUrl: "",
            })
        );

        const file = formData.get("file") as File | null;
        if (file) {
            body.append("file", file);
        }

        const response = await fetch(
            "http://api.devasee.lk/api/v1/product/book/admin/addBook",
            {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${token}`,
                    // ⚠️ Don't set Content-Type manually; fetch will set correct boundary
                },
                body,
            }
        );

        if (!response.ok) {
            const errorBody = await response.text();
            console.error("API Error Response:", errorBody);
            throw new Error(
                `Failed to add book: ${response.status} ${response.statusText}`
            );
        }

        return await response.json();
    } catch (error) {
        console.error("Error adding book:", error);
        throw error;
    }
}
