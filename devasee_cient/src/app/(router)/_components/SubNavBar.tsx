"use client";

import { useEffect, useRef } from "react";
import gsap from "gsap";
import Link from "next/link";

interface SubNavBarProps {
    path: string;
}

export default function SubNavBar({ path }: SubNavBarProps) {
    const pathRef = useRef<HTMLSpanElement>(null);

    useEffect(() => {
        if (pathRef.current) {
            gsap.fromTo(
                pathRef.current,
                { x: -50, opacity: 0 }, // Start from left (-50px)
                {
                    x: 0,
                    opacity: 1,
                    duration: 0.8,
                    ease: "power3.out",
                }
            );
            gsap.fromTo(
                pathRef.current,
                { x: 50, opacity: 0 }, // Start from right (50px)
                {
                    x: 0,
                    opacity: 1,
                    duration: 0.8,
                    ease: "power3.out",
                }
            );
        }
    }, [path]);

    return (
        <nav ref={pathRef} className="w-full flex items-center text-sm lg:text-[16px] justify-center pt-32 lg:pt-40 bg-gradient-to-r from-[#f5f5ff] via-[#dee2f5] to-[#f5f5ff] p-6 lg:p-8">
            <h2 className="text-[#20145a]">
                <Link href={".."}>HOME</Link>&nbsp;&nbsp;/&nbsp;&nbsp;<span>{path}</span>
            </h2>
        </nav>
    );
}
