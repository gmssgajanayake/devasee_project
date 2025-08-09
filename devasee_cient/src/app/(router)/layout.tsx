import React from "react";

export default function RouterLayout({
                                         children,
                                     }: {
    children: React.ReactNode;
}) {
    return (
        <div>
            <div className={"w-screen"}>{children}</div>
        </div>
    );
}
