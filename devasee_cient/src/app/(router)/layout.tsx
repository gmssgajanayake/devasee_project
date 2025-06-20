import SubNavBar from "@/app/(router)/_components/SubNavBar";

export default function RouterLayout({
                                         children,
                                     }: {
    children: React.ReactNode;
}) {
    return (
        <div>
            <SubNavBar/>
            <div className="min-h-screen p-4">{children}</div>
        </div>
    );
}
