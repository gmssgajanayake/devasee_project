"use client";

import {SignUp} from "@clerk/nextjs";
import Image from "next/image";
import loginImage from "@/assets/devasee login page.png";
import {useEffect, useRef} from "react";
import gsap from "gsap";

export default function Page() {
    const leftRef = useRef<HTMLDivElement>(null);
    const rightRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const ctx = gsap.context(() => {
            gsap.fromTo(
                leftRef.current,
                {opacity: 0, x: -100},
                {opacity: 1, x: 0, duration: 1.5, ease: "power3.out", delay: 0.2}
            );

            gsap.fromTo(
                rightRef.current,
                {opacity: 0, x: 100},
                {opacity: 1, x: 0, duration: 1.5, ease: "power3.out", delay: 0.5}
            );
        });

        return () => ctx.revert();
    }, []);

    return (
        <div className="w-full mt-24 lg:mt-28 mb-10 h-auto flex justify-center items-center">
            {/* Left Panel */}
            <div
                ref={leftRef}
                className="w-1/2 px-8 hidden h-full py-24 bg-white gap-3 lg:flex flex-col justify-center items-center"
            >
                <h2 className="text-4xl text-[#2b216d] text-center font-semibold">
                    Create Account | Devasee Bookshop & Printing
                </h2>
                <div className="flex justify-center items-start">
                    <p className="pl-2 mt-24 text-[#2b216d] text-opacity-20 text-center italic">
                        &quot;Join Devasee Bookshop to discover and order books, manage your printing needs, and enjoy a
                        personalized shopping experience. Sign up in seconds and get started today.&quot;
                    </p>


                    <Image src={loginImage} className="w-96 h-auto" alt="Register Visual"/>
                </div>
            </div>

            {/* Right Panel */}
            <div
                ref={rightRef}
                className="w-full lg:w-1/2 py-12 bg-[#0000ff] flex justify-center items-center"
            >
                <SignUp/>
            </div>
        </div>
    );
}
