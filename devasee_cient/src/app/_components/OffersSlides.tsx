"use client";

import {useEffect, useState} from "react";
import MainNavBar from "@/app/_components/MainNavBar";
import ContactBar from "@/app/_components/ContactBar";
import book1 from "@/assets/offer img.png";
import Offers from "@/app/_components/Offers";

export default function OffersSlides() {
    const [currentIndex, setCurrentIndex] = useState(0);

    const demoAds = [
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

    const goToNextAd = () => {
        setCurrentIndex((prevIndex) => (prevIndex + 1) % demoAds.length);
    };

    useEffect(() => {
        const interval = setInterval(goToNextAd, 4500);
        return () => clearInterval(interval);
    }, []);

    return (
        <div className="w-screen h-auto overflow-hidden bg-white">
            <ContactBar/>
            <MainNavBar/>
            <div className="w-full h-auto relative">
                <Offers
                    key={demoAds[currentIndex].id}
                    title={demoAds[currentIndex].title}
                    description={demoAds[currentIndex].description}
                    image={demoAds[currentIndex].image.src}
                    total={demoAds.length}
                    current={currentIndex}
                    endDate={demoAds[currentIndex].endDate}
                />
            </div>
        </div>
    );
}
