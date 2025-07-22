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

export type CartContextType = {
    cartItems: CartItem[];
    addToCart: (book: Book) => void;
    removeFromCart: (id: string) => void;
    updateItemQuantity: (id: string, quantity: number) => void;
    clearCart: () => void;
};

export type SortOption = "title" | "author" | "price";

export type ContainerProps = {
    books: Book[];
    sortBy: SortOption;
    setSortBy: (option: SortOption) => void;
    addToCart: (book: Book) => void;
    removeFromCart: (id: string) => void;
    cartItems: CartItem[];
};

export type Contact = {
    name: string;
    email: string;
    message: string;
};

export type Coupon = {
    code: string;
    discount?: number;      // e.g., 0.1 for 10%
    freeShipping?: boolean; // true if shipping should be free
};


export type OrderDetailsType = {
    customer: {
        name: string | FormDataEntryValue | null;
        email: string;
        phone: string | FormDataEntryValue | null;
        address: string | FormDataEntryValue | null;
    };
    items: CartItem[];
    subtotal: number;
    deliveryFee: number;
    tax: number;
    discount: number;
    total: number;
    couponUsed: string | null;
    orderDate: string;
    orderId: string;
};

export type OrderContextType = {
    orderDetails: OrderDetailsType | null;
    setOrderDetails: (details: OrderDetailsType) => void;
    clearOrderDetails: () => void;
};




