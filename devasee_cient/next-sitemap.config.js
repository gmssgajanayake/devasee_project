/** @type {import('next-sitemap').IConfig} */
module.exports = {
    siteUrl: 'https://www.devasee.lk',         // Base URL of your site (always use HTTPS + www if you're forcing that)
    generateRobotsTxt: true,                   // Generate robots.txt automatically
    sitemapSize: 7000,                         // Break into multiple files if URLs exceed 7000
    exclude: ['/404', '/500', '/privacy'],     // Exclude unwanted pages if needed

    robotsTxtOptions: {
        policies: [
            {
                userAgent: '*',
                allow: '/',
            },
        ],
        additionalSitemaps: [
            'https://www.devasee.lk/sitemap-0.xml', // Optional: manually add additional sitemap parts
        ],
    },
};
