export default function ItemCard(){
    return (
      <div className="overflow-hidden group">
              <div className="w-48 h-64 shadow-lg relative flex-col flex justify-end items-center">
                <img src="https://grantha.lk/media/catalog/product/cache/25fccda23befa3a8b49210419c7720b7/i/m/img_20220130_0005.jpg" alt="cover photo"
                className="mb-5 w-36 h-54 object-cover" />
                <button className="absolute bg-[#0000FF] mb-16 tracking-widest text-white text-[12px] px-10 py-2 hover:bg-blue-700 transition duration-200">ADD TO CART</button>
              </div>
             
              <div className=""></div>
      </div>
    );
}