"use client";

import { useCart } from "@/app/context/CartContext";
import SubNavBar from "@/app/(router)/_components/SubNavBar";
import Link from "next/link";
import Image from "next/image";
import { useUser } from "@clerk/nextjs";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
    faChevronLeft,
    faSpinner,
    faMoneyBillWave,
    faFileUpload,
    faCheckCircle
} from "@fortawesome/free-solid-svg-icons";

type CheckoutStep = 'cart' | 'address' | 'payment';
type PaymentMethod = 'cod' | 'slip';

export default function Checkout() {
    const { cartItems, updateItemQuantity, removeFromCart, clearCart } = useCart();
    const { isSignedIn } = useUser();
    const router = useRouter();

    // CHANGED: Using steps instead of boolean
    const [checkoutStep, setCheckoutStep] = useState<CheckoutStep>('cart');
    const [isSubmitting, setIsSubmitting] = useState(false);

    // NEW: Payment States
    const [paymentMethod, setPaymentMethod] = useState<PaymentMethod>('cod');
    const [paymentSlip, setPaymentSlip] = useState<File | null>(null);

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

    const handleStartCheckout = () => {
        if (!isSignedIn) {
            router.push("/sign-in?redirect_url=/products/checkout");
            return;
        }
        setCheckoutStep('address');
    };

    const handleBack = () => {
        if (checkoutStep === 'payment') {
            setCheckoutStep('address');
        } else {
            setCheckoutStep('cart');
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            setPaymentSlip(e.target.files[0]);
        }
    };

    // Move from Address -> Payment
    const handleAddressSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        setCheckoutStep('payment');
        window.scrollTo(0, 0);
    };

    // Final Submission
    const handleFinalSubmit = async () => {
        if (paymentMethod === 'slip' && !paymentSlip) {
            alert("Please upload your payment slip.");
            return;
        }

        setIsSubmitting(true);

        try {
            // Simulate API call
            // In a real app, you would upload the 'paymentSlip' file to S3/Cloudinary here
            await new Promise(resolve => setTimeout(resolve, 1500));

            console.log("Order Placed:", {
                ...formData,
                paymentMethod,
                paymentSlipName: paymentSlip?.name,
                items: cartItems
            });

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
                    <div className="w-screen h-auto flex flex-col justify-center items-center lg:items-start lg:flex-row">

                        {/* CASE 1: CART VIEW */}
                        {checkoutStep === 'cart' && (
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
                                                <div className="image-container relative overflow-hidden rounded-md">
                                                    <div className="w-28 h-36 relative overflow-hidden rounded-md flex-shrink-0">
                                                        <Image
                                                            src={item.image}
                                                            alt={item.title}
                                                            fill
                                                            className="object-cover w-full h-full"
                                                            sizes="112px"
                                                        />
                                                    </div>
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
                                                    <div className="flex  sm:hidden items-center justify-between bg-gray-100 gap-1">
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
                                                        className="px-1 py-1 sm:px-4 sm:py-6 bg-gray-300 text-white cursor-pointer hover:bg-gray-400 transition-colors duration-200"
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

                                {/* CART SUMMARY */}
                                <div className="order-summary w-full lg:w-1/3 mt-8 lg:mt-0 flex flex-col justify-center px-6 lg:pl-8 h-full lg:max-h-[600px] max-w-3xl space-y-4 hide-scrollbar overflow-y-scroll overflow-x-hidden">
                                    <h4 className="font-bold text-2xl lg:px-2 lg:text-4xl">Order Summary</h4>

                                    {/* ... Summary Details (Prices) ... */}
                                    <div>
                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Total Items</p>
                                            <p className="lg:text-lg text-gray-700">{cartItems.length}</p>
                                        </div>
                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4 font-bold text-xl lg:text-2xl text-gray-700">
                                            <p>Total Price</p>
                                            <p>{new Intl.NumberFormat("en-LK", { style: "currency", currency: "LKR", minimumFractionDigits: 2 }).format(finalTotal)}</p>
                                        </div>
                                    </div>

                                    <div className="overflow-hidden">
                                        <button
                                            onClick={handleStartCheckout}
                                            className="w-full mt-4 bg-[#0000ff] border border-[#0000ff] cursor-pointer text-white text-sm sm:text-base font-semibold py-4 px-6 transition-all duration-300 ease-in-out hover:scale-105 hover:shadow-lg"
                                        >
                                            Proceed to Checkout
                                        </button>
                                    </div>
                                </div>
                            </>
                        )}

                        {/* CASE 2: ADDRESS & PAYMENT STEPS (Shared Layout) */}
                        {checkoutStep !== 'cart' && (
                            <>
                                <div className="w-full lg:w-2/3 max-w-3xl px-6 lg:px-20 space-y-6">
                                    <div className="flex flex-wrap justify-between items-center gap-4">
                                        <h1 className="text-2xl lg:text-4xl font-bold text-gray-800">
                                            {checkoutStep === 'address' ? 'SHIPPING DETAILS' : 'PAYMENT METHOD'}
                                        </h1>
                                        <button
                                            onClick={handleBack}
                                            className="text-gray-800 text-sm flex items-center gap-1 cursor-pointer hover:text-blue-600 transition-colors duration-200 whitespace-nowrap"
                                        >
                                            <FontAwesomeIcon icon={faChevronLeft} />
                                            {checkoutStep === 'address' ? 'Back to Cart' : 'Back to Address'}
                                        </button>
                                    </div>

                                    <div className="px-8 bg-blue-100/30 rounded-xl flex flex-col gap-4 py-8">

                                        {/* --- STEP 2: ADDRESS FORM --- */}
                                        {checkoutStep === 'address' && (
                                            <form id="address-form" onSubmit={handleAddressSubmit} className="flex flex-col gap-6">
                                                {/* ... (Existing Address Fields) ... */}
                                                <div className="space-y-4">
                                                    <h2 className="text-gray-800 font-bold text-2xl">Contact Information</h2>
                                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                                        <label className="flex flex-col">
                                                            <span className="font-semibold text-gray-700 mb-1">First Name</span>
                                                            <input type="text" required name="firstName" value={formData.firstName} onChange={handleInputChange} className="border border-gray-400 px-3 py-4 focus:ring-0 rounded-none transition-all duration-200" />
                                                        </label>
                                                        <label className="flex flex-col">
                                                            <span className="font-semibold text-gray-700 mb-1">Last Name</span>
                                                            <input type="text" required name="lastName" value={formData.lastName} onChange={handleInputChange} className="border border-gray-400 rounded-none px-3 py-4 focus:ring-0 transition-all duration-200" />
                                                        </label>
                                                    </div>
                                                    <label className="flex flex-col">
                                                        <span className="font-semibold text-gray-700 mb-1">Phone Number</span>
                                                        <input type="tel" required name="phone" value={formData.phone} onChange={handleInputChange} className="border border-gray-400 rounded-none px-3 py-4 focus:ring-0 transition-all duration-200" placeholder="+94 XX XXX XXXX" />
                                                    </label>
                                                </div>

                                                <div className="space-y-4">
                                                    <h2 className="text-gray-800 font-bold text-2xl">Shipping Address</h2>
                                                    <label className="flex flex-col">
                                                        <span className="font-semibold text-gray-700 mb-1">Address</span>
                                                        <textarea required name="address" value={formData.address} onChange={handleInputChange} className="border border-gray-400 rounded-none px-3 py-4 focus:ring-0 transition-all duration-200" rows={3} placeholder="Street address, apartment, floor, etc." />
                                                    </label>
                                                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                                                        <label className="flex flex-col">
                                                            <span className="font-semibold text-gray-700 mb-1">City</span>
                                                            <input type="text" required name="city" value={formData.city} onChange={handleInputChange} className="border border-gray-400 rounded-none px-3 py-4 focus:ring-0 transition-all duration-200" />
                                                        </label>
                                                        <label className="flex flex-col">
                                                            <span className="font-semibold text-gray-700 mb-1">Postal Code</span>
                                                            <input type="text" required name="postalCode" value={formData.postalCode} onChange={handleInputChange} className="border border-gray-400 rounded-none px-3 py-4 focus:ring-0 transition-all duration-200" />
                                                        </label>
                                                        <label className="flex flex-col">
                                                            <span className="font-semibold text-gray-700 mb-1">Country</span>
                                                            <select name="country" className="border border-gray-400 rounded-none px-3 py-4 focus:ring-0 transition-all duration-200" defaultValue="Sri Lanka">
                                                                <option value="Sri Lanka">Sri Lanka</option>
                                                                <option value="Other">Other</option>
                                                            </select>
                                                        </label>
                                                    </div>
                                                </div>

                                                <button
                                                    type="submit"
                                                    className="w-full mt-4 bg-[#0000ff] border border-[#0000ff] cursor-pointer text-white font-semibold py-4 px-6 transition-all duration-300 hover:scale-105"
                                                >
                                                    Continue to Payment
                                                </button>
                                            </form>
                                        )}

                                        {/* --- STEP 3: PAYMENT SELECTION --- */}
                                        {checkoutStep === 'payment' && (
                                            <div className="flex flex-col gap-6 animate-in fade-in duration-500">
                                                <p className="text-gray-600">Please select your preferred payment method.</p>

                                                {/* Method 1: COD */}
                                                <label
                                                    className={`relative flex items-center p-5 border-2 cursor-pointer transition-all duration-200 ${paymentMethod === 'cod' ? 'border-blue-600 bg-blue-50' : 'border-gray-200 hover:border-blue-300'}`}
                                                >
                                                    <input
                                                        type="radio"
                                                        name="payment"
                                                        value="cod"
                                                        checked={paymentMethod === 'cod'}
                                                        onChange={() => setPaymentMethod('cod')}
                                                        className="w-5 h-5 text-blue-600 focus:ring-blue-500 border-gray-300"
                                                    />
                                                    <div className="ml-4 flex items-center gap-3">
                                                        <div className="w-10 h-10 bg-green-100 rounded-full flex items-center justify-center text-green-600">
                                                            <FontAwesomeIcon icon={faMoneyBillWave} />
                                                        </div>
                                                        <div>
                                                            <span className="block font-bold text-gray-800">Cash on Delivery</span>
                                                            <span className="block text-sm text-gray-500">Pay with cash upon arrival.</span>
                                                        </div>
                                                    </div>
                                                </label>

                                                {/* Method 2: Bank Slip */}
                                                <label
                                                    className={`relative flex items-start p-5 border-2 cursor-pointer transition-all duration-200 ${paymentMethod === 'slip' ? 'border-blue-600 bg-blue-50' : 'border-gray-200 hover:border-blue-300'}`}
                                                >
                                                    <input
                                                        type="radio"
                                                        name="payment"
                                                        value="slip"
                                                        checked={paymentMethod === 'slip'}
                                                        onChange={() => setPaymentMethod('slip')}
                                                        className="w-5 h-5 mt-3 text-blue-600 focus:ring-blue-500 border-gray-300"
                                                    />
                                                    <div className="ml-4 w-full">
                                                        <div className="flex items-center gap-3 mb-2">
                                                            <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center text-blue-600">
                                                                <FontAwesomeIcon icon={faFileUpload} />
                                                            </div>
                                                            <div>
                                                                <span className="block font-bold text-gray-800">Bank Transfer / Slip Upload</span>
                                                                <span className="block text-sm text-gray-500">Upload your transaction proof.</span>
                                                            </div>
                                                        </div>

                                                        {/* Bank Details Info */}
                                                        {paymentMethod === 'slip' && (
                                                            <div className="mt-4 pl-2 border-l-4 border-blue-200">
                                                                <p className="text-sm text-gray-700 font-semibold">Bank Details:</p>
                                                                <p className="text-sm text-gray-600">Acct Name: Your Company Ltd</p>
                                                                <p className="text-sm text-gray-600">Acct No: 1234 5678 90</p>
                                                                <p className="text-sm text-gray-600">Bank: Commercial Bank</p>

                                                                <div className="mt-4">
                                                                    <p className="text-sm font-semibold text-gray-700 mb-2">Upload Receipt (JPG/PNG/PDF)</p>
                                                                    <input
                                                                        type="file"
                                                                        accept="image/*,.pdf"
                                                                        onChange={handleFileChange}
                                                                        className="block w-full text-sm text-gray-500
                                                                            file:mr-4 file:py-2 file:px-4
                                                                            file:rounded-full file:border-0
                                                                            file:text-sm file:font-semibold
                                                                            file:bg-blue-50 file:text-blue-700
                                                                            hover:file:bg-blue-100
                                                                            cursor-pointer"
                                                                    />
                                                                    {paymentSlip && (
                                                                        <p className="mt-2 text-green-600 text-xs flex items-center gap-1">
                                                                            <FontAwesomeIcon icon={faCheckCircle} />
                                                                            {paymentSlip.name} selected
                                                                        </p>
                                                                    )}
                                                                </div>
                                                            </div>
                                                        )}
                                                    </div>
                                                </label>

                                                <button
                                                    onClick={handleFinalSubmit}
                                                    className="submit-button w-full mt-4 bg-[#0000ff] border border-[#0000ff] cursor-pointer text-white font-semibold py-4 px-6 transition-all duration-300 hover:scale-105 disabled:opacity-70 disabled:cursor-not-allowed"
                                                    disabled={isSubmitting || (paymentMethod === 'slip' && !paymentSlip)}
                                                >
                                                    {isSubmitting ? (
                                                        <>
                                                            <FontAwesomeIcon icon={faSpinner} className="fa-spin mr-2" />
                                                            Processing Order...
                                                        </>
                                                    ) : (
                                                        "Confirm & Place Order"
                                                    )}
                                                </button>
                                            </div>
                                        )}
                                    </div>
                                </div>

                                {/* SHARED ORDER SUMMARY SIDEBAR */}
                                <div className="order-summary w-full lg:w-1/3 mt-8 lg:mt-0 flex flex-col justify-center px-6 lg:pl-8 h-full lg:max-h-[600px] max-w-3xl space-y-4 hide-scrollbar overflow-y-scroll overflow-x-hidden">
                                    <h4 className="font-bold text-2xl lg:px-2 lg:text-4xl">Order Summary</h4>
                                    {/* ... (Existing summary logic remains same) ... */}
                                    <div>
                                        {cartItems.map(item => (
                                            <div key={item.id} className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3">
                                                <div className="flex items-center gap-2">
                                                    <div className="w-10 h-10 relative rounded overflow-hidden flex-shrink-0">
                                                        <Image src={item.image} fill className="object-cover" alt={item.title} sizes="40px" />
                                                    </div>
                                                    <p className="text-gray-700 line-clamp-1">{item.title}</p>
                                                </div>
                                                <p className="text-gray-700 whitespace-nowrap">
                                                    {item.quantity} × {new Intl.NumberFormat("en-LK", { style: "currency", currency: "LKR", minimumFractionDigits: 2 }).format(item.price)}
                                                </p>
                                            </div>
                                        ))}
                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Subtotal</p>
                                            <p className="lg:text-lg text-gray-700">{new Intl.NumberFormat("en-LK", { style: "currency", currency: "LKR", minimumFractionDigits: 2 }).format(totalPrice)}</p>
                                        </div>
                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Delivery Fee</p>
                                            <p className="lg:text-lg text-gray-700">{new Intl.NumberFormat("en-LK", { style: "currency", currency: "LKR", minimumFractionDigits: 2 }).format(deliveryFee)}</p>
                                        </div>
                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Tax (8%)</p>
                                            <p className="lg:text-lg text-gray-700">{new Intl.NumberFormat("en-LK", { style: "currency", currency: "LKR", minimumFractionDigits: 2 }).format(taxAmount)}</p>
                                        </div>
                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4">
                                            <p className="lg:text-lg text-gray-700">Discount</p>
                                            <p className="lg:text-lg text-gray-700">- {new Intl.NumberFormat("en-LK", { style: "currency", currency: "LKR", minimumFractionDigits: 2 }).format(discount)}</p>
                                        </div>
                                        <div className="flex px-2 justify-between items-center border-b border-gray-400/20 py-3 lg:py-4 font-bold text-xl lg:text-2xl text-gray-700">
                                            <p>Total Price</p>
                                            <p>{new Intl.NumberFormat("en-LK", { style: "currency", currency: "LKR", minimumFractionDigits: 2 }).format(finalTotal)}</p>
                                        </div>
                                    </div>
                                </div>
                            </>
                        )}
                    </div>
                ) : (
                    <div className="text-center">
                        <h1 className="text-2xl sm:text-3xl font-bold text-gray-700 mb-4">Your Cart is Empty</h1>
                        <p className="text-gray-500 text-lg mb-6">No items in the cart</p>
                        <Link href="/products" className="group relative inline-flex items-center gap-1 text-[#0000ff] text-lg font-semibold transition-all duration-300 hover:text-blue-600">
                            <span className="relative z-10 text-sm sm:text-lg">Explore our products</span>
                            <span className="transform transition-transform duration-300 group-hover:translate-x-1">&rarr;</span>
                        </Link>
                    </div>
                )}
            </div>
        </div>
    );
}