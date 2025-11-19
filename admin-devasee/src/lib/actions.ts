// src/lib/actions.ts
"use server";

export async function addBook(formData: FormData, JWTtoken: string | null) {
    try {
        if (!JWTtoken) {
            throw new Error("Missing authentication token");
        }

        // 1. Construct the book object from the incoming form data
        // Ensure we map the frontend field names (like 'stockQuantity') to the backend expected names (like 'initialQuantity')
        const bookData = {
            title: formData.get("title"),
            author: formData.get("author"),
            publisher: formData.get("publisher"),
            category: formData.get("category"),
            genres: [], // Logic to parse genres if sent from frontend, e.g., formData.getAll("genres")
            description: formData.get("description"),
            language: formData.get("language"),
            price: Number(formData.get("price")),
            initialQuantity: Number(formData.get("stockQuantity")),
            isbn: formData.get("isbn"),
            keywords: []
        };

        // 2. Create the FormData payload expected by the backend
        // The backend expects a multipart/form-data request with:
        // - 'book': A stringified JSON object
        // - 'file': The actual image file
        const body = new FormData();

        body.append("book", JSON.stringify(bookData));

        const file = formData.get("file") as File | null;
        if (file) {
            body.append("file", file);
        }

        // 3. Send the request
        const response = await fetch(
            "http://api.devasee.lk/api/v1/product/books",
            {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${JWTtoken}`,
                    // NOTE: Do NOT set 'Content-Type' manually when using FormData.
                    // The browser/fetch will automatically set it with the correct boundary.
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

// app/actions/auth.ts
export async function authenticateWithAPI(token: string) {
    try {
        const response = await fetch('http://api.devasee.lk/api/v1/users/auth', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error('API authentication failed');
        }

        return { success: true };
    } catch (error) {
        console.error('API authentication error:', error);
        return { success: false, error: 'Failed to authenticate with API' };
    }
}