"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Minus, Plus, Facebook, Twitter, Linkedin, Mail } from "lucide-react";
import { motion } from "framer-motion";

// Mock Data to simulate fetching a product based on ID
const MOCK_PRODUCT = {
    id: "1",
    name: "Customized White Mug Printing – Normal Handle",
    price: 950.00,
    sku: "MUG-001",
    description: "A customized white mug is a great way to surprise your loved ones on a special occasion.",
    features: [
        "White ceramic 11oz mug",
        "Printed in full colour & photo quality",
        "Boxed in white box"
    ],
    image: "https://placehold.co/600x600/png?text=Mug+Preview",
};

export default function ProductDetailsPage() {
    const router = useRouter();
    const [quantity, setQuantity] = useState(1);
    const [hasDesign, setHasDesign] = useState("no");
    const [isGiftWrapped, setIsGiftWrapped] = useState(false);

    // Calculations
    const totalPrice = MOCK_PRODUCT.price * quantity + (isGiftWrapped ? 150 : 0);

    return (
        <div className="min-h-screen bg-white text-gray-800 font-sans pb-20">
            {/* Breadcrumb / Top Bar Mockup */}
            <div className="bg-blue-50/50 py-4 mb-8">
                <div className="max-w-7xl mx-auto px-4 text-sm text-gray-500">
          <span className="cursor-pointer hover:text-blue-600" onClick={() => router.back()}>
            Home
          </span>
                    {" / "}
                    <span className="cursor-pointer hover:text-blue-600" onClick={() => router.back()}>
            Printing Services
          </span>
                    {" / "}
                    <span className="text-gray-800 font-semibold">{MOCK_PRODUCT.name}</span>
                </div>
            </div>

            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex flex-col lg:flex-row gap-12">

                    {/* Left Column: Product Image */}
                    <div className="w-full lg:w-1/2">
                        <motion.div
                            initial={{ opacity: 0, x: -20 }}
                            animate={{ opacity: 1, x: 0 }}
                            className="aspect-square bg-gray-50 rounded-xl overflow-hidden border border-gray-100 relative flex items-center justify-center"
                        >
                            <img
                                src={MOCK_PRODUCT.image}
                                alt={MOCK_PRODUCT.name}
                                className="w-3/4 h-auto object-contain drop-shadow-xl"
                            />
                            <button className="absolute bottom-4 left-4 p-2 bg-white rounded-full shadow-md hover:bg-gray-50">
                                <span className="sr-only">Expand</span>
                                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                                    <path d="M15 3h6v6M9 21H3v-6M21 3l-7 7M3 21l7-7"/>
                                </svg>
                            </button>
                        </motion.div>
                    </div>

                    {/* Right Column: Product Details */}
                    <div className="w-full lg:w-1/2 space-y-8">
                        <div>
                            <h1 className="text-3xl font-bold text-gray-900 mb-2">{MOCK_PRODUCT.name}</h1>
                            <p className="text-2xl font-bold text-gray-900">Rs. {MOCK_PRODUCT.price.toFixed(2)}</p>
                        </div>

                        {/* Features List */}
                        <ul className="space-y-2 text-sm text-gray-600">
                            {MOCK_PRODUCT.features.map((feature, index) => (
                                <li key={index} className="flex items-center gap-2">
                                    <span className="w-1.5 h-1.5 bg-gray-400 rounded-full" />
                                    {feature}
                                </li>
                            ))}
                        </ul>

                        <div className="border-t border-gray-200 pt-6 space-y-6">

                            {/* Design Option */}
                            <div className="space-y-3">
                                <p className="font-semibold text-sm">Do you have a design? <span className="text-red-500">*</span></p>
                                <div className="flex gap-6">
                                    <label className="flex items-center gap-2 cursor-pointer">
                                        <input
                                            type="radio"
                                            name="design"
                                            value="yes"
                                            checked={hasDesign === "yes"}
                                            onChange={(e) => setHasDesign(e.target.value)}
                                            className="w-4 h-4 text-blue-600 border-gray-300 focus:ring-blue-500"
                                        />
                                        <span className="text-sm">Yes, I have a design</span>
                                    </label>
                                    <label className="flex items-center gap-2 cursor-pointer">
                                        <input
                                            type="radio"
                                            name="design"
                                            value="no"
                                            checked={hasDesign === "no"}
                                            onChange={(e) => setHasDesign(e.target.value)}
                                            className="w-4 h-4 text-blue-600 border-gray-300 focus:ring-blue-500"
                                        />
                                        <span className="text-sm">No, I will send all information to design</span>
                                    </label>
                                </div>
                            </div>

                            {/* File Upload */}
                            <div className="space-y-2">
                                <label className="block text-sm font-semibold">Upload your photos</label>
                                <div className="border-2 border-dashed border-gray-300 rounded-lg p-6 text-center hover:bg-gray-50 transition-colors cursor-pointer bg-gray-50/50">
                                    <div className="flex flex-col items-center gap-2">
                                        <button className="bg-blue-500 hover:bg-blue-600 text-white px-6 py-2 rounded text-sm font-medium transition-colors">
                                            Select photo
                                        </button>
                                        <p className="text-xs text-gray-500">Drag File Here</p>
                                    </div>
                                </div>
                            </div>

                            {/* Text Areas */}
                            <div className="space-y-4">
                                <div>
                                    <label className="block text-sm font-semibold mb-1">Wordings</label>
                                    <textarea
                                        className="w-full border border-gray-300 rounded-lg p-3 text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
                                        rows={3}
                                        placeholder="Type quotes, wishes, etc. here..."
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-semibold mb-1">Design guide</label>
                                    <textarea
                                        className="w-full border border-gray-300 rounded-lg p-3 text-sm focus:ring-2 focus:ring-blue-500 focus:border-transparent outline-none"
                                        rows={3}
                                        placeholder="Tell us how the design should be..."
                                    />
                                </div>
                            </div>

                            {/* Price Summary */}
                            <div className="bg-gray-50 p-4 rounded-lg flex justify-between items-center font-bold text-sm">
                                <span>PRODUCT PRICE RS. {MOCK_PRODUCT.price.toFixed(2)} X {quantity}</span>
                                {/* FIXED: Used totalPrice variable here */}
                                <span>RS. {totalPrice.toFixed(2)}</span>
                            </div>

                            {/* Actions Row */}
                            <div className="flex flex-col sm:flex-row gap-4 items-start sm:items-center">
                                {/* Quantity Counter */}
                                <div className="flex items-center border border-gray-300 rounded bg-white">
                                    <button
                                        onClick={() => setQuantity(Math.max(1, quantity - 1))}
                                        className="px-4 py-2 hover:bg-gray-100 transition-colors"
                                    >
                                        <Minus className="w-3 h-3" />
                                    </button>
                                    <span className="w-12 text-center font-medium text-sm">{quantity}</span>
                                    <button
                                        onClick={() => setQuantity(quantity + 1)}
                                        className="px-4 py-2 hover:bg-gray-100 transition-colors"
                                    >
                                        <Plus className="w-3 h-3" />
                                    </button>
                                </div>

                                {/* Add to Cart Button */}
                                <button className="flex-1 bg-black text-white py-2.5 px-8 rounded font-bold hover:bg-gray-800 transition-all uppercase text-sm tracking-wide shadow-lg hover:shadow-xl transform active:scale-95">
                                    Add to cart
                                </button>
                            </div>

                            {/* Gift Wrap Checkbox */}
                            <div className="flex items-center gap-2 pt-2">
                                <input
                                    type="checkbox"
                                    id="gift-wrap"
                                    checked={isGiftWrapped}
                                    onChange={(e) => setIsGiftWrapped(e.target.checked)}
                                    className="w-4 h-4 rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                                />
                                <label htmlFor="gift-wrap" className="text-sm font-semibold cursor-pointer select-none">
                                    Gift wrap this item for <span className="font-bold">Rs. 150.00?</span>
                                </label>
                            </div>

                            {/* Social Share */}
                            <div className="flex items-center gap-4 pt-4 border-t border-gray-100">
                                <span className="text-xs font-bold uppercase text-gray-500">Share:</span>
                                <div className="flex gap-3 text-gray-400">
                                    <Facebook className="w-4 h-4 hover:text-blue-600 cursor-pointer transition-colors" />
                                    <Twitter className="w-4 h-4 hover:text-blue-400 cursor-pointer transition-colors" />
                                    <Linkedin className="w-4 h-4 hover:text-blue-700 cursor-pointer transition-colors" />
                                    <Mail className="w-4 h-4 hover:text-red-500 cursor-pointer transition-colors" />
                                </div>
                            </div>

                        </div>
                    </div>
                </div>

                {/* Bottom Tabs / Description */}
                <div className="mt-20 border-t border-gray-200 pt-10">
                    <div className="flex gap-8 border-b border-gray-200 mb-8">
                        <button className="pb-4 border-b-2 border-blue-600 font-bold text-blue-600 text-sm uppercase tracking-wide">
                            Description
                        </button>
                        <button className="pb-4 border-b-2 border-transparent font-bold text-gray-400 hover:text-gray-600 text-sm uppercase tracking-wide">
                            Additional Information
                        </button>
                        <button className="pb-4 border-b-2 border-transparent font-bold text-gray-400 hover:text-gray-600 text-sm uppercase tracking-wide">
                            Reviews (0)
                        </button>
                    </div>

                    <div className="prose prose-blue max-w-none text-gray-600 text-sm leading-relaxed">
                        <h3 className="text-lg font-bold text-gray-900 mb-4">White Mug Printing with your photos and wordings</h3>
                        <p className="mb-4">A beautiful way to gift with personal touch. A customized white mug is a great way to surprise your loved ones on a special occasion of their lives.</p>
                        <ul className="list-disc pl-5 space-y-1 mb-6">
                            <li>Customize with your photos, name and wishes</li>
                            <li>250ml ceramic coffee mug</li>
                            <li>Full colour print</li>
                            <li>Microwave safe</li>
                        </ul>
                        <p className="font-semibold">Order for personalized mug printing online with us.</p>
                    </div>
                </div>
            </div>
        </div>
    );
}