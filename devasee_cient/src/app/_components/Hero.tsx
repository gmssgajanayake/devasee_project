"use client";

import { useEffect, useRef } from "react";
import gsap from "gsap";

export default function Hero() {
    const textRef = useRef<HTMLHeadingElement>(null);

    useEffect(() => {
        if (textRef.current) {
            gsap.fromTo(
                textRef.current,
                { y: -50, opacity: 0 },
                { y: 0, opacity: 1, duration: 1, ease: "power3.out" }
            );
        }
    }, []);

    return (
        <section ref={textRef} className="p-10 text-center w-full h-full bg-blue-50">
            <h1  className="text-4xl font-bold">
                Welcome to Devasee Bookshop
            </h1>
        </section>
    );
}
