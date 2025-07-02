"use client";

import Image from "next/image";
import image1 from "@/assets/article image/img.png";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFacebook, faInstagram, faXTwitter } from "@fortawesome/free-brands-svg-icons";
import { useEffect, useRef, useCallback } from "react";
import gsap from "gsap";
import { ScrollTrigger } from "gsap/ScrollTrigger";

gsap.registerPlugin(ScrollTrigger);

const articles = [
    {
        image: image1,
        date: "1 April, 2023",
        title: "Reading books always makes the moments happy",
    },
    {
        image: image1,
        date: "1 April, 2023",
        title: "Reading books always makes the moments happy",
    },
    {
        image: image1,
        date: "1 April, 2023",
        title: "Reading books always makes the moments happy",
    },
];

export default function Article() {
    const articleRefs = useRef<(HTMLDivElement | null)[]>([]);

    // Clear refs before rendering again
    articleRefs.current = [];

    // Safe ref callback
    const setRefs = useCallback((el: HTMLDivElement | null) => {
        if (el) articleRefs.current.push(el);
    }, []);

    useEffect(() => {
        const mm = gsap.matchMedia();

        mm.add(
            {
                // Desktop media query (min-width: 768px)
                isDesktop: "(min-width: 768px)",
                // Mobile media query (max-width: 767px)
                isMobile: "(max-width: 767px)",
            },
            (context) => {
                const { isDesktop, isMobile } = context.conditions;

                articleRefs.current.forEach((el, index) => {
                    if (!el) return;

                    if (isDesktop) {
                        // Bottom to top animation on desktop
                        gsap.fromTo(
                            el,
                            { opacity: 0, y: 60, scale: 0.95 },
                            {
                                opacity: 1,
                                y: 0,
                                scale: 1,
                                duration: 1,
                                ease: "power3.out",
                                scrollTrigger: {
                                    trigger: el,
                                    start: "top 90%",
                                    toggleActions: "play none none reverse",
                                },
                            }
                        );
                    } else if (isMobile) {
                        // Left/right horizontal slide on mobile
                        const fromX = index % 2 === 0 ? 100 : -100;

                        gsap.fromTo(
                            el,
                            { opacity: 0, y: 0, x: fromX, scale: 0.95 },
                            {
                                opacity: 1,
                                y: 0,
                                x: 0,
                                scale: 1,
                                duration: 1,
                                ease: "power3.out",
                                scrollTrigger: {
                                    trigger: el,
                                    start: "top 90%",
                                    toggleActions: "play none none reverse",
                                },
                            }
                        );
                    }
                });

                return () => {
                    ScrollTrigger.getAll().forEach((trigger) => trigger.kill());
                };
            }
        );

        return () => {
            mm.revert();
        };
    }, []);

    return (
        <div className="bg-[#f9f7ff] overflow-hidden flex flex-col w-full px-4 py-12 mt-14">
            <p className="text-xs tracking-widest text-center text-gray-800/50">
                READ OUR ARTICLES
            </p>

            <div className="flex items-center justify-center w-full px-8 my-6">
                <hr className="w-full text-gray-300/80" />
                <h2 className="text-3xl tracking-wide md:text-4xl mx-4 font-medium text-[#2b216d] whitespace-nowrap">
                    Latest&nbsp;Articles
                </h2>
                <hr className="w-full text-gray-300/80" />
            </div>

            <div className="flex flex-col md:flex-row justify-between items-stretch gap-6">
                {articles.map((article, index) => (
                    <div
                        key={index}
                        ref={setRefs}
                        className="flex flex-col p-6 gap-4 md:w-1/3 w-full bg-white transition-all duration-300"
                    >
                        <Image
                            src={article.image}
                            alt={`Article ${index + 1}`}
                            className="w-full h-auto"
                        />
                        <p className="mt-2 tracking-wider text-gray-800/40 text-xs">
                            {article.date}
                        </p>
                        <p className="text-xl text-gray-700 font-light">{article.title}</p>
                        <div className="bg-gray-300/50 w-full h-[1px] my-2" />
                        <div className="flex justify-end">
                            <div className="flex gap-3 text-[#2b216d]">
                                <FontAwesomeIcon icon={faFacebook} className="cursor-pointer" />
                                <FontAwesomeIcon icon={faXTwitter} className="cursor-pointer" />
                                <FontAwesomeIcon icon={faInstagram} className="cursor-pointer" />
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            <div className="w-full flex justify-center items-center mt-10">
                <button className="text-xs tracking-wider text-gray-700 border border-gray-400 hover:bg-[#2b216d] hover:text-white transition px-6 py-3 cursor-pointer">
                    READ MORE ARTICLES &#8594;
                </button>
            </div>
        </div>
    );
}
