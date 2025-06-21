import logo from "@/assets/devasee logo.png"
import Image from "next/image";
import Link from "next/link";
import {faUser, faClipboard, faHeart} from "@fortawesome/free-regular-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {AlignJustify, X} from 'lucide-react';
import {useEffect, useRef} from "react";
import gsap from "gsap";


export default function MainNavBar() {
    const textRef = useRef<HTMLHeadingElement>(null);

    useEffect(() => {
        if (textRef.current) {
            gsap.fromTo(
                textRef.current,
                {y: -50, opacity: 0},
                {y: 0, opacity: 1, duration: 1, ease: "power3.out"}
            );
        }
    }, []);

    return (
        <div className={"w-screen h-auto"}>
            {/*Desktop menu bar*/}
            <nav className={"w-full bg-white h-auto flex items-center py-3 px-5 lg:px-8 justify-between"}>
                <Link href="/">
                    <Image
                        src={logo}
                        alt={"logo"}

                        priority
                        className={"h-10 w-10 lg:w-16 lg:h-16"}
                    />
                </Link>
                <div className={"items-center justify-center gap-4 hidden lg:flex"}>
                    <Link
                        href="/"
                        className="text-sm font-semibold tracking-wide text-gray-600 dark:text-gray-800 transition-colors duration-200 hover:text-[#0000FF]"
                    >
                        HOME
                    </Link>
                    <span>|</span>
                    <Link href={"about"}
                          className="text-sm font-semibold tracking-wide text-gray-600 dark:text-gray-800 transition-colors duration-200 hover:text-[#0000FF]">
                        ABOUT US
                    </Link>
                    <span>|</span>
                    <Link href={"products"}
                          className="text-sm font-semibold tracking-wide text-gray-600 dark:text-gray-800 transition-colors duration-200 hover:text-[#0000FF]">
                        BOOKS
                    </Link>
                    <span>|</span>
                    <Link href={"services"}
                          className="text-sm font-semibold tracking-wide text-gray-600 dark:text-gray-800 transition-colors duration-200 hover:text-[#0000FF]">
                        PRINTING SERVICES
                    </Link>
                    <span>|</span>
                    <Link href={"contact"}
                          className="text-sm font-semibold tracking-wide text-gray-600 dark:text-gray-800 transition-colors duration-200 hover:text-[#0000FF]">
                        CONTACT US
                    </Link>
                </div>
                <div className={"hidden lg:flex items-center justify-center gap-4 "}>
                    <FontAwesomeIcon className={"w-3.5 h-3.5 cursor-pointer text-gray-600"} icon={faUser}/>
                    <span>|</span>
                    <FontAwesomeIcon className={"w-3 h-3 cursor-pointer text-gray-600"} icon={faClipboard}/>
                    <span>|</span>
                    <FontAwesomeIcon className={"w-4 h-4 cursor-pointer text-gray-600"} icon={faHeart}/>
                </div>
                {/*For mobile menu open and close*/}
                <div className={"flex lg:hidden"}>
                    <AlignJustify size={28} className={"text-gray-600"}/>
                    <X className={"hidden"}/>
                </div>
            </nav>
            {/*Mobile menu bar*/}
        </div>
    );
}