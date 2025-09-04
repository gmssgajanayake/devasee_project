import {SignUp} from "@clerk/nextjs";

export default function Page(){
    return(
        <div>
            <SignUp
            signInUrl={"/sign-in"}
            redirectUrl={"/dashboard"}
            />
        </div>
    );
}