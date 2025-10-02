"use client";

import { useEffect, useRef, useState } from "react";
import gsap from "gsap";
import SubNavBar from "@/app/(router)/_components/SubNavBar";
import FilterBar from "@/app/(router)/products/_components/FilterBar";
import { StaticImageData } from "next/image";

import printingService1 from "@/assets/items image/img.png";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
    faArrowUpWideShort,
    faSliders,
    faXmark,
    faSearch,
    faAngleDown,
} from "@fortawesome/free-solid-svg-icons";

// Types
interface PrintingService {
    id: string;
    itemName: string;
    type: string;
    brand: string;
    price: number;
    image: string | StaticImageData;
}

interface CartContextType {
    addToCart: (item: PrintingService) => void;
    removeFromCart: (id: string) => void;
    cartItems: PrintingService[];
}

// Mock data for printing services
const mockPrintingServices: PrintingService[] = [
    {
        id: "1",
        itemName: "Business Cards",
        type: "Business Printing",
        brand: "Devasee",
        price: 500,
        image: printingService1,
    },
    {
        id: "2", 
        itemName: "Banners",
        type: "Large Format",
        brand: "Devasee",
        price: 1500,
        image: printingService1,
    },
    {
        id: "3",
        itemName: "Brochures",
        type: "Marketing Materials",
        brand: "Devasee", 
        price: 750,
        image: printingService1,
    },
    {
        id: "4",
        itemName: "Custom Mugs",
        type: "Promotional Items",
        brand: "Other",
        price: 300,
        image: printingService1,
    },
    {
        id: "5",
        itemName: "T-Shirt Printing",
        type: "Apparel",
        brand: "Devasee",
        price: 800,
        image: printingService1,
    },
];

// Mock cart context
const useCart = (): CartContextType => ({
    addToCart: () => {},
    removeFromCart: () => {},
    cartItems: [],
});

