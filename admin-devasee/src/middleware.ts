// src/middleware.ts
import { clerkMiddleware } from "@clerk/nextjs/server";

export default clerkMiddleware();

// Match all routes except Next.js internals
export const config = {
    matcher: ["/((?!_next|.*\\..*|favicon.ico).*)"],
};
