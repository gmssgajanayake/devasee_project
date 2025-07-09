"use client";

import { useRef, useState, useEffect } from "react";
import gsap from "gsap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMinus, faPlus } from "@fortawesome/free-solid-svg-icons";

interface FilterSectionProps {
    title: string;
    children: React.ReactNode;
    isOpen: boolean;
    onClick: () => void;
}

function FilterSection({ title, children, isOpen, onClick }: FilterSectionProps) {
    const contentRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (contentRef.current) {
            if (isOpen) {
                gsap.fromTo(
                    contentRef.current,
                    { height: 0, opacity: 0 },
                    {
                        height: "auto",
                        opacity: 1,
                        duration: 0.4,
                        ease: "power2.out",
                        display: "block"
                    }
                );
            } else {
                gsap.to(contentRef.current, {
                    height: 0,
                    opacity: 0,
                    duration: 0.3,
                    ease: "power2.in",
                    onComplete: () => {
                        if (contentRef.current) {
                            contentRef.current.style.display = "none";
                        }
                    }
                });
            }
        }
    }, [isOpen]);

    return (
        <div className="w-full flex flex-col justify-center">
            <div
                className="flex border-b border-gray-400/30 py-4 px-1 items-center justify-between cursor-pointer"
                onClick={onClick}
            >
                <p className={`font-bold ${isOpen ? "text-[#0000ff]" : "text-[#2b216d]"}`}>
                    {title}
                </p>
                <FontAwesomeIcon
                    className={`${isOpen ? "text-[#0000ff]" : "text-[#2b216d]"}`}
                    icon={isOpen ? faMinus : faPlus}
                />
            </div>
            <div ref={contentRef} style={{ overflow: "hidden", height: 0, display: "none" }}>
                <div className="px-2 py-3">{children}</div>
            </div>
        </div>
    );
}

export default function FilterBar() {
    const [activeSection, setActiveSection] = useState<string | null>(null);
    const [priceRange, setPriceRange] = useState<[number, number]>([0, 5000]);

    const toggleSection = (section: string) => {
        setActiveSection(prev => (prev === section ? null : section));
    };

    return (
        <div className="w-1/4 h-screen flex flex-col items-center p-8 overflow-y-auto">
            {/* Price Section */}
            <FilterSection
                title="Price"
                isOpen={activeSection === "price"}
                onClick={() => toggleSection("price")}
            >
                <div className="flex flex-col gap-4">
                    <div className="flex justify-between text-sm text-gray-700 font-medium">
                        <span>Rs. {priceRange[0]}</span>
                        <span>Rs. {priceRange[1]}</span>
                    </div>

                    <div className="relative  w-full">
                        {/* Background Bar */}
                        <div className="absolute h-1 bg-gray-300 rounded w-full top-[27px] transform -translate-y-1/2 z-0" />

                        {/* Selected Range Segment */}
                        <div
                            className="absolute h-1 bg-blue-600 rounded top-[27px] transform -translate-y-1/2 z-10"
                            style={{
                                left: `${(priceRange[0] / 5000) * 100}%`,
                                width: `${((priceRange[1] - priceRange[0]) / 5000) * 100}%`,
                            }}
                        />

                        {/* Min Slider */}
                        <input
                            type="range"
                            min={0}
                            max={5000}
                            step={100}
                            value={priceRange[0]}
                            onChange={(e) => {
                                const newMin = Math.min(Number(e.target.value), priceRange[1] - 100);
                                setPriceRange([newMin, priceRange[1]]);
                            }}
                            className="absolute w-full appearance-none bg-transparent z-20 pointer-events-auto"
                        />

                        {/* Max Slider */}
                        <input
                            type="range"
                            min={0}
                            max={5000}
                            step={100}
                            value={priceRange[1]}
                            onChange={(e) => {
                                const newMax = Math.max(Number(e.target.value), priceRange[0] + 100);
                                setPriceRange([priceRange[0], newMax]);
                            }}
                            className="absolute w-full appearance-none bg-transparent z-20 pointer-events-auto"
                        />
                    </div>
                </div>
            </FilterSection>

            {/* Product Type */}
            <FilterSection
                title="Product type"
                isOpen={activeSection === "productType"}
                onClick={() => toggleSection("productType")}
            >
                <div className="flex flex-col gap-1">
                    <label><input type="checkbox" /> Books</label>
                    <label><input type="checkbox" /> Stationery</label>
                    <label><input type="checkbox" /> Accessories</label>
                </div>
            </FilterSection>

            {/* Availability */}
            <FilterSection
                title="Availability"
                isOpen={activeSection === "availability"}
                onClick={() => toggleSection("availability")}
            >
                <div className="flex flex-col gap-1">
                    <label><input type="checkbox" /> In Stock</label>
                    <label><input type="checkbox" /> Out of Stock</label>
                </div>
            </FilterSection>

            {/* Brand */}
            <FilterSection
                title="Brand"
                isOpen={activeSection === "brand"}
                onClick={() => toggleSection("brand")}
            >
                <div className="flex flex-col gap-1">
                    <label><input type="checkbox" /> Devasee</label>
                    <label><input type="checkbox" /> Other</label>
                </div>
            </FilterSection>

            {/* Color */}
            <FilterSection
                title="Color"
                isOpen={activeSection === "color"}
                onClick={() => toggleSection("color")}
            >
                <div className="flex flex-col gap-1">
                    <label><input type="checkbox" /> Red</label>
                    <label><input type="checkbox" /> Blue</label>
                    <label><input type="checkbox" /> Black</label>
                </div>
            </FilterSection>

            {/* Material */}
            <FilterSection
                title="Material"
                isOpen={activeSection === "material"}
                onClick={() => toggleSection("material")}
            >
                <div className="flex flex-col gap-1">
                    <label><input type="checkbox" /> Paper</label>
                    <label><input type="checkbox" /> Plastic</label>
                    <label><input type="checkbox" /> Cloth</label>
                </div>
            </FilterSection>
        </div>
    );
}