export default function PrintingServicesPage() {
    const [priceRange, setPriceRange] = useState<[number, number]>([0, 5000]);
    const [selectedTypes, setSelectedTypes] = useState<string[]>([]);
    const [selectedBrands, setSelectedBrands] = useState<string[]>([]);
    const [sortBy, setSortBy] = useState<"itemName" | "price">("itemName");
    const [showMobileFilter, setShowMobileFilter] = useState(false);
    const [showSortModal, setShowSortModal] = useState(false);
    const [searchQuery, setSearchQuery] = useState("");
    const filterRef = useRef<HTMLDivElement>(null);

    const { addToCart, removeFromCart, cartItems } = useCart();

    const [printingServices, setPrintingServices] = useState<PrintingService[]>([]);
    const [loading, setLoading] = useState(true);
    const [isMounted, setIsMounted] = useState(false);

    useEffect(() => {
        async function fetchPrintingServices() {
            setLoading(true);
            // Simulate API call
            setTimeout(() => {
                setPrintingServices(mockPrintingServices);
                setLoading(false);
            }, 1000);
        }
        fetchPrintingServices()
    }, []);

    useEffect(() => {
        if (filterRef.current) {
            gsap.fromTo(
                filterRef.current,
                { y: -50, opacity: 0 },
                { y: 0, opacity: 1, duration: 0.8, ease: "power3.out" }
            );
        }
        if (typeof window !== "undefined") {
            window.scrollTo({ top: 0, behavior: "auto" });
        }
    }, []);

    useEffect(() => {
        if (showMobileFilter || showSortModal) {
            document.body.style.overflow = "hidden";
        } else {
            document.body.style.overflow = "auto";
        }
        return () => {
            document.body.style.overflow = "auto";
        };
    }, [showMobileFilter, showSortModal]);

    useEffect(() => {
        setIsMounted(true);
    }, []);

    if (!isMounted) {
        return null;
    }

    const filteredPrintingServices = printingServices
        .filter((service) => {
            const inPriceRange = service.price >= priceRange[0] && service.price <= priceRange[1];
            const matchesType = selectedTypes.length === 0 || selectedTypes.includes(service.type);
            const matchesBrand = selectedBrands.length === 0 || selectedBrands.includes(service.brand);
            const matchesSearch = service.itemName.toLowerCase().includes(searchQuery.toLowerCase());
            return inPriceRange && matchesType && matchesBrand && matchesSearch;
        })
        .sort((a, b) => {
            if (sortBy === "itemName") return a.itemName.localeCompare(b.itemName);
            return a.price - b.price;
        });

    if (!isMounted || loading) {
        return (
            <div className="w-full h-[calc(100vh-80px)] flex justify-center items-center">
                <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    return (
        <>
            <div className="w-full">
                <SubNavBar path="PRINTING SERVICES"/>

                {/* Search Bar */}
                <div className="w-full px-4 py-3 lg:mt-4 flex justify-center items-center ">
                    <div className="relative w-full max-w-xl">
                        <input
                            type="text"
                            placeholder="Search printing services..."
                            className="w-full px-4 text-gray-700 py-3 border border-gray-300 rounded-lg focus:outline-none "
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                        />
                        <FontAwesomeIcon
                            icon={faSearch}
                            className="absolute cursor-pointer right-4 top-1/2 transform -translate-y-1/2 text-gray-400"
                        />
                    </div>
                </div>

                {/* Mobile Filter and Sort */}
                <div className="w-full lg:hidden flex justify-center items-center">
                    <div
                        onClick={() => setShowMobileFilter(true)}
                        className="w-1/2 flex cursor-pointer justify-center items-center border-r border-gray-400/30 bg-gray-100/50 gap-3 px-4 py-3"
                    >
                        <FontAwesomeIcon className="rotate-90 text-[#2b216d]" icon={faSliders}/>
                        <p className="font-bold text-[#2b216d]">FILTER</p>
                    </div>
                    <div
                        onClick={() => setShowSortModal(true)}
                        className="w-1/2 flex gap-3 cursor-pointer justify-center items-center bg-gray-100/50 px-4 py-3"
                    >
                        <FontAwesomeIcon className="rotate-180 text-[#2b216d]" icon={faArrowUpWideShort}/>
                        <p className="font-bold text-[#2b216d]">SORT</p>
                    </div>
                </div>

                {/* Filter + Service Grid */}
                <div ref={filterRef} className="flex w-full h-[calc(100vh-80px)]">
                    <div className="w-[280px] lg:flex hidden shrink-0 h-full overflow-y-auto hide-scrollbar">
                        <FilterBar
                            priceRange={priceRange}
                            setPriceRange={setPriceRange}
                            selectedTypes={selectedTypes}
                            setSelectedTypes={setSelectedTypes}
                            selectedBrands={selectedBrands}
                            setSelectedBrands={setSelectedBrands}
                        />
                    </div>
                    <div className="flex-1 overflow-y-auto h-full hide-scrollbar">
                        <PrintingContainer
                            printingServices={filteredPrintingServices}
                            sortBy={sortBy}
                            setSortBy={setSortBy}
                            addToCart={addToCart}
                            cartItems={cartItems}
                            removeFromCart={removeFromCart}
                        />
                    </div>
                </div>

                {/* Mobile Filter Modal */}
                {showMobileFilter && (
                    <div className="fixed inset-0 z-50 bg-black/40 flex lg:hidden">
                        <div className="bg-white w-[60%] h-full pt-16 shadow-lg relative overflow-y-auto">
                            <div className="flex justify-between items-center px-4 mb-4">
                                <h2 className="font-bold text-lg text-[#2b216d]">Filters</h2>
                                <FontAwesomeIcon
                                    icon={faXmark}
                                    className="text-2xl text-[#2b216d] cursor-pointer"
                                    onClick={() => setShowMobileFilter(false)}
                                />
                            </div>
                            <FilterBar
                                priceRange={priceRange}
                                setPriceRange={setPriceRange}
                                selectedTypes={selectedTypes}
                                setSelectedTypes={setSelectedTypes}
                                selectedBrands={selectedBrands}
                                setSelectedBrands={setSelectedBrands}
                            />
                        </div>
                        <div className="flex-1" onClick={() => setShowMobileFilter(false)}/>
                    </div>
                )}

                {/* Sort Modal */}
                {showSortModal && (
                    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 lg:hidden">
                        <div className="w-[60%] max-w-sm rounded-xl shadow-xl p-6 relative bg-white">
                            <div className="flex justify-between items-center mb-4">
                                <h2 className="font-bold text-lg text-[#2b216d]">Sort Options</h2>
                                <FontAwesomeIcon
                                    icon={faXmark}
                                    className="text-2xl text-[#2b216d] cursor-pointer"
                                    onClick={() => setShowSortModal(false)}
                                />
                            </div>
                            <div className="space-y-3">
                                {["itemName", "price"].map((option) => (
                                    <button
                                        key={option}
                                        className={`w-full font-bold text-left p-2 rounded ${
                                            sortBy === option ? "bg-blue-100 text-blue-600" : "text-[#2b216d]"
                                        }`}
                                        onClick={() => {
                                            setSortBy(option as "itemName" | "price");
                                            setShowSortModal(false);
                                        }}
                                    >
                                        {option === "itemName"
                                            ? "Alphabetically, A-Z"
                                            : "Price: Low to High"}
                                    </button>
                                ))}
                            </div>
                        </div>
                    </div>
                )}
            </div>
        </>
    );
}

// Custom Container component for printing services
interface PrintingContainerProps {
    printingServices: PrintingService[];
    sortBy: "itemName" | "price";
    setSortBy: (value: "itemName" | "price") => void;
    addToCart: (service: PrintingService) => void;
    removeFromCart: (id: string) => void;
    cartItems: PrintingService[];
}

const ITEMS_PER_PAGE = 24;

function PrintingContainer({
    printingServices,
    sortBy,
    setSortBy,
    addToCart,
    removeFromCart,
    cartItems,
}: PrintingContainerProps) {
    const [currentPage, setCurrentPage] = useState(1);
    const [sortedServices, setSortedServices] = useState<PrintingService[]>([]);

    useEffect(() => {
        const sorted = [...printingServices];
        if (sortBy === "itemName") {
            sorted.sort((a, b) => a.itemName.localeCompare(b.itemName));
        } else if (sortBy === "price") {
            sorted.sort((a, b) => a.price - b.price);
        }
        setSortedServices(sorted);
        setCurrentPage(1);
    }, [printingServices, sortBy]);

    const totalPages = Math.ceil(sortedServices.length / ITEMS_PER_PAGE);
    const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
    const currentServices = sortedServices.slice(
        startIndex,
        startIndex + ITEMS_PER_PAGE
    );

    const goToPage = (page: number) => {
        if (page >= 1 && page <= totalPages) {
            setCurrentPage(page);
            if (typeof window !== "undefined") {
                window.scrollTo({ top: 0, behavior: "smooth" });
            }
        }
    };

    return (
        <div className="w-full py-10 px-6 sm:px-12">
            <PrintingSortOptions sortBy={sortBy} setSortBy={setSortBy} />

            <div className="grid gap-2 grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 2xl:grid-cols-7">
                {currentServices.map((service) => (
                    <div key={service.id} className="w-full">
                        <PrintingItemCard
                            image={service.image}
                            itemName={service.itemName}
                            price={service.price}
                            onAddToCart={() => addToCart(service)}
                            onRemoveFromCart={() => removeFromCart(service.id)}
                            isInCart={cartItems.some((item) => item.id === service.id)}
                        />
                    </div>
                ))}
            </div>

            <div className="flex justify-center items-center gap-3 flex-wrap mt-10">
                <button
                    onClick={() => goToPage(currentPage - 1)}
                    disabled={currentPage === 1}
                    className="w-10 h-10 flex justify-center items-center bg-[#0000ff] text-white rounded-full disabled:bg-[#c7defe]"
                >
                    &#8592;
                </button>

                {[...Array(totalPages)].map((_, i) => (
                    <button
                        key={i}
                        onClick={() => goToPage(i + 1)}
                        className={`w-10 h-10 text-sm font-medium flex items-center justify-center ${
                            currentPage === i + 1
                                ? "bg-[#0000ff] text-white"
                                : "bg-white border border-gray-300 text-gray-700"
                        } rounded-full transition-all duration-200`}
                    >
                        {i + 1}
                    </button>
                ))}

                <button
                    onClick={() => goToPage(currentPage + 1)}
                    disabled={currentPage === totalPages}
                    className="w-10 h-10 flex justify-center items-center bg-[#0000ff] text-white rounded-full disabled:bg-[#c7defe]"
                >
                    &#8594;
                </button>
            </div>
        </div>
    );
}

// Custom Sort Options component for printing services
interface PrintingSortOptionsProps {
    sortBy: "itemName" | "price";
    setSortBy: (value: "itemName" | "price") => void;
}

function PrintingSortOptions({ sortBy, setSortBy }: PrintingSortOptionsProps) {
    return (
        <div className="w-full lg:flex hidden items-center gap-12 mb-8">
            <div
                className="flex items-center gap-3 cursor-pointer"
                onClick={() => setSortBy("itemName")}
            >
                <p className="font-bold text-[#20145a]">Sort by : Alphabetically, A-Z</p>
                {sortBy === "itemName" && (
                    <FontAwesomeIcon
                        className="font-bold text-[#20145a]"
                        icon={faAngleDown}
                    />
                )}
            </div>
            <div
                className="flex items-center gap-3 cursor-pointer"
                onClick={() => setSortBy("price")}
            >
                <p className="font-bold text-[#20145a]">Sort by : Price</p>
                {sortBy === "price" && (
                    <FontAwesomeIcon
                        className="font-bold text-[#20145a]"
                        icon={faAngleDown}
                    />
                )}
            </div>
        </div>
    );
}

// Custom Item Card component for printing services
interface PrintingItemCardProps {
    image: string | StaticImageData;
    itemName: string;
    price: number;
    onAddToCart: () => void;
    onRemoveFromCart: () => void;
    isInCart: boolean;
}

function PrintingItemCard({
    image,
    itemName,
    price,
    onAddToCart,
    onRemoveFromCart,
    isInCart,
}: PrintingItemCardProps) {
    return (
        <div className="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow duration-200 overflow-hidden">
            <div className="relative w-full aspect-[3/4]">
                <img
                    src={typeof image === 'string' ? image : image.src}
                    alt={itemName}
                    className="w-full h-full object-cover"
                />
            </div>
            <div className="p-3">
                <h3 className="text-sm font-semibold text-gray-800 mb-1 line-clamp-2">
                    {itemName}
                </h3>
                <p className="text-lg font-bold text-[#2b216d] mb-2">
                    Rs. {price}
                </p>
                <button
                    onClick={isInCart ? onRemoveFromCart : onAddToCart}
                    className={`w-full py-2 px-3 rounded text-sm font-medium transition-colors ${
                        isInCart
                            ? "bg-red-500 text-white hover:bg-red-600"
                            : "bg-[#2b216d] text-white hover:bg-[#1a1242]"
                    }`}
                >
                    {isInCart ? "Remove from Cart" : "Add to Cart"}
                </button>
            </div>
        </div>
    );
}