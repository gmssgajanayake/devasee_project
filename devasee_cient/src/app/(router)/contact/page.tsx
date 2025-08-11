import React from "react";
import SubNavBar from "@/app/(router)/_components/SubNavBar";
import Head from "@/app/(router)/contact/head";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFacebookF, faXTwitter, faInstagram, faYoutube, faLinkedinIn, faWhatsapp, faThreads } from "@fortawesome/free-brands-svg-icons";
import { faPhone, faLocationDot } from "@fortawesome/free-solid-svg-icons";

export default function Page() {
    return (
        <>
            <Head />
            <div className="w-full">
                <SubNavBar path="CONTACT" />

                {/* Heading */}
                <div className="text-center mt-10 px-4">
                    <h1 className="text-4xl md:text-5xl font-extrabold tracking-tight text-gray-900">
                        Contact Us
                    </h1>
                    <p className="mt-3 text-base md:text-lg text-gray-600 max-w-3xl mx-auto">
                        Stay In Touch With Us! Contact Us For Any Inquiries Or Questions You May Have.
                    </p>
                </div>

                {/* Content */}
                <div className="mt-10 grid grid-cols-1 md:grid-cols-2 gap-6 px-4 md:px-8">
                    {/* Left: Tabs */}
                    <div className="flex flex-col gap-4">
                        {/* Address 1 */}
                        <a
                            href="https://www.google.com/maps?q=YOUR_ADDRESS_1"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="group w-full flex items-center gap-4 rounded-xl border border-blue-100 bg-blue-50 hover:bg-blue-600 transition-colors p-5 shadow-sm"
                            aria-label="Address 1"
                        >
              <span className="text-blue-600 group-hover:text-white text-2xl">
                <FontAwesomeIcon icon={faLocationDot} />
              </span>
                            <span className="text-lg font-semibold text-blue-900 group-hover:text-white">
                Address 1
              </span>
                        </a>

                        {/* Address 2 */}
                        <a
                            href="https://www.google.com/maps?q=YOUR_ADDRESS_2"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="group w-full flex items-center gap-4 rounded-xl border border-blue-100 bg-blue-50 hover:bg-blue-600 transition-colors p-5 shadow-sm"
                            aria-label="Address 2"
                        >
              <span className="text-blue-600 group-hover:text-white text-2xl">
                <FontAwesomeIcon icon={faLocationDot} />
              </span>
                            <span className="text-lg font-semibold text-blue-900 group-hover:text-white">
                Address 2
              </span>
                        </a>

                        {/* Telephone */}
                        <a
                            href="tel:+YOUR_PHONE_NUMBER"
                            className="group w-full flex items-center gap-4 rounded-xl border border-blue-100 bg-blue-50 hover:bg-blue-600 transition-colors p-5 shadow-sm"
                            aria-label="Telephone"
                        >
              <span className="text-blue-600 group-hover:text-white text-2xl">
                <FontAwesomeIcon icon={faPhone} />
              </span>
                            <span className="text-lg font-semibold text-blue-900 group-hover:text-white">
                Telephone
              </span>
                        </a>

                        {/* YouTube */}
                        <a
                            href="https://youtube.com/YOUR_CHANNEL"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="group w-full flex items-center gap-4 rounded-xl border border-blue-100 bg-blue-50 hover:bg-blue-600 transition-colors p-5 shadow-sm"
                            aria-label="YouTube"
                        >
              <span className="text-blue-600 group-hover:text-white text-2xl">
                <FontAwesomeIcon icon={faYoutube} />
              </span>
                            <span className="text-lg font-semibold text-blue-900 group-hover:text-white">
                YouTube
              </span>
                        </a>

                        {/* Facebook */}
                        <a
                            href="https://www.facebook.com/devaseepvtltd/"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="group w-full flex items-center gap-4 rounded-xl border border-blue-100 bg-blue-50 hover:bg-blue-600 transition-colors p-5 shadow-sm"
                            aria-label="Facebook"
                        >
              <span className="text-blue-600 group-hover:text-white text-2xl">
                <FontAwesomeIcon icon={faFacebookF} />
              </span>
                            <span className="text-lg font-semibold text-blue-900 group-hover:text-white">
                Facebook
              </span>
                        </a>

                        {/* WhatsApp */}
                        <a
                            href="https://wa.me/YOUR_PHONE_NUMBER"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="group w-full flex items-center gap-4 rounded-xl border border-blue-100 bg-blue-50 hover:bg-blue-600 transition-colors p-5 shadow-sm"
                            aria-label="WhatsApp"
                        >
              <span className="text-blue-600 group-hover:text-white text-2xl">
                <FontAwesomeIcon icon={faWhatsapp} />
              </span>
                            <span className="text-lg font-semibold text-blue-900 group-hover:text-white">
                WhatsApp
              </span>
                        </a>

                        {/* Instagram */}
                        <a
                            href="https://www.instagram.com/devaseelk/"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="group w-full flex items-center gap-4 rounded-xl border border-blue-100 bg-blue-50 hover:bg-blue-600 transition-colors p-5 shadow-sm"
                            aria-label="Instagram"
                        >
              <span className="text-blue-600 group-hover:text-white text-2xl">
                <FontAwesomeIcon icon={faInstagram} />
              </span>
                            <span className="text-lg font-semibold text-blue-900 group-hover:text-white">
                Instagram
              </span>
                        </a>

                        {/* X (Twitter) */}
                        <a
                            href="https://x.com/Devaseelk"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="group w-full flex items-center gap-4 rounded-xl border border-blue-100 bg-blue-50 hover:bg-blue-600 transition-colors p-5 shadow-sm"
                            aria-label="X (Twitter)"
                        >
              <span className="text-blue-600 group-hover:text-white text-2xl">
                <FontAwesomeIcon icon={faXTwitter} />
              </span>
                            <span className="text-lg font-semibold text-blue-900 group-hover:text-white">
                X
              </span>
                        </a>

                        {/* Threads */}
                        <a
                            href="https://www.threads.com/@devaseelk"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="group w-full flex items-center gap-4 rounded-xl border border-blue-100 bg-blue-50 hover:bg-blue-600 transition-colors p-5 shadow-sm"
                            aria-label="Threads"
                        >
              <span className="text-blue-600 group-hover:text-white text-2xl">
                <FontAwesomeIcon icon={faThreads} />
              </span>
                            <span className="text-lg font-semibold text-blue-900 group-hover:text-white">
                Threads
              </span>
                        </a>

                        {/* LinkedIn */}
                        <a
                            href="https://linkedin.com/in/YOUR_PROFILE"
                            target="_blank"
                            rel="noopener noreferrer"
                            className="group w-full flex items-center gap-4 rounded-xl border border-blue-100 bg-blue-50 hover:bg-blue-600 transition-colors p-5 shadow-sm"
                            aria-label="LinkedIn"
                        >
              <span className="text-blue-600 group-hover:text-white text-2xl">
                <FontAwesomeIcon icon={faLinkedinIn} />
              </span>
                            <span className="text-lg font-semibold text-blue-900 group-hover:text-white">
                LinkedIn
              </span>
                        </a>
                    </div>

                    {/* Right: Google Map Preview */}
                    <div className="w-full h-80 md:h-[34rem]">
                        <iframe
                            title="Location"
                            // Replace the src below with your own Google Maps Embed URL
                            src="https://maps.app.goo.gl/wK3XtruuEtXq3dah7?g_st=ac"
                            loading="lazy"
                            className="w-full h-full rounded-xl border-0 shadow-sm"
                            allowFullScreen
                            referrerPolicy="no-referrer-when-downgrade"
                        ></iframe>
                    </div>
                </div>
            </div>
        </>
    );
}