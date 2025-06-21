import type {Metadata} from "next";
import {Inter} from "next/font/google";
import "./globals.css";
import Head from "@/app/head";
// import ContactBar from "@/app/_components/ContactBar";
// import Footer from "@/app/_components/Footer";

const inter = Inter({
    subsets: ["latin"],
    weight: ["400", "500", "600", "700"], // Add only the weights you use
    variable: "--font-inter", // Tailwind will use this
});

export const metadata: Metadata = {
    title: "Devasee | Bookshop & Printing",
    description: "Discover and order your favorite books and printing services at Devasee.",
    openGraph: {
        title: "Devasee | Bookshop & Printing",
        description: "Discover and order your favorite books and printing services at Devasee.",
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
        <html lang="en">
        <Head/>
        <body className={`${inter.variable} font-sans antialiased`}>
        {/*<ContactBar/>*/}
        {children}
        {/*<Footer/>*/}
        </body>
        </html>
    );
}
