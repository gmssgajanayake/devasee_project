"use client";

import { createContext, useContext, useEffect, useState } from "react";
import { StaticImageData } from "next/image";

type Book = {
    id: string; // Unique identifier
    image: StaticImageData;
    title: string;
    author: string;
    price: number;
    type: string;
    brand: string;
    stock: number;
    quantity: number;  // Add quantity here
    description?: string;
    rating?: number;
    publicationDate?: string;
    isbn?: string;
    language?: string;
    pages?: number;
    publisher?: string;
    dimensions?: string;
    weight?: string;
};

interface CartContextType {
    cartItems: Book[];
    addToCart: (book: Book) => void;
    removeFromCart: (id: string) => void;
    updateItemQuantity: (id: string, quantity: number) => void;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider = ({ children }: { children: React.ReactNode }) => {
    const [cartItems, setCartItems] = useState<Book[]>([]);

    // Load from localStorage once on mount
    useEffect(() => {
        const storedCart = localStorage.getItem("devasee-cart");
        if (storedCart) {
            try {
                const parsedCart = JSON.parse(storedCart) as Book[];
                setCartItems(parsedCart);
            } catch (error) {
                console.error("Failed to parse stored cart:", error);
                localStorage.removeItem("devasee-cart");
            }
        }
    }, []);

    // Save to localStorage whenever cartItems changes
    useEffect(() => {
        localStorage.setItem("devasee-cart", JSON.stringify(cartItems));
    }, [cartItems]);

    // Add to cart or increase quantity if already exists
    const addToCart = (book: Book) => {
        setCartItems((prev) => {
            const existing = prev.find((item) => item.id === book.id);
            if (existing) {
                // If already in cart, increase quantity by 1, max stock limit
                const newQuantity = (existing.quantity ?? 1) + 1;
                if (book.stock && newQuantity > book.stock) {
                    return prev; // exceed stock, don't increase
                }
                return prev.map((item) =>
                    item.id === book.id ? { ...item, quantity: newQuantity } : item
                );
            } else {
                // New item, set quantity = 1 by default
                return [...prev, { ...book, quantity: 1 }];
            }
        });
    };

    const removeFromCart = (id: string) => {
        setCartItems((prev) => prev.filter((item) => item.id !== id));
    };

    // Update item quantity but don't exceed stock and minimum 1
    const updateItemQuantity = (id: string, quantity: number) => {
        setCartItems((prev) =>
            prev.map((item) => {
                if (item.id === id) {
                    const validQuantity = Math.min(
                        Math.max(quantity, 1),
                        item.stock ?? quantity
                    );
                    return { ...item, quantity: validQuantity };
                }
                return item;
            })
        );
    };

    return (
        <CartContext.Provider
            value={{ cartItems, addToCart, removeFromCart, updateItemQuantity }}
        >
            {children}
        </CartContext.Provider>
    );
};

export const useCart = () => {
    const context = useContext(CartContext);
    if (!context) throw new Error("useCart must be used within a CartProvider");
    return context;
};
