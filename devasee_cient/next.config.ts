import type { NextConfig } from "next";

const nextConfig: NextConfig = {
    images: {
        // You can keep this for backward compatibility if needed, but it's less preferred than remotePatterns
        domains: ["devaseebookstoreimages.blob.core.windows.net"],
        remotePatterns: [
            {
                protocol: "https",
                hostname: "images.unsplash.com",
            },
            // ✨ ADD THIS ENTRY TO FIX THE ERROR!
            {
                protocol: "https",
                // This is the hostname from your error message:
                hostname: "devasee.blob.core.windows.net",
            },
        ],
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