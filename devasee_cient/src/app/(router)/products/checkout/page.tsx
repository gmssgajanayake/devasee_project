"use client";

import { useCart } from "@/app/context/CartContext";
import SubNavBar from "@/app/(router)/_components/SubNavBar";

export default function Page() {
    const { cartItems, updateItemQuantity, removeFromCart } = useCart();

    return (
        <div>
            <SubNavBar path={"PRODUCTS\u00A0\u00A0/\u00A0\u00A0CHECKOUT"} />

            <div className="w-screen min-h-screen flex flex-col items-center justify-start gap-6 py-8 px-4">


                <h1 className="text-3xl font-bold">Your Cart</h1>

                {cartItems.length > 0 ? (
                    <div className="w-full max-w-3xl space-y-6">
                        {cartItems.map((item) => (
                            <div
                                key={item.id}
                                className="border  rounded-lg p-4 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4"
                            >
                                <div className="flex flex-col">
                                    <p className="font-semibold text-lg">{item.title}</p>
                                    <p className="text-gray-600 text-sm">{item.author}</p>
                                    <p className="text-gray-500 text-xs mb-1">
                                        {new Intl.NumberFormat("en-LK", {
                                            style: "currency",
                                            currency: "LKR",
                                            minimumFractionDigits: 2,
                                        }).format(item.price)}
                                    </p>

                                </div>

                                <div className="flex items-center gap-3 text-sm">
                                    <button
                                        onClick={() =>
                                            item.quantity > 1 && updateItemQuantity(item.id, item.quantity - 1)
                                        }
                                        className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300"
                                    >
                                        −
                                    </button>
                                    <span className="w-8 text-center font-mono">{item.quantity}</span>
                                    <button
                                        onClick={() =>
                                            item.quantity < item.stock && updateItemQuantity(item.id, item.quantity + 1)
                                        }
                                        className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300"
                                    >
                                        +
                                    </button>
                                </div>

                                <button
                                    onClick={() => removeFromCart(item.id)}
                                    className="text-red-600 hover:underline text-sm font-semibold"
                                >
                                    Remove
                                </button>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p className="text-gray-500 text-lg">No items in the cart</p>
                )}
            </div>
        </div>

    );
}
