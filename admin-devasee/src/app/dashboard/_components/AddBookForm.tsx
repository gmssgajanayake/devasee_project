"use client";

import { useEffect, useState } from "react";
import { addBook } from "@/lib/actions";
import { useAuth, useUser } from "@clerk/nextjs";

export default function AddBookForm() {
    const [loading, setLoading] = useState(false);
    const [token, setToken] = useState<string | null>(null);
    const [preview, setPreview] = useState<string | null>(null);

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
        if (user) console.log("User Info:", user);
    }, [user]);

    async function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
        e.preventDefault();
        setLoading(true);

        const form = e.currentTarget;
        const formData = new FormData(form);

        try {
            const result = await addBook(formData, token);
            console.log("Book saved:", result);
            alert("Book added successfully!");
            form.reset();
            setPreview(null); // reset preview after save
        } catch (error) {
            alert("Failed to save book.");
        } finally {
            setLoading(false);
        }
    }

    function handleFileChange(e: React.ChangeEvent<HTMLInputElement>) {
        const file = e.target.files?.[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                setPreview(reader.result as string);
            };
            reader.readAsDataURL(file);
        } else {
            setPreview(null);
        }
    }

    return (
        <form
            onSubmit={handleSubmit}
            className="space-y-6 p-6 bg-white rounded-2xl "
        >
            <h2 className="text-xl font-semibold text-gray-800 border-b pb-2">
                📖 Add New Book
            </h2>

            {/* Book Info Section */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <input
                    type="text"
                    name="title"
                    placeholder="Title"
                    required
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                <input
                    type="text"
                    name="author"
                    placeholder="Author"
                    required
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                <input
                    type="text"
                    name="publisher"
                    placeholder="Publisher"
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                <input
                    type="text"
                    name="category"
                    placeholder="Category"
                    required
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
            </div>

            {/* Description */}
            <textarea
                name="description"
                placeholder="Description"
                className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none min-h-[100px]"
            />

            {/* Other Details */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <input
                    type="text"
                    name="language"
                    placeholder="Language"
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                <input
                    type="number"
                    step="0.01"
                    name="price"
                    placeholder="Price"
                    required
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                <input
                    type="number"
                    name="stockQuantity"
                    placeholder="Stock Quantity"
                    required
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
            </div>

            {/* ISBN & File Upload */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <input
                    type="text"
                    name="isbn"
                    placeholder="ISBN"
                    className="w-full h-11 px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />

                <div>
                    <input
                        type="file"
                        name="file"
                        accept="image/*"
                        onChange={handleFileChange}
                        className="w-full px-4 py-2 border border-gray-300 rounded-xl bg-gray-50 text-gray-600 cursor-pointer focus:ring-2 focus:ring-blue-500 focus:outline-none"
                    />

                    {/* Image Preview */}
                    {preview && (
                        <div className="mt-3 flex justify-center">
                            <img
                                src={preview}
                                alt="Preview"
                                className="w-[200px] h-[300px] object-cover rounded-lg border shadow"
                            />
                        </div>
                    )}
                </div>
            </div>

            {/* Submit Button */}
            <button
                type="submit"
                disabled={loading}
                className="w-full bg-blue-600 text-white py-3 rounded-xl shadow hover:bg-blue-700 transition disabled:opacity-50"
            >
                {loading ? "Saving..." : "Save Book"}
            </button>
        </form>
    );
}







/*
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
        // <form onSubmit={handleSubmit} className="space-y-4 p-4 border rounded">
        //     <input type="text" name="title" placeholder="Title" required className="border p-2 w-full" />
        //     <input type="text" name="author" placeholder="Author" required className="border p-2 w-full" />
        //     <input type="text" name="publisher" placeholder="Publisher" className="border p-2 w-full" />
        //     <input type="text" name="category" placeholder="Category" required className="border p-2 w-full" />
        //     <textarea name="description" placeholder="Description" className="border p-2 w-full" />
        //     <input type="text" name="language" placeholder="Language" className="border p-2 w-full" />
        //     <input type="number" step="0.01" name="price" placeholder="Price" required className="border p-2 w-full" />
        //     <input type="number" name="stockQuantity" placeholder="Stock Quantity" required className="border p-2 w-full" />
        //     <input type="text" name="isbn" placeholder="ISBN" className="border p-2 w-full" />
        //     <input type="file" name="file" accept="image/!*" className="border p-2 w-full" />
        //
        //     <button
        //         type="submit"
        //         disabled={loading}
        //         className="bg-blue-600 text-white px-4 py-2 rounded"
        //     >
        //         {loading ? "Saving..." : "Save Book"}
        //     </button>
        // </form>
        <form
            onSubmit={handleSubmit}
            className="space-y-6 p-6 bg-white rounded-2xl shadow-md"
        >
            {/!* Form Header *!/}
            <h2 className="text-xl font-semibold text-gray-800 border-b pb-2">
                📖 Add New Book
            </h2>

            {/!* Book Info Section *!/}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <input
                    type="text"
                    name="title"
                    placeholder="Title"
                    required
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                <input
                    type="text"
                    name="author"
                    placeholder="Author"
                    required
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                <input
                    type="text"
                    name="publisher"
                    placeholder="Publisher"
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                <input
                    type="text"
                    name="category"
                    placeholder="Category"
                    required
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
            </div>

            {/!* Description *!/}
            <textarea
                name="description"
                placeholder="Description"
                className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none min-h-[100px]"
            />

            {/!* Other Details *!/}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <input
                    type="text"
                    name="language"
                    placeholder="Language"
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                <input
                    type="number"
                    step="0.01"
                    name="price"
                    placeholder="Price"
                    required
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                <input
                    type="number"
                    name="stockQuantity"
                    placeholder="Stock Quantity"
                    required
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
            </div>

            {/!* ISBN & File *!/}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <input
                    type="text"
                    name="isbn"
                    placeholder="ISBN"
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
                <input
                    type="file"
                    name="file"
                    accept="image/!*"
                    className="w-full px-4 py-2 border border-gray-300 rounded-xl bg-gray-50 text-gray-600 cursor-pointer focus:ring-2 focus:ring-blue-500 focus:outline-none"
                />
            </div>

            {/!* Submit Button *!/}
            <button
                type="submit"
                disabled={loading}
                className="w-full bg-blue-600 text-white py-3 rounded-xl shadow hover:bg-blue-700 transition disabled:opacity-50"
            >
                {loading ? "Saving..." : "Save Book"}
            </button>
        </form>


    );
}*/
