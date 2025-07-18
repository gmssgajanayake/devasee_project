import Image from "next/image";

interface ItemCardProps {
    isHovered: boolean;
    imageUrl: string;
    title: string;
    author: string;
    price: number;
    onAddToCart?: () => void;
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
                                     imageUrl,
                                     title,
                                     author,
                                     price,
                                     onAddToCart,
                                 }: ItemCardProps) {
    return (
        <div className="flex items-center justify-center flex-col gap-6">
            <div className="group w-42 h-64 sm:w-56 sm:h-72 relative flex justify-center items-center bg-white shadow-md overflow-hidden">
                <Image
                    src={imageUrl}
                    alt="product"
                    width={224}
                    height={288}
                    className="absolute w-full h-full z-10 object-contain"
                />
                <div className="w-full h-full p-4 bg-white relative flex-col flex justify-end items-center">
                    <button
                        onClick={onAddToCart}
                        className={`
                            absolute w-48 cursor-pointer bg-[#0000FF] z-40
                            justify-center items-center mb-14 tracking-widest text-white text-[12px] py-3
                            transition duration-200
                            hover:bg-blue-700
                            flex lg:hidden
                            group-hover:flex lg:group-hover:flex
                        `}
                    >
                        ADD TO CART
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
