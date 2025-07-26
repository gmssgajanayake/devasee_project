"use client";

import { useLayoutEffect, useRef } from "react";
import Image from "next/image";
import gsap from "gsap";
import { ScrollTrigger } from "gsap/ScrollTrigger";
import SubNavBar from "@/app/(router)/_components/SubNavBar";

gsap.registerPlugin(ScrollTrigger);

const galleryImages = [
    {
        src: "https://images.unsplash.com/photo-1495640388908-05fa85288e61?auto=format&fit=crop&w=1200&q=80",
        alt: "Bookstore interior with shelves of books",
    },
    {
        src: "https://images.unsplash.com/photo-1507842217343-583bb7270b66?auto=format&fit=crop&w=1200&q=80",
        alt: "Person reading in a cozy library",
    },
    {
        src: "https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=1200&q=80",
        alt: "Creative stationery arrangement",
    },
    {
        src: "https://images.unsplash.com/photo-1491841550275-ad7854e35ca6?auto=format&fit=crop&w=1200&q=80",
        alt: "Printing equipment in our workshop",
    },
    {
        src: "https://images.unsplash.com/photo-1491975474562-1f4e30bc9468?auto=format&fit=crop&w=1200&q=80",
        alt: "Close-up of high-quality printed materials",
    },
    {
        src: "https://images.unsplash.com/photo-1532012197267-da84d127e765?auto=format&fit=crop&w=1200&q=80",
        alt: "Organized desk with books and stationery",
    },
];

