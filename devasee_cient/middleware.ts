import { NextRequest, NextResponse } from 'next/server';

export function middleware(request: NextRequest) {
    const isLoggedIn = Boolean(request.cookies.get('auth-token'));

    const isProtectedRoute = request.nextUrl.pathname.startsWith('/(router)');

    if (!isLoggedIn && isProtectedRoute) {
        return NextResponse.redirect(new URL('/auth/sign-in', request.url));
    }

    return NextResponse.next();
}

// Apply to all pages in the (router) group
export const config = {
    matcher: ['/(router)/:path*'],
};
