"use client";

import { useEffect } from "react";
import { useUser, useAuth } from "@clerk/nextjs";
import { AdvertisementSlides } from "@/app/_components/AdvertisementSlides";
import TopCategories from "@/app/_components/TopCategories";
import NewRelease from "@/app/_components/NewRelease";
import BooksAdvertisementSlides from "@/app/_components/BooksAdvertisementSlides";
import OffersSlides from "@/app/_components/OffersSlides";
import NewsSubscribe from "@/app/_components/NewsSubscribe";
import Article from "@/app/_components/Article";
import Head from "@/app/head";

export default function Home() {
    const { user } = useUser();
    const { getToken } = useAuth();

    useEffect(() => {
        const fetchToken = async () => {
            const token = await getToken(); // You can optionally pass { template: "your-template-name" }
            console.log("User Token:", token);
        };

        if (user) {
            console.log("User Info:", user);
            fetchToken();
        }
    }, [user]);


    return (
        <div className="bg-white">
            <Head />
            <AdvertisementSlides />
            <TopCategories />
            <NewRelease />
            <BooksAdvertisementSlides />
            <OffersSlides />
            <NewsSubscribe />
            <Article />
        </div>
    );
}