export default function AboutPage() {
    const component = useRef<HTMLDivElement>(null);
    const journeyRef = useRef<HTMLDivElement>(null);
    const chooseRef = useRef<HTMLDivElement>(null);
    const bubblesContainer = useRef<HTMLDivElement>(null);

    const safeGSAP = (cb: () => void) => {
        if ('requestIdleCallback' in window) {
            (window as Window & { requestIdleCallback: (cb: () => void) => void }).requestIdleCallback(cb);
        } else {
            setTimeout(cb, 200);
        }
    };

    useLayoutEffect(() => {
        const ctx = gsap.context(() => {
            const createBubbles = () => {
                if (!bubblesContainer.current) return;
                bubblesContainer.current.innerHTML = "";

                const bubbleCount = 6;
                for (let i = 0; i < bubbleCount; i++) {
                    const bubble = document.createElement("div");
                    bubble.className = "absolute rounded-full bg-blue-500/30";
                    const size = Math.random() * 40 + 30;
                    bubble.style.width = `${size}px`;
                    bubble.style.height = `${size}px`;
                    bubble.style.left = `${Math.random() * 100}%`;
                    bubble.style.top = `${Math.random() * 100}%`;
                    bubble.style.willChange = "transform, opacity";
                    bubblesContainer.current.appendChild(bubble);

                    const duration = Math.random() * 4 + 3;
                    const distance = Math.random() * 200 + 100;

                    gsap.to(bubble, {
                        x: `+=${Math.random() > 0.5 ? distance : -distance}`,
                        y: `+=${Math.random() > 0.5 ? distance : -distance}`,
                        rotation: Math.random() * 360,
                        scale: Math.random() * 0.5 + 0.8,
                        duration: duration,
                        repeat: -1,
                        yoyo: true,
                        ease: "sine.inOut",
                    });
                }
            };

            safeGSAP(() => createBubbles());

            gsap.utils.toArray<HTMLElement>("[data-anim]").forEach((el) => {
                gsap.set(el, { y: 50, opacity: 0 });
                ScrollTrigger.create({
                    trigger: el,
                    start: "top 90%",
                    onEnter: () => {
                        gsap.to(el, {
                            y: 0,
                            opacity: 1,
                            duration: 0.8,
                            ease: "power3.out",
                            overwrite: "auto",
                        });
                    },
                    once: true,
                });
            });

            if (journeyRef.current) {
                gsap.set(journeyRef.current, { x: -80, opacity: 0 });
                ScrollTrigger.create({
                    trigger: journeyRef.current,
                    start: "top 85%",
                    onEnter: () => {
                        gsap.to(journeyRef.current, {
                            x: 0,
                            opacity: 1,
                            duration: 1,
                            ease: "expo.out",
                            overwrite: "auto",
                        });
                    },
                    once: true,
                });
            }

            if (chooseRef.current) {
                gsap.set(chooseRef.current, { x: 80, opacity: 0 });
                ScrollTrigger.create({
                    trigger: chooseRef.current,
                    start: "top 85%",
                    onEnter: () => {
                        gsap.to(chooseRef.current, {
                            x: 0,
                            opacity: 1,
                            duration: 1,
                            ease: "expo.out",
                            overwrite: "auto",
                        });
                    },
                    once: true,
                });
            }
        }, component);

        return () => ctx.revert();
    }, []);

    useLayoutEffect(() => {
        const galleryContainer = document.querySelector<HTMLElement>(".gallery-container");
        const galleryWrapper = document.querySelector<HTMLElement>(".gallery-wrapper");

        if (!galleryContainer || !galleryWrapper) return;

        const amountToScroll = galleryWrapper.scrollWidth - galleryContainer.clientWidth;

        const galleryTween = gsap.to(galleryWrapper, {
            x: -amountToScroll,
            ease: "none",
        });

        ScrollTrigger.create({
            trigger: galleryContainer,
            start: "top top",
            end: () => `+=${amountToScroll}`,
            pin: true,
            scrub: 1,
            animation: galleryTween,
            invalidateOnRefresh: true,
            onUpdate: (self) => {
                const progress = self.progress.toFixed(2);
                document.documentElement.style.setProperty("--scroll-progress", progress);
            },
        });

        gsap.utils.toArray(".gallery-item").forEach((item, i) => {
            if (i === 0) return;
            const galleryItem = item as HTMLElement;
            gsap.set(galleryItem, { opacity: 0, y: 30 });
            ScrollTrigger.create({
                trigger: galleryItem,
                start: "left 80%",
                end: "left 20%",
                containerAnimation: galleryTween,
                onEnter: () =>
                    gsap.to(galleryItem, {
                        opacity: 1,
                        y: 0,
                        duration: 0.7,
                        delay: 0.2 * (i % 3),
                        ease: "sine.out",
                    }),
                once: true,
            });
        });

        return () => {
            galleryTween.kill();
            ScrollTrigger.getAll().forEach(trigger => trigger.kill());
        };
    }, []);

    return (
        <div className="bg-gradient-to-b from-white to-blue-50 min-h-screen overflow-x-hidden relative">
            <div
                ref={bubblesContainer}
                className="fixed inset-0 pointer-events-none overflow-hidden z-0"
            />

            <SubNavBar path="ABOUT" />
            <div ref={component}>
                <div className="fixed top-0 left-1/2 transform -translate-x-1/2 w-64 h-1 bg-blue-100 rounded-full z-50">
                    <div
                        className="h-full bg-blue-600 rounded-full transition-all duration-300"
                        style={{
                            width: `calc(var(--scroll-progress, 0) * 100%)`,
                            maxWidth: "100%",
                        }}
                    />
                </div>

                {/* About Section */}
                <section className="max-w-6xl mx-auto px-4 py-16 relative z-10">
                    <div className="text-center space-y-6 mb-20">
                        <div data-anim className="text-blue-600 text-sm tracking-widest font-semibold uppercase">
                            About Us
                        </div>
                        <h4 data-anim className="text-4xl md:text-6xl font-bold text-[#1e205a]">
                            Crafting Solutions for Your Success
                        </h4>
                        <p data-anim className="text-xl text-[#1e205a]/60 font-light max-w-3xl mx-auto">
                            Devasee is Sri Lanka&#39;s premier destination for books, stationery, and bespoke printing.
                            We are dedicated to delivering quality and fostering innovation for all your educational and creative endeavors.
                        </p>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mt-16">
                        <div ref={journeyRef} className="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-all duration-300 border border-blue-50 relative z-10">
                            <h2 className="text-2xl font-bold text-blue-600 mb-4">Our Journey</h2>
                            <p className="text-gray-700 leading-relaxed">
                                What started as a small local bookstore has now evolved into a trusted brand across Sri Lanka.
                                With a mission to provide the finest educational and printing solutions, Devasee continues to grow with its community.
                            </p>
                        </div>
                        <div ref={chooseRef} className="bg-white p-8 rounded-2xl shadow-lg hover:shadow-xl transition-all duration-300 border border-blue-50 relative z-10">
                            <h2 className="text-2xl font-bold text-blue-600 mb-4">Why Choose Us?</h2>
                            <p className="text-gray-700 leading-relaxed">
                                Our dedication to quality, personalized service, and customer satisfaction sets us apart.
                                We source the best materials and use cutting-edge technology to ensure superior results every time.
                            </p>
                        </div>
                    </div>
                </section>

                {/* Gallery */}
                <section className="gallery-container h-screen flex items-center overflow-hidden relative z-10">
                    <div className="gallery-wrapper flex gap-6 md:gap-8 py-8 pl-8">
                        <div className="gallery-item bg-blue-100/60 px-6 rounded-2xl flex-shrink-0 w-[35vw] md:w-[30vw] flex flex-col justify-center text-left">
                            <h2 className="text-3xl lg:text-5xl font-bold text-[#1e205a] mb-4">Our Gallery</h2>
                            <p className="text-[#1e205a]/60 lg:text-xl">
                                A glimpse into our world of books, stationery, and creative spaces.
                            </p>
                        </div>

                        {galleryImages.map((image, index) => (
                            <div
                                key={index}
                                className="gallery-item rounded-xl shadow-md hover:shadow-2xl transition-all duration-300 overflow-hidden flex-shrink-0 w-[70vw] xs:w-[60vw] sm:w-[50vw] md:w-[400px] h-[50vh] md:h-[55vh]"
                            >
                                <Image
                                    src={image.src}
                                    alt={image.alt}
                                    width={600}
                                    height={800}
                                    className="w-full h-full object-cover"
                                    placeholder="blur"
                                    blurDataURL="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAwIiBoZWlnaHQ9IjgwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjZjVmNWY1Ii8+PC9zdmc+"
                                    {...(index === 0
                                        ? { priority: true }
                                        : { loading: "lazy" })}
                                />

                            </div>
                        ))}

                        <div className="gallery-item flex-shrink-0 w-16 md:w-24"></div>
                    </div>
                </section>

                {/* Closing Section */}
                <div className="h-[40vh] w-screen relative overflow-hidden z-10">
                    <div className="absolute inset-0 w-screen flex items-center justify-center">
                        <div className="max-w-5xl w-screen flex gap-6 flex-col mx-auto text-center px-4">
                            <h4 data-anim className="text-4xl md:text-6xl font-bold text-[#1e205a]">
                                We Believe in Growing Together
                            </h4>
                            <p data-anim className="text-xl text-[#1e205a]/60 font-light max-w-3xl mx-auto">
                                At Devasee, our story is your story. We innovate, serve, and grow — together.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
