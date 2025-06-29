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
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faArrowRight} from "@fortawesome/free-solid-svg-icons";
import Link from "next/link";

gsap.registerPlugin(ScrollTrigger);

const books = [
    { image: book1, title: "Learn React", author: "John Doe", price: 1299.00 },
    { image: book2, title: "Advanced React", author: "Jane Doe", price: 1399.00 },
    { image: book3, title: "React Native", author: "Alex Smith", price: 1349.00 },
    { image: book4, title: "React Hooks", author: "Sara Lee", price: 1249.00 },
    { image: book5, title: "React + TypeScript", author: "Chris Ray", price: 1329.00 },
    { image: book6, title: "React Testing", author: "Emma Stone", price: 1289.00 },
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
                onUpdate: (self) => {
                    const progress = self.progress;
                    const currentPage = Math.round(progress * (totalPages - 1));
                    setActivePage(currentPage);
                },
            },
        });

        return () => {
            tween.kill();
            ScrollTrigger.getAll().forEach((trigger) => trigger.kill());
        };
    }, [totalPages]);

    return (
        <div ref={sectionRef} className="relative w-screen bg-[#e8ebff] py-20">
            {/* Title */}
            <p className="text-xs tracking-widest text-center text-gray-800/50">SOME QUALITY ITEMS</p>
            <div className="flex items-center justify-center w-screen px-8 my-6">
                <hr className="w-full text-gray-300/80" />
                <h2 className="text-3xl md:text-4xl mx-4 font-bold text-[#2b216d]">
                    New&nbsp;Release&nbsp;Books
                </h2>
                <hr className="w-full text-gray-300/80" />
            </div>

            {/* Horizontal Scroll Items */}
            <div className="overflow-hidden pt-8 w-full">
                <div
                    ref={scrollContainerRef}
                    className="flex gap-10 px-10"
                    style={{ width: `${books.length * 250}px` }} // Adjust width if needed
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

            {/* Dots */}
            <div className="flex  justify-center  md:justify-end items-center px-4 mt-6">
                <div className={" w-full md:w-1/2 flex items-center justify-between"}>
                    <div className="flex gap-4 ">
                        {Array.from({ length: totalPages }).map((_, index) => (
                            <div key={index} className={` ${index === activePage ? "border" : "border-0"} w-8 h-8 flex justify-center items-center  transition-all duration-300 border-[#0000ff] rounded-full`}>
                                <div
                                    key={index}
                                    className={`w-3 h-3 rounded-full transition-all duration-300 ${
                                        index === activePage ? "bg-[#0000ff]" : "bg-gray-400/30"
                                    }`}
                                />
                            </div>

                        ))}
                    </div>

                    <div>
                        <Link href={"products"}  className={"text-[#0000ff] cursor-pointer font-bold text-sm"}>
                            View All Products
                            <FontAwesomeIcon icon={faArrowRight} className="ml-2" />
                        </Link>
                    </div>
                </div>

            </div>
        </div>
    );
}
