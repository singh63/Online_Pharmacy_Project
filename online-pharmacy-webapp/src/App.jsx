import React, {lazy, Suspense} from "react";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import MyState from "./context/myState";
import {Toaster} from "react-hot-toast";
import {ProtectedRouteForUser} from "./protectedRoute/ProtectedRouteForUser";
import {ProtectedRouteForAdmin} from "./protectedRoute/ProtectedRouteForAdmin";
import ScrollTop from "./components/scrollTop/ScrollTop";
import axios from "axios";
import {initializeCart} from "./redux/cartSlice";
import {useDispatch} from "react-redux";

// Lazy-loaded components
const HomePage = lazy(() => import("./pages/home/HomePage"));
const NoPage = lazy(() => import("./pages/noPage/NoPage"));
const ProductInfo = lazy(() => import("./pages/productInfo/ProductInfo"));
const CartPage = lazy(() => import("./pages/cart/CartPage"));
const AllProduct = lazy(() => import("./pages/allProduct/AllProduct"));
const Signup = lazy(() => import("./pages/registration/Signup"));
const Login = lazy(() => import("./pages/registration/Login"));
const UserDashboard = lazy(() => import("./pages/user/UserDashboard"));
const AdminDashboard = lazy(() => import("./pages/admin/AdminDashboard"));
const AddProductPage = lazy(() => import("./pages/admin/AddProductPage"));
const UpdateProductPage = lazy(() => import("./pages/admin/UpdateProductPage"));
const CategoryPage = lazy(() => import("./pages/category/CategoryPage"));

const App = () => {

    const dispatch = useDispatch();

    const initializeCartState = async () => {
        const cartId = JSON.parse(localStorage.getItem("cartId"));
        const email = JSON.parse(localStorage.getItem("user"));
        const token = JSON.parse(localStorage.getItem("token"));
        if (email === null) {
            return;
        }
        const response = await axios.get(`http://localhost:8080/api/users/${email}/carts/${cartId}`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        dispatch(initializeCart(response.data.products));
    };

    const email = JSON.parse(localStorage.getItem("user"));
    if (email !== null) {
        initializeCartState();
    }

    return (
        <MyState>
            <Router>
                <ScrollTop/>
                <Suspense fallback={<div>Loading...</div>}>
                    <Routes>
                        <Route
                            path="/"
                            element={
                                <ProtectedRouteForUser>
                                    <HomePage/>
                                </ProtectedRouteForUser>
                            }
                        />
                        <Route path="/*" element={<NoPage/>}/>
                        <Route path="/signup" element={<Signup/>}/>
                        <Route path="/login" element={<Login/>}/>
                        <Route
                            path="/productinfo/:id"
                            element={
                                <ProtectedRouteForUser>
                                    <ProductInfo/>
                                </ProtectedRouteForUser>
                            }
                        />
                        <Route
                            path="/cart"
                            element={
                                <ProtectedRouteForUser>
                                    <CartPage/>
                                </ProtectedRouteForUser>
                            }
                        />
                        <Route
                            path="/allproduct"
                            element={
                                <ProtectedRouteForUser>
                                    <AllProduct/>
                                </ProtectedRouteForUser>
                            }
                        />
                        <Route
                            path="/category/:categoryname"
                            element={
                                <ProtectedRouteForUser>
                                    <CategoryPage/>
                                </ProtectedRouteForUser>
                            }
                        />
                        <Route
                            path="/user-dashboard"
                            element={
                                <ProtectedRouteForUser>
                                    <UserDashboard/>
                                </ProtectedRouteForUser>
                            }
                        />
                        <Route
                            path="/admin-dashboard"
                            element={
                                <ProtectedRouteForAdmin>
                                    <AdminDashboard/>
                                </ProtectedRouteForAdmin>
                            }
                        />
                        <Route
                            path="/addproduct"
                            element={
                                <ProtectedRouteForAdmin>
                                    <AddProductPage/>
                                </ProtectedRouteForAdmin>
                            }
                        />
                        <Route
                            path="/updateproduct/:id"
                            element={
                                <ProtectedRouteForAdmin>
                                    <UpdateProductPage/>
                                </ProtectedRouteForAdmin>
                            }
                        />
                    </Routes>
                </Suspense>
                <Toaster/>
            </Router>
        </MyState>
    );
};

export default App;
