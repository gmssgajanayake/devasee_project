import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import Head from "@/app/head";

const geistSans = Geist({
    variable: "--font-geist-sans",
    subsets: ["latin"],
});

const geistMono = Geist_Mono({
    variable: "--font-geist-mono",
    subsets: ["latin"],
});

export const metadata: Metadata = {
    title: "Devasee | Bookshop & Printing",
    description: "Discover and order your favorite books and printing services at Devasee.",
    openGraph: {
        title: "Devasee | Bookshop & Printing",
        description: "Discover and order your favorite books and printing services at Devasee.",
        url: "https://www.devasee.com",
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
        <html lang="en">
        <Head/>
        <body
            className={`${geistSans.variable} ${geistMono.variable} antialiased`}
        >
        {children}
        </body>
        </html>
    );
}
