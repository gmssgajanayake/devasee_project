"use client";

import { useEffect, useRef } from "react";
import gsap from "gsap";
import { ScrollTrigger } from "gsap/ScrollTrigger";
import ItemCard from "@/components/ItemCard";

import book1 from "@/assets/items image/img.png";
import book2 from "@/assets/items image/img_1.png";
import book3 from "@/assets/items image/img_2.png";
import book4 from "@/assets/items image/img_3.png";
import book5 from "@/assets/items image/img_1.png";
import book6 from "@/assets/items image/img_2.png";

gsap.registerPlugin(ScrollTrigger);

const books = [
    { image: book1, title: "Learn React", author: "John Doe", price: 29.99 },
    { image: book2, title: "Advanced React", author: "Jane Doe", price: 39.99 },
    { image: book3, title: "React Native", author: "Alex Smith", price: 34.99 },
    { image: book4, title: "React Hooks", author: "Sara Lee", price: 24.99 },
    { image: book5, title: "React + TypeScript", author: "Chris Ray", price: 32.99 },
    { image: book6, title: "React Testing", author: "Emma Stone", price: 28.99 },
    { image: book4, title: "React Hooks", author: "Sara Lee", price: 24.99 },
    { image: book5, title: "React + TypeScript", author: "Chris Ray", price: 32.99 },
    { image: book6, title: "React Testing", author: "Emma Stone", price: 28.99 },
];

export default function NewRelease() {
    const scrollContainerRef = useRef<HTMLDivElement>(null);
    const sectionRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const container = scrollContainerRef.current;
        const section = sectionRef.current;
        if (!container || !section) return;

        const totalScrollWidth = container.scrollWidth;
        const viewportWidth = section.offsetWidth;
        const scrollDistance = 40+totalScrollWidth - viewportWidth;

        const tween = gsap.to(container, {
            x: () => `-${scrollDistance}px`,
            ease: "none",
            scrollTrigger: {
                trigger: section,
                start: "top top",
                end: () => `+=${scrollDistance}`,
                scrub: 1,
                pin: true,
                anticipatePin: 1,
                invalidateOnRefresh: true,
            },
        });

        return () => {
            tween.kill();
            ScrollTrigger.getAll().forEach(trigger => trigger.kill());
        };
    }, []);

    return (
        <div ref={sectionRef} className="relative w-screen bg-[#e8ebff] py-20">
            {/* Title Section */}
            <p className="text-xs tracking-widest text-center text-gray-800/50">SOME QUALITY ITEMS</p>
            <div className="flex items-center justify-center w-screen px-8 my-6">
                <hr className="w-full text-gray-300/80" />
                <h2 className="text-3xl md:text-4xl mx-4 font-bold text-[#2b216d]">
                    New&nbsp;Release&nbsp;Books
                </h2>
                <hr className="w-full text-gray-300/80" />
            </div>

            {/* Horizontal Scroll Section */}
            <div className="overflow-hidden  pt-8 w-full">
                <div
                    ref={scrollContainerRef}
                    className="flex gap-10 px-10"
                    style={{ width: `${books.length * 224}px` }} // Adjust if needed
                >
                    {books.map((book, index) => (
                        <ItemCard
                            key={index}
                            isHovered={false}
                            imageUrl={book.image.src}
                            title={book.title}
                            author={book.author}
                            price={book.price}
                        />
                    ))}
                </div>
            </div>
        </div>
    );
}
