import { SignUp} from "@clerk/nextjs";

export default function Page() {
    return (
        <div className={"w-full mt-40 mb-10 h-auto flex justify-center items-center"}>
            <SignUp />
        </div>
    );
}