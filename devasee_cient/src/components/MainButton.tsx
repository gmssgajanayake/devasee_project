interface MainButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    name: string;
}

export default function MainButton({ name, ...props }: MainButtonProps) {
    return (
        <button
            {...props}
            className={` cursor-pointer px-6 py-3 tracking-widest text-sm border border-[#2b216d] rounded-md text-[#2b216d] hover:bg-[#2b216d] hover:text-white transition duration-300 ${props.className || ""}`}
        >
            {name}
        </button>
    );
}