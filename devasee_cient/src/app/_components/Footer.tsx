// src/components/Footer.js
import Link from "next/link";
import Image from "next/image";
import logo from "@/assets/devasee.png";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
    faFacebook,
    faLinkedinIn,
    faXTwitter,
    faYoutube
} from "@fortawesome/free-brands-svg-icons";

export default function Footer() {
    return (
        <footer className="bg-white w-full">
            {/* Main Footer Content */}
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 py-12">
                    {/* Logo and Description */}
                    <div className="col-span-1 md:col-span-2 lg:col-span-1">
                        <div className="flex flex-col gap-6">
                            <Link href="/" className="w-fit">
                                <div className="relative h-16 w-16">
                                    <Image
                                        src={logo}
                                        alt="Devasee Logo"
                                        fill
                                        className="object-contain"
                                    />
                                </div>
                            </Link>

                            <p className="text-indigo-900 text-base sm:text-lg">
                                Devasee — Trusted bookshop and printing services for every need.
                            </p>

                            <div className="flex gap-4">
                                <a href="#" className="text-blue-600 hover:text-blue-800 transition-colors">
                                    <FontAwesomeIcon icon={faFacebook} className="text-xl md:text-2xl" />
                                </a>
                                <a href="#" className="text-blue-600 hover:text-blue-800 transition-colors">
                                    <FontAwesomeIcon icon={faLinkedinIn} className="text-xl md:text-2xl" />
                                </a>
                                <a href="#" className="text-blue-600 hover:text-blue-800 transition-colors">
                                    <FontAwesomeIcon icon={faXTwitter} className="text-xl md:text-2xl" />
                                </a>
                                <a href="#" className="text-blue-600 hover:text-blue-800 transition-colors">
                                    <FontAwesomeIcon icon={faYoutube} className="text-xl md:text-2xl" />
                                </a>
                            </div>
                        </div>
                    </div>

                    {/* Pages Links */}
                    <div>
                        <h3 className="font-bold text-indigo-900 text-lg md:text-xl mb-4">Pages</h3>
                        <ul className="space-y-2">
                            <li>
                                <Link href="/" className="text-indigo-900 text-base hover:underline">Home</Link>
                            </li>
                            <li>
                                <Link href="/about" className="text-indigo-900 text-base hover:underline">About Us</Link>
                            </li>
                            <li>
                                <Link href="/products" className="text-indigo-900 text-base hover:underline">Products</Link>
                            </li>
                            <li>
                                <Link href="/services" className="text-indigo-900 text-base hover:underline">Printing Services</Link>
                            </li>
                            <li>
                                <Link href="/contact" className="text-indigo-900 text-base hover:underline">Contact Us</Link>
                            </li>
                        </ul>
                    </div>

                    {/* Important Links */}
                    <div>
                        <h3 className="font-bold text-indigo-900 text-lg md:text-xl mb-4">Important Links</h3>
                        <ul className="space-y-2">
                            <li>
                                <Link href="/privacy" className="text-indigo-900 text-base hover:underline">Privacy Policy</Link>
                            </li>
                            <li>
                                <Link href="/faq" className="text-indigo-900 text-base hover:underline">FAQs</Link>
                            </li>
                            <li>
                                <Link href="/terms" className="text-indigo-900 text-base hover:underline">Terms of Service</Link>
                            </li>
                        </ul>

                        {/* Decorative dots - visible on medium screens and up */}
                        <div className="hidden md:block mt-8">
                            <div className="flex gap-8">
                                <div className="flex flex-col gap-4">
                                    <div className="flex gap-8">
                                        <div className="w-3 h-3 bg-[#f8bfb8] "></div>
                                        <div className="w-3 h-3 bg-[#fae0b1] "></div>
                                    </div>
                                    <div className="flex gap-8 ml-8">
                                        <div className="w-3 h-3 bg-[#fae0b1] "></div>
                                        <div className="w-3 h-3 bg-[#f8bfb8] "></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Decorative dots - only for large screens */}
                    <div className="hidden lg:flex justify-end pt-20">
                        <div className="flex flex-col gap-4">
                            <div className="flex gap-8">
                                <div className="w-3 h-3 bg-gray-400 "></div>
                                <div className="w-3 h-3 bg-indigo-200 "></div>
                            </div>
                            <div className="flex gap-8 ml-8">
                                <div className="w-3 h-3 bg-indigo-200 "></div>
                                <div className="w-3 h-3 bg-gray-400 "></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Copyright Section */}
            <div className="bg-[#0000ff] w-full py-3">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex flex-col md:flex-row justify-between items-center gap-2">
                        <p className="text-white text-xs sm:text-sm text-center md:text-left">
                            © 2025 DEVASEE. All Rights Reserved.
                        </p>
                        <div className="flex gap-4">
                            <Link href="/privacy" className="text-white text-xs sm:text-sm font-bold hover:underline">Privacy</Link>
                            <Link href="/terms" className="text-white text-xs sm:text-sm font-bold hover:underline">Terms of Service</Link>
                        </div>
                    </div>
                </div>
            </div>
        </footer>
    );
}