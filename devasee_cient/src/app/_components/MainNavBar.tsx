"use client";

import logo from "@/assets/devasee logo.png";
import Image from "next/image";
import Link from "next/link";
import {faUser, faClipboard} from "@fortawesome/free-regular-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {AlignJustify, X} from "lucide-react";
import {useEffect, useRef, useState} from "react";
import gsap from "gsap";
import {SignedIn, SignedOut, UserButton} from "@clerk/nextjs";
import {useCart} from "@/app/context/CartContext";


export default function MainNavBar() {
    const textRef = useRef<HTMLHeadingElement>(null);
    const menuRef = useRef<HTMLDivElement>(null);
    const navRef = useRef<HTMLElement>(null);
    const hoverRef = useRef<HTMLDivElement>(null);
    const dropdownRef = useRef<HTMLDivElement>(null);
    const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
    const [isScrolled, setIsScrolled] = useState(false);
    const {cartItems, updateItemQuantity, removeFromCart} = useCart();

    useEffect(() => {
        if (textRef.current) {
            gsap.fromTo(
                textRef.current,
                {y: -50, opacity: 0},
                {y: 0, opacity: 1, duration: 1, ease: "power3.out"}
            );
        }

        const handleScroll = () => setIsScrolled(window.scrollY > 10);
        window.addEventListener("scroll", handleScroll);
        return () => window.removeEventListener("scroll", handleScroll);
    }, []);

    useEffect(() => {
        if (!dropdownRef.current || !hoverRef.current) return;

        const tl = gsap.timeline({paused: true});
        tl.fromTo(
            dropdownRef.current,
            {opacity: 0, y: -10},
            {opacity: 1, y: 0, duration: 0.3, ease: "power3.out"}
        );

        const handleMouseEnter = () => tl.play();
        const handleMouseLeave = () => tl.reverse();

        const hoverElem = hoverRef.current;
        hoverElem.addEventListener("mouseenter", handleMouseEnter);
        hoverElem.addEventListener("mouseleave", handleMouseLeave);

        return () => {
            hoverElem.removeEventListener("mouseenter", handleMouseEnter);
            hoverElem.removeEventListener("mouseleave", handleMouseLeave);
        };
    }, []);


    // Mobile menu toggle
    const toggleMobileMenu = () => {
        const body = document.body;
        if (!menuRef.current) return;

        if (!isMobileMenuOpen) {
            menuRef.current.style.display = "block";
            gsap.fromTo(
                menuRef.current,
                {y: -30, opacity: 0},
                {y: 0, opacity: 1, duration: 0.3, ease: "power3.out"}
            );
            setIsMobileMenuOpen(true);
            body.classList.add("overflow-hidden");
        } else {
            gsap.to(menuRef.current, {
                y: -30,
                opacity: 0,
                duration: 0.2,
                ease: "power2.in",
                onComplete: () => {
                    if (menuRef.current) {
                        menuRef.current.style.display = "none";
                    }
                    setIsMobileMenuOpen(false);
                    body.classList.remove("overflow-hidden");
                },
            });
        }
    };

    // Dropdown animation for cart items
    useEffect(() => {
        if (!dropdownRef.current || !hoverRef.current) return;

        const dropdown = dropdownRef.current;
        const hoverArea = hoverRef.current;

        gsap.set(dropdown, {
            opacity: 0,
            y: -10,
            pointerEvents: "none",
            display: "none"
        });

        const tl = gsap.timeline({ paused: true });
        tl.to(dropdown, {
            opacity: 1,
            y: 0,
            pointerEvents: "auto",
            display: "block",
            duration: 0.3,
            ease: "power3.out"
        });

        const handleEnter = () => tl.play();
        const handleLeave = () => tl.reverse();

        hoverArea.addEventListener("mouseenter", handleEnter);
        hoverArea.addEventListener("mouseleave", handleLeave);

        return () => {
            hoverArea.removeEventListener("mouseenter", handleEnter);
            hoverArea.removeEventListener("mouseleave", handleLeave);
        };
    }, [cartItems]);


    const linkStyle =
        "text-sm font-semibold overflow-hidden tracking-wide text-gray-600 dark:text-gray-800 transition-colors duration-200 hover:text-[#0000FF] relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full";

    return (
        <div className="w-screen h-auto bg-white relative">
            {/* Desktop menu bar */}
            <nav
                ref={navRef}
                className={`w-full fixed top-9 z-50 transition-all duration-500 px-5 lg:px-8 py-3 flex items-center justify-between ${
                    isScrolled
                        ? "bg-white/70 backdrop-blur-md shadow-md"
                        : "bg-white"
                }`}
            >
                <Link href="/">
                    <Image
                        src={logo}
                        alt={"logo"}
                        priority
                        className="h-10 w-10 lg:w-16 lg:h-16"
                    />
                </Link>
                <div className="items-center justify-center gap-4 hidden lg:flex">
                    <Link href="/" className={linkStyle}>HOME</Link>
                    <span>|</span>
                    <Link href="/about" className={linkStyle}>ABOUT US</Link>
                    <span>|</span>
                    <Link href="/products" className={linkStyle}>BOOKS</Link>
                    <span>|</span>
                    <Link href="/services" className={linkStyle}>PRINTING SERVICES</Link>
                    <span>|</span>
                    <Link href="/contact" className={linkStyle}>CONTACT US</Link>
                </div>
                <div className="hidden lg:flex items-center justify-center gap-4">
                    <SignedOut>
                        <Link href="/sign-in"
                              className="text-lg font-semibold text-gray-600 dark:text-gray-800 hover:text-[#0000FF]">
                            <FontAwesomeIcon className="w-3.5 h-3.5 cursor-pointer text-gray-600" icon={faUser}/>
                        </Link>
                    </SignedOut>
                    <SignedIn>
                        <UserButton/>
                    </SignedIn>

                    <span>|</span>
                    <div className="relative" ref={hoverRef}>
                        <Link
                            href="/products/checkout"
                            className="flex items-center gap-2 text-gray-600"
                        >
                            <FontAwesomeIcon icon={faClipboard} className="w-5 h-5" />
                            {cartItems.length > 0 && (
                                <span className="absolute -top-2 -right-2.5 bg-blue-600 text-white text-[10px] font-bold px-1 py-0.5 rounded-full leading-none">
                  {cartItems.length}
                </span>
                            )}
                        </Link>

                        {/* Hoverable Cart */}
                        {cartItems.length > 0 && (
                            <div
                                ref={dropdownRef}
                                className="absolute right-0 mt-2 w-72 bg-white shadow-xl rounded-lg p-4 z-50"
                                style={{ display: 'none' }}
                            >
                                <h4 className="text-sm font-semibold mb-2 text-gray-700">
                                    Your Cart ({cartItems.length})
                                </h4>
                                <div className="space-y-2 max-h-64 overflow-y-auto pr-1">
                                    {cartItems.map((item) => (
                                        <div key={item.id} className="text-sm border-b pb-2">
                                            <p className="font-medium">{item.title}</p>
                                            <p className="text-gray-500 text-xs mb-1">Rs. {item.price}</p>
                                            <div className="flex items-center justify-between gap-2 text-xs">
                                                <div className="flex items-center gap-1">
                                                    <button
                                                        onClick={() =>
                                                            item.quantity > 1 &&
                                                            updateItemQuantity(item.id, item.quantity - 1)
                                                        }
                                                        className="px-1.5 bg-gray-200 rounded hover:bg-gray-300"
                                                    >
                                                        −
                                                    </button>
                                                    <span className="w-5 text-center">{item.quantity}</span>
                                                    <button
                                                        onClick={() =>
                                                            item.quantity < item.stock &&
                                                            updateItemQuantity(item.id, item.quantity + 1)
                                                        }
                                                        className="px-1.5 bg-gray-200 rounded hover:bg-gray-300"
                                                    >
                                                        +
                                                    </button>
                                                </div>
                                                <button
                                                    onClick={() => removeFromCart(item.id)}
                                                    className="text-red-500 hover:underline"
                                                >
                                                    Remove
                                                </button>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                                <Link
                                    href="/products/checkout"
                                    className="block text-center mt-4 text-blue-600 text-sm font-medium hover:underline"
                                >
                                    Go to Checkout →
                                </Link>
                            </div>
                        )}
                    </div>
                </div>
                {/* Mobile menu open and close */}
                <div className="flex lg:hidden items-center">

                    <div className={
                        "text-gray-600 cursor-pointer " +
                        (isMobileMenuOpen ? "hidden" : "block")
                    }>
                        <AlignJustify
                            size={28}
                            className={
                                "text-gray-600 cursor-pointer " +
                                (isMobileMenuOpen ? "hidden" : "block")
                            }
                            onClick={toggleMobileMenu}
                        />
                        <div className={"relative flex items-center justify-center" +
                            (isMobileMenuOpen ? "hidden" : "block")}>
                            {cartItems.length > 0 && (
                                <div className={"w-2 h-2 absolute bottom-6 left-7 rounded-full bg-blue-600"}></div>
                            )}
                        </div>
                    </div>


                    <X
                        size={28}
                        className={
                            "text-gray-600 cursor-pointer ml-4 " +
                            (isMobileMenuOpen ? "block" : "hidden")
                        }
                        onClick={toggleMobileMenu}
                    />
                </div>
            </nav>

            {/* Mobile menu bar */}
            <div
                ref={menuRef}
                className={`lg:hidden fixed bg-white/50 backdrop-blur-md top-[72px] left-0 right-0 z-40 ${
                    isMobileMenuOpen ? "block" : "hidden"
                }`}
                style={{height: "calc(100vh - 72px)"}}

            >
                <div className="w-full relative h-full flex flex-col justify-between items-center">
                    <div className="h-20 w-full flex mt-6 px-6 justify-end items-center  bg-[#0000ff]">
                        <div className="flex items-center justify-center gap-3">
                            <SignedOut>
                                <Link href="/sign-in" onClick={toggleMobileMenu}
                                      className="text-lg font-semibold text-white dark:text-white hover:text-[#0000FF]">
                                    <FontAwesomeIcon className="w-3.5 h-3.5 cursor-pointer text-white" icon={faUser}/>
                                </Link>
                            </SignedOut>
                            <SignedIn>
                                <UserButton/>
                            </SignedIn>

                            <span className={"text-white font-light"}>|</span>
                            <div className="relative">
                                <Link onClick={toggleMobileMenu}
                                      href="/products/checkout"
                                      className="flex items-center gap-2 text-white transition-colors"
                                >

                                    <FontAwesomeIcon
                                        className="w-5 h-5 cursor-pointer"
                                        icon={faClipboard}
                                    />


                                    {cartItems.length > 0 && (
                                        <span
                                            className="absolute -top-2 -right-2.5 bg-blue-100 text-blue-600 text-[10px] font-bold px-1 py-0.5 rounded-full leading-none">
        {cartItems.length}
      </span>
                                    )}
                                </Link>
                            </div>

                        </div>
                    </div>
                    <div
                        className="relative z-50 flex flex-col justify-center items-center gap-8 px-4 pb-12 h-full overflow-y-auto">
                        <Link href="/" onClick={toggleMobileMenu}
                              className="text-gray-600 text-xl font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]">
                            HOME
                        </Link>
                        <Link href="/about" onClick={toggleMobileMenu}
                              className="text-gray-600 text-xl font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]">
                            ABOUT US
                        </Link>
                        <Link href="/products" onClick={toggleMobileMenu}
                              className="text-gray-600 text-xl font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]">
                            BOOKS
                        </Link>
                        <Link href="/services" onClick={toggleMobileMenu}
                              className="text-gray-600 text-xl font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]">
                            PRINTING SERVICES
                        </Link>
                        <Link href="/contact" onClick={toggleMobileMenu}
                              className="text-gray-600 text-xl font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]">
                            CONTACT US
                        </Link>
                    </div>
                </div>

            </div>
        </div>
    );
}
