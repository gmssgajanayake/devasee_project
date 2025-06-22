//import SubNavBar from "@/app/(router)/_components/SubNavBar";

export default function RouterLayout({
                                         children,
                                     }: {
    children: React.ReactNode;
}) {
    return (
        <div>
            {/*<SubNavBar/>*/}
            <div className={"w-screen"}>{children}</div>
        </div>
    );
}
