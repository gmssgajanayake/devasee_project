"use client";

import { useState, useMemo } from "react";
import Image from "next/image";

// Example product data with Unsplash images
const PRODUCTS = [
  { id: 1, name: "Custom Banner", category: "Banners", image: "https://images.unsplash.com/photo-1593642634315-48f5414c3ad9" },
  { id: 2, name: "Personalized Mug", category: "Mugs", image: "https://images.unsplash.com/photo-1593642634315-48f5414c3ad9" },
  { id: 3, name: "Premium Paper Prints", category: "Papers", image: "https://images.unsplash.com/photo-1593642634315-48f5414c3ad9" },
  { id: 4, name: "3D Printed Model", category: "Advanced", image: "https://images.unsplash.com/photo-1593642634315-48f5414c3ad9" },
  { id: 5, name: "Large Banner", category: "Banners", image: "https://images.unsplash.com/photo-1593642634315-48f5414c3ad9" },
  { id: 6, name: "Ceramic Mug", category: "Mugs", image: "https://images.unsplash.com/photo-1593642634315-48f5414c3ad9" },
];

const CATEGORIES = ["All", "Banners", "Mugs", "Papers", "Advanced"];

export default function PrintingServicesPage() {
  const [selectedCategory, setSelectedCategory] = useState("All");

  // Filter products based on selected category
  const filteredProducts = useMemo(() => {
    if (selectedCategory === "All") return PRODUCTS;
    return PRODUCTS.filter((product) => product.category === selectedCategory);
  }, [selectedCategory]);

  return (
    <div className={"pt-24 lg:pt-30"}>
      {/* Hero / Banner Section */}
      <section className="relative bg-[#0000ff] text-white py-16 text-center">
        <h1 className="text-4xl font-bold">Printing Services</h1>
        <p className="mt-2 text-lg">High-quality printing for banners, mugs, papers, and more</p>
      </section>

      {/* Filter Buttons */}
      <section className="py-8">
        <div className="flex justify-center gap-2 flex-wrap">
          {CATEGORIES.map((cat) => (
            <button
              key={cat}
              onClick={() => setSelectedCategory(cat)}
              className={`px-3 py-1.5 rounded-full border text-sm transition ${
                selectedCategory === cat
                  ? "bg-blue-600 text-white"
                  : "bg-white text-gray-700 border-gray-300 hover:bg-gray-100"
              }`}
            >
              {cat}
            </button>
          ))}
        </div>
      </section>

      {/* Product Grid */}
      <section className="px-4 pb-12">
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
          {filteredProducts.map((product) => (
            <div
              key={product.id}
              className="bg-white rounded-md shadow hover:shadow-lg transition overflow-hidden text-sm"
            >
              <Image
                src={product.image}
                alt={product.name}
                width={400}
                height={300}
                className="w-full h-40 object-cover"
              />
              <div className="p-3">
                <h3 className="text-base font-semibold">{product.name}</h3>
                <p className="text-xs text-gray-500">{product.category}</p>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Contact Section */}
      <section className="bg-blue-50 py-12 text-center">
        <h2 className="text-2xl font-bold mb-4">Need Custom Printing?</h2>
        <p className="text-gray-700 mb-6">Contact us for bulk orders or custom designs.</p>
        <a
          href="/contact"
          className="bg-blue-600 text-white px-6 py-3 rounded shadow hover:bg-blue-700"
        >
          Get in Touch
        </a>
      </section>
    </div>
  );
}