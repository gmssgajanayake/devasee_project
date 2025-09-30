"use client"

import { SignUp, useAuth, useUser } from "@clerk/nextjs";
import { useEffect, useState } from "react";
import { authenticateWithAPI } from "@/lib/actions";

export default function Page() {
    const { user } = useUser();
    const { getToken } = useAuth();
    const [token, setToken] = useState<string | null>(null);

    async function getCustomJwt() {
        try {
            const jwt = await getToken({ template: "devasee_user_token" });
            setToken(jwt);
            console.log("JWT with user details:", jwt);
            return jwt;
        } catch (err) {
            console.error("Error getting JWT:", err);
            return null;
        }
    }

    useEffect(() => {
        (async () => {
            const jwt = await getCustomJwt();
            if (jwt) {
                await authenticateWithAPI(jwt);
            }
        })();

        if (user) console.log("User Info:", user);
    }, [user]);

    return (
        <div>
            <SignUp signInUrl={"/sign-in"} redirectUrl={"/dashboard"} />
        </div>
    );
}
