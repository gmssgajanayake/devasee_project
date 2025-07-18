"use client";

import { useEffect, useRef, useState } from "react";
import gsap from "gsap";
import { ScrollTrigger } from "gsap/ScrollTrigger";
import ItemCard from "@/components/ItemCard";
import { useCart } from "@/app/context/CartContext";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faArrowRight } from "@fortawesome/free-solid-svg-icons";
import Link from "next/link";
import { Book } from "@/types/types";

// Import images
import book1 from "@/assets/items image/img.png";
import book2 from "@/assets/items image/img_1.png";
import book3 from "@/assets/items image/img_2.png";
import book4 from "@/assets/items image/img_3.png";
import book5 from "@/assets/items image/img_1.png";
import book6 from "@/assets/items image/img_2.png";
import book7 from "@/assets/items image/img_3.png";

gsap.registerPlugin(ScrollTrigger);

// Dummy data
const allBooks: Book[] = Array.from({ length: 13 }, (_, i) => ({
    id: `book-${i + 1}`,
    image: [book1, book2, book3, book4, book5, book6, book7][i % 7],
    title: `Book ${i + 1}`,
    author: `Author ${i + 1}`,
    price: 1000 + (i % 7) * 100,
    type: i % 2 === 0 ? "Books" : "Stationery",
    brand: i % 3 === 0 ? "Devasee" : "Other",
    stock: 5 + (i % 5),      // Random stock between 5–9
    quantity: 1              // Default quantity when added
}));

const ITEMS_PER_PAGE = 12;

export default function NewRelease() {
    const scrollContainerRef = useRef<HTMLDivElement>(null);
    const sectionRef = useRef<HTMLDivElement>(null);
    const [activePage, setActivePage] = useState(0);
    const [currentPage] = useState(1);

    const totalPages = Math.ceil(allBooks.length / ITEMS_PER_PAGE);
    const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
    const currentBooks = allBooks.slice(startIndex, startIndex + ITEMS_PER_PAGE);

    const { addToCart, removeFromCart, cartItems } = useCart();

    useEffect(() => {
        const container = scrollContainerRef.current;
        const section = sectionRef.current;
        if (!container || !section) return;

        const scrollDistance = container.scrollWidth - section.offsetWidth + 40;

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
                }
            }
        });

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
                    toggleActions: "play none none none"
                }
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
            className="relative w-full overflow-hidden bg-[#e8ebff] py-20 opacity-0 translate-y-24"
        >
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

            <div className="pt-8 w-full overflow-hidden">
                <div
                    ref={scrollContainerRef}
                    className="flex px-4 gap-4 no-scrollbar"
                    style={{ minWidth: `${allBooks.length * 250}px` }}
                >
                    {currentBooks.map((book) => (
                        <div key={book.id} className="min-w-[250px]">
                            <ItemCard
                                image={book.image}
                                title={book.title}
                                author={book.author}
                                price={book.price}
                                stock={book.stock}
                                onAddToCart={() => addToCart(book)}
                                onRemoveFromCart={() => removeFromCart(book.id)}
                                isInCart={cartItems.some((item) => item.id === book.id)}
                                isHovered={false}  // <-- Add this line
                            />
                        </div>
                    ))}
                </div>
            </div>

            <hr className="w-full text-gray-300/80" />

            <div className="flex justify-center md:justify-end items-center px-4 mt-6">
                <div className="w-full md:w-1/2 flex items-center justify-between">
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

                    <div>
                        <Link
                            href="/products"
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
