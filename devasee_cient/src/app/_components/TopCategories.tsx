"use client";

import { useLayoutEffect, useRef } from "react";
import { gsap } from "gsap";
import { ScrollTrigger } from "gsap/dist/ScrollTrigger";
import Image from "next/image";
import MainButton from "@/components/MainButton";
import Link from "next/link";
import c1 from "@/assets/categoryies image/img.png";
import c2 from "@/assets/categoryies image/img_3.png";
import c3 from "@/assets/categoryies image/img_2.png";

const categories = [
    {
        title: "Higher Education",
        image: c1,
    },
    {
        title: "Management Books",
        image: c2,
    },
    {
        title: "Engineering Books",
        image: c3,
    },
];

export default function TopCategories() {
    const containerRef = useRef<HTMLDivElement>(null);

    useLayoutEffect(() => {
        gsap.registerPlugin(ScrollTrigger);

        if (!containerRef.current) return;

        const elements = containerRef.current.querySelectorAll(".category");
        const isMobile = window.innerWidth < 768;

        elements.forEach((el, i) => {
            let animationProps;

            if (isMobile) {
                // Mobile: 1st left→right, 2nd right→left, 3rd bottom→top
                animationProps = [
                    { x: -50, opacity: 0, scale: 0.95 },
                    { x: 50, opacity: 0, scale: 0.95 },
                    { y: 50, opacity: 0, scale: 0.95 },
                ][i];
            } else {
                // Desktop: 1st left→right, 2nd right→left, 3rd bottom→top
                animationProps = [
                    { x: -100, opacity: 0, scale: 0.9 },
                    { x: 100, opacity: 0, scale: 0.9 },
                    { y: 100, opacity: 0, scale: 0.9 },
                ][i];
            }

            gsap.fromTo(
                el,
                animationProps,
                {
                    x: 0,
                    y: 0,
                    opacity: 1,
                    scale: 1,
                    duration: 1.2,
                    ease: "power3.out",
                    scrollTrigger: {
                        trigger: el,
                        start: "top 80%",
                        toggleActions: "play none none none",
                    },
                }
            );
        });
    }, []);


    return (
        <div
            ref={containerRef}
            className="flex overflow-hidden flex-col items-start justify-start w-screen h-auto bg-white"
        >
            <div className="flex justify-start px-4 md:px-8 items-center gap-2 mb-5 mt-5 md:mt-10 w-full">
                <div className="h-0.5 border border-[#0000ff] rounded-full w-10 bg-[#0000ff]" />
                <p className="font-bold text-[#0000ff] text-sm tracking-widest">Categories</p>
            </div>

            <div className="w-full px-4 md:px-8 flex items-center justify-start">
                <h2 className="font-bold text-[#2b216d] text-lg md:text-3xl w-full">
                    Explore our Top Categories
                </h2>
            </div>

            <div className="w-full mt-4 px-4 gap-8 md:gap-4 md:px-8 flex-col md:flex-row flex items-center justify-start mb-8 lg:my-8">
                {categories.map((category, index) => (
                    <div
                        key={index}
                        className="category my-4 gap-4 md:w-1/3 h-auto flex items-center justify-center flex-col lg:gap-8"
                    >
                        <Image
                            src={category.image}
                            alt={category.title}
                            priority={index === 0}
                            className="w-full h-48 lg:h-64 object-contain"
                        />
                        <h2 className="text-xl text-center font-bold text-[#2b216d]">
                            {category.title}
                        </h2>
                    </div>
                ))}
            </div>

            <Link
                href="products"
                className="w-full flex text-center mb-12 justify-center items-center"
            >
                <MainButton name="VIEW MORE" />
            </Link>
        </div>
    );
}
