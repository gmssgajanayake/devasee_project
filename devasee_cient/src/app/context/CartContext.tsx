"use client";

import { createContext, useContext, useEffect, useState } from "react";
import { Book } from "@/types/types"; // Only use the shared type

interface CartContextType {
    cartItems: Book[];
    addToCart: (book: Book) => void;
    removeFromCart: (id: string) => void;
    updateItemQuantity: (id: string, quantity: number) => void;
    clearCart: () => void; // ✅ New function added
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
        setCartItems((prev) => {
            const existing = prev.find((item) => item.id === book.id);
            if (existing) {
                const newQuantity = (existing.quantity ?? 1) + 1;
                if (book.stock && newQuantity > book.stock) return prev;
                return prev.map((item) =>
                    item.id === book.id ? { ...item, quantity: newQuantity } : item
                );
            } else {
                return [...prev, { ...book, quantity: 1 }];
            }
        });
    };

    const removeFromCart = (id: string) => {
        setCartItems((prev) => prev.filter((item) => item.id !== id));
    };

    const updateItemQuantity = (id: string, quantity: number) => {
        setCartItems((prev) =>
            prev.map((item) =>
                item.id === id
                    ? {
                        ...item,
                        quantity: Math.min(Math.max(quantity, 1), item.stock ?? quantity),
                    }
                    : item
            )
        );
    };

    const clearCart = () => {
        setCartItems([]);
        localStorage.removeItem("devasee-cart");
    };

    return (
        <CartContext.Provider
            value={{ cartItems, addToCart, removeFromCart, updateItemQuantity, clearCart }}
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
