import type {Metadata} from "next";
import {Inter} from "next/font/google";
import "./globals.css";
import Head from "@/app/head";
import ContactBar from "@/app/_components/ContactBar";
import MainNavBar from "@/app/_components/MainNavBar";
import ProgressBar from "@/app/_components/ProgressBar";
import {ClerkProvider} from "@clerk/nextjs";
import Footer from "@/app/_components/Footer";
import React from "react";


const inter = Inter({
    subsets: ["latin"],
    weight: ["400", "500", "600", "700"], // Add only the weights you use
    variable: "--font-inter", // Tailwind will use this
});

export const metadata: Metadata = {
    title: "Devasee | Bookshop & Printing Services",
    description: "Devasee is a trusted bookshop and printing service in Sri Lanka, offering books, custom prints, banners, mugs, and more — with fast delivery islandwide",
    keywords: ["bookshop", "printing", "Devasee", "Sri Lanka", "custom print", "educational books"],
    metadataBase: new URL("https://devasee.lk"),
    openGraph: {
        title: "Devasee | Bookshop & Printing Services",
        description: "Devasee is a trusted bookshop and printing service in Sri Lanka, offering books, custom prints, banners, mugs, and more — with fast delivery islandwide" +
            "With a strong reputation for trust, quality, and innovation, Devasee is your go-to partner for books and printing services anywhere in Sri Lanka.",
        url: "https://www.devasee.lk",
        siteName: "Devasee",
        locale: "en_US",
        type: "website",
    },
    twitter: {
        card: "summary_large_image",
        title: "Devasee | Bookshop & Printing",
        description: "Discover and order your favorite books and printing services at Devasee.",
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
            <Head/>
            <body className={`${inter.variable}  w-screen overflow-x-hidden font-sans antialiased !bg-white`}>
            <ContactBar/>
            <MainNavBar/>
            <ProgressBar/>
            {children}
            <Footer/>
            </body>
            </html>
        </ClerkProvider>
    );
}