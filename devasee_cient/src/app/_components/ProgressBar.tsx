"use client";

import { useEffect } from "react";
import { usePathname } from "next/navigation";
import NProgress from "nprogress";
import Router from "next/router"; // ← Note: This is from next/router, not useRouter

import "nprogress/nprogress.css"; // If using global.css, you can also move this there

export default function ProgressBar() {
    const pathname = usePathname();

    useEffect(() => {
        NProgress.configure({ showSpinner: false });

        const handleStart = () => NProgress.start();
        const handleStop = () => NProgress.done();

        Router.events.on("routeChangeStart", handleStart);
        Router.events.on("routeChangeComplete", handleStop);
        Router.events.on("routeChangeError", handleStop);

        return () => {
            Router.events.off("routeChangeStart", handleStart);
            Router.events.off("routeChangeComplete", handleStop);
            Router.events.off("routeChangeError", handleStop);
        };
    }, [pathname]);

    return null;
}
