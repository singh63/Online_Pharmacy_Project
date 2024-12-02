import {useNavigate} from "react-router";
import "../../App.css";
import {useEffect, useState} from "react";
import axios from "axios";

const Category = () => {
    const navigate = useNavigate();
    const [categories, setCategories] = useState([]);

    const fetchCategories = async () => {
        try {
            const response = await axios.get("http://localhost:8080/api/public/categories?pageSize=100");
            const categoriesArray = response.data.content || [];
            setCategories(categoriesArray);
        } catch (error) {
            console.log(error);
        }
    };

    useEffect(() => {
        fetchCategories();
    }, []);

    return (

        <div className="container mt-5">
            <div className="d-flex justify-content-center">
                <div className="d-flex flex-nowrap overflow-auto">
                    {categories.map((item, index) => (
                        <div key={index} className="p-2 text-center">
                            <div
                                onClick={() => navigate(`/category/${item.categoryName}`)}
                                className="d-flex justify-content-center align-items-center rounded-circle bg-info category-item cursor-pointer mb-2"
                                style={{width: "4rem", height: "4rem", maxWidth: "6rem"}}
                            >
                                <img src={item.image} alt={item.categoryName} className="img-fluid category-image"/>
                            </div>
                            <h5 className="text-muted">{item.categoryName}</h5>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default Category;
