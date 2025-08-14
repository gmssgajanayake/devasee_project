"use client";


import { useEffect } from "react";
import AOS from "aos";
import "aos/dist/aos.css";

const Page = () => {
    useEffect(() => {
        AOS.init({
            duration: 900,
            once: true,
            easing: "ease-out-cubic",
            offset: 80,
        });
    }, []);

    return (
        <section className="relative min-h-screen bg-gradient-to-b from-gray-50 to-gray-100 pt-32 lg:pt-40 pb-16 px-4 sm:px-6 lg:px-8 overflow-hidden">
            {/* Background Bubble Animation */}
            <div className="absolute -top-40 -right-40  w-[500px] h-[500px] lg:w-[900px] lg:h-[900px] bg-gradient-to-br from-blue-500/20 to-indigo-500/20 rounded-[60%] blur-3xl animate-[float_8s_ease-in-out_infinite]"></div>
            <div className="absolute -bottom-48 -left-40 w-[400px] h-[400px] bg-gradient-to-tr from-indigo-400/20 to-blue-400/20 rounded-[50%] blur-3xl animate-[float_10s_ease-in-out_infinite]"></div>

            <div className="max-w-7xl mx-auto relative z-10">
                {/* Heading */}
                <div className="text-center mb-16">
                    <h1
                        data-aos="fade-down"
                        className="text-4xl md:text-5xl font-extrabold text-[#1e205a]  bg-clip-text mb-4"
                    >
                        Contact Devasee Book Shop
                    </h1>
                    <p
                        data-aos="fade-up"
                        data-aos-delay="150"
                        className="text-xl text-[#1e205a]/60 max-w-3xl mx-auto"
                    >
                        We would love to hear from you! Reach out for inquiries, suggestions, or just to say hello.
                    </p>
                </div>

                <div className="grid grid-cols-1 lg:grid-cols-2 gap-12">
                    {/* Contact Form */}
                    <div
                        data-aos="fade-right"
                        className="bg-white/80 backdrop-blur-sm p-8 rounded-2xl shadow-lg border border-gray-100 hover:shadow-2xl transition duration-300"
                    >
                        <h2 className="text-2xl font-semibold text-[#1e205a] bg-clip-text mb-6">
                            Send us a message
                        </h2>
                        <form className="space-y-6">
                            {[
                                { id: "name", label: "Your Name", type: "text", placeholder: "John Doe" },
                                { id: "email", label: "Email Address", type: "email", placeholder: "your.email@example.com" },
                                { id: "phone", label: "Phone Number (Optional)", type: "tel", placeholder: "+94 77 123 4567" },
                            ].map((field, index) => (
                                <div key={field.id} data-aos="fade-up" data-aos-delay={100 * index}>
                                    <label htmlFor={field.id} className="block  text-sm font-medium text-gray-700 mb-1">
                                        {field.label}
                                    </label>
                                    <input
                                        type={field.type}
                                        id={field.id}
                                        name={field.id}
                                        className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-0 focus:ring-blue-500 focus:border-blue-500 transition"
                                        placeholder={field.placeholder}
                                    />
                                </div>
                            ))}

                            <div data-aos="fade-up" data-aos-delay="300">
                                <label htmlFor="message" className="block text-sm font-medium text-gray-700 mb-1">
                                    Your Message
                                </label>
                                <textarea
                                    id="message"
                                    name="message"
                                    rows={5}
                                    className="w-full px-4 py-3 rounded-xl border border-gray-300 focus:ring-0 focus:ring-blue-500 focus:border-blue-500 transition"
                                    placeholder="How can we help you?"
                                ></textarea>
                            </div>

                            <button
                                type="submit"
                                data-aos="zoom-in"
                                className="w-full bg-[#0000ff] hover:to-indigo-700 text-white font-medium py-3 px-6 rounded-xl transition duration-300 transform hover:scale-105"
                            >
                                Send Message
                            </button>
                        </form>
                    </div>

                    {/* Map and Contact Info */}
                    <div className="space-y-8">
                        {/* Map */}
                        <div
                            data-aos="fade-left"
                            className="overflow-hidden rounded-2xl shadow-lg h-96 hover:scale-[1.01] transition-transform duration-300"
                        >
                            <iframe
                                src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3964.0379584025322!2d80.2294905744099!3d6.516879693475571!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3ae3cfad68602ffd%3A0x738d46583b531a7a!2sDevasee%20Book%20Shop!5e0!3m2!1sen!2slk!4v1754892499744!5m2!1sen!2slk"
                                width="100%"
                                height="100%"
                                style={{ border: 0 }}
                                allowFullScreen
                                loading="lazy"
                                referrerPolicy="no-referrer-when-downgrade"
                            ></iframe>
                        </div>

                        {/* Contact Info */}
                        <div
                            data-aos="fade-up"
                            className="bg-white/80 backdrop-blur-sm p-8 rounded-2xl shadow-lg border border-gray-100"
                        >
                            <h2 className="text-2xl font-semibold  text-[#1e205a] bg-clip-text mb-6">
                                Our Information
                            </h2>
                            <div className="space-y-4">
                                {[
                                    {
                                        icon: (
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                                        ),
                                        title: "Address",
                                        text: "123 Book Street, Colombo, Sri Lanka"
                                    },
                                    {
                                        icon: (
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
                                        ),
                                        title: "Phone",
                                        text: "+94 11 234 5678"
                                    },
                                    {
                                        icon: (
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                                        ),
                                        title: "Email",
                                        text: "info@devaseebooks.com"
                                    },
                                    {
                                        icon: (
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                        ),
                                        title: "Opening Hours",
                                        text: (
                                            <>
                                                <p>Monday - Friday: 9:00 AM - 7:00 PM</p>
                                                <p>Saturday - Sunday: 10:00 AM - 5:00 PM</p>
                                            </>
                                        )
                                    }
                                ].map((item, i) => (
                                    <div
                                        className="flex items-start"
                                        key={i}
                                        data-aos="fade-up"
                                        data-aos-delay={100 * (i + 1)}
                                    >
                                        <div className="flex-shrink-0 text-[#0000ff]">
                                            <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                {item.icon}
                                            </svg>
                                        </div>
                                        <div className="ml-3">
                                            <h3 className="text-lg font-medium text-gray-900">{item.title}</h3>
                                            <div className="text-gray-700">{item.text}</div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* Floating animation keyframes */}
            <style jsx global>{`
        @keyframes float {
          0%, 100% { transform: translateY(0px); }
          50% { transform: translateY(-20px); }
        }
      `}</style>
        </section>
    );
};

export default Page;