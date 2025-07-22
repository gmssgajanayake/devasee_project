"use client";

import { useCart } from "@/app/context/CartContext";
import SubNavBar from "@/app/(router)/_components/SubNavBar";
import Link from "next/link";
import Image from "next/image";
import { useUser } from "@clerk/nextjs";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faChevronLeft, faSpinner } from "@fortawesome/free-solid-svg-icons";


export default function Checkout() {
    const { cartItems, updateItemQuantity, removeFromCart, clearCart } = useCart();
    const { isSignedIn, user } = useUser();
    const router = useRouter();
    const [showAddressForm, setShowAddressForm] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        phone: "",
        address: "",
        city: "",
        postalCode: "",
        notes: ""
    });

    // Calculate prices
    const totalPrice = cartItems.reduce(
        (total, item) => total + item.price * item.quantity,
        0
    );

    const deliveryFee = 300;
    const taxRate = 0.08;
    const discount = 500;
    const taxAmount = totalPrice * taxRate;
    const finalTotal = totalPrice + deliveryFee + taxAmount - discount;

    const handlePlaceOrder = () => {
        if (!isSignedIn) {
            router.push("/sign-in?redirect_url=/products/checkout");
            return;
        }
        setShowAddressForm(true);
    };

    const handleBackToCart = () => {
        setShowAddressForm(false);
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmitOrder = async (e: React.FormEvent) => {
        e.preventDefault();
        setIsSubmitting(true);

        try {
            // Simulate API call
            await new Promise(resolve => setTimeout(resolve, 1500));

            // Clear cart and redirect
            clearCart();
            router.push("/order-confirmation");
        } catch (error) {
            console.error("Order submission failed:", error);
            setIsSubmitting(false);
        }
    };

    return (
        <div className="checkout-page">
            <SubNavBar path={"PRODUCTS\u00A0\u00A0/\u00A0\u00A0CHECKOUT"} />

            <div className="w-screen h-auto bg-blue-50 flex flex-col items-center justify-center gap-6 py-12 px-4">
                {cartItems.length > 0 ? (
                    <div className="w-screen h-auto   flex flex-col justify-center items-center lg:items-start lg:flex-row">
                        {!showAddressForm ? (
                            <>
                                {/* ORDER DETAILS */}
                                <div className="w-full h-auto lg:w-2/3 lg:max-h-[600px] lg:overflow-scroll hide-scrollbar max-w-3xl space-y-6 px-6 lg:pr-30">
                                    <div className="flex flex-col gap-4">
                                        <h1 className="text-2xl lg:text-4xl font-bold text-gray-800">
                                            ORDER DETAILS
                                        </h1>
                                        <p className="text-gray-600 text-sm">
                                            Review your items before checkout. Delivery in 4–5 days.
                                        </p>
                                    </div>

                                    {cartItems.map((item, index) => (
                                        <div
                                            key={item.id}
                                            className={`text-sm justify-between flex items-center border-b border-gray-400/20 pb-6 ${index === cartItems.length - 1 ? 'last-item' : ''}`}
                                        >
                                            <div className="flex items-center gap-6">
                                                <div className="image-container w-16 h-16 relative overflow-hidden rounded-md">
                                                    <Image
                                                        src={item.image}
                                                        fill
                                                        className="object-cover"
                                                        alt={item.title}
                                                    />
                                                </div>
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
                                                    <div className="flex sm:hidden items-center justify-between bg-gray-100 gap-1">
                                                        <button
                                                            onClick={() =>
                                                                item.quantity > 1 &&
                                                                updateItemQuantity(item.id, item.quantity - 1)
                                                            }
                                                            className="px-2 sm:px-4 sm:py-6 bg-gray-300 text-white cursor-pointer hover:bg-gray-400 transition-colors duration-200"
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
                                                            className="px-2 sm:px-4 sm:py-6 bg-gray-300 text-white cursor-pointer hover:bg-gray-400 transition-colors duration-200"
                                                        >
                                                            +
                                                        </button>
                                                    </div>
                                                </div>
                                                <div className="sm:flex hidden items-center justify-between bg-gray-100 gap-1">
                                                    <button
                                                        onClick={() =>
                                                            item.quantity > 1 &&
                                                            updateItemQuantity(item.id, item.quantity - 1)
                                                        }
                                                        className="px-2 py-3 sm:px-4 sm:py-6 bg-gray-300 text-white cursor-pointer hover:bg-gray-400 transition-colors duration-200"
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
                                                        className="px-2 py-3 sm:px-4 sm:py-6 bg-gray-300 text-white cursor-pointer hover:bg-gray-400 transition-colors duration-200"
                                                    >
                                                        +
                                                    </button>
                                                </div>
                                            </div>

                                            <div className="flex items-center justify-between gap-2 text-xs">
                                                <button
                                                    onClick={() => removeFromCart(item.id)}
                                                    className="text-blue-50 bg-gray-600 border border-gray-600 px-6 sm:px-8 py-3 sm:py-4 cursor-pointer tracking-widest hover:text-gray-600 hover:bg-blue-50 transition-all duration-300 ease-in-out"
                                                >
                                                    REMOVE
                                                </button>
                                            </div>
                                        </div>
                                    ))}
                                </div>

                                {/* ORDER SUMMARY */}
                                <div className="order-summary w-full lg:w-1/3 mt-8 lg:mt-0 flex flex-col justify-center px-6 lg:pl-8 h-full lg:max-h-[600px] max-w-3xl space-y-4 hide-scrollbar overflow-y-scroll overflow-x-hidden">
                                    <h4 className="font-bold text-2xl lg:px-2 lg:text-4xl">Order Summary</h4>

                                    <div>
                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Total Items</p>
                                            <p className="lg:text-lg text-gray-700">{cartItems.length}</p>
                                        </div>

                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Price</p>
                                            <p className="lg:text-lg text-gray-700">
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(totalPrice)}
                                            </p>
                                        </div>

                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Delivery Fee</p>
                                            <p className="lg:text-lg text-gray-700">
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(deliveryFee)}
                                            </p>
                                        </div>

                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Tax (8%)</p>
                                            <p className="lg:text-lg text-gray-700">
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(taxAmount)}
                                            </p>
                                        </div>

                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
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

                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4 font-bold text-xl lg:text-2xl text-gray-700">
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

                                    <div className="overflow-hidden">
                                        <button
                                            onClick={handlePlaceOrder}
                                            className="w-full mt-4 bg-[#0000ff] border border-[#0000ff] cursor-pointer text-white text-sm sm:text-base font-semibold py-4 px-6 transition-all duration-300 ease-in-out hover:scale-105 hover:shadow-lg"
                                        >
                                            Proceed to Checkout
                                        </button>
                                    </div>
                                </div>
                            </>
                        ) : (
                            <>
                                {/* ADDRESS FORM */}
                                <div className="w-full  lg:w-2/3 max-w-3xl px-6 lg:px-20 space-y-6">
                                    <div className="flex flex-wrap justify-between items-center gap-4">
                                        <h1 className="text-2xl lg:text-4xl font-bold text-gray-800">
                                            SHIPPING DETAILS
                                        </h1>
                                        <button
                                            onClick={handleBackToCart}
                                            className="text-gray-800 text-sm flex items-center gap-1 cursor-pointer hover:text-blue-600 transition-colors duration-200 whitespace-nowrap"
                                        >
                                            <FontAwesomeIcon icon={faChevronLeft} /> Back to Cart
                                        </button>
                                    </div>

                                    <div className="px-8 bg-blue-100/30 rounded-xl flex flex-col gap-4 py-8">
                                        <form
                                            onSubmit={handleSubmitOrder}
                                            className="flex flex-col gap-6"
                                            id="checkout-form"
                                        >
                                            <div className="space-y-4">
                                                <h2 className="text-gray-800 font-bold text-2xl">
                                                    Contact Information
                                                </h2>

                                                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                                    <label className="flex flex-col">
                                                        <span className="font-semibold text-gray-700 mb-1">First Name</span>
                                                        <input
                                                            type="text"
                                                            required
                                                            name="firstName"
                                                            value={formData.firstName}
                                                            onChange={handleInputChange}
                                                            className="border border-gray-400  px-3 py-4  focus:ring-0  rounded-none transition-all duration-200"
                                                        />
                                                    </label>

                                                    <label className="flex flex-col">
                                                        <span className="font-semibold text-gray-700 mb-1">Last Name</span>
                                                        <input
                                                            type="text"
                                                            required
                                                            name="lastName"
                                                            value={formData.lastName}
                                                            onChange={handleInputChange}
                                                            className="border border-gray-400 rounded-none px-3 py-4 focus:ring-0 focus:ring-blue-600 focus:border-blue-600 transition-all duration-200"
                                                        />
                                                    </label>
                                                </div>

                                                <label className="flex flex-col">
                                                    <span className="font-semibold text-gray-700 mb-1">Email</span>
                                                    <input
                                                        type="email"
                                                        required
                                                        value={user?.emailAddresses[0]?.emailAddress || ""}
                                                        className="border border-gray-400 rounded-none px-3 py-4  cursor-not-allowed focus:ring-0 focus:ring-blue-600 transition-all duration-200"
                                                        readOnly
                                                    />
                                                </label>

                                                <label className="flex flex-col">
                                                    <span className="font-semibold text-gray-700 mb-1">Phone Number</span>
                                                    <input
                                                        type="tel"
                                                        required
                                                        name="phone"
                                                        value={formData.phone}
                                                        onChange={handleInputChange}
                                                        className="border border-gray-400 rounded-none px-3 py-4 focus:ring-0 focus:ring-blue-500 focus:border-blue-500 transition-all duration-200"
                                                        placeholder="+94 XX XXX XXXX"
                                                    />
                                                </label>
                                            </div>

                                            <div className="space-y-4">
                                                <h2 className="text-gray-800 font-bold text-2xl">
                                                    Shipping Address
                                                </h2>

                                                <label className="flex flex-col">
                                                    <span className="font-semibold text-gray-700 mb-1">Address</span>
                                                    <textarea
                                                        required
                                                        name="address"
                                                        value={formData.address}
                                                        onChange={handleInputChange}
                                                        className="border border-gray-400 rounded-none px-3 py-4 focus:ring-0 focus:ring-blue-600 focus:border-blue-600 transition-all duration-200"
                                                        rows={3}
                                                        placeholder="Street address, apartment, floor, etc."
                                                    />
                                                </label>

                                                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                                                    <label className="flex flex-col">
                                                        <span className="font-semibold text-gray-700 mb-1">City</span>
                                                        <input
                                                            type="text"
                                                            required
                                                            name="city"
                                                            value={formData.city}
                                                            onChange={handleInputChange}
                                                            className="border border-gray-400 rounded-none px-3 py-4 focus:ring-0 focus:ring-blue-600 focus:border-blue-600 transition-all duration-200"
                                                        />
                                                    </label>

                                                    <label className="flex flex-col">
                                                        <span className="font-semibold text-gray-700 mb-1">Postal Code</span>
                                                        <input
                                                            type="text"
                                                            required
                                                            name="postalCode"
                                                            value={formData.postalCode}
                                                            onChange={handleInputChange}
                                                            className="border border-gray-400 rounded-none px-3 py-4 focus:ring-0 focus:ring-blue-600 focus:border-blue-600 transition-all duration-200"
                                                        />
                                                    </label>

                                                    <label className="flex flex-col">
                                                        <span className="font-semibold text-gray-700 mb-1">Country</span>
                                                        <select
                                                            name="country"
                                                            className="border border-gray-400 rounded-none px-3 py-4 focus:ring-0 focus:ring-blue-600 focus:border-blue-600 transition-all duration-200"
                                                            defaultValue="Sri Lanka"
                                                        >
                                                            <option value="Sri Lanka">Sri Lanka</option>
                                                            <option value="Other">Other</option>
                                                        </select>
                                                    </label>
                                                </div>
                                            </div>

                                            <div className="space-y-4">
                                                <h2 className="text-gray-800 font-bold text-2xl">
                                                    Order Notes
                                                </h2>

                                                <label className="flex flex-col">
                                                    <span className="font-semibold text-gray-700 mb-1">Special Instructions</span>
                                                    <textarea
                                                        name="notes"
                                                        value={formData.notes}
                                                        onChange={handleInputChange}
                                                        className="border border-gray-400 rounded-none px-3 py-4 focus:ring-0 focus:ring-blue-600 focus:border-blue-600 transition-all duration-200"
                                                        rows={2}
                                                        placeholder="Any special delivery instructions?"
                                                    />
                                                </label>
                                            </div>

                                            <div className="overflow-hidden">
                                                <button
                                                    type="submit"
                                                    className="submit-button w-full mt-4 bg-[#0000ff] border border-[#0000ff] cursor-pointer text-white text-sm sm:text-base font-semibold py-4 px-6 transition-all duration-300 ease-in-out hover:scale-105 hover:shadow-lg disabled:opacity-70 disabled:cursor-not-allowed"
                                                    disabled={isSubmitting}
                                                >
                                                    {isSubmitting ? (
                                                        <>
                                                            <FontAwesomeIcon icon={faSpinner} className="fa-spin mr-2" />
                                                            Processing...
                                                        </>
                                                    ) : (
                                                        "Place Order"
                                                    )}
                                                </button>
                                            </div>
                                        </form>
                                    </div>
                                </div>

                                {/* ORDER SUMMARY */}
                                <div className="order-summary w-full lg:w-1/3 mt-8 lg:mt-0 flex flex-col justify-center px-6 lg:pl-8 h-full lg:max-h-[600px] max-w-3xl space-y-4 hide-scrollbar overflow-y-scroll overflow-x-hidden">
                                    <h4 className="font-bold text-2xl lg:px-2 lg:text-4xl">Order Summary</h4>

                                    <div>
                                        {cartItems.map(item => (
                                            <div key={item.id} className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3">
                                                <div className="flex items-center gap-2">
                                                    <div className="w-10 h-10 relative rounded overflow-hidden">
                                                        <Image
                                                            src={item.image}
                                                            fill
                                                            className="object-cover"
                                                            alt={item.title}
                                                        />
                                                    </div>
                                                    <p className="text-gray-700 line-clamp-1">{item.title}</p>
                                                </div>
                                                <p className="text-gray-700 whitespace-nowrap">
                                                    {item.quantity} × {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(item.price)}
                                                </p>
                                            </div>
                                        ))}

                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Subtotal</p>
                                            <p className="lg:text-lg text-gray-700">
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(totalPrice)}
                                            </p>
                                        </div>

                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Delivery Fee</p>
                                            <p className="lg:text-lg text-gray-700">
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(deliveryFee)}
                                            </p>
                                        </div>

                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Tax (8%)</p>
                                            <p className="lg:text-lg text-gray-700">
                                                {new Intl.NumberFormat("en-LK", {
                                                    style: "currency",
                                                    currency: "LKR",
                                                    minimumFractionDigits: 2,
                                                }).format(taxAmount)}
                                            </p>
                                        </div>

                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
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

                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4 font-bold text-xl lg:text-2xl text-gray-700">
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
                                </div>
                            </>
                        )}
                    </div>
                ) : (
                    <div className="text-center">
                        <h1 className="text-2xl sm:text-3xl font-bold text-gray-700 mb-4">
                            Your Cart is Empty
                        </h1>
                        <p className="text-gray-500 text-lg mb-6">No items in the cart</p>
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
                        </Link>
                    </div>
                )}
            </div>
        </div>
    );
}