"use client";

import { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMinus, faPlus } from "@fortawesome/free-solid-svg-icons";
import { useEffect, useRef } from "react";
import gsap from "gsap";

interface FilterSectionProps {
    title: string;
    isOpen: boolean;
    onClick: () => void;
    children: React.ReactNode;
}

function FilterSection({ title, isOpen, onClick, children }: FilterSectionProps) {
    const contentRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (!contentRef.current) return;

        if (isOpen) {
            gsap.fromTo(
                contentRef.current,
                { height: 0, opacity: 0, display: "none" },
                {
                    height: "auto",
                    opacity: 1,
                    display: "block",
                    duration: 0.4,
                    ease: "power2.out",
                }
            );
        } else {
            gsap.to(contentRef.current, {
                height: 0,
                opacity: 0,
                duration: 0.3,
                ease: "power2.in",
                onComplete: () => {
                    if (contentRef.current) contentRef.current.style.display = "none";
                },
            });
        }
    }, [isOpen]);

    return (
        <div className="w-full flex flex-col justify-center">
            <div
                className="flex border-b border-gray-400/30 py-4 px-1 items-center justify-between cursor-pointer"
                onClick={onClick}
            >
                <p className={`font-bold ${isOpen ? "text-[#0000ff]" : "text-[#2b216d]"}`}>{title}</p>
                <FontAwesomeIcon
                    className={`${isOpen ? "text-[#0000ff]" : "text-[#2b216d]"}`}
                    icon={isOpen ? faMinus : faPlus}
                />
            </div>
            <div
                ref={contentRef}
                style={{ overflow: "hidden", height: 0, display: "none" }}
                className="px-2 py-3"
            >
                {children}
            </div>
        </div>
    );
}

interface FilterBarProps {
    priceRange: [number, number];
    setPriceRange: (range: [number, number]) => void;
    selectedTypes: string[];
    setSelectedTypes: (types: string[]) => void;
    selectedBrands: string[];
    setSelectedBrands: (brands: string[]) => void;
}

export default function FilterBar({
                                      priceRange,
                                      setPriceRange,
                                      selectedTypes,
                                      setSelectedTypes,
                                      selectedBrands,
                                      setSelectedBrands,
                                  }: FilterBarProps) {
    const [activeSection, setActiveSection] = useState<string | null>("price");

    const toggleSection = (section: string) => {
        setActiveSection((prev) => (prev === section ? null : section));
    };

    const toggleCheckbox = (
        value: string,
        current: string[],
        setter: (values: string[]) => void
    ) => {
        if (current.includes(value)) {
            setter(current.filter((v) => v !== value));
        } else {
            setter([...current, value]);
        }
    };

    return (
        <div className="w-full flex flex-col items-center p-6">
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

                    <div className="relative w-full">
                        <div className="absolute h-1 bg-gray-300 rounded w-full top-[27px] transform -translate-y-1/2 z-0" />

                        <div
                            className="absolute h-1 bg-blue-600 rounded top-[27px] transform -translate-y-1/2 z-10"
                            style={{
                                left: `${(priceRange[0] / 5000) * 100}%`,
                                width: `${((priceRange[1] - priceRange[0]) / 5000) * 100}%`,
                            }}
                        />

                        <input
                            type="range"
                            min={0}
                            max={5000}
                            step={100}
                            value={priceRange[0]}
                            onChange={(e) => {
                                const val = Math.min(Number(e.target.value), priceRange[1] - 100);
                                setPriceRange([val, priceRange[1]]);
                            }}
                            className="absolute w-full appearance-none bg-transparent z-20 pointer-events-auto"
                        />

                        <input
                            type="range"
                            min={0}
                            max={5000}
                            step={100}
                            value={priceRange[1]}
                            onChange={(e) => {
                                const val = Math.max(Number(e.target.value), priceRange[0] + 100);
                                setPriceRange([priceRange[0], val]);
                            }}
                            className="absolute w-full appearance-none bg-transparent z-20 pointer-events-auto"
                        />
                    </div>
                </div>
            </FilterSection>

            <FilterSection
                title="Product type"
                isOpen={activeSection === "productType"}
                onClick={() => toggleSection("productType")}
            >
                {["Books", "Stationery", "Accessories","Translations"].map((type) => (
                    <label key={type} className="block cursor-pointer">
                        <input
                            type="checkbox"
                            checked={selectedTypes.includes(type)}
                            onChange={() => toggleCheckbox(type, selectedTypes, setSelectedTypes)}
                            className="mr-2"
                        />
                        {type}
                    </label>
                ))}
            </FilterSection>

            <FilterSection
                title="Brand"
                isOpen={activeSection === "brand"}
                onClick={() => toggleSection("brand")}
            >
                {["Devasee", "Other"].map((brand) => (
                    <label key={brand} className="block cursor-pointer">
                        <input
                            type="checkbox"
                            checked={selectedBrands.includes(brand)}
                            onChange={() => toggleCheckbox(brand, selectedBrands, setSelectedBrands)}
                            className="mr-2"
                        />
                        {brand}
                    </label>
                ))}
            </FilterSection>
        </div>
    );
}