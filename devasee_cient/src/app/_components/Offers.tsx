"use client";

import { useEffect, useLayoutEffect, useRef, useState } from "react";
import gsap from "gsap";
import Image from "next/image";

interface BookAdvertisementProps {
    title: string;
    description: string;
    image: string;
    total: number;
    current: number;
    endDate?: string;
}

type TimeLeft = {
    days: number;
    hours: number;
    minutes: number;
    seconds: number;
    expired: boolean;
};

const Offers: React.FC<BookAdvertisementProps> = ({
                                                      title,
                                                      image,
                                                      description,
                                                      total,
                                                      current,
                                                      endDate,
                                                  }) => {
    const textRef = useRef<HTMLDivElement>(null);
    const imageRef = useRef<HTMLDivElement>(null);

    const [timeLeft, setTimeLeft] = useState<TimeLeft>({
        days: 0,
        hours: 0,
        minutes: 0,
        seconds: 0,
        expired: false,
    });

    // Countdown logic
    useEffect(() => {
        if (!endDate) return;

        const updateCountdown = () => {
            const now = new Date().getTime();
            const target = new Date(endDate).getTime();
            const distance = target - now;

            if (distance < 0) {
                setTimeLeft({
                    days: 0,
                    hours: 0,
                    minutes: 0,
                    seconds: 0,
                    expired: true,
                });
                return;
            }

            const days = Math.floor(distance / (1000 * 60 * 60 * 24));
            const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            const seconds = Math.floor((distance % (1000 * 60)) / 1000);

            setTimeLeft({ days, hours, minutes, seconds, expired: false });
        };

        updateCountdown();
        const interval = setInterval(updateCountdown, 1000);
        return () => clearInterval(interval);
    }, [endDate]);

    // GSAP animation
    useLayoutEffect(() => {
        const tl = gsap.timeline();

        tl.fromTo(
            textRef.current,
            { autoAlpha: 0, x: -50 },
            { autoAlpha: 1, x: 0, duration: 0.6, ease: "power2.out" }
        ).fromTo(
            imageRef.current,
            { autoAlpha: 0, x: 50 },
            { autoAlpha: 1, x: 0, duration: 0.6, ease: "power2.out" },
            "<"
        );

        return () => {
            gsap.set([textRef.current, imageRef.current], { clearProps: "all" });
        };
    }, [current]);

    return (
        <section className="flex flex-col items-center justify-center w-full h-full bg-white">
            <div className="flex flex-col-reverse md:flex-row px-4 sm:px-8 md:px-20 py-14 bg-[#e5e9fb] xl:rounded-4xl items-center justify-center w-full max-w-6xl mx-auto gap-6 sm:gap-10 md:gap-12">
                {/* Left: Text & Countdown */}
                <div
                    ref={textRef}
                    className="w-full md:w-1/2 flex flex-col justify-center items-center md:items-start gap-4"
                >
                    <h2 className="text-xl lg:text-2xl text-center md:text-left font-bold text-[#2b216d]">
                        {title}
                    </h2>
                    <p className="text-sm sm:text-base text-center md:text-left text-gray-800/50">
                        {description}
                    </p>

                    {/* Countdown */}
                    <div className="w-full flex items-center justify-center md:justify-start gap-6 mt-2">
                        {!timeLeft.expired ? (
                            ["days", "hours", "minutes", "seconds"].map((unit, index) => (
                                <div key={unit} className="flex flex-col items-center">
                                    <p className="text-xl font-bold text-blue-700">
                                        {timeLeft[unit as keyof TimeLeft]}
                                    </p>
                                    <p className="text-xs text-gray-600">
                                        {unit.toUpperCase().slice(0, 3)}
                                    </p>
                                </div>
                            ))
                        ) : (
                            <p className="text-red-600 font-semibold text-lg">EXPIRED</p>
                        )}
                    </div>

                    {/* Dot Indicator */}
                    <div className="flex gap-2 mt-4">
                        {Array.from({ length: total }).map((_, index) => (
                            <div key={index} className="w-[30px] h-[30px] flex justify-center items-center">
                                <div
                                    className={`h-[30px] w-[30px] rounded-full flex items-center justify-center ${
                                        index === current ? "border border-[#0000ff]" : "border-0"
                                    }`}
                                >
                                    <div
                                        className={`h-[10px] w-[10px] rounded-full ${
                                            index === current ? "bg-[#0000ff]" : "bg-gray-400/30"
                                        }`}
                                    />
                                </div>
                            </div>
                        ))}
                    </div>
                </div>

                {/* Right: Image */}
                <div ref={imageRef} className="w-full md:w-1/2 flex justify-center items-center">
                    <Image
                        src={image}
                        alt={title}
                        width={1500}
                        height={1500}
                        className="w-full h-auto max-h-[400px] object-contain mr-10 md:mr-0"
                        priority={current === 0}
                    />
                </div>
            </div>
        </section>
    );
};

export default Offers;
