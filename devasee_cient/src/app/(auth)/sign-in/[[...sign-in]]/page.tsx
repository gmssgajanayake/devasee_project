"use client";

import { SignIn } from "@clerk/nextjs";
import loginImage from "@/assets/devasee login page.png";
import Image from "next/image";
import { useEffect, useRef } from "react";
import gsap from "gsap";

export default function Page() {
    const leftRef = useRef<HTMLDivElement>(null);
    const rightRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const ctx = gsap.context(() => {
            gsap.fromTo(
                leftRef.current,
                { opacity: 0, x: -100 },
                { opacity: 1, x: 0, duration: 1.5, ease: "power3.out", delay: 0.2 }
            );

            gsap.fromTo(
                rightRef.current,
                { opacity: 0, x: 100 },
                { opacity: 1, x: 0, duration: 1.5, ease: "power3.out", delay: 0.5 }
            );
        });

        return () => ctx.revert();
    }, []);

    return (
        <div className="w-full mt-24 lg:mt-28 mb-10 h-auto flex justify-center items-center">
            {/* Left panel */}
            <div
                ref={leftRef}
                className="w-1/2 p-8 hidden bg-white h-full gap-3 lg:flex flex-col justify-center items-center"
            >
                <h2 className="text-4xl text-gray-700 text-center font-semibold">
                    Login | Devasee Bookshop & Printing
                </h2>
                <div className="flex text-[#2b216d] text-opacity-20 justify-center items-start">
                    <p className="pl-2 text-[#2b216d] text-opacity-20 mt-24 text-center italic">
                        &quot;Access your Devasee Bookshop account to track orders, manage
                        purchases, and explore a wide range of books and printing services. Fast,
                        secure login for book lovers and students.&quot;
                    </p>

                    <Image src={loginImage} className="w-96 h-auto" alt="Login Visual" />
                </div>
            </div>

            {/* Right panel (Sign in form) */}
            <div
                ref={rightRef}
                className="w-full lg:w-1/2 bg-[#0000ff] py-12 flex justify-center items-center"
            >
                <SignIn />
            </div>
        </div>
    );
}
