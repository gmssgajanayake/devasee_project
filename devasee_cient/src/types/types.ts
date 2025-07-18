import { StaticImageData } from "next/image";

export type Book = {
    id: string;
    image: StaticImageData;
    title: string;
    author: string;
    price: number;
    type: string;
    brand: string;
    stock: number;
    quantity: number;
    description?: string;
    rating?: number;
    publicationDate?: string;
    isbn?: string;
    language?: string;
    pages?: number;
    publisher?: string;
    dimensions?: string;
    weight?: string;
};


export type CartItem = Book & {
    quantity: number;
};
