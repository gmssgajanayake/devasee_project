import { clerkMiddleware, createRouteMatcher } from "@clerk/nextjs/server";

const isPublicRoute = createRouteMatcher([
    "/sign-in(.*)",
    "/sign-up(.*)",
    "/favicon.ico",
]);

export default clerkMiddleware(async (auth, req) => {
    if (!isPublicRoute(req)) {
        await auth.protect();
    }
});

export const config = {
    matcher: [
        // Skip internal assets and static files
        "/((?!_next|.*\\.(?:png|jpg|jpeg|svg|gif|ico|css|js|woff2?|ttf|eot|json|txt|map)).*)",
        // Include API routes
        "/(api|trpc)(.*)",
    ],
};
