// src/lib/actions.ts
"use server";

export async function addBook(formData: FormData, JWTtoken: string | null) {
    try {
        //const token = JWTtoken;
        const token = JWTtoken;
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
                initialQuantity: 40,
                isbn: 12038481112
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
