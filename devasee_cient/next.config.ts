import type { NextConfig } from "next";

const nextConfig: NextConfig = {
    images: {
        domains: ["images.unsplash.com"],
    },

    async redirects() {
        return [
            {
                source: "/:path*",
                has: [
                    {
                        type: "host",
                        value: "devasee.lk",
                    },
                ],
                destination: "https://www.devasee.lk/:path*",
                permanent: true,
            },
            {
                source: "/contact/",
                destination: "/contact",
                permanent: true,
            },
            {
                source: "/about/",
                destination: "/about",
                permanent: true,
            },
            {
                source: "/products/",
                destination: "/products",
                permanent: true,
            },
            {
                source: "/services/",
                destination: "/services",
                permanent: true,
            },
        ];
    },
};

export default nextConfig;
