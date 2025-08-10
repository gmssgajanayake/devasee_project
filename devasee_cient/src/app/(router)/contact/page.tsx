import React from "react";
import SubNavBar from "@/app/(router)/_components/SubNavBar";
import Head from "@/app/(router)/contact/head";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFacebookF, faTwitter, faInstagram, faYoutube, faLinkedinIn } from "@fortawesome/free-brands-svg-icons";

export default function Page() {
    return (
        <>
            <Head />
            <div>
                <SubNavBar path="CONTACT" />
                <div className="flex flex-col md:flex-row w-full mt-8 gap-6">
                    {/* Google Map Embed */}
                    <div className="w-full md:w-1/2 h-80 md:h-[28rem]">
                        <iframe
                            title="Shop Location"
                            src="https://www.google.com/maps/embed?pb=!1m18!...replace_with_your_params..."
                            loading="lazy"
                            className="w-full h-full rounded-lg border-0"
                            allowFullScreen
                            referrerPolicy="no-referrer-when-downgrade"
                        ></iframe>
                    </div>
                    {/* Social Media Icons */}
                    <div className="w-full md:w-1/2 flex flex-col items-center justify-center gap-8">
                        <div className="flex flex-wrap justify-center gap-8">
                            <a
                                href="https://facebook.com/yourpage"
                                target="_blank"
                                rel="noopener noreferrer"
                                className="text-blue-600 hover:text-blue-800 transition text-5xl"
                                aria-label="Facebook"
                            >
                                <FontAwesomeIcon icon={faFacebookF} />
                            </a>
                            <a
                                href="https://twitter.com/yourpage"
                                target="_blank"
                                rel="noopener noreferrer"
                                className="text-black hover:text-gray-700 transition text-5xl"
                                aria-label="Twitter"
                            >
                                <FontAwesomeIcon icon={faTwitter} />
                            </a>
                            <a
                                href="https://instagram.com/yourpage"
                                target="_blank"
                                rel="noopener noreferrer"
                                className="text-pink-500 hover:text-pink-600 transition text-5xl"
                                aria-label="Instagram"
                            >
                                <FontAwesomeIcon icon={faInstagram} />
                            </a>
                            <a
                                href="https://youtube.com/yourpage"
                                target="_blank"
                                rel="noopener noreferrer"
                                className="text-red-600 hover:text-red-800 transition text-5xl"
                                aria-label="YouTube"
                            >
                                <FontAwesomeIcon icon={faYoutube} />
                            </a>
                            <a
                                href="https://linkedin.com/yourpage"
                                target="_blank"
                                rel="noopener noreferrer"
                                className="text-blue-700 hover:text-blue-900 transition text-5xl"
                                aria-label="LinkedIn"
                            >
                                <FontAwesomeIcon icon={faLinkedinIn} />
                            </a>
                        </div>
                        <div className="text-lg text-gray-500 mt-4">
                            Connect with us on social media!
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}
