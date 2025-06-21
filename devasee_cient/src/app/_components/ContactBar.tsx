"use client";

import Link from "next/link";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPhone } from "@fortawesome/free-solid-svg-icons";
import {
    faFacebookF,
    faLinkedinIn,
    faXTwitter,
    faThreads,
    faInstagram,
} from "@fortawesome/free-brands-svg-icons";

interface ContactBarProps {
    iconSize?: string;
}

export default function ContactBar({ iconSize = "w-3 h-3" }: ContactBarProps) {
    return (
        <nav className="w-full flex justify-center sm:justify-between bg-[#0000FF]  lg:px-8 px-6 py-1.5">
            {/* Phone */}
            <div className="flex items-center">
                <FontAwesomeIcon icon={faPhone} className={`mr-2 text-gray-50 ${iconSize}`} />
                <a href="tel:+94342244909" className="text-gray-50 font-bold">
                    +94 34 2244909
                </a>
            </div>

            {/* Social Icons */}
            <div className="hidden sm:flex items-center gap-6">
                <Link href="#">
                    <FontAwesomeIcon icon={faFacebookF} className={"text-gray-50 w-2 h-2"} />
                </Link>
                <Link href="#">
                    <FontAwesomeIcon icon={faInstagram} className={`text-gray-50 ${iconSize}`} />
                </Link>
                <Link href="#">
                    <FontAwesomeIcon icon={faLinkedinIn} className={`text-gray-50 ${iconSize}`} />
                </Link>
                <Link href="#">
                    <FontAwesomeIcon icon={faXTwitter} className={`text-gray-50 ${iconSize}`} />
                </Link>
                <Link href="#">
                    <FontAwesomeIcon icon={faThreads} className={`text-gray-50 ${iconSize}`} />
                </Link>
            </div>
        </nav>
    );
}
