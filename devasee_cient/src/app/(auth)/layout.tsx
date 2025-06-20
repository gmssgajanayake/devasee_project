export default function AuthLayout({
                                       children,
                                   }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <div>
            <h1>Authentication page</h1>
            {children}
        </div>
    );
}