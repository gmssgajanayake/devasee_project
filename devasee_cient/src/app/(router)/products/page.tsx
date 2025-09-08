"use client";

import {useEffect, useRef, useState} from "react";
import gsap from "gsap";
import SubNavBar from "@/app/(router)/_components/SubNavBar";
import FilterBar from "@/app/(router)/products/_components/FilterBar";
import Container from "@/app/(router)/products/_components/Container";

import book1 from "@/assets/items image/img.png";


import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faArrowUpWideShort,
    faSliders,
    faXmark,
    faSearch,
} from "@fortawesome/free-solid-svg-icons";

import {useCart} from "@/app/context/CartContext";
import {Book} from "@/types/types";
import Head from "@/app/(router)/products/head";
import {getAllBooks} from "@/lib/actions";



export default function  Page() {
    const [priceRange, setPriceRange] = useState<[number, number]>([0, 5000]);
    const [selectedTypes, setSelectedTypes] = useState<string[]>([]);
    const [selectedBrands, setSelectedBrands] = useState<string[]>([]);
    const [sortBy, setSortBy] = useState<"title" | "author" | "price">("title");
    const [showMobileFilter, setShowMobileFilter] = useState(false);
    const [showSortModal, setShowSortModal] = useState(false);
    const [searchQuery, setSearchQuery] = useState("");
    const filterRef = useRef<HTMLDivElement>(null);

    const {addToCart, removeFromCart, cartItems} = useCart();

    const [books, setBooks] = useState<Book[]>([]);
    const [loading, setLoading] = useState(true);
    const [isMounted, setIsMounted] = useState(false);



    useEffect(() => {
        async function fetchBooks() {
            setLoading(true);
            const data = await getAllBooks();
            setBooks(data);
            setLoading(false);
        }
        fetchBooks();
    }, []);


    useEffect(() => {
        if (filterRef.current) {
            gsap.fromTo(
                filterRef.current,
                {y: -50, opacity: 0},
                {y: 0, opacity: 1, duration: 0.8, ease: "power3.out"}
            );
        }
        if (typeof window !== "undefined") {
            window.scrollTo({top: 0, behavior: "auto"});
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
        return null; // or a loading placeholder
    }

    const filteredBooks = books
        .filter((book) => {
            const inPriceRange = book.price >= priceRange[0] && book.price <= priceRange[1];
            const matchesType = selectedTypes.length === 0 || selectedTypes.includes(book.type);
            const matchesBrand = selectedBrands.length === 0 || selectedBrands.includes(book.brand);
            const matchesSearch =
                book.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
                book.author.toLowerCase().includes(searchQuery.toLowerCase());
            return inPriceRange && matchesType && matchesBrand && matchesSearch;
        })
        .sort((a, b) => {
            if (sortBy === "title") return a.title.localeCompare(b.title);
            if (sortBy === "author") return a.author.localeCompare(b.author);
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
            <Head/>
            <div className="w-full">
                <SubNavBar path="PRODUCTS"/>

                {/* Search Bar */}
                <div className="w-full px-4 py-3 lg:mt-4 flex justify-center items-center ">
                    <div className="relative w-full max-w-xl">
                        <input
                            type="text"
                            placeholder="Search books..."
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

                {/* Filter + Book Grid */}
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
                    <div className="flex-1 overflow-y-auto h-full  hide-scrollbar">
                        <Container
                            books={filteredBooks.map((book) => ({
                                ...book,
                                image: book.image || book1, // fallback to placeholder if image is missing
                            }))}
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
                        <div className="bg-white w-[60%] max-w-sm rounded-xl shadow-xl p-6 relative">
                            <div className="flex justify-between items-center mb-4">
                                <h2 className="font-bold text-lg text-[#2b216d]">Sort Options</h2>
                                <FontAwesomeIcon
                                    icon={faXmark}
                                    className="text-2xl text-[#2b216d] cursor-pointer"
                                    onClick={() => setShowSortModal(false)}
                                />
                            </div>
                            <div className="space-y-3">
                                {["title", "author", "price"].map((option) => (
                                    <button
                                        key={option}
                                        className={`w-full font-bold text-left p-2 rounded ${
                                            sortBy === option ? "bg-blue-100 text-blue-600" : "text-[#2b216d]"
                                        }`}
                                        onClick={() => {
                                            setSortBy(option as "title" | "author" | "price");
                                            setShowSortModal(false);
                                        }}
                                    >
                                        {option === "title"
                                            ? "Alphabetically, A-Z"
                                            : option === "author"
                                                ? "Author"
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