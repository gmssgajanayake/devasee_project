import Image, { StaticImageData } from "next/image";

interface ItemCardProps {
    isHovered: boolean;
    image: string | StaticImageData;
    title: string;
    author: string;
    price: number;
    stock?: number;
    onAddToCart?: () => void;
    onRemoveFromCart?: () => void;
    isInCart?: boolean;
}

export function formatPriceLKR(price: number): string {
    const rounded = Math.ceil(price);
    return new Intl.NumberFormat("en-LK", {
        style: "currency",
        currency: "LKR",
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
    }).format(rounded);
}

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
        <div className="flex items-center  justify-center flex-col gap-6">
            <div className="group w-42 h-64 sm:w-56 sm:h-72 relative flex justify-center items-center bg-white shadow-md overflow-hidden">
                <Image
                    width={200}   // set your desired width
                    height={300}
                    src={image}
                    alt={title}
                    className="absolute w-full h-full z-10 object-contain"
                />
                <div className="w-full h-full p-4 bg-white relative flex-col flex justify-end items-center">
                    <button
                        onClick={isInCart ? onRemoveFromCart : onAddToCart}
                        className={`
              absolute w-48 z-40 justify-center cursor-pointer items-center mb-14 tracking-widest 
              text-white text-[12px] py-3 transition duration-200 flex lg:hidden group-hover:flex
              ${isInCart ? "bg-gray-400" : "bg-[#0000FF] hover:bg-blue-700"}
            `}
                    >
                        {isInCart ? "REMOVE FROM CART" : "ADD TO CART"}
                    </button>
                </div>
            </div>

            <div className="text-center mb-8">
                <h3 className="text-lg font-semibold text-gray-800">{title}</h3>
                <p className="text-sm text-gray-600">{author}</p>
                <p className="text-lg font-bold text-[#0000FF] mt-2">
                    {formatPriceLKR(price)}
                </p>
            </div>
        </div>
    );
}