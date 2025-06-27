"use client";

import Link from "next/link";
import {useEffect, useRef} from "react";
import gsap from "gsap";
import Image from "next/image";

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
                                          current
                                      }: AdvertisementProps) {
    const textRef = useRef<HTMLHeadingElement>(null);

    useEffect(() => {
        if (textRef.current) {
            gsap.fromTo(
                textRef.current,
                {opacity: 0, x: 100}, // start from right
                {opacity: 1, x: 0, duration: 1, ease: "power3.out"} // animate into view
            );
        }
    }, [title]);

    return (
        <section
            className="p-10 mt-16 flex items-center justify-center text-center w-full h-full bg-gradient-to-b md:bg-gradient-to-r from-[#e8ebff] to-white"
        >
            <div className="flex flex-col gap-6 md:flex-row items-center justify-between w-full max-w-6xl mx-auto">
                {/* Content */}
                <div ref={textRef} className="w-full gap-2 h-1/2 md:w-1/2 flex flex-col justify-center items-center md:items-start">
                    <h1 className="text-4xl lg:text-5xl text-center md:text-left font-bold text-[#2b216d]">
                        {title}
                    </h1>
                    <p className="text-sm md:text-lg mt-2 text-center md:text-left text-[#2b216d]">
                        {description}
                    </p>
                    <Link href={"about"} className="flex justify-center md:justify-start">
                        <button
                            className="my-6 cursor-pointer px-4 py-2 text-sm border border-[#2b216d] rounded-md text-[#2b216d] hover:bg-[#2b216d] hover:text-white transition duration-300">
                            READ MORE
                        </button>
                    </Link>

                    {/* Indicator Dots */}
                    <div className="hidden md:flex mt-4 gap-2">
                        {Array.from({length: total}).map((_, index) => (
                            <div
                                key={index}
                                className="w-[30px] h-[30px] flex justify-center items-center"
                            >
                                <div className={`h-[30px] w-[30px] rounded-full flex items-center justify-center ${
                                    index === current ? "border border-[#0000ff]" : "border-0 "
                                }`}>
                                    <div
                                        className={`h-[10px] w-[10px] rounded-full ${
                                            index === current ? "bg-[#0000ff]" : "bg-gray-400"
                                        }`}
                                    ></div>
                                </div>

                            </div>
                        ))}
                    </div>
                </div>

                {/* Image */}
                <div className="w-full md:w-1/2 h-1/2 flex flex-col justify-center items-center md:items-end">
                    <Image
                        src={image}
                        alt={"Devasee"}
                        width={460}
                        height={460}
                        className="w-[240px] h-auto md:w-[460px] md:h-auto"
                    />
                </div>
            </div>
        </section>
    );
}
