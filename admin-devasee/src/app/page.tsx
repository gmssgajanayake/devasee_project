export default function Home() {
  return (
    <div className="font-sans grid grid-rows-[20px_1fr_20px] items-center justify-items-center min-h-screen p-8 pb-20 gap-16 sm:p-20">
        <h1 className="text-4xl underline sm:text-5xl font-bold text-center text-gray-800">
            Welcome to Devasee Admin Panel
        </h1>
        <p className="text-lg sm:text-xl text-center text-gray-600 max-w-2xl">
            Manage your products, orders, and customers efficiently with our intuitive admin interface.
        </p>
    </div>
  );
}
