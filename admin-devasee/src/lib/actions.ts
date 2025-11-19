// src/lib/actions.ts
"use server";

"use server";

export async function addBook(formData: FormData, JWTtoken: string | null) {
    try {
        // ⚠️ Security Note: Do not hardcode tokens in production code.
        // Ensure JWTtoken is passed correctly from the component or session.
        const token = JWTtoken || "YOUR_FALLBACK_TOKEN_IF_NEEDED";

        if (!token) {
            throw new Error("Missing authentication token");
        }

        const body = new FormData();

        // 1. Append 'book' JSON string
        body.append(
            "book",
            JSON.stringify({
                title: formData.get("title"),
                author: formData.get("author"),
                publisher: formData.get("publisher"),
                category: formData.get("category"),
                genres: [],
                description: formData.get("description"),
                language: formData.get("language"),
                price: Number(formData.get("price")),
                initialQuantity: Number(formData.get("stockQuantity")),
                isbn: formData.get("isbn"),
                keywords: [],
            })
        );

        // 2. Append the main 'file' (cover image)
        const file = formData.get("file") as File | null;
        if (file) {
            body.append("file", file);
        }

        // 3. FIX: Append 'otherFiles' as a File (Blob), not a String
        // We create an empty Blob to satisfy the server's expectation of a file part.
        // The third argument ("empty.txt") ensures a filename is sent in the headers.
        const emptyBlob = new Blob([], { type: "application/octet-stream" });
        body.append("otherFiles", emptyBlob, "empty.bin");

        const response = await fetch("http://api.devasee.lk/api/v1/product/books", {
            method: "POST",
            headers: {
                Authorization: `Bearer ${token}`,
                // Fetch automatically sets the boundary for Multipart forms.
                // DO NOT set Content-Type manually.
            },
            body,
        });

        if (!response.ok) {
            const errorBody = await response.text();
            console.error("API Error Response:", errorBody);
            throw new Error(
                `Failed to add book: ${response.status} ${response.statusText} - ${errorBody}`
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


        // Log the status code and response body for better debugging
        if (!response.ok) {
            const errorBody = await response.text();
            console.error('API Auth Failed - Status:', response.status);
            console.error('API Auth Failed - Response Body:', errorBody);
            throw new Error('API authentication failed');
        }

        return { success: true };
    } catch (error) {
        console.error('API authentication error:', error);
        return { success: false, error: 'Failed to authenticate with API' };
    }
}