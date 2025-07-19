import type {Metadata} from "next";
import {Inter} from "next/font/google";
import "./globals.css";
import ContactBar from "@/app/_components/ContactBar";
import MainNavBar from "@/app/_components/MainNavBar";
import ProgressBar from "@/app/_components/ProgressBar";
import {ClerkProvider} from "@clerk/nextjs";
import Footer from "@/app/_components/Footer";
import React from "react";
import {CartProvider} from "@/app/context/CartContext";


const inter = Inter({
    subsets: ["latin"],
    weight: ["400", "500", "600", "700"], // Add only the weights you use
    variable: "--font-inter", // Tailwind will use this
});




// Example: /src/app/layout.tsx or /src/app/(router)/layout.tsx
export const metadata: Metadata = {
    title: "Devasee | Bookshop & Printing Services",
    description:
        "Devasee is a trusted bookshop and printing service in Sri Lanka, offering books, custom prints, banners, mugs, and more — with fast delivery islandwide.",
    keywords: [
        "bookshop",
        "printing",
        "Devasee",
        "Sri Lanka",
        "custom print",
        "educational books",
        "banners",
        "mugs",
        "school books",
    ],
    metadataBase: new URL("https://www.devasee.lk"),
    icons: {
        icon: [
            { url: "/favicon.ico" },
            { url: "/favicon-16x16.png", sizes: "16x16", type: "image/png" },
            { url: "/favicon-32x32.png", sizes: "32x32", type: "image/png" },
        ],
        apple: [
            { url: "/icon-192x192.png", sizes: "192x192" },
            { url: "/icon-512x512.png", sizes: "512x512" },
        ],
        shortcut: "/favicon-16x16.png",
    },
    manifest: "/manifest.json",
    openGraph: {
        title: "Devasee | Bookshop & Printing Services",
        description:
            "Devasee is your trusted partner for books and printing services in Sri Lanka. Browse our wide range of products and enjoy fast delivery.",
        url: "https://www.devasee.lk",
        siteName: "Devasee",
        locale: "en_US",
        type: "website",
        images: [
            {
                url: "https://www.devasee.lk/og-image.jpg",
                width: 1200,
                height: 630,
                alt: "Devasee Bookshop and Printing",
            },
        ],
    },
    twitter: {
        card: "summary_large_image",
        title: "Devasee | Bookshop & Printing",
        description:
            "Discover and order your favorite books and printing services at Devasee.",
        images: ["https://www.devasee.lk/og-image.jpg"],
        creator: "@devasee_lk", // optional
    },
};



export default function RootLayout({
                                       children,
                                   }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <ClerkProvider appearance={
            {
                variables: {
                    colorPrimary: "#0000ff", // Tailwind's blue-800
                    colorText: "#1F2937", // Tailwind's gray-800
                    colorBackground: "#ffffff", // White background
                    fontFamily: inter.style.fontFamily,
                    // Use Inter font
                },
            }
        }>
            <html lang="en">
            <body className={`${inter.variable}  w-screen overflow-x-hidden font-sans antialiased !bg-white`}>
            <CartProvider>
            <ContactBar/>
            <MainNavBar/>
            <ProgressBar/>
            {children}
            <Footer/>
            </CartProvider>
            </body>
            </html>
        </ClerkProvider>
    );
}