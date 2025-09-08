import AddBookForm from "@/app/dashboard/_components/AddBookForm";

export default function InventoryPage() {
    return (
        <div>
            <h2 className="text-2xl font-bold">📦 Inventory</h2>
            <p className="mt-4 text-gray-600">Manage your inventory here.</p>
            <AddBookForm/>
        </div>
    );


}
