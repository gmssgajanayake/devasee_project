"use client";

import { useEffect, useRef, useState, useCallback } from "react";
import { StaticImageData } from "next/image";
import gsap from "gsap";
import { ScrollTrigger } from "gsap/ScrollTrigger";

import book1 from "@/assets/offer img.png";
import Offers from "@/app/_components/Offers";

gsap.registerPlugin(ScrollTrigger);

interface OfferAd {
    id: number;
    title: string;
    description: string;
    image: StaticImageData;
    endDate: string;
}

export default function OffersSlides() {
    const [currentIndex, setCurrentIndex] = useState(0);
    const sectionRef = useRef<HTMLDivElement>(null);

    const demoAds: OfferAd[] = [
        {
            id: 2,
            title: "Computer science books are 10% off now! Don't miss such a deal!",
            description:
                "Enjoy 10% off on all Computer Science books for a limited time! Upgrade your skills and save while you learn. Don’t miss out—grab your favorites before the offer ends!",
            image: book1,
            endDate: "2026-10-01T12:00:00Z",
        },
        {
            id: 1,
            title: "Engineering books are 25% off now! Don't miss such a deal!",
            description:
                "Get 25% off on all Engineering books for a limited time! Grab this chance to boost your knowledge and save big. Don’t miss out—shop now before the offer ends!",
            image: book1,
            endDate: "2025-10-01T12:00:00Z",
        },
        {
            id: 3,
            title: "Computer science books are 15% off now! Don't miss such a deal!",
            description:
                "Enjoy 10% off on all Computer Science books for a limited time! Upgrade your skills and save while you learn. Don’t miss out—grab your favorites before the offer ends!",
            image: book1,
            endDate: "2024-10-01T12:00:00Z",
        },
    ];

    const goToNextAd = useCallback(() => {
        setCurrentIndex((prevIndex) => (prevIndex + 1) % demoAds.length);
    }, [demoAds.length]);

    useEffect(() => {
        const interval = setInterval(goToNextAd, 4500);
        return () => clearInterval(interval);
    }, [goToNextAd]); // ✅ Fixed React warning

    useEffect(() => {
        if (!sectionRef.current) return;

        gsap.fromTo(
            sectionRef.current,
            { scale: 0.9, opacity: 0 },
            {
                scale: 1,
                opacity: 1,
                duration: 1.2,
                ease: "power2.out",
                scrollTrigger: {
                    trigger: sectionRef.current,
                    start: "top 80%",
                    toggleActions: "play none none none",
                },
            }
        );
    }, []);

    const currentAd = demoAds[currentIndex];

    return (
        <div className="w-screen h-auto overflow-hidden bg-white" ref={sectionRef}>
            <div className="w-full h-auto relative">
                <Offers
                    key={currentAd.id}
                    title={currentAd.title}
                    description={currentAd.description}
                    image={currentAd.image.src}
                    total={demoAds.length}
                    current={currentIndex}
                    endDate={currentAd.endDate}
                />
            </div>
        </div>
    );
}
