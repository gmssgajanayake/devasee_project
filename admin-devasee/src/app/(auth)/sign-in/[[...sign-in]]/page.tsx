import {SignIn} from "@clerk/nextjs";

export default function Page(){
    return(
        <div>
            <SignIn
            signUpUrl="/sign-up"     // redirect to your local sign-up page
            afterSignInUrl="/dashboard" // optional: where to go after sign in
            />
        </div>
    );
}