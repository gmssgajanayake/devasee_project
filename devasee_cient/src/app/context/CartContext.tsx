"use client";

import { createContext, useContext, useEffect, useState } from "react";
import { StaticImageData } from "next/image";

type Book = {
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
    removeFromCart: (title: string) => void;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider = ({ children }: { children: React.ReactNode }) => {
    const [cartItems, setCartItems] = useState<Book[]>([]);

    // ✅ Load cart from localStorage once on initial mount
    useEffect(() => {
        const storedCart = localStorage.getItem("devasee-cart");
        if (storedCart) {
            try {
                const parsedCart = JSON.parse(storedCart) as Book[];
                setCartItems(parsedCart);
            } catch (error) {
                console.error("Failed to parse stored cart:", error);
            }
        }
    }, []);

    // ✅ Save cart to localStorage whenever it changes
    useEffect(() => {
        localStorage.setItem("devasee-cart", JSON.stringify(cartItems));
    }, [cartItems]);

    const addToCart = (book: Book) => {
        setCartItems((prev) =>
            prev.some((item) => item.title === book.title) ? prev : [...prev, book]
        );
    };

    const removeFromCart = (title: string) => {
        setCartItems((prev) => prev.filter((item) => item.title !== title));
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
