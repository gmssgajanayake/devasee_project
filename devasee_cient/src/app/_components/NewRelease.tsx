"use client";

import { useEffect, useRef, useState } from "react";
import gsap from "gsap";
import { ScrollTrigger } from "gsap/ScrollTrigger";
import ItemCard from "@/components/ItemCard";

import book1 from "@/assets/items image/img.png";
import book2 from "@/assets/items image/img_1.png";
import book3 from "@/assets/items image/img_2.png";
import book4 from "@/assets/items image/img_3.png";
import book5 from "@/assets/items image/img_1.png";
import book6 from "@/assets/items image/img_2.png";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowRight } from "@fortawesome/free-solid-svg-icons";
import Link from "next/link";

gsap.registerPlugin(ScrollTrigger);

const books = [
    { image: book1, title: "Learn React", author: "John Doe", price: 1299.0 },
    { image: book2, title: "Advanced React", author: "Jane Doe", price: 1399.0 },
    { image: book3, title: "React Native", author: "Alex Smith", price: 1349.0 },
    { image: book4, title: "React Hooks", author: "Sara Lee", price: 1249.0 },
    { image: book5, title: "React + TypeScript", author: "Chris Ray", price: 1329.0 },
    { image: book6, title: "React Testing", author: "Emma Stone", price: 1289.0 },
];

export default function NewRelease() {
    const scrollContainerRef = useRef<HTMLDivElement>(null);
    const sectionRef = useRef<HTMLDivElement>(null);
    const [activePage, setActivePage] = useState(0);

    const itemsPerPage = 3;
    const totalPages = Math.ceil(books.length / itemsPerPage);

    useEffect(() => {
        const container = scrollContainerRef.current;
        const section = sectionRef.current;
        if (!container || !section) return;

        // Horizontal Scroll Animation
        const totalScrollWidth = container.scrollWidth;
        const viewportWidth = section.offsetWidth;
        const scrollDistance = totalScrollWidth - viewportWidth + 40;

        const horizontalTween = gsap.to(container, {
            x: () => `-${scrollDistance}px`,
            ease: "none",
            scrollTrigger: {
                trigger: section,
                start: "top top",
                end: `+=${scrollDistance}`,
                scrub: 1,
                pin: true,
                anticipatePin: 1,
                invalidateOnRefresh: true,
                onUpdate: (self) => {
                    const progress = self.progress;
                    const currentPage = Math.round(progress * (totalPages - 1));
                    setActivePage(currentPage);
                },
            },
        });

        // Fade + Rise Animation for entire section (only once)
        const revealTween = gsap.fromTo(
            section,
            { opacity: 0, y: 100 },
            {
                opacity: 1,
                y: 0,
                duration: 1.2,
                ease: "power3.out",
                scrollTrigger: {
                    trigger: section,
                    start: "top 80%",
                    toggleActions: "play none none none",
                },
            }
        );

        return () => {
            horizontalTween.kill();
            revealTween.kill();
            ScrollTrigger.getAll().forEach((trigger) => trigger.kill());
        };
    }, [totalPages]);

    return (
        <div
            ref={sectionRef}
            className="relative w-full overflow-hidden overflow-x-hidden bg-[#e8ebff] py-20 opacity-0 translate-y-24"
        >
            {/* Title */}
            <p className="text-xs tracking-widest text-center text-gray-800/50">
                SOME QUALITY ITEMS
            </p>
            <div className="flex items-center justify-center w-full px-8 my-6">
                <hr className="w-full text-gray-300/80" />
                <h2 className="text-3xl md:text-4xl mx-4 font-bold text-[#2b216d]">
                    New&nbsp;Release&nbsp;Books
                </h2>
                <hr className="w-full text-gray-300/80" />
            </div>

            {/* Scrollable content */}
            <div className="pt-8 w-full overflow-hidden">
                <div
                    ref={scrollContainerRef}
                    className="flex gap-10 px-10 no-scrollbar"
                    style={{ minWidth: `${books.length * 250}px` }}
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

            <hr className="w-full text-gray-300/80" />

            {/* Page Dots and CTA */}
            <div className="flex justify-center md:justify-end items-center px-4 mt-6">
                <div className="w-full md:w-1/2 flex items-center justify-between">
                    {/* Dots */}
                    <div className="flex gap-4">
                        {Array.from({ length: totalPages }).map((_, index) => (
                            <div
                                key={index}
                                className={`${
                                    index === activePage ? "border" : "border-0"
                                } w-8 h-8 flex justify-center items-center transition-all duration-300 border-[#0000ff] rounded-full`}
                            >
                                <div
                                    className={`w-3 h-3 rounded-full transition-all duration-300 ${
                                        index === activePage ? "bg-[#0000ff]" : "bg-gray-400/30"
                                    }`}
                                />
                            </div>
                        ))}
                    </div>

                    {/* View All CTA */}
                    <div>
                        <Link
                            href={"products"}
                            className="text-[#0000ff] cursor-pointer font-bold text-sm"
                        >
                            View All Products
                            <FontAwesomeIcon icon={faArrowRight} className="ml-2" />
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}
