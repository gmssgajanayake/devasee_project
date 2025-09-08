import AddBookForm from "@/app/dashboard/_components/AddBookForm";

export default function InventoryPage() {
    return (
        <div className="p-6 space-y-6">
            {/* Header Section */}
            <div className="flex items-center justify-between">
                <div>
                    <h2 className="text-3xl font-bold text-gray-800">📦 Inventory</h2>
                    <p className="mt-1 text-gray-500">
                        Add and manage your bookstore inventory here.
                    </p>
                </div>
                <button className="px-4 py-2 text-white bg-blue-600 rounded-xl shadow hover:bg-blue-700 transition">
                    View All Books
                </button>
            </div>

            {/* Content Section */}
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
                {/* Left: Add Book Form */}
                <div className="lg:col-span-2">
                    <div className="bg-white rounded-2xl shadow-md p-6">
                        <h3 className="text-xl font-semibold text-gray-700 mb-4">➕ Add a New Book</h3>
                        <AddBookForm />
                    </div>
                </div>
                <div className="bg-white rounded-2xl shadow-md p-6">
                    <h3 className="text-lg font-semibold text-gray-800 mb-4">📊 Quick Stats</h3>
                    <div className="grid grid-cols-2 gap-4">
                        {/* Total Books */}
                        <div className="flex flex-col items-center justify-center bg-gray-50 rounded-xl p-4 shadow-sm hover:shadow-md transition">
                            <span className="text-2xl">📕</span>
                            <p className="mt-2 text-xl font-bold text-gray-800">120</p>
                            <p className="text-sm text-gray-500">Total Books</p>
                        </div>

                        {/* In Stock */}
                        <div className="flex flex-col items-center justify-center bg-green-50 rounded-xl p-4 shadow-sm hover:shadow-md transition">
                            <span className="text-2xl">✅</span>
                            <p className="mt-2 text-xl font-bold text-green-700">95</p>
                            <p className="text-sm text-gray-600">In Stock</p>
                        </div>

                        {/* Low Stock */}
                        <div className="flex flex-col items-center justify-center bg-yellow-50 rounded-xl p-4 shadow-sm hover:shadow-md transition">
                            <span className="text-2xl">⚠️</span>
                            <p className="mt-2 text-xl font-bold text-yellow-600">10</p>
                            <p className="text-sm text-gray-600">Low Stock</p>
                        </div>

                        {/* Out of Stock */}
                        <div className="flex flex-col items-center justify-center bg-red-50 rounded-xl p-4 shadow-sm hover:shadow-md transition">
                            <span className="text-2xl">❌</span>
                            <p className="mt-2 text-xl font-bold text-red-600">15</p>
                            <p className="text-sm text-gray-600">Out of Stock</p>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    );
}
