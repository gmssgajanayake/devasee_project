"use client";

import logo from "@/assets/devasee logo.png";
import Image from "next/image";
import Link from "next/link";
import { faUser, faClipboard, faHeart } from "@fortawesome/free-regular-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { AlignJustify, X } from "lucide-react";
import { useEffect, useRef, useState } from "react";
import gsap from "gsap";
import {SignedIn, SignedOut,  UserButton} from "@clerk/nextjs";

export default function MainNavBar() {
    const textRef = useRef<HTMLHeadingElement>(null);
    const menuRef = useRef<HTMLDivElement>(null);
    const navRef = useRef<HTMLElement>(null);
    const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
    const [isScrolled, setIsScrolled] = useState(false);

    useEffect(() => {
        if (textRef.current) {
            gsap.fromTo(
                textRef.current,
                { y: -50, opacity: 0 },
                { y: 0, opacity: 1, duration: 1, ease: "power3.out" }
            );
        }

        const handleScroll = () => {
            setIsScrolled(window.scrollY > 10);
        };

        window.addEventListener("scroll", handleScroll);
        return () => window.removeEventListener("scroll", handleScroll);
    }, []);

    const toggleMobileMenu = () => {
        const body = document.body;

        if (menuRef.current) {
            if (!isMobileMenuOpen) {
                menuRef.current.style.display = "block";
                gsap.fromTo(
                    menuRef.current,
                    { y: -30, opacity: 0 },
                    { y: 0, opacity: 1, duration: 0.3, ease: "power3.out" }
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
        }
    };

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
                       <Link href="/sign-in" className="text-sm font-semibold text-gray-600 dark:text-gray-800 hover:text-[#0000FF]">
                            <FontAwesomeIcon className="w-3.5 h-3.5 cursor-pointer text-gray-600" icon={faUser} />
                       </Link>
                    </SignedOut>
                    <SignedIn>
                        <UserButton />
                    </SignedIn>
                    <span>|</span>
                    <FontAwesomeIcon className="w-3 h-3 cursor-pointer text-gray-600" icon={faClipboard} />
                    <span>|</span>
                    <FontAwesomeIcon className="w-4 h-4 cursor-pointer text-gray-600" icon={faHeart} />

                </div>
                {/* Mobile menu open and close */}
                <div className="flex lg:hidden items-center">
                    <AlignJustify
                        size={28}
                        className={
                            "text-gray-600 cursor-pointer " +
                            (isMobileMenuOpen ? "hidden" : "block")
                        }
                        onClick={toggleMobileMenu}
                    />
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
                className={`lg:hidden fixed bg-white/30 backdrop-blur-md top-[72px] left-0 right-0 z-40 ${
                    isMobileMenuOpen ? "block" : "hidden"
                }`}
                style={{ height: "calc(100vh - 72px)" }}
            >
                <div className="relative z-50 flex flex-col justify-center items-center gap-8 p-4 h-full overflow-y-auto">
                    <Link href="/" onClick={toggleMobileMenu} className="text-gray-600 text-xl font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]">
                        HOME
                    </Link>
                    <Link href="/about" onClick={toggleMobileMenu} className="text-gray-600 text-xl font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]">
                        ABOUT US
                    </Link>
                    <Link href="/products" onClick={toggleMobileMenu} className="text-gray-600 text-xl font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]">
                        BOOKS
                    </Link>
                    <Link href="/services" onClick={toggleMobileMenu} className="text-gray-600 text-xl font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]">
                        PRINTING SERVICES
                    </Link>
                    <Link href="/contact" onClick={toggleMobileMenu} className="text-gray-600 text-xl font-semibold relative after:absolute after:left-0 after:bottom-0 after:w-0 after:h-[2px] after:bg-[#0000FF] after:transition-all after:duration-300 hover:after:w-full hover:text-[#0000FF]">
                        CONTACT US
                    </Link>
                </div>
            </div>
        </div>
    );
}
