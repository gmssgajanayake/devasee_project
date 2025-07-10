"use client";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAngleDown } from "@fortawesome/free-solid-svg-icons";

interface SortOptionsProps {
    sortBy: "title" | "author" | "price";
    setSortBy: (value: "title" | "author" | "price") => void;
}

export default function SortOptions({ sortBy, setSortBy }: SortOptionsProps) {
    return (
        <div className="w-full lg:flex hidden items-center gap-12 mb-8">
            <div
                className="flex items-center gap-3 cursor-pointer"
                onClick={() => setSortBy("title")}
            >
                <p className="font-bold text-[#20145a]">Sort by : Alphabetically, A-Z</p>
                {sortBy === "title" && (
                    <FontAwesomeIcon
                        className="font-bold text-[#20145a]"
                        icon={faAngleDown}
                    />
                )}
            </div>
            <div
                className="flex items-center gap-3 cursor-pointer"
                onClick={() => setSortBy("author")}
            >
                <p className="font-bold text-[#20145a]">Sort by : Author</p>
                {sortBy === "author" && (
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