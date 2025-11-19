"use client"

import {SignIn, useAuth, useUser} from "@clerk/nextjs";
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
            //const jwt = await getCustomJwt();
            const jwt = "eyJhbGciOiJSUzI1NiIsImNhdCI6ImNsX0I3ZDRQRDIyMkFBQSIsImtpZCI6Imluc18yemt5Q3ZzcllvdFV2RWRmTTdFTURKZm4zZjUiLCJ0eXAiOiJKV1QifQ.eyJhenAiOiJodHRwczovL3d3dy5kZXZhc2VlLmxrIiwiZW1haWwiOiIyMDIxc3AwMjZAdW5pdi5qZm4uYWMubGsiLCJlbWFpbFZlcmlmaWVkIjp0cnVlLCJleHAiOjE3NTkwNzAxNjMsImZpcnN0TmFtZSI6IjIwMjFzcDAyNiIsImlhdCI6MTc1OTAzNDE2MywiaW1hZ2VVcmwiOiJodHRwczovL2ltZy5jbGVyay5jb20vZXlKMGVYQmxJam9pWkdWbVlYVnNkQ0lzSW1scFpDSTZJbWx1YzE4eWVtdDVRM1p6Y2xsdmRGVjJSV1JtVFRkRlRVUktabTR6WmpVaUxDSnlhV1FpT2lKMWMyVnlYek15U0dsdFpUSm1VakpqU0V4M1JtNVRWbXBFYkVkSFdHdGtheUlzSW1sdWFYUnBZV3h6SWpvaU1sTWlmUSIsImlzcyI6Imh0dHBzOi8vY2xlcmsuZGV2YXNlZS5sayIsImp0aSI6ImViYTUyOTJjM2JiOTJlMmM3ZTgzIiwibGFzdE5hbWUiOiJTYWt1amEgU2hhbWFsIEdhamFuYXlha2UiLCJuYmYiOjE3NTkwMzQxNTgsIm9yZ0lkIjpudWxsLCJvcmdOYW1lIjpudWxsLCJvcmdSb2xlIjpudWxsLCJwaG9uZU51bWJlclZlcmlmaWVkIjpmYWxzZSwicHJpbWFyeUVtYWlsQWRkcmVzcyI6IjIwMjFzcDAyNkB1bml2Lmpmbi5hYy5sayIsInByaW1hcnlQaG9uZU51bWJlciI6bnVsbCwicHJpbWFyeVdlYjNXYWxsZXQiOm51bGwsInN1YiI6InVzZXJfMzJIaW1lMmZSMmNITHdGblNWakRsR0dYa2RrIiwidXNlcklkIjoidXNlcl8zMkhpbWUyZlIyY0hMd0ZuU1ZqRGxHR1hrZGsiLCJ1c2VybmFtZSI6InNoYW1hbCJ9.rvpHYQL7_UCsOumpbI1awY4aUIOyr2dwgeTUBK96PH76R1m2GkPeKYUAUezZBzesyqyi9_dNosDK9cqBmx4cGJXU-YmYWr4s2mXZVgzwB1Z9pCH_ZKI4c-ApiGgHE9_j_e68MTIcEKo1XFHicWp8FeeXuxo9rxTDwlmQzBo9gs69ouJfShtTdinsO_LuAqeJay-dTf_80IPoFxiZZZBLJdULy1Zi9ZYhzUkqIVC1WaS9pEigT9uzBD1qX9TrD-DFIJ62RKZ9FqRl72Qd5FSi5hykVCjGAUcMTUB2yhefZ_tvUStplklwwuhNmiDIH1ol9XqBARYDQe0M08yOw-WVaQ"
            if (jwt) {
                await authenticateWithAPI(jwt);
            }
        })();
        if (user) console.log("User Info:", user);
        console.log("Token:", token);
    }, [user]);

    return (
        <div>
            <SignIn
                signUpUrl="/sign-up"     // redirect to your local sign-up page
                afterSignInUrl="/dashboard" // optional: where to go after sign in
            />
        </div>
    );
}
