"use client";

import {useEffect, useState} from "react";
import { addBook } from "@/lib/actions";
import {useAuth, useUser} from "@clerk/nextjs";

export default function AddBookForm() {
    const [loading, setLoading] = useState(false);
    const [token, setToken] = useState<string | null>(null);

    const { user } = useUser();
    const { getToken } = useAuth();


    async function getCustomJwt() {
        try {
            const jwt = await getToken({ template: "devasee_user_token" });
            setToken(jwt);
            console.log("JWT with user details:", jwt);
            return jwt;
        } catch (err) {
            console.error("Error getting JWT:", err);
            return null;
        }
    }

    useEffect(() => {
        getCustomJwt();
        console.log(token)
        if (user) console.log("User Info:", user);
    }, [user]);

    async function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
        e.preventDefault();
        setLoading(true);

        const form = e.currentTarget;
        const formData = new FormData(form); // Correct way to create FormData

        try {
            const result = await addBook(formData,token);
            console.log("Book saved:", result);
            alert("Book added successfully!");
            form.reset();
        } catch (error) {
            alert("Failed to save book.");
        } finally {
            setLoading(false);
        }
    }

    return (
        <form onSubmit={handleSubmit} className="space-y-4 p-4 border rounded">
            <input type="text" name="title" placeholder="Title" required className="border p-2 w-full" />
            <input type="text" name="author" placeholder="Author" required className="border p-2 w-full" />
            <input type="text" name="publisher" placeholder="Publisher" className="border p-2 w-full" />
            <input type="text" name="category" placeholder="Category" required className="border p-2 w-full" />
            <textarea name="description" placeholder="Description" className="border p-2 w-full" />
            <input type="text" name="language" placeholder="Language" className="border p-2 w-full" />
            <input type="number" step="0.01" name="price" placeholder="Price" required className="border p-2 w-full" />
            <input type="number" name="stockQuantity" placeholder="Stock Quantity" required className="border p-2 w-full" />
            <input type="text" name="isbn" placeholder="ISBN" className="border p-2 w-full" />
            <input type="file" name="file" accept="image/*" className="border p-2 w-full" />

            <button
                type="submit"
                disabled={loading}
                className="bg-blue-600 text-white px-4 py-2 rounded"
            >
                {loading ? "Saving..." : "Save Book"}
            </button>
        </form>
    );
}