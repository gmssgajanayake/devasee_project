"use client";

import { useEffect, useState } from "react";
import MainNavBar from "@/app/_components/MainNavBar";
import Advertisement from "@/app/_components/Advertisement";
import ContactBar from "@/app/_components/ContactBar";
import adImage1 from "@/assets/devasee-p1.png";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faArrowLeft, faArrowRight} from "@fortawesome/free-solid-svg-icons";

export function AdvertisementSlides() {
    const [currentIndex, setCurrentIndex] = useState(0);

    const demoAds = [
        {
            id: 1,
            title: "Devasee Bookshop",
            description: "Devasee Bookshop offers a wide collection of books across various genres including education, fiction, and children’s literature. It’s a trusted destination for readers and students seeking quality books in Sri Lanka.",
            image: adImage1,
        },
        {
            id: 2,
            title: "Personalized Printing",
            description: "Personalized printing adds a custom touch to items like gifts, apparel, and stationery. It enhances uniqueness, making products more meaningful for personal use, branding, or special occasions.",
            image: adImage1,
        },
        {
            id: 3,
            title: "Student Stationery Deals",
            description: "Student stationery deals offer affordable bundles of essential school supplies like pens, notebooks, highlighters, and folders. These budget-friendly packs are perfect for students preparing for a new term.",
            image: adImage1,
        },
    ];

    const goToPreviousAd = () => {
        setCurrentIndex((prevIndex) => (prevIndex === 0 ? demoAds.length - 1 : prevIndex - 1));
    };

    const goToNextAd = () => {
        setCurrentIndex((prevIndex) => (prevIndex + 1) % demoAds.length);
    };

    useEffect(() => {
        const interval = setInterval(goToNextAd, 3000);
        return () => clearInterval(interval);
    }, []);

    return (
        <div className="w-screen h-screen overflow-hidden bg-white">
            <ContactBar />
            <MainNavBar />
            <div className="w-full h-full relative">
                <Advertisement
                    key={demoAds[currentIndex].id}
                    title={demoAds[currentIndex].title}
                    description={demoAds[currentIndex].description}
                    image={demoAds[currentIndex].image.src}
                    total={demoAds.length}
                    current={currentIndex}></Advertisement>
                {/* Left Arrow */}
                <div
                    className="absolute hidden lg:flex left-8 top-1/2 transform -translate-y-1/2 z-10 w-12 h-12  items-center justify-center cursor-pointer"
                    onClick={goToPreviousAd}
                >
                    <div className="w-10 h-10 bg-white rounded-full border border-[#0000ff] flex items-center justify-center shadow-md">
                        <FontAwesomeIcon icon={faArrowLeft} className="text-[#0000ff]" />
                    </div>
                </div>
                {/* Right Arrow */}
                <div
                    className="absolute hidden lg:flex right-8 top-1/2 transform -translate-y-1/2 z-10 w-12 h-12 items-center justify-center cursor-pointer"
                    onClick={goToNextAd}
                >
                    <div className="w-10 h-10 bg-white rounded-full border border-[#0000ff] flex items-center justify-center shadow-md">
                        <FontAwesomeIcon icon={faArrowRight} className="text-[#0000ff]" />
                    </div>
                </div>
            </div>
        </div>
    );
}
