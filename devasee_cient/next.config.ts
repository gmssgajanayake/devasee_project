import type { NextConfig } from "next";

const nextConfig: NextConfig = {
    async redirects() {
        return [
            // Redirect non-trailing slash to trailing slash (if needed)
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

            // Optional: redirect old URLs or fix casing
            // {
            //   source: "/Contact",
            //   destination: "/contact",
            //   permanent: true,
            // },
        ];
    },
};

export default nextConfig;
