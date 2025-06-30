import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEnvelope} from "@fortawesome/free-solid-svg-icons";

export default function NewsSubscribe() {
    return (
        <div>
            <div className="bg-[#e4e8fa] mt-14 relative lg:flex flex-col hidden h-100  ">
                <div className={" w-12 ml-4 flex flex-col absolute top-0 items-center justify-between h-32"}>
                    <div className={" flex items-center w-full justify-between"}>
                        <div className={"bg-[#6e61d0] w-3 h-3"}></div>
                        <div className={"bg-white  w-3 h-3"}></div>
                    </div>
                    <div className={" flex items-center w-full justify-between"}>
                        <div className={"bg-[#f2f2f2] w-3 h-3"}></div>
                        <div className={"bg-[#0000ff]  w-3 h-3"}></div>
                    </div>
                    <div className={" flex items-center w-full justify-between"}>
                        <div className={"bg-[#f2f2f2] w-3 h-3"}></div>
                        <div className={"bg-[#f2f2f2]  w-3 h-3"}></div>
                    </div>
                    <div className={" flex items-center w-full justify-between"}>
                        <div className={"bg-[#f2f2f2] w-3 h-3"}></div>
                        <div className={"bg-[#f2f2f2]  w-3 h-3"}></div>
                    </div>
                </div>
                <div className=" w-36 pb-4 flex items-center absolute bottom-0 justify-between">
                    <div className="w-3 h-3 bg-[#f2f2f2]"></div>
                    <div className="w-3 h-3 bg-white"></div>
                    <div className="w-3 h-3 bg-[#f2f2f2]"></div>
                    <div className="w-3 h-3 bg-white"></div>
                    <div className="w-3 h-3 bg-[#f2f2f2]"></div>
                </div>
                <div className="w-32 h-24 mb-4  flex flex-col justify-between items-center right-0 bottom-0 absolute">
                    <div className="w-full flex justify-between items-center">
                        <div className="w-3 h-3 bg-white"></div>
                        <div className="w-3 h-3 bg-white"></div>
                        <div className="w-3 h-3 bg-white"></div>
                        <div className="w-3 h-3 bg-white"></div>
                    </div>
                    <div className="w-full flex justify-between items-center">
                        <div className="w-3 h-3 bg-white"></div>
                        <div className="w-3 h-3 bg-white"></div>
                        <div className="w-3 h-3 bg-white"></div>
                        <div className="w-3 h-3 bg-white"></div>
                    </div>
                    <div className="w-full flex justify-between items-center">
                        <div className="w-3 h-3 bg-white"></div>
                        <div className="w-3 h-3 bg-white"></div>
                        <div className="w-3 h-3 bg-white"></div>
                        <div className="w-3 h-3 bg-white"></div>
                    </div>
                </div>
                <div
                    className="w-auto  h-72  md:h-auto px-10 py-4 md:p-20 bg-[#0000ff] flex-col gap-4 mx-10 md:mx-20 flex justify-center items-center">
                    <h3 className={"text-2xl text-center md:text-4xl lg:text-5xl text-white font-bold "}>Subscibe to Our
                        Newsletter</h3>
                    <p className={" md:w-2xl   text-center text-[16px] md:text-[18px] lg:text-[16px]  px-4 text-white"}>Stay
                        updated with the latest news, exclusive offers, and promotions! Subscribe to our newsletter for
                        curated content delivered straight to your inbox</p>
                </div>
                <div className={" p-10 flex justify-center items-center w-full absolute bottom-8 md:bottom-12"}>
                    <div className="flex bg-white items-center px-6 py-4 w-md">
                        <FontAwesomeIcon icon={faEnvelope} className="text-gray-400 mr-2"/>
                        <input
                            id="price"
                            name="price"
                            type="text"
                            placeholder="youremail@gmail.com"
                            className="block min-w-0 grow text-base border-0 text-gray-900 placeholder:text-gray-400 focus:outline-none sm:text-sm"
                        />
                        <button
                            className={"bg-[#0000ff] cursor-pointer text-white px-10 py-4 font-extrabold text-xs tracking-wider"}>
                            SUBSCRIBE
                        </button>
                    </div>
                </div>
            </div>
            <div className={"mt-14 gap-4 lg:hidden  bg-[#0000ff] pt-2 flex flex-col justify-center items-center"}>
                <div className="w-screen h-auto bg-[#0000ff] p-4  flex flex-col justify-center items-center">
                    <h3 className={"text-2xl text-center mt-3 md:text-4xl lg:text-5xl text-white font-bold "}>Subscibe to Our
                        Newsletter</h3>
                    <p className={" md:w-2xl   text-center text-[16px] md:text-[18px] lg:text-[16px]  px-4 text-white"}>Stay
                        updated with the latest news, exclusive offers, and promotions! Subscribe to our newsletter for
                        curated content delivered straight to your inbox</p>
                </div>
                <div className="flex mb-8 w-sm bg-white items-center px-3  md:px-4 md:py-3 py-2 ">
                    <FontAwesomeIcon icon={faEnvelope} className="text-gray-400 mr-2"/>
                    <input
                        id="price"
                        name="price"
                        type="text"
                        placeholder="youremail@gmail.com"
                        className="block min-w-0 grow text-base border-0 text-gray-900 placeholder:text-gray-400 focus:outline-none sm:text-sm"
                    />
                    <button
                        className={"bg-[#0000ff] cursor-pointer text-white px-10 py-4 font-extrabold text-xs tracking-wider"}>
                        SUBSCRIBE
                    </button>
                </div>

            </div>
        </div>

    );
}