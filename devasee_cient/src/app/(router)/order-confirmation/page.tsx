"use client";

import Link from "next/link";

import { useEffect, useRef } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCheckCircle, faHome, faShoppingBag } from "@fortawesome/free-solid-svg-icons";
import gsap from "gsap";

export default function OrderConfirmation() {
    const containerRef = useRef<HTMLDivElement>(null);
    const iconRef = useRef<HTMLDivElement>(null);
    const headingRef = useRef<HTMLHeadingElement>(null);
    const textRef = useRef<HTMLParagraphElement>(null);
    const buttonsRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const tl = gsap.timeline();

        // Initial setup
        gsap.set([iconRef.current, headingRef.current, textRef.current, buttonsRef.current], {
            opacity: 0,
            y: 20
        });

        // Animation sequence
        tl.to(containerRef.current, {
            opacity: 1,
            duration: 0.5,
            ease: "power2.out"
        })
            .fromTo(iconRef.current,
                { scale: 0.5, opacity: 0 },
                { scale: 1, opacity: 1, duration: 0.6, ease: "back.out(3)" }
            )
            .fromTo(headingRef.current,
                { y: 30, opacity: 0 },
                { y: 0, opacity: 1, duration: 0.5, ease: "power2.out" },
                "-=0.3"
            )
            .fromTo(textRef.current,
                { y: 20, opacity: 0 },
                { y: 0, opacity: 1, duration: 0.5, ease: "power2.out" },
                "-=0.2"
            )
            .fromTo(buttonsRef.current,
                { y: 20, opacity: 0 },
                { y: 0, opacity: 1, duration: 0.5, ease: "power2.out" },
                "-=0.2"
            );

        // Floating animation for the check icon
        gsap.to(iconRef.current, {
            y: -10,
            duration: 2,
            repeat: -1,
            yoyo: true,
            ease: "sine.inOut",
            delay: 1
        });

        return () => {
            tl.kill();
        };
    }, []);

    return (
        <div
            ref={containerRef}
            className="w-screen min-h-screen mt-24 lg:mt-30 bg-blue-50 flex flex-col items-center justify-center px-4 py-12 opacity-0"
        >
            <div className="max-w-2xl w-full bg-white rounded-xl shadow-lg p-8 md:p-12 text-center">
                {/* Animated Check Icon */}
                <div
                    ref={iconRef}
                    className="mx-auto mb-6 text-green-500"
                >
                    <FontAwesomeIcon
                        icon={faCheckCircle}
                        className="text-6xl md:text-7xl"
                    />
                </div>

                {/* Animated Heading */}
                <h1
                    ref={headingRef}
                    className="text-3xl md:text-4xl font-bold text-gray-800 mb-4"
                >
                    Order Confirmed!
                </h1>

                {/* Animated Text */}
                <p
                    ref={textRef}
                    className="text-gray-600 text-lg md:text-xl mb-8"
                >
                    Thank you for your purchase! Your order has been received and is being processed.
                    You will receive a confirmation email with your order details shortly.
                </p>

                {/* Order Details (optional) */}
                <div className="mb-8 p-6 bg-blue-50 rounded-lg text-left">
                    <h3 className="font-semibold text-gray-700 mb-3">Order Summary</h3>
                    <div className="space-y-2">
                        <p className="text-gray-600">
                            <span className="font-medium">Order Number:</span> #{Math.floor(Math.random() * 1000000)}
                        </p>
                        <p className="text-gray-600">
                            <span className="font-medium">Estimated Delivery:</span> {new Date(Date.now() + 5 * 24 * 60 * 60 * 1000).toLocaleDateString()}
                        </p>
                    </div>
                </div>

                {/* Animated Buttons */}
                <div
                    ref={buttonsRef}
                    className="flex flex-col sm:flex-row justify-center gap-4"
                >
                    <Link
                        href="/"
                        className="flex items-center justify-center gap-2 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition-colors duration-300"
                    >
                        <FontAwesomeIcon icon={faHome} />
                        Back to Home
                    </Link>
                    <Link
                        href="/products"
                        className="flex items-center justify-center gap-2 bg-white border border-blue-600 text-blue-600 hover:bg-blue-50 font-semibold py-3 px-6 rounded-lg transition-colors duration-300"
                    >
                        <FontAwesomeIcon icon={faShoppingBag} />
                        Continue Shopping
                    </Link>
                </div>
            </div>


        </div>
    );
}