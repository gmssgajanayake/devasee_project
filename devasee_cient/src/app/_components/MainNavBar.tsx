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
import {faListCheck} from "@fortawesome/free-solid-svg-icons";


export default function MainNavBar() {
    const textRef = useRef<HTMLHeadingElement>(null);
    const menuRef = useRef<HTMLDivElement>(null);
    const navRef = useRef<HTMLElement>(null);
    const hoverRef = useRef<HTMLDivElement>(null);
    const dropdownRef = useRef<HTMLDivElement>(null);
    const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
    const [isScrolled, setIsScrolled] = useState(false);
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);

    const {cartItems, updateItemQuantity, removeFromCart} = useCart();

    // Track how many hover areas are currently hovered (clipboard icon or dropdown)
    const hoverCount = useRef(0);

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

    // Animate dropdown visibility based on isDropdownOpen state
    useEffect(() => {
        if (!dropdownRef.current) return;
        const dropdown = dropdownRef.current;

        if (isDropdownOpen) {
            gsap.to(dropdown, {
                opacity: 1,
                y: 0,
                pointerEvents: "auto",
                display: "block",
                duration: 0.3,
                ease: "power3.out",
            });
        } else {
            gsap.to(dropdown, {
                opacity: 0,
                y: -10,
                pointerEvents: "none",
                display: "none",
                duration: 0.3,
                ease: "power3.in",
            });
        }
    }, [isDropdownOpen]);

    // Called when mouse enters either icon area or dropdown
    const handleMouseEnter = () => {
        hoverCount.current += 1;
        setIsDropdownOpen(true);
    };

    // Called when mouse leaves either icon area or dropdown
    const handleMouseLeave = () => {
        hoverCount.current -= 1;
        setTimeout(() => {
            if (hoverCount.current <= 0) {
                setIsDropdownOpen(false);
                hoverCount.current = 0; // reset just in case
            }
        }, 150);
    };


    const linkStyle =
        "text-sm font-semibold overflow-hidden tracking-wide text-gray-600 dark:text-gray-800 transition-colors duration-200 hover:text-[#0000FF] relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full";

    return (
        <div className="w-screen h-auto bg-white relative">
            {/* Desktop menu bar */}
            <nav
                ref={navRef}
                className={`w-full fixed top-9 z-50 transition-all duration-500 px-5 lg:px-8 py-3 flex items-center justify-between ${
                    isScrolled ? "bg-white/70 backdrop-blur-md shadow-md" : "bg-white"
                }`}
            >
                <Link href="/">
                    <Image
                        src={logo}
                        alt={"logo"}
                        priority
                        width={200}
                        height={300}
                        className="h-10 w-10 lg:w-16 lg:h-16"
                    />
                </Link>
                <div className="items-center justify-center gap-4 hidden lg:flex">
                    <Link href="/" className={linkStyle}>
                        HOME
                    </Link>
                    <span>|</span>
                    <Link href="/about" className={linkStyle}>
                        ABOUT US
                    </Link>
                    <span>|</span>
                    <Link href="/products" className={linkStyle}>
                        BOOKS
                    </Link>
                    <span>|</span>
                    <Link href="/services" className={linkStyle}>
                        PRINTING SERVICES
                    </Link>
                    <span>|</span>
                    <Link href="/contact" className={linkStyle}>
                        CONTACT US
                    </Link>
                </div>
                <div className="hidden lg:flex items-center justify-center gap-4">
                    <SignedOut>
                        <Link
                            href="/sign-in"
                            className="text-lg font-semibold text-gray-600 dark:text-gray-800 hover:text-[#0000FF]"
                        >
                            <FontAwesomeIcon
                                className="w-3.5 h-3.5 cursor-pointer text-gray-600"
                                icon={faUser}
                            />
                        </Link>
                    </SignedOut>
                    <SignedIn>
                        <UserButton/>
                        <Link href={"/my-orders"}>
                            <FontAwesomeIcon icon={faListCheck}/>
                        </Link>
                    </SignedIn>

                    <span>|</span>
                    <div
                        className="relative"
                        ref={hoverRef}
                        onMouseEnter={handleMouseEnter}
                        onMouseLeave={handleMouseLeave}
                    >
                        <Link
                            href="/products/checkout"
                            className="flex items-center gap-2 text-gray-600"
                        >
                            <FontAwesomeIcon icon={faClipboard} className="w-5 h-5"/>
                            {cartItems.length > 0 && (
                                <span
                                    className="absolute -top-2 -right-2.5 bg-blue-600 text-white text-[10px] font-bold px-1 py-0.5 rounded-full leading-none">
                  {cartItems.length}
                </span>
                            )}
                        </Link>

                        {/* Hoverable Cart */}
                        {cartItems.length > 0 && (
                            <div
                                ref={dropdownRef}
                                onMouseEnter={handleMouseEnter}
                                onMouseLeave={handleMouseLeave}
                                className="absolute  right-0 mt-2 w-86 bg-white shadow-xl rounded-lg p-4 z-50"
                                style={{opacity: 0, pointerEvents: "none", display: "none"}}
                            >
                                <h4 className="text-sm font-semibold mb-2 text-gray-700">
                                    Your Cart ({cartItems.length})
                                </h4>
                                <div className="">
                                    <div className="space-y-2 max-h-64  overflow-y-auto pr-1">
                                        {cartItems.map((item) => (
                                            <div key={item.id}
                                                 className="text-sm justify-between flex items-center gap-4 border-b border-gray-400/20 pb-2">
                                                <div className="flex items-center gap-2">
                                                    <Image src={item.image}  width={200}
                                                           height={300} className={'w-10 h-auto'}
                                                           alt={"Item image"}/>
                                                    <div className="">
                                                        <p className="font-medium">{item.title}</p>
                                                        <p className="text-gray-500 text-xs mb-1">
                                                            {new Intl.NumberFormat("en-LK", {
                                                                style: "currency",
                                                                currency: "LKR",
                                                                minimumFractionDigits: 2,
                                                            }).format(item.price)}
                                                        </p>

                                                    </div>
                                                    <div className="flex items-center bg-gray-100  gap-1 ">
                                                        <button
                                                            onClick={() =>
                                                                item.quantity > 1 &&
                                                                updateItemQuantity(item.id, item.quantity - 1)
                                                            }
                                                            className="px-2 py-3  bg-gray-300 text-white cursor-pointer  hover:bg-gray-400"
                                                        >
                                                            −
                                                        </button>
                                                        <span
                                                            className="w-4 text-center text-gray-600">{item.quantity}</span>
                                                        <button
                                                            onClick={() =>
                                                                item.quantity < item.stock &&
                                                                updateItemQuantity(item.id, item.quantity + 1)
                                                            }
                                                            className="px-2 py-3 bg-gray-300 text-white cursor-pointer hover:bg-gray-400"
                                                        >
                                                            +
                                                        </button>
                                                    </div>
                                                </div>

                                                <div className="flex items-center justify-between gap-2 text-xs">
                                                    <button
                                                        onClick={() => removeFromCart(item.id)}
                                                        className="text-white bg-gray-600 border border-gray-600 px-3 py-3 text-[10px] cursor-pointer tracking-widest
               hover:text-gray-600 hover:bg-white transition-all duration-300 ease-in-out transform hover:scale-105"
                                                    >
                                                        REMOVE
                                                    </button>
                                                </div>

                                            </div>
                                        ))}
                                    </div>
                                </div>

                                <Link
                                    href="/products/checkout"
                                    className="group relative mt-4 block text-center text-sm font-semibold text-[#0000ff] transition-all duration-300 hover:text-blue-600"
                                >
                                    <span className="relative z-10">Go to Checkout</span>
                                    <span
                                        className="ml-1 inline-block transition-transform duration-300 group-hover:translate-x-1">
    →
  </span>
                                    <span
                                        className="absolute left-1/2 bottom-0 h-[1.5px] w-0 -translate-x-1/2 transform bg-[#0000ff] transition-all duration-300 group-hover:w-[45%]"
                                    />
                                </Link>


                            </div>
                        )}
                    </div>
                </div>
                {/* Mobile menu open and close */}
                <div className="flex lg:hidden items-center">
                    <div
                        className={
                            "text-gray-600 cursor-pointer " + (isMobileMenuOpen ? "hidden" : "block")
                        }
                    >
                        <AlignJustify
                            size={28}
                            className={
                                "text-gray-600 cursor-pointer " + (isMobileMenuOpen ? "hidden" : "block")
                            }
                            onClick={toggleMobileMenu}
                        />
                        <div
                            className={
                                "relative flex items-center justify-center" + (isMobileMenuOpen ? "hidden" : "block")
                            }
                        >
                            {cartItems.length > 0 && (
                                <div className="w-2 h-2 absolute bottom-6 left-7 rounded-full bg-blue-600"></div>
                            )}
                        </div>
                    </div>

                    <X
                        size={28}
                        className={
                            "text-gray-600 cursor-pointer ml-4 " + (isMobileMenuOpen ? "block" : "hidden")
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
                                <Link
                                    href="/sign-in"
                                    onClick={toggleMobileMenu}
                                    className="text-lg font-semibold text-white dark:text-white hover:text-[#0000FF]"
                                >
                                    <FontAwesomeIcon
                                        className="w-3.5 h-3.5 cursor-pointer text-white"
                                        icon={faUser}
                                    />
                                </Link>
                            </SignedOut>
                            <SignedIn>
                                <UserButton/>
                                <Link  onClick={toggleMobileMenu} href={"/my-orders"} >
                                    <FontAwesomeIcon className={"text-white mt-2"} icon={faListCheck}/>
                                </Link>
                            </SignedIn>

                            <span className={"text-white font-light"}>|</span>

                            <div className="relative">
                                <Link
                                    onClick={toggleMobileMenu}
                                    href="/products/checkout"
                                    className="flex items-center gap-2 text-white transition-colors"
                                >
                                    <FontAwesomeIcon className="w-5 h-5 cursor-pointer" icon={faClipboard}/>

                                    {cartItems.length > 0 && (
                                        <span
                                            className="absolute -top-2 -right-2.5 bg-blue-50 text-blue-600 text-[10px] font-bold px-1 py-0.5 rounded-full leading-none">
                      {cartItems.length}
                    </span>
                                    )}
                                </Link>
                            </div>
                        </div>
                    </div>
                    <div
                        className="relative z-50 flex flex-col justify-center items-center gap-8 px-4 pb-12 h-full overflow-y-auto">
                        <Link
                            href="/"
                            onClick={toggleMobileMenu}
                            className="text-gray-600 text-lg font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]"
                        >
                            HOME
                        </Link>
                        <Link
                            href="/about"
                            onClick={toggleMobileMenu}
                            className="text-gray-600 text-lg font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]"
                        >
                            ABOUT US
                        </Link>
                        <Link
                            href="/products"
                            onClick={toggleMobileMenu}
                            className="text-gray-600 text-lg font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]"
                        >
                            BOOKS
                        </Link>
                        <Link
                            href="/services"
                            onClick={toggleMobileMenu}
                            className="text-gray-600 text-lg font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]"
                        >
                            PRINTING SERVICES
                        </Link>
                        <Link
                            href="/contact"
                            onClick={toggleMobileMenu}
                            className="text-gray-600 text-lg font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]"
                        >
                            CONTACT US
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}