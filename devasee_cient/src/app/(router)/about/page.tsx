import React from "react";
import SubNavBar from "@/app/(router)/_components/SubNavBar";

export default function Page() {
  return (
    <div>
      <SubNavBar path="ABOUT" />
      {/* Top, Centered Section */}
      <div className="max-w-4xl mx-auto py-12 px-4 flex flex-col items-center">
        <h5 className="text-xs font-semibold text-blue-600 tracking-widest mb-2 uppercase text-center">
          WHO WE ARE
        </h5>
        <h1 className="text-3xl md:text-4xl font-semibold text-black text-center mb-3">
          Explore Our Story, Growth, and Commitment to Quality
        </h1>
        <div className="w-16 h-2 bg-blue-500 rounded-full mb-4 mx-auto"></div>
        <p className="text-blue-600 text-center text-lg md:text-xl mb-4">
          Your One-Stop Hub for Books, Stationery, and Custom Printing in Sri Lanka.
        </p>
        <p className="text-center text-gray-600 mb-8 max-w-2xl">
          At Devasee, we believe in quality, variety, and convenience. Since our inception, we've grown from a neighborhood shop to Sri Lanka's go-to source for all things books, stationery, and personalized printing. Our passion drives us to deliver unmatched service, competitive prices, and an ever-evolving product selection tailored to your needs.
        </p>
      </div>

      {/* Break out to wider containers below */}
      <div className="w-full flex flex-col items-center px-2 mb-8">
        {/* Wide Photo */}
        <div
          className="w-full max-w-7xl h-64 md:h-96 bg-blue-100 rounded-2xl flex items-center justify-center mb-10"
          style={{
            backgroundImage: "url('/about-us-photo.jpg')",
            backgroundSize: "cover",
            backgroundPosition: "center",
          }}
        >
          <span className="text-blue-400 text-xl font-bold bg-white bg-opacity-70 rounded-lg px-4 py-2">
            Our vibrant workspace
          </span>
        </div>
        {/* Wide Two-Column Paragraphs */}
        <div className="grid md:grid-cols-2 gap-12 w-full max-w-7xl">
          <div className="text-gray-700 text-center md:text-left text-base md:text-lg">
            <h2 className="text-lg font-semibold mb-2 text-blue-500">Our Mission</h2>
            <p>
              To inspire learning, creativity, and productivity by providing the best products and printing services under one roof. We strive to build lasting relationships with our customers and partners through integrity, reliability, and continual innovation.
            </p>
          </div>
          <div className="text-gray-700 text-center md:text-left text-base md:text-lg">
            <h2 className="text-lg font-semibold mb-2 text-blue-500">Our Values</h2>
            <p>
              Each member of our team is committed to customer satisfaction, community support, and sustainability. We pride ourselves on a transparent, friendly service approach—making Devasee a trusted partner for students, professionals, and businesses across Sri Lanka.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}