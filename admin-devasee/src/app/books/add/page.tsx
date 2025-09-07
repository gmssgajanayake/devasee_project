import AddBookForm from "@/app/(router)/books/AddBookForm";

export default function AddBookPage() {
    return (
        <div className="max-w-2xl mx-auto py-10">
            <h1 className="text-2xl font-bold mb-6">Add a New Book</h1>
            <AddBookForm />
        </div>
    );
}
