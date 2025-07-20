"use client";

import {useCart} from "@/app/context/CartContext";
import SubNavBar from "@/app/(router)/_components/SubNavBar";
import Link from "next/link";
import Image from "next/image";
import {useUser} from "@clerk/nextjs";
import {useRouter} from "next/navigation";
import {useState} from "react";

export default function Checkout() {
    const {cartItems, updateItemQuantity, removeFromCart} = useCart();
    const {isSignedIn, user} = useUser();
    const router = useRouter();
    const [showAddressForm, setShowAddressForm] = useState(false);

    // Calculate total price for convenience
    const totalPrice = cartItems.reduce(
        (total, item) => total + item.price * item.quantity,
        0
    );

    // Dummy delivery, tax, discount values (you can update with your logic)
    const deliveryFee = 300;
    const taxRate = 0.08; // 8% tax
    const discount = 500;

    const taxAmount = totalPrice * taxRate;
    const finalTotal = totalPrice + deliveryFee + taxAmount - discount;

    function handlePlaceOrder() {
        if (!isSignedIn) {
            router.push("/sign-in?redirect_url=/products/checkout");
            return;
        }
        setShowAddressForm(true);
    }

    return (
        <div>
            <SubNavBar path={"PRODUCTS\u00A0\u00A0/\u00A0\u00A0CHECKOUT"}/>

            <div className="w-screen h-auto bg-blue-50 flex flex-col items-center justify-center gap-6 py-12 px-4">
                {cartItems.length > 0 ? (
                    <div
                        className="w-screen h-auto lg:max-h-[600px]  flex flex-col justify-center  items-center lg:items-start lg:flex-row">
                        {!showAddressForm ? (
                            <>
                                {/* ORDER DETAILS */}
                                <div
                                    className="w-full h-auto lg:w-2/3 lg:max-h-[600px] lg:overflow-scroll hide-scrollbar max-w-3xl space-y-6 px-6 lg:pr-30">
                                    <div className="flex flex-col gap-4">
                                        <h1 className="text-2xl lg:text-4xl font-bold text-gray-800">
                                            ORDER DETAILS
                                        </h1>
                                        <p className="text-gray-600 text-sm">
                                            Review your items before checkout. Delivery in 4–5 days.
                                        </p>
                                    </div>

                                    {cartItems.map((item) => (
                                        <div
                                            key={item.id}
                                            className="text-sm justify-between flex items-center border-b border-gray-400/20 pb-6"
                                        >
                                            <div className="flex items-center gap-6">
                                                <Image
                                                    src={item.image}
                                                    className={"w-16 h-auto"}
                                                    alt={"Item image"}
                                                />
                                                <div className="flex flex-col">
                                                    <p className="font-bold sm:text-xl text-gray-700">
                                                        {item.title}
                                                    </p>
                                                    <p className="font-medium text-xs sm:text-lg text-gray-700 mb-1">
                                                        {new Intl.NumberFormat("en-LK", {
                                                            style: "currency",
                                                            currency: "LKR",
                                                            minimumFractionDigits: 2,
                                                        }).format(item.price)}
                                                    </p>
                                                    <div
                                                        className="flex sm:hidden items-center justify-between bg-gray-100 gap-1">
                                                        <button
                                                            onClick={() =>
                                                                item.quantity > 1 &&
                                                                updateItemQuantity(item.id, item.quantity - 1)
                                                            }
                                                            className="px-2 sm:px-4 sm:py-6 bg-gray-300 text-white cursor-pointer hover:bg-gray-400"
                                                        >
                                                            −
                                                        </button>
                                                        <span className="w-2 sm:w-8 text-center text-gray-600">
                              {item.quantity}
                            </span>
                                                        <button
                                                            onClick={() =>
                                                                item.quantity < item.stock &&
                                                                updateItemQuantity(item.id, item.quantity + 1)
                                                            }
                                                            className="px-2 sm:px-4 sm:py-6 bg-gray-300 text-white cursor-pointer hover:bg-gray-400"
                                                        >
                                                            +
                                                        </button>
                                                    </div>
                                                </div>
                                                <div
                                                    className="sm:flex hidden items-center justify-between bg-gray-100 gap-1">
                                                    <button
                                                        onClick={() =>
                                                            item.quantity > 1 &&
                                                            updateItemQuantity(item.id, item.quantity - 1)
                                                        }
                                                        className="px-2 py-3 sm:px-4 sm:py-6 bg-gray-300 text-white cursor-pointer hover:bg-gray-400"
                                                    >
                                                        −
                                                    </button>
                                                    <span className="w-4 sm:w-8 text-center text-gray-600">
                            {item.quantity}
                          </span>
                                                    <button
                                                        onClick={() =>
                                                            item.quantity < item.stock &&
                                                            updateItemQuantity(item.id, item.quantity + 1)
                                                        }
                                                        className="px-2 py-3 sm:px-4 sm:py-6 bg-gray-300 text-white cursor-pointer hover:bg-gray-400"
                                                    >
                                                        +
                                                    </button>
                                                </div>
                                            </div>

                                            <div className="flex items-center justify-between gap-2 text-xs">
                                                <button
                                                    onClick={() => removeFromCart(item.id)}
                                                    className="text-blue-50 bg-gray-400 border border-gray-400 px-6 sm:px-8 py-3 sm:py-4 cursor-pointer tracking-widest hover:text-gray-600 hover:bg-blue-50 transition-all duration-300 ease-in-out transform hover:scale-105"
                                                >
                                                    REMOVE
                                                </button>
                                            </div>
                                        </div>
                                    ))}
                                </div>

                                {/* ORDER SUMMARY */}
                                <div
                                    className="  w-full lg:w-1/3 mt-8 lg:mt-0 flex flex-col justify-center px-6 lg:pl-8 h-full lg:max-h-[600px] max-w-3xl space-y-4 hide-scrollbar overflow-y-scroll overflow-x-hidden">
                                    <h4 className="font-bold text-2xl lg:px-2 lg:text-4xl">Order Summary</h4>

                                    <div>
                                        <div
                                            className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Total Items</p>
                                            <p className="lg:text-lg text-gray-700">{cartItems.length}</p>
                                        </div>

                                        <div
                                            className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Price</p>
                                            <p className="lg:text-lg text-gray-700">
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(totalPrice)}
                                            </p>
                                        </div>

                                        <div
                                            className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Delivery Fee</p>
                                            <p className="lg:text-lg text-gray-700">
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(deliveryFee)}
                                            </p>
                                        </div>

                                        <div
                                            className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Tax (8%)</p>
                                            <p className="lg:text-lg text-gray-700">
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(taxAmount)}
                                            </p>
                                        </div>

                                        <div
                                            className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Discount</p>
                                            <p className="lg:text-lg text-gray-700">
                                                -{" "}
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(discount)}
                                            </p>
                                        </div>

                                        <div
                                            className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4 font-bold text-xl lg:text-2xl text-gray-700">
                                            <p>Total Price</p>
                                            <p>
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(finalTotal)}
                                            </p>
                                        </div>
                                    </div>

                                    {/* Wrap button in overflow-hidden container */}
                                    <div className="overflow-hidden">
                                        <button
                                            onClick={handlePlaceOrder}
                                            className="w-full mt-4 bg-[#0000ff] border border-[#0000ff] cursor-pointer text-white text-sm sm:text-base font-semibold py-4 px-6  transition-all duration-300 ease-in-out transform hover:scale-105"
                                        >
                                            Place Order
                                        </button>
                                    </div>
                                </div>

                            </>
                        ) : (
                            <div
                                className="w-screen h-auto lg:max-h-[600px]  flex flex-col justify-center lg:justify-start items-center lg:items-start lg:flex-row">
                                {/*USER DETAILS + ADDRESS FORM */}
                                <div
                                    className="w-full h-auto lg:w-2/3 max-w-3xl px-6 lg:px-20 space-y-6 bg-white rounded shadow">
                                    <h1 className="text-2xl lg:text-4xl font-bold text-gray-800">
                                        Enter Your Details
                                    </h1>
                                    <form
                                        onSubmit={(e) => {
                                            e.preventDefault();
                                            alert("Order placed! Implement your submit logic.");
                                            // Here you could send data to your API or backend
                                        }}
                                        className="flex flex-col gap-4"
                                    >
                                        <label className="flex flex-col">
                                            <span className="font-semibold text-gray-700">Name</span>
                                            <input
                                                type="text"
                                                required
                                                defaultValue={user?.fullName || ""}
                                                className="border border-gray-300 rounded px-3 py-2"
                                                name="name"
                                            />
                                        </label>

                                        <label className="flex flex-col">
                                            <span className="font-semibold text-gray-700">Email (cannot change)</span>
                                            <input
                                                type="email"
                                                required
                                                value={user?.emailAddresses[0]?.emailAddress || ""}
                                                className="border border-gray-300 rounded px-3 py-2 bg-gray-100 cursor-not-allowed"
                                                readOnly
                                                name="email"
                                            />
                                        </label>

                                        <label className="flex flex-col">
                                            <span className="font-semibold text-gray-700">Phone Number</span>
                                            <input
                                                type="tel"
                                                required
                                                className="border border-gray-300 rounded px-3 py-2"
                                                name="phone"
                                            />
                                        </label>

                                        <label className="flex flex-col">
                                            <span className="font-semibold text-gray-700">Address</span>
                                            <textarea
                                                required
                                                className="border border-gray-300 rounded px-3 py-2"
                                                rows={4}
                                                name="address"
                                            />
                                        </label>

                                        <button
                                            type="submit"
                                            className="w-full mt-4 bg-green-600 text-white text-sm sm:text-base font-semibold py-3 px-6 rounded hover:bg-green-700 transition-all duration-300"
                                        >
                                            Confirm Order
                                        </button>
                                    </form>
                                </div>
                                {/* ORDER SUMMARY */}
                                <div
                                    className="lg:w-1/3 pl-16 h-full lg:max-h-[600px] max-w-3xl space-y-4 hide-scrollbar overflow-y-scroll overflow-x-hidden">
                                    <h4 className="font-bold text-4xl">Order Summary</h4>

                                    <div>
                                        <div
                                            className="flex justify-between items-center border-b border-gray-400/20 py-4">
                                            <p className="text-lg text-gray-700">Total Items</p>
                                            <p className="text-lg text-gray-700">{cartItems.length}</p>
                                        </div>

                                        <div
                                            className="flex justify-between items-center border-b border-gray-400/20 py-4">
                                            <p className="text-lg text-gray-700">Price</p>
                                            <p className="text-lg text-gray-700">
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(totalPrice)}
                                            </p>
                                        </div>

                                        <div
                                            className="flex justify-between items-center border-b border-gray-400/20 py-4">
                                            <p className="text-lg text-gray-700">Delivery Fee</p>
                                            <p className="text-lg text-gray-700">
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(deliveryFee)}
                                            </p>
                                        </div>

                                        <div
                                            className="flex justify-between items-center border-b border-gray-400/20 py-4">
                                            <p className="text-lg text-gray-700">Tax (8%)</p>
                                            <p className="text-lg text-gray-700">
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(taxAmount)}
                                            </p>
                                        </div>

                                        <div
                                            className="flex justify-between items-center border-b border-gray-400/20 py-4">
                                            <p className="text-lg text-gray-700">Discount</p>
                                            <p className="text-lg text-gray-700">
                                                -{" "}
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(discount)}
                                            </p>
                                        </div>

                                        <div
                                            className="flex justify-between items-center border-b border-gray-400/20 py-4 font-bold text-2xl text-gray-700">
                                            <p>Total Price</p>
                                            <p>
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(finalTotal)}
                                            </p>
                                        </div>
                                    </div>

                                    {/* Wrap button in overflow-hidden container */}
                                    <div className="overflow-hidden">
                                        <button
                                            onClick={handlePlaceOrder}
                                            className="w-full mt-4 bg-[#0000ff] border border-[#0000ff] cursor-pointer text-white text-sm sm:text-base font-semibold py-4 px-6  transition-all duration-300 ease-in-out transform hover:scale-105"
                                        >
                                            Place Order
                                        </button>
                                    </div>
                                </div>

                            </div>
                        )}
                    </div>
                ) : (
                    <>
                        <h1 className="text-2xl sm:text-3xl font-bold text-gray-700">
                            Your Cart is Empty
                        </h1>
                        <p className="text-gray-500 text-lg">No items in the cart</p>
                        <Link
                            href="/products"
                            className="group relative inline-flex items-center gap-1 text-[#0000ff] text-lg font-semibold transition-all duration-300 hover:text-blue-600"
                        >
              <span className="relative z-10 text-sm sm:text-lg">
                Explore our products
              </span>
                            <span className="transform transition-transform duration-300 group-hover:translate-x-1">
                &rarr;
              </span>
                            <span
                                className="absolute bottom-0 left-0 h-[2px] w-full scale-x-0 bg-[#0000ff] transition-transform duration-300 group-hover:scale-x-100"/>
                        </Link>
                    </>
                )}
            </div>
        </div>
    );
}

