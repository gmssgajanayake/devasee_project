"use client";

import MainNavBar from "@/app/_components/MainNavBar";
import Hero from "@/app/_components/Hero";
import ContactBar from "@/app/_components/ContactBar";

export default function Advertisement() {
    return (
        <section  className={"w-screen h-screen flex flex-col items-center justify-center"}>
            <ContactBar/>
            <MainNavBar/>
            <Hero/>
        </section>
    );
}
