"use client";
import logo from "@/assets/devasee.png";
import Image from "next/image";
import Link from "next/link";


import { useEffect, useRef } from "react";
import gsap from "gsap";

export default function Maintenance() {
    const textRef = useRef<HTMLHeadingElement>(null);

    useEffect(() => {
        if (textRef.current) {
            gsap.fromTo(
                textRef.current,
                { y: -150, opacity: 0 },
                { y: 0, opacity: 1, duration: 1, ease: "power3.out" }
            );
        }
    }, []);

    return (
        <div  className={"w-full h-screen flex flex-col justify-center bg-blue-50"}>
            <div ref={textRef} className={"h-full  flex flex-col items-center gap-5 justify-center"}>
                <Image
                    src={logo}
                    alt="Devasee logo"
                    className="w-36 h-auto bg-white rounded-full"
                    priority
                />
                <div  className={"flex flex-col items-center justify-center"}>
                    <h1 className={"text-xl md:text-3xl text-gray-800 font-normal"}>Welcome to Devasee Bookshop</h1>
                    <p className={"text-sm md:text-lg text-gray-800"} ><Link className={"underline cursor-pointer"} href={"#"}>www.devasee.lk</Link><span className={""}> under maintenance</span></p>
                    <p className={"text-gray-800                                                text-xs md:text-sm"}>This website will be available soon ...</p>
                </div>
            </div>
        </div>
    );
}