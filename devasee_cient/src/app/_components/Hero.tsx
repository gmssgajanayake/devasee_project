"use client";

import { useEffect, useRef } from "react";
import gsap from "gsap";
import Image from "next/image";
import adImage1 from "@/assets/devasee-p1.png";
import Link from "next/link";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faArrowLeft, faArrowRight} from "@fortawesome/free-solid-svg-icons";

export default function Hero() {
    const textRef = useRef<HTMLHeadingElement>(null);

    useEffect(() => {
        if (textRef.current) {
            gsap.fromTo(
                textRef.current,
                { y: -50, opacity: 0 },
                { y: 0, opacity: 1, duration: 1, ease: "power3.out" }
            );
        }
    }, []);

    return (
        <section ref={textRef} className="p-10 mt-24 flex items-center justify-center lg:mt-28 text-center w-full h-full bg-blue-50 md:bg-gradient-to-r md:from-[#e8ebff]  md:to-white bg-gradient-to-b from-[#e8ebff] to-white">
            <div className="flex flex-col md:flex-row items-center justify-between  ">
                <div className={"w-1/12 hidden lg:flex justify-center items-center h-full"}>
                    <div className={"w-[40] h-[40] cursor-pointer  bg-white rounded-full flex justify-center items-center border border-[#0000ff]"}>
                        <FontAwesomeIcon className={"font-extralight text-[#0000ff]"} icon={faArrowLeft}/>
                    </div>
                </div>
                <div className={"w-full lg:px-6 h-1/2  md:w-1/2 md:flex md:flex-col "}>
                    <h1 className="text-4xl lg:text-5xl font-bold text-left  text-[#2b216d]">
                        Devasee Bookshop
                    </h1>
                    <p className="text-sm text-left md:text-lg text-[#2b216d] mt-2">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
                    </p>
                    <Link href={"about"} className={"flex"}>
                        <button className="mt-6 mb-6 px-4 py-2 text-[12px] md:text-sm border border-[#2b216d] rounded-md text-[#2b216d] font-extralight tracking-widest hover:bg-[#2b216d] hover:text-white transition-colors duration-300">
                            READ MORE
                        </button>
                    </Link>
                    {/*Advertisement changing indicator*/}
                    <div className={"md:flex h-auto hidden  gap-1 "} >
                        <div className={"w-[30] h-[30] flex justify-center items-center border-1 border-[#0000ff] rounded-full"}>
                            <div className={"h-[10] w-[10] rounded-full bg-[#0000ff]"}>

                            </div>
                        </div>
                        <div className={"w-[30] h-[30] flex justify-center items-center  rounded-full"}>
                            <div className={"h-[10] w-[10] rounded-full bg-gray-400"}>

                            </div>
                        </div>
                        <div className={"w-[30] h-[30] flex justify-center items-center rounded-full"}>
                            <div className={"h-[10] w-[10] rounded-full bg-gray-400"}>

                            </div>
                        </div>
                        <div className={"w-[30] h-[30] flex justify-center items-center  rounded-full"}>
                            <div className={"h-[10] w-[10] rounded-full bg-gray-400"}>

                            </div>
                        </div>
                    </div>

                </div>
                <div className={"w-full h-1/2 flex items-center justify-center md:w-1/2 "}>
                    <Image src={adImage1} alt={"Devasee"} className={"w-[240] h-[240] md:w-[460] md:h-[460] "}/>
                </div>
                <div className={"w-1/12 hidden lg:flex justify-center items-center h-full"}>
                    <div className={"w-[40] h-[40] cursor-pointer bg-white rounded-full flex justify-center items-center border border-[#0000ff]"}>
                        <FontAwesomeIcon className={"font-extralight text-[#0000ff]"} icon={faArrowRight}/>
                    </div>
                </div>
            </div>
        </section>
    );
}


/*
"use client";

import { useEffect, useRef } from "react";
import gsap from "gsap";
import Link from "next/link";


import {faArrowLeft, faArrowRight} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

export default function Hero() {
    const textRef = useRef<HTMLHeadingElement>(null);

    useEffect(() => {
        if (textRef.current) {
            gsap.fromTo(
                textRef.current,
                { y: -50, opacity: 0 },
                { y: 0, opacity: 1, duration: 1, ease: "power3.out" }
            );
        }
    }, []);

    return (
        <section ref={textRef} className="flex-col gap-4 md:flex-row px-2 py-10 md:p-10  mt-52 md:mt-24 lg:mt-28 flex  justify-center  text-center md:w-full  md:h-full md:bg-gradient-to-r md:from-[#e8ebff]  md:to-white bg-gradient-to-b from-[#e8ebff] to-white">
            <div className={"w-1/12  hidden lg:flex  justify-center items-center h-full"}>
                <div className={"w-[40] h-[40] cursor-pointer  bg-white rounded-full flex justify-center items-center border border-[#0000ff]"}>
                    <FontAwesomeIcon className={"font-extralight text-[#0000ff]"} icon={faArrowLeft}/>
                </div>
            </div>
            <div className={"md:w-1/2 flex gap-2 h-full  justify-center px-8 flex-col"}>
                <h1 className="text-4xl  lg:text-5xl text-left font-bold text-[#2b216d]">
                    Devasee Bookshop
                </h1>
                <p className="text-sm md:text-lg text-left text-[#2b216d] mt-2">
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
                </p>
                <button className={"flex border-1 cursor-pointer border-[#2b216d] w-[120] h-[10] md:w-[160px] md:h-[40px] items-center justify-center mt-6 mb-6 rounded-md  py-5 md:py-6 transition-colors duration-300"}>
                    <Link href="#contact" className="text-[12px] md:text-sm tracking-widest text-[#2b216d] font-extralight ">
                        READ MORE
                    </Link>
                </button>
                {/!*Advertisement changing indicator*!/}
                <div className={"md:flex h-auto hidden  gap-1 "} >
                    <div className={"w-[30] h-[30] flex justify-center items-center border-1 border-[#0000ff] rounded-full"}>
                        <div className={"h-[10] w-[10] rounded-full bg-[#0000ff]"}>

                        </div>
                    </div>
                    <div className={"w-[30] h-[30] flex justify-center items-center  rounded-full"}>
                        <div className={"h-[10] w-[10] rounded-full bg-gray-400"}>

                        </div>
                    </div>
                    <div className={"w-[30] h-[30] flex justify-center items-center rounded-full"}>
                        <div className={"h-[10] w-[10] rounded-full bg-gray-400"}>

                        </div>
                    </div>
                    <div className={"w-[30] h-[30] flex justify-center items-center  rounded-full"}>
                        <div className={"h-[10] w-[10] rounded-full bg-gray-400"}>

                        </div>
                    </div>
                </div>
            </div>
            <div className={"md:w-1/2 flex justify-center items-center h-full"}>
                <Image src={adImage1} alt={"Devasee"} className={"w-[340] h-[340] md:w-[460] md:h-[460] drop-shadow-2xl"}/>
            </div>
            <div className={"w-1/12 hidden lg:flex justify-center items-center h-full"}>
                <div className={"w-[40] h-[40] cursor-pointer bg-white rounded-full flex justify-center items-center border border-[#0000ff]"}>
                    <FontAwesomeIcon className={"font-extralight text-[#0000ff]"} icon={faArrowRight}/>
                </div>
            </div>
        </section>
    );
}

*/
