"use client";

import Link from "next/link";
import { useEffect, useRef } from "react";
import gsap from "gsap";
import Image from "next/image";
import MainButton from "@/components/MainButton";

interface AdvertisementProps {
    title: string;
    description: string;
    image: string;
    total: number;
    current: number;
}

export default function Advertisement({
                                          title,
                                          image,
                                          description,
                                          total,
                                          current,
                                      }: AdvertisementProps) {
    const textRef = useRef<HTMLHeadingElement>(null);

    useEffect(() => {
        if (textRef.current) {
            gsap.fromTo(
                textRef.current,
                { opacity: 0, x: 100 },
                { opacity: 1, x: 0, duration: 1, ease: "power3.out" }
            );
        }
    }, [title]);

    return (
        <section
            className="overflow-x-hidden p-10 mt-16 flex items-center justify-center text-center w-full h-full bg-gradient-to-b md:bg-gradient-to-r from-[#e8ebff] to-white" // FIX: added overflow-x-hidden
        >
            <div
                ref={textRef}
                className="flex flex-col gap-6 md:flex-row items-center justify-between w-full max-w-6xl mx-auto px-4 md:px-6" // FIX: added px-4/md:px-6 to keep content padded without forcing overflow
            >
                {/* Content */}
                <div className="w-full gap-2 h-1/2 md:w-1/2 flex flex-col justify-center items-center md:items-start">
                    <h1 className="text-4xl lg:text-5xl text-center md:text-left font-bold text-[#2b216d]">
                        {title}
                    </h1>
                    <p className="text-sm md:text-lg mt-2 text-center md:text-left text-[#2b216d]">
                        {description}
                    </p>
                    <Link href={"about"} className="flex justify-center md:justify-start">
                        <MainButton className={"mt-4"} name={"READ MORE"} />
                    </Link>

                    {/* Indicator Dots */}
                    <div className="hidden md:flex mt-4 gap-2 flex-wrap"> {/* FIX: added flex-wrap for safety */}
                        {Array.from({ length: total }).map((_, index) => (
                            <div
                                key={index}
                                className="w-[30px] h-[30px] flex justify-center items-center"
                            >
                                <div
                                    className={`h-[30px] w-[30px] rounded-full flex items-center justify-center ${
                                        index === current ? "border border-[#0000ff]" : "border-0"
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

                {/* Image */}
                <div
                    ref={textRef}
                    className="w-full md:w-1/2 h-1/2 flex flex-col justify-center items-center md:items-end"
                >
                    <Image
                        src={image}
                        alt={"Devasee"}
                        width={460}
                        height={460}
                        className="w-[240px] md:w-[460px] h-auto"
                    />
                </div>
            </div>
        </section>
    );
}
