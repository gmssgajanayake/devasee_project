import Image from "next/image";

interface ItemCardProps {
    isHovered: boolean;
    imageUrl: string;
    title: string;
    author: string;
    price: number;
}

export default function ItemCard({
                                     imageUrl,
                                     title,
                                     author,
                                     price,
                                 }: ItemCardProps) {
    return (
        <div className="flex items-center justify-center flex-col gap-6">
            <div className="group w-56 h-72 relative flex justify-center items-center bg-white shadow-md overflow-hidden">
                <Image
                    src={imageUrl}
                    alt="product"
                    width={224}
                    height={288}
                    className="absolute w-full h-full z-10 object-contain"
                />
                <div className="w-full h-full p-4 bg-white relative flex-col flex justify-end items-center">
                    {/* Hover button */}
                    <button
                        className="absolute w-48 cursor-pointer bg-[#0000FF] z-40 justify-center items-center mb-20 tracking-widest text-white text-[12px] py-3 hover:bg-blue-700 transition duration-200 hidden group-hover:flex"
                    >
                        ADD TO CART
                    </button>
                </div>
            </div>

            <div className="text-center mb-8">
                <h2 className="text-lg font-semibold text-gray-800">{title}</h2>
                <p className="text-sm text-gray-600">{author}</p>
                <p className="text-lg font-bold text-[#0000FF] mt-2">${price}</p>
            </div>
        </div>
    );
}
