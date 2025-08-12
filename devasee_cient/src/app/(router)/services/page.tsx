"use client";

import React, { useMemo, useState } from "react";

type PrintingProduct = {
  id: string;
  name: string;
  category: string;
  description: string;
  image: string;
};

const PRODUCTS: PrintingProduct[] = [
  /* ... same product list as you provided ... */
  {
    id: "p-1",
    name: "Outdoor Vinyl Banner",
    category: "Banners",
    description:
        "Durable outdoor vinyl banner with weather-resistant ink. Perfect for events, storefronts, and promotions. Available with grommets and hems.",
    image: "https://placehold.co/600x400/png?text=Banners",
  },
  // (keep the rest unchanged)
];

export default function PrintingServicesPage() {
  const [query, setQuery] = useState("");
  const [selectedCategories, setSelectedCategories] = useState<string[]>([]);
  const [showMobileFilter, setShowMobileFilter] = useState(false);
  const [openProduct, setOpenProduct] = useState<PrintingProduct | null>(null);

  const categories = useMemo(
      () => Array.from(new Set(PRODUCTS.map((p) => p.category))).sort(),
      []
  );

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    return PRODUCTS.filter((p) => {
      const matchesCat =
          selectedCategories.length === 0 ||
          selectedCategories.includes(p.category);
      const matchesQuery =
          q.length === 0 ||
          p.name.toLowerCase().includes(q) ||
          p.category.toLowerCase().includes(q) ||
          p.description.toLowerCase().includes(q);
      return matchesCat && matchesQuery;
    });
  }, [query, selectedCategories]);

  const toggleCategory = (cat: string) => {
    setSelectedCategories((prev) =>
        prev.includes(cat) ? prev.filter((c) => c !== cat) : [...prev, cat]
    );
  };

  const clearFilters = () => {
    setSelectedCategories([]);
    setQuery("");
  };

  return (
      <div className="min-h-screen bg-gray-50">
        {/* Header */}
        <div className="w-full border-b bg-white">
          <div className="mx-auto max-w-7xl px-4 py-6">
            <h1 className="text-2xl font-bold text-gray-900">Printing Services</h1>
            <p className="mt-1 text-sm text-gray-600">
              Browse our range of customizable printed products. Online purchasing is not available; view items and descriptions only.
            </p>
          </div>
        </div>

        {/* Controls */}
        <div className="mx-auto max-w-7xl px-4 py-4">
          <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
            {/* Search */}
            <div className="w-full sm:max-w-md">
              <label htmlFor="search" className="sr-only">
                Search products
              </label>
              <div className="relative">
                <input
                    id="search"
                    type="text"
                    placeholder="Search products (e.g., banners, mugs, medals)..."
                    className="w-full rounded-lg border border-gray-300 bg-white px-4 py-2.5 text-sm text-gray-800 placeholder:text-gray-400 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-100"
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                />
                <div className="pointer-events-none absolute right-3 top-1/2 -translate-y-1/2 text-gray-400">
                  <svg
                      xmlns="http://www.w3.org/2000/svg"
                      className="h-5 w-5"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="currentColor"
                      strokeWidth="2"
                  >
                    <circle cx="11" cy="11" r="8"></circle>
                    <path d="m21 21-4.3-4.3"></path>
                  </svg>
                </div>
              </div>
            </div>

            {/* Buttons */}
            <div className="flex items-center gap-2">
              <button
                  type="button"
                  onClick={() => setShowMobileFilter(true)}
                  className="inline-flex items-center gap-2 rounded-lg border border-gray-300 bg-white px-3 py-2 text-sm font-medium text-gray-700 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-200 sm:hidden"
                  aria-label="Open filters"
              >
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="h-4 w-4"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="2"
                >
                  <path d="M3 6h18M6 12h12M10 18h4" />
                </svg>
                Filters
              </button>
              <button
                  type="button"
                  onClick={clearFilters}
                  className="inline-flex items-center gap-2 rounded-lg border border-transparent bg-gray-200 px-3 py-2 text-sm font-medium text-gray-700 hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-200"
              >
                Clear
              </button>
            </div>
          </div>

          {/* Filters (Desktop) */}
          <div className="mt-4 hidden rounded-lg border border-gray-200 bg-white p-4 sm:block">
            <h2 className="mb-3 text-sm font-semibold text-gray-800">
              Filter by category
            </h2>
            <div className="flex flex-wrap gap-2">
              {categories.map((cat) => {
                const active = selectedCategories.includes(cat);
                return (
                    <button
                        key={cat}
                        onClick={() => toggleCategory(cat)}
                        className={`rounded-full border px-3 py-1.5 text-sm transition ${
                            active
                                ? "border-blue-600 bg-blue-50 text-blue-700"
                                : "border-gray-300 bg-white text-gray-700 hover:bg-gray-100"
                        }`}
                    >
                      {cat}
                    </button>
                );
              })}
            </div>
          </div>
        </div>

        {/* Results summary */}
        <div className="mx-auto max-w-7xl px-4">
          <p className="mb-3 text-sm text-gray-600">
            Showing {filtered.length} {filtered.length === 1 ? "item" : "items"}
          </p>
        </div>

        {/* Grid */}
        <div className="mx-auto max-w-7xl px-4 pb-10">
          {filtered.length === 0 ? (
              <div className="rounded-lg border border-dashed border-gray-300 bg-white p-10 text-center text-gray-600">
                No products found. Try adjusting your search or filters.
              </div>
          ) : (
              <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
                {filtered.map((p) => (
                    <article
                        key={p.id}
                        className="group overflow-hidden rounded-xl border border-gray-200 bg-white shadow-sm transition hover:shadow-md"
                    >
                      {/* image block replaced with Tailwind classes; backgroundImage uses minimal inline style */}
                      <div
                          className="aspect-[4/3] w-full overflow-hidden bg-gray-100 bg-cover bg-center transition duration-300 group-hover:scale-[1.03]"
                          style={{ backgroundImage: `url(${p.image})` }}
                          role="img"
                          aria-label={p.name}
                      />
                      <div className="p-4">
                        <div className="mb-2 flex items-center gap-2">
                    <span className="inline-flex items-center rounded-full bg-blue-50 px-2 py-0.5 text-xs font-medium text-blue-700">
                      {p.category}
                    </span>
                        </div>
                        <h3 className="text-base font-semibold text-gray-900">
                          {p.name}
                        </h3>
                        <p className="mt-1 text-sm text-gray-600">
                          {p.description.length > 120
                              ? p.description.slice(0, 120) + "..."
                              : p.description}
                        </p>
                        <div className="mt-4 flex items-center justify-between">
                          <span className="text-xs text-gray-400">View description</span>
                          <button
                              type="button"
                              onClick={() => setOpenProduct(p)}
                              className="rounded-md border border-gray-300 bg-white px-3 py-1.5 text-sm font-medium text-gray-700 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-200"
                          >
                            Details
                          </button>
                        </div>
                      </div>
                    </article>
                ))}
              </div>
          )}
        </div>

        {/* Mobile Filters Drawer */}
        {showMobileFilter && (
            <div className="fixed inset-0 z-50">
              <div
                  className="absolute inset-0 bg-black/40"
                  onClick={() => setShowMobileFilter(false)}
              />
              <div className="absolute right-0 top-0 h-full w-4/5 max-w-sm bg-white shadow-xl">
                <div className="flex items-center justify-between border-b px-4 py-3">
                  <h2 className="text-base font-semibold text-gray-900">Filters</h2>
                  <button
                      className="rounded-md p-2 text-gray-600 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-200"
                      onClick={() => setShowMobileFilter(false)}
                      aria-label="Close filters"
                  >
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        className="h-5 w-5"
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        strokeWidth="2"
                    >
                      <path d="M6 18 18 6M6 6l12 12" />
                    </svg>
                  </button>
                </div>
                <div className="p-4">
                  <h3 className="mb-2 text-sm font-semibold text-gray-800">Category</h3>
                  <div className="flex flex-wrap gap-2">
                    {categories.map((cat) => {
                      const active = selectedCategories.includes(cat);
                      return (
                          <button
                              key={cat}
                              onClick={() => toggleCategory(cat)}
                              className={`rounded-full border px-3 py-1.5 text-sm ${
                                  active
                                      ? "border-blue-600 bg-blue-50 text-blue-700"
                                      : "border-gray-300 bg-white text-gray-700"
                              }`}
                          >
                            {cat}
                          </button>
                      );
                    })}
                  </div>
                  <div className="mt-6 flex items-center justify-between">
                    <button
                        onClick={clearFilters}
                        className="rounded-md border border-gray-300 bg-white px-3 py-2 text-sm font-medium text-gray-700 hover:bg-gray-100"
                    >
                      Clear
                    </button>
                    <button
                        onClick={() => setShowMobileFilter(false)}
                        className="rounded-md bg-blue-600 px-4 py-2 text-sm font-medium text-white hover:bg-blue-700"
                    >
                      Apply
                    </button>
                  </div>
                </div>
              </div>
            </div>
        )}

        {/* Details Modal */}
        {openProduct && (
            <div className="fixed inset-0 z-50">
              <div
                  className="absolute inset-0 bg-black/40"
                  onClick={() => setOpenProduct(null)}
              />
              <div className="absolute inset-0 flex items-center justify-center p-4">
                <div className="w-full max-w-2xl overflow-hidden rounded-xl bg-white shadow-2xl">
                  <div className="flex items-center justify-between border-b px-5 py-3">
                    <h3 className="text-lg font-semibold text-gray-900">{openProduct.name}</h3>
                    <button
                        onClick={() => setOpenProduct(null)}
                        className="rounded-md p-2 text-gray-600 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-blue-200"
                        aria-label="Close details"
                    >
                      <svg
                          xmlns="http://www.w3.org/2000/svg"
                          className="h-5 w-5"
                          viewBox="0 0 24 24"
                          fill="none"
                          stroke="currentColor"
                          strokeWidth="2"
                      >
                        <path d="M6 18 18 6M6 6l12 12" />
                      </svg>
                    </button>
                  </div>

                  <div className="grid gap-0 sm:grid-cols-2">
                    <div
                        className="h-56 w-full sm:h-full bg-cover bg-center"
                        style={{ backgroundImage: `url(${openProduct.image})` }}
                        role="img"
                        aria-label={openProduct.name}
                    />
                    <div className="p-5">
                  <span className="inline-flex items-center rounded-full bg-blue-50 px-2 py-0.5 text-xs font-medium text-blue-700">
                    {openProduct.category}
                  </span>
                      <p className="mt-3 text-sm leading-6 text-gray-700">{openProduct.description}</p>
                      <div className="mt-5 rounded-md border border-amber-300 bg-amber-50 p-3 text-sm text-amber-800">
                        Online purchasing is not available for printing services. View details and contact our team to proceed with customization.
                      </div>
                    </div>
                  </div>

                  <div className="flex items-center justify-end gap-2 border-t px-5 py-3">
                    <button
                        onClick={() => setOpenProduct(null)}
                        className="rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-100"
                    >
                      Close
                    </button>
                  </div>
                </div>
              </div>
            </div>
        )}
      </div>
  );
}
