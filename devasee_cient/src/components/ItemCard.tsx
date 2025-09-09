import Image, { StaticImageData } from "next/image";

interface ItemCardProps {
    isHovered: boolean; // Note: This prop is not used in the provided code.
    image: string | StaticImageData;
    title: string;
    author: string;
    price: number;
    stock?: number;
    onAddToCart?: () => void;
    onRemoveFromCart?: () => void;
    isInCart?: boolean;
}

/**
 * Formats a number as a Sri Lankan Rupee (LKR) currency string.
 * @param price The price number to format.
 * @returns A formatted currency string (e.g., "LKR 1,200.00").
 */
export function formatPriceLKR(price: number): string {
    const rounded = Math.ceil(price);
    return new Intl.NumberFormat("en-LK", {
        style: "currency",
        currency: "LKR",
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
    }).format(rounded);
}

/**
 * A responsive card component to display an item, such as a book.
 * It features a fixed height, handles long text by truncating it,
 * and shows the full text on hover.
 */
export default function ItemCard({
                                     image,
                                     title,
                                     author,
                                     price,
                                     onAddToCart,
                                     onRemoveFromCart,
                                     isInCart = false,
                                 }: ItemCardProps) {
    return (
        // Set a fixed width for the entire card component for consistency.
        <div className="flex w-52 flex-col items-center justify-center gap-4">

            {/* --- IMAGE CONTAINER --- */}
            <div className="group relative h-64 w-48 flex justify-center items-center bg-white shadow-md overflow-hidden">
                <Image
                    width={200}
                    height={300}
                    src={image}
                    alt={title}
                    className="absolute z-10 h-60 w-40 bg-gray-100 object-cover"
                />
                <div className="relative flex h-full w-full flex-col items-center justify-end bg-white p-4">
                    <button
                        onClick={isInCart ? onRemoveFromCart : onAddToCart}
                        className={`
              absolute z-40 mb-14 flex w-48 cursor-pointer items-center justify-center 
              tracking-widest text-white text-[12px] py-3 transition duration-200 lg:hidden group-hover:flex
              ${isInCart ? "bg-gray-400" : "bg-[#0000FF] hover:bg-blue-700"}
            `}
                    >
                        {isInCart ? "REMOVE FROM CART" : "ADD TO CART"}
                    </button>
                </div>
            </div>

            {/* --- TEXT CONTAINER --- */}
            {/* This container has a fixed height (h-28) and uses flexbox
                to ensure the price is always pushed to the bottom, creating a uniform look.
            */}
            <div className="text-center h-28 w-full flex flex-col justify-between">
                {/* Wrapper for title and author to handle truncation */}
                <div>
                    <h3
                        className="text-lg px-4 font-semibold text-gray-800 line-clamp-2"
                        title={title} // Show full title on hover via native tooltip
                    >
                        {title}
                    </h3>
                    <p
                        className="text-sm text-gray-600 truncate"
                        title={author} // Show full author on hover via native tooltip
                    >
                        {author}
                    </p>
                </div>
                <p className="text-lg font-bold text-[#0000FF]">
                    {formatPriceLKR(price)}
                </p>
            </div>
        </div>
    );
}