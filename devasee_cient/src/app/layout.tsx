import type {Metadata} from "next";
import {Inter} from "next/font/google";
import "./globals.css";
import Head from "@/app/head";
import Footer from "@/app/_components/Footer";
import ContactBar from "@/app/_components/ContactBar";
import MainNavBar from "@/app/_components/MainNavBar";


const inter = Inter({
    subsets: ["latin"],
    weight: ["400", "500", "600", "700"], // Add only the weights you use
    variable: "--font-inter", // Tailwind will use this
});

export const metadata: Metadata = {
    title: "Devasee | Bookshop & Printing Services",
    description: "Devasee is a leading bookshop and professional printing service based in Sri Lanka, dedicated to delivering high-quality products at affordable prices. We offer an extensive range of books, including academic texts, novels, children’s books, and more, catering to the needs of students, educators, and book lovers alike. In addition to our diverse book collection, Devasee specializes in a variety of printing services tailored for both personal and business purposes.\n" +
        "\n" +
        "Our printing solutions include advertising banner printing, customized gift mug printing, T-shirt printing, flyers, brochures, wedding cards, and more. We focus on ensuring every printed product meets high standards of clarity, color accuracy, and durability. Whether you're a student preparing for a project, a business looking for promotional materials, or an individual seeking personalized gifts, we have a solution for you.\n" +
        "\n" +
        "At Devasee, customer satisfaction is our priority. Our website offers a seamless browsing and ordering experience, allowing you to explore our products, request custom prints, and place orders from the comfort of your home. We deliver across Sri Lanka with fast, reliable service.\n" +
        "\n" +
        "With a strong reputation for trust, quality, and innovation, Devasee is your go-to partner for books and printing services anywhere in Sri Lanka.",
    keywords: ["bookshop", "printing", "Devasee", "Sri Lanka", "custom print", "educational books"],
    metadataBase: new URL("https://devasee.lk"),
    openGraph: {
        title: "Devasee | Bookshop & Printing Services",
        description: "Devasee is a leading bookshop and professional printing service based in Sri Lanka, dedicated to delivering high-quality products at affordable prices. We offer an extensive range of books, including academic texts, novels, children’s books, and more, catering to the needs of students, educators, and book lovers alike. In addition to our diverse book collection, Devasee specializes in a variety of printing services tailored for both personal and business purposes.\n" +
            "\n" +
            "Our printing solutions include advertising banner printing, customized gift mug printing, T-shirt printing, flyers, brochures, wedding cards, and more. We focus on ensuring every printed product meets high standards of clarity, color accuracy, and durability. Whether you're a student preparing for a project, a business looking for promotional materials, or an individual seeking personalized gifts, we have a solution for you.\n" +
            "\n" +
            "At Devasee, customer satisfaction is our priority. Our website offers a seamless browsing and ordering experience, allowing you to explore our products, request custom prints, and place orders from the comfort of your home. We deliver across Sri Lanka with fast, reliable service.\n" +
            "\n" +
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
        <html lang="en">
        <Head/>
        <body className={`${inter.variable}  w-screen overflow-x-hidden font-sans antialiased !bg-white`}>
        <ContactBar />
        <MainNavBar />
        {children}
        <Footer/>
        </body>
        </html>
    );
}