"use client";

import { useEffect, useState } from "react";
import MainNavBar from "@/app/_components/MainNavBar";
import ContactBar from "@/app/_components/ContactBar";
import book1 from "@/assets/items image/img_4.png";
import book3 from "@/assets/items image/img_2.png";
import book2 from "@/assets/items image/img_3.png";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faArrowLeft, faArrowRight} from "@fortawesome/free-solid-svg-icons";
import BooksAdvertisement from "@/app/_components/BooksAdvertisement";

export default function BooksAdvertisementSlides() {
    const [currentIndex, setCurrentIndex] = useState(0);

    const demoAds = [
        {
            id: 1,
            title: "Devasee Bookshop",
            description: "Devasee Bookshop offers a wide collection of books across various genres including education, fiction, and children’s literature. It’s a trusted destination for readers and students seeking quality books in Sri Lanka.",
            image: book1,
            price: 1299.00,
        },
        {
            id: 2,
            title: "Personalized Printing",
            description: "Personalized printing adds a custom touch to items like gifts, apparel, and stationery. It enhances uniqueness, making products more meaningful for personal use, branding, or special occasions.",
            image: book2,
            price: 1299.00,
        },
        {
            id: 3,
            title: "Student Stationery Deals",
            description: "Student stationery deals offer affordable bundles of essential school supplies like pens, notebooks, highlighters, and folders. These budget-friendly packs are perfect for students preparing for a new term.",
            image: book3,
            price: 1299.00,
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
        <div className="w-screen h-auto overflow-hidden bg-white">
            <ContactBar />
            <MainNavBar />
            <div className="w-full h-auto relative">
                <BooksAdvertisement
                    key={demoAds[currentIndex].id}
                    title={demoAds[currentIndex].title}
                    description={demoAds[currentIndex].description}
                    image={demoAds[currentIndex].image.src}
                    total={demoAds.length}
                    current={currentIndex}
                    price={demoAds[currentIndex].price}>
                </BooksAdvertisement>
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
