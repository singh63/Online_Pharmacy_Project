import {useContext, useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import myContext from "../../context/myContext"; // Adjust import based on your project structure

const SearchBar = () => {
    const context = useContext(myContext);
    const [search, setSearch] = useState("");
    const [products, setProducts] = useState([]);
    const navigate = useNavigate();

    const fetchData = async (input) => {
        try {
            const response = await axios.get(`http://localhost:8080/api/public/products/keyword/${input}?pageSize=8`);
            const productsArray = response.data.content || [];
            setProducts(productsArray);
        } catch (error) {
            console.error("Error fetching products:", error);
        }
    };

    const handleSearchInputChange = (input) => {
        setSearch(input);
        fetchData(input);
    };

    return (
        <div className="position-relative">
            <div className="d-flex justify-content-center mb-2">
                <input
                    type="text"
                    placeholder="Search here"
                    value={search}
                    onChange={(e) => handleSearchInputChange(e.target.value)}
                    className="form-control"
                    style={{width: '100%'}}
                />
            </div>

            {search && (
                <div className="position-absolute bg-light border rounded shadow-lg z-index-100 mt-1"
                     style={{width: '100%'}}
                >
                    {products.length > 0 ? (
                        products.map((item) => (
                            <div
                                key={item.productId}
                                className="p-2 cursor-pointer d-flex align-items-center border-bottom"
                                onClick={() => navigate(`/productinfo/${item.productId}`)}
                            >
                                <img className="w-25 h-25 me-2 rounded" src={item.image} alt={item.productName}/>
                                <span>{item.productName}</span>
                            </div>
                        ))
                    ) : (
                        <div className="d-flex justify-content-center align-items-center p-3">
                            <img className="w-50" src="/no-results.png" alt="No results"/>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default SearchBar;
