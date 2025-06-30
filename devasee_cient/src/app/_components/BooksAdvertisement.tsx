"use client";

import Link from "next/link";
import { useEffect, useRef } from "react";
import gsap from "gsap";
import Image from "next/image";
import MainButton from "@/components/MainButton";

interface BookAdvertisementProps {
    title: string;
    description: string;
    image: string;
    total: number;
    current: number;
    price: number;
}

export function formatPriceLKR(price: number): string {
    const rounded = Math.ceil(price); // Always round up
    const formatted = new Intl.NumberFormat("en-LK", {
        style: "currency",
        currency: "LKR",
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
    }).format(rounded);

    return formatted;
}

export default function BooksAdvertisement({
                                               title,
                                               image,
                                               description,
                                               total,
                                               current,
                                               price,
                                           }: BookAdvertisementProps) {
    const textRef = useRef<HTMLHeadingElement>(null);

    useEffect(() => {
        if (textRef.current) {
            gsap.fromTo(
                textRef.current,
                { opacity: 0, x: 100 }, // start from right
                { opacity: 1, x: 0, duration: 1, ease: "power3.out" } // animate into view
            );
        }
    }, [title]);

    return (
        <section
            className="overflow-x-hidden  py-8 px-4 md:px-10 mt-16 flex-col flex items-center justify-center text-center w-full h-full bg-[#f2f3f7]" // ✅ FIXED
        >
            <div
                ref={textRef}
                className="flex flex-col gap-12 md:flex-row items-center justify-center w-full max-w-6xl mx-auto"
            >
                {/* Image */}
                <div
                    ref={textRef}
                    className="w-full md:w-1/2 h-1/2 flex flex-col justify-center items-center"
                >
                    <Image
                        src={image}
                        alt={"Devasee"}
                        width={300}
                        height={300}
                        className="w-[140px] h-auto md:w-[360px] md:h-auto"
                    />
                </div>

                {/* Content */}
                <div className="w-full gap-4 h-1/2 md:w-1/2 flex flex-col justify-center items-center md:items-start">
                    <h2 className="text-3xl lg:text-5xl text-center md:text-left font-bold text-[#2b216d]">
                        Featured Books
                    </h2>
                    <div className={"w-[80px] mt-10 h-[2px] bg-[#0000ff]"}></div>
                    <p className="text-xs tracking-widest text-center text-gray-800/50">
                        SOME QUALITY ITEMS
                    </p>
                    <h2 className="text-2xl lg:text-3xl text-center md:text-left font-bold text-[#2b216d]">
                        {title}
                    </h2>
                    <p className="text-sm md:text-left text-center text-gray-800/50">
                        {description}
                    </p>
                    <p className={"text-xl font-bold text-[#0000ff]"}>
                        {formatPriceLKR(price)}
                    </p>
                    <Link href={"about"} className="flex justify-center md:justify-start">
                        <MainButton className={"mt-4"} name={"READ MORE"} />
                    </Link>
                </div>
            </div>

            {/* Indicator Dots */}
            <div className="w-full h-auto mt-8 transition duration-300 flex items-center justify-center overflow-x-auto">
                <div className="flex gap-2 flex-wrap justify-center">
                    {Array.from({ length: total }).map((_, index) => (
                        <div
                            key={index}
                            className="w-[30px] h-[30px] flex justify-center items-center"
                        >
                            <div
                                className={`h-[30px] w-[30px] rounded-full flex items-center justify-center ${
                                    index === current
                                        ? "border border-[#0000ff]"
                                        : "border-0"
                                }`}
                            >
                                <div
                                    className={`h-[10px] w-[10px] rounded-full ${
                                        index === current
                                            ? "bg-[#0000ff]"
                                            : "bg-gray-400/30"
                                    }`}
                                ></div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    );
}
