"use client";

import { useLayoutEffect, useRef } from "react";
import gsap from "gsap";
import { ScrollTrigger } from "gsap/ScrollTrigger";
import SubNavBar from "@/app/(router)/_components/SubNavBar";

gsap.registerPlugin(ScrollTrigger);

// Gallery images
const galleryImages = [
    { src: "https://images.unsplash.com/photo-1495640388908-05fa85288e61?auto=format&fit=crop&w=600&q=80" },
    { src: "https://images.unsplash.com/photo-1507842217343-583bb7270b66?auto=format&fit=crop&w=600&q=80" },
    { src: "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=600&q=80" },
    { src: "https://images.unsplash.com/photo-1491841550275-ad7854e35ca6?auto=format&fit=crop&w=600&q=80" },
    { src: "https://images.unsplash.com/photo-1491975474562-1f4e30bc9468?auto=format&fit=crop&w=600&q=80" },
];

export default function AboutPage() {
    const elementsRef = useRef<(HTMLDivElement | null)[]>([]);
    const galleryItemsRef = useRef<HTMLDivElement[]>([]);
    const sectionRef = useRef<HTMLDivElement>(null);

    useLayoutEffect(() => {
        const ctx = gsap.context(() => {
            // Header and content fade-in
            gsap.from(elementsRef.current, {
                y: 40,
                opacity: 0,
                stagger: 0.15,
                duration: 0.8,
                ease: "power3.out",
                delay: 0.2,
            });

            // Gallery items animation
            gsap.from(galleryItemsRef.current, {
                y: 50,
                opacity: 0,
                stagger: 0.15,
                duration: 0.8,
                ease: "power2.out",
                scrollTrigger: {
                    trigger: sectionRef.current,
                    start: "top 70%",
                },
            });
        }, sectionRef);

        return () => ctx.revert();
    }, []);

    const addToRefs = (el: HTMLDivElement | null) => {
        if (el && !elementsRef.current.includes(el)) {
            elementsRef.current.push(el);
        }
    };


    return (
        <div className="bg-gradient-to-b from-white to-blue-50 min-h-screen overflow-hidden">
            <SubNavBar path="ABOUT" />

            <section className="max-w-6xl mx-auto px-4 py-16" ref={sectionRef}>
                <div className="text-center space-y-6 mb-16">
                    <div
                        ref={addToRefs}
                        className="text-blue-600 text-sm tracking-widest font-semibold uppercase"
                    >
                        About Us
                    </div>

                    <h1
                        ref={addToRefs}
                        className="text-4xl md:text-5xl font-bold text-[#1e205a]"
                    >
                        We Craft Solutions for Your Educational & Creative Needs
                    </h1>

                    <p
                        ref={addToRefs}
                        className="text-xl text-[#1e205a]/60 italic font-light max-w-2xl mx-auto"
                    >
                        Devasee is Sri Lanka's go-to for books, stationery, and custom printing.
                        Quality and innovation at your service.
                    </p>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mt-16">
                    <div data-aos="fade-right" className="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-all duration-300 border border-blue-50">
                        <h2 className="text-2xl font-bold text-blue-600 mb-4">Our Journey</h2>
                        <p className="text-gray-700 leading-relaxed">
                            What started as a small local bookstore has now evolved into a trusted brand across Sri Lanka.
                            With a mission to provide the finest educational and printing solutions, Devasee continues to
                            grow with its community. We've expanded our offerings while maintaining our commitment to
                            quality and personalized service.
                        </p>
                    </div>

                    <div data-aos="fade-right" className="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-all duration-300 border border-blue-50">
                        <h2 className="text-2xl font-bold text-blue-600 mb-4">Why Choose Us?</h2>
                        <p className="text-gray-700 leading-relaxed">
                            Our dedication to quality, personalized service, and customer satisfaction has set us apart.
                            Whether you're a student, educator, or entrepreneur, we tailor our offerings to meet your
                            unique needs. We source the best materials and use cutting-edge printing technology to
                            ensure superior results every time.
                        </p>
                    </div>
                </div>

                <div ref={addToRefs} className="text-center mt-20 py-12 px-4 bg-blue-50 rounded-2xl">
                    <h2 className="text-3xl font-bold text-[#1e205a] mb-4">
                        We Believe in Growth Together
                    </h2>
                    <p className="text-[#1e205a] max-w-2xl mx-auto text-lg">
                        At Devasee, our story is your story. We innovate, serve, and grow — together.
                    </p>
                </div>

                <div className="mt-20">
                    <h2 ref={addToRefs} className="text-3xl font-bold text-[#1e205a] text-center mb-4">
                        Our Gallery
                    </h2>
                    <p ref={addToRefs} className="text-blue-600 text-center max-w-2xl mx-auto mb-12 text-lg">
                        A glimpse into our world of books, stationery, and creative spaces
                    </p>

                    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
                        {galleryImages.map((image, index) => (
                            <div
                                key={index}
                                data-aos="flip-left"
                                className="rounded-xl shadow-md hover:shadow-xl transition-all duration-300 overflow-hidden"
                            >
                                <img
                                    src={image.src}
                                    alt={`Devasee Gallery ${index + 1}`}
                                    className="w-full h-[220px] object-cover rounded-xl"
                                />
                            </div>
                        ))}
                    </div>
                </div>
            </section>
        </div>
    );
}
