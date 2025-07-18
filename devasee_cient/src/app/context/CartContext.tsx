"use client";

import { createContext, useContext, useEffect, useState } from "react";
import { StaticImageData } from "next/image";

type Book = {
    id: string; // Added ID property
    image: StaticImageData;
    title: string;
    author: string;
    price: number;
    type: string;
    brand: string;
};

interface CartContextType {
    cartItems: Book[];
    addToCart: (book: Book) => void;
    removeFromCart: (id: string) => void; // Changed to use ID
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider = ({ children }: { children: React.ReactNode }) => {
    const [cartItems, setCartItems] = useState<Book[]>([]);

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

    useEffect(() => {
        localStorage.setItem("devasee-cart", JSON.stringify(cartItems));
    }, [cartItems]);

    const addToCart = (book: Book) => {
        setCartItems((prev) =>
            prev.some((item) => item.id === book.id) ? prev : [...prev, book]
        );
    };

    const removeFromCart = (id: string) => {
        setCartItems((prev) => prev.filter((item) => item.id !== id));
    };

    return (
        <CartContext.Provider value={{ cartItems, addToCart, removeFromCart }}>
            {children}
        </CartContext.Provider>
    );
};

export const useCart = () => {
    const context = useContext(CartContext);
    if (!context) throw new Error("useCart must be used within a CartProvider");
    return context;
};