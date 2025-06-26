import type {Metadata} from "next";
import {Inter} from "next/font/google";
import "./globals.css";
import Head from "@/app/head";
/*import Footer from "@/app/_components/Footer";*/

const inter = Inter({
    subsets: ["latin"],
    weight: ["400", "500", "600", "700"], // Add only the weights you use
    variable: "--font-inter", // Tailwind will use this
});

export const metadata: Metadata = {
    title: "Devasee | Bookshop & Printing Services",
    description: "Discover and order your favorite books and printing services at Devasee.",
    keywords: ["bookshop", "printing", "Devasee", "Sri Lanka", "custom print", "educational books"],
    metadataBase: new URL("https://devasee.lk"),
    openGraph: {
        title: "Devasee | Bookshop & Printing Services",
        description: "Devasee is a leading bookshop and printing service based in Sri Lanka, offering a wide selection of books along with professional printing solutions. Our services include advertising banner printing, gift mug printing, and custom printing for personal or business needs. With a focus on quality and affordability, Devasee caters to students, professionals, and organizations across the country. Our website provides a user-friendly experience, allowing customers to explore products, place orders online, and enjoy fast, reliable service. Whether you're looking for academic materials or creative print solutions, Devasee is your trusted partner for all book and printing needs in Sri Lanka.",
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
        <body className={`${inter.variable} font-sans antialiased bg-white`}>
        {children}
        {/*<Footer/>*/}
        </body>
        </html>
    );
}
