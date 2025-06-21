import {BrandFacebookSolid, BrandLinkedinSolid} from "@mynaui/icons-react";
import Link from "next/link";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPhone} from "@fortawesome/free-solid-svg-icons/faPhone";


export default function ContactBar(){
    return (
        <nav className="w-full h-auto flex justify-center md:justify-between bg-[#0000FF] pl-2 pr-2 pt-1 pb-1">
            <div className={" flex justify-center items-center"}>
                <FontAwesomeIcon icon={faPhone} className="mr-2 size-3.5 text-gray-50 " />
                <a href="tel:+94342244909" className="text-gray-50 font-bold">
                    +94 34 224 4909
                </a>
            </div>
            <div className={"hidden md:flex justify-between items-center"}>
                <Link href={""} className={"text-gray-50  font-normal "}>
                    <BrandFacebookSolid size={18}/>
                </Link>
                <Link href={""} className={"text-gray-50  font-normal "}>
                    <BrandLinkedinSolid size={18}/>
                </Link>
                <Link href={""} className={"text-gray-50  font-normal "}>
                    <BrandFacebookSolid size={18}/>
                </Link>
                <Link href={""} className={"text-gray-50  font-normal "}>
                    <BrandFacebookSolid size={18}/>
                </Link>

            </div>
        </nav>
    );
}