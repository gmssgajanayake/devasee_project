"use client";

import { useCart } from "@/app/context/CartContext";

export default function Page() {
    const { cartItems } = useCart();

    return (
        <div className="w-screen h-screen flex flex-col items-center justify-center gap-4">
            <h1 className="text-2xl font-bold">Your Cart</h1>
            {cartItems.length > 0 ? (
                cartItems.map((item, index) => (
                    <div key={index} className="text-center border p-4 rounded shadow">
                        <p className="font-semibold">{item.title}</p>
                        <p>{item.author}</p>
                        <p className="text-green-600">Rs. {item.price}</p>
                    </div>
                ))
            ) : (
                <p className="text-gray-500">No items in the cart</p>
            )}
        </div>
    );
}
