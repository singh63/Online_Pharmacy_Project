import React, {useEffect, useState} from "react";
import axios from "axios";
import ProductDetail from "../../components/admin/ProductDetail";
import OrderDetail from "../../components/admin/OrderDetail";
import UserDetail from "../../components/admin/UserDetail";
import {Tab, Tabs} from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import {useNavigate} from "react-router-dom";

const AdminDashboard = () => {

    const navigate = useNavigate();

    const [products, setProducts] = useState([]);
    const [orders, setOrders] = useState([]);
    const [users, setUsers] = useState([]);

    // Getting information of admin
    const userEmail = JSON.parse(localStorage.getItem("user"));
    const Role = JSON.parse(localStorage.getItem("role"));
    const firstName = JSON.parse(localStorage.getItem("firstName"));
    const lastName = JSON.parse(localStorage.getItem("lastName"));

    useEffect(() => {
        // Fetch all products
        const fetchProducts = async () => {
            try {
                const response = await axios.get("http://localhost:8080/api/public/products");
                const productsArray = response.data.content || [];
                setProducts(productsArray);
            } catch (error) {
                console.error("Error fetching products:", error);
            }
        };

        // Fetch all orders
        const fetchOrders = async () => {
            try {
                const response = await axios.get("/api/orders");
                setOrders(response.data);
            } catch (error) {
                console.error("Error fetching orders:", error);
            }
        };

        // Fetch all users
        const fetchUsers = async () => {
            try {
                const token = JSON.parse(localStorage.getItem('token'));
                const response = await axios.get('http://localhost:8080/api/admin/users', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                const usersArray = response.data.content || [];
                setUsers(usersArray);
            } catch (error) {
                console.error("Error fetching users:", error);
            }
        };

        fetchProducts();
        fetchOrders();
        fetchUsers();
    }, []);

    // Logout function
    const logout = () => {

        // Remove user-related data from localStorage
        localStorage.removeItem("user");
        localStorage.removeItem("token");
        localStorage.removeItem("role");
        localStorage.removeItem("lastName");
        localStorage.removeItem("firstName");

        // Navigate to home
        navigate("/");
    };

    return (
        <div className="container mt-5">


            {/* Top */}
            <div className="mb-5 text-center">
                <div className="bg-light p-5 border rounded shadow-sm">
                    <button className="nav-link btn btn-link text-primary float-end" onClick={logout}
                            aria-label="Logout">
                        Logout
                    </button>
                    <h1 className="text-primary">Admin Dashboard</h1>
                </div>
            </div>

            <div>
                {/* Mid */}
                <div className="mb-5">
                    {/* main */}
                    <div className="bg-light p-5 rounded border shadow-sm">
                        {/* image */}
                        <div className="text-center mb-3">
                            <img src="https://cdn-icons-png.flaticon.com/128/2202/2202112.png" alt="Admin Avatar"
                                 className="img-fluid rounded-circle" style={{width: "150px", height: "150px"}}/>
                        </div>
                        {/* text */}
                        <div className="text-center">
                            {/* Name */}
                            <h1>
                                <span className="font-weight-bold">Name: </span>
                                {firstName + " " + lastName}
                            </h1>
                            {/* Email */}
                            <h1>
                                <span className="font-weight-bold">Email: </span>
                                {userEmail}
                            </h1>
                            {/* Role */}
                            <h1>
                                <span className="font-weight-bold">Role: </span>
                                {Role}
                            </h1>
                        </div>
                    </div>
                </div>

                {/* Bottom */}
                <div>
                    <Tabs defaultActiveKey="products" id="admin-dashboard-tabs" className="mb-3">
                        <Tab eventKey="products" title={`Total Products (${products.length})`}>
                            <ProductDetail products={products}/>
                        </Tab>
                        <Tab eventKey="orders" title={`Total Orders (${orders.length})`}>
                            <OrderDetail orders={orders}/>
                        </Tab>
                        <Tab eventKey="users" title={`Total Users (${users.length})`}>
                            <UserDetail users={users}/>
                        </Tab>
                    </Tabs>
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard;
