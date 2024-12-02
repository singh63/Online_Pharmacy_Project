import {Link, useNavigate} from "react-router-dom";
import SearchBar from "../searchBar/SearchBar";
import {useDispatch, useSelector} from "react-redux";
import {resetCart} from "../../redux/cartSlice"; // Adjust the path as needed
import "../../App.css";

const Navbar = () => {
    const role = JSON.parse(localStorage.getItem("role")); // Get user role from localStorage
    const navigate = useNavigate(); // Navigate
    const dispatch = useDispatch(); // Initialize dispatch
    const cartItems = useSelector((state) => state.cart); // CartItems

    // Logout function
    const logout = () => {
        // Dispatch the resetCart action to clear the cart in Redux
        dispatch(resetCart());

        // Remove user-related data from localStorage
        localStorage.removeItem("user");
        localStorage.removeItem("token");
        localStorage.removeItem("role");
        localStorage.removeItem("lastName");
        localStorage.removeItem("firstName");

        // Navigate to home
        navigate("/");
    };

    // NavList Data
    const navList = (
        <ul className="navbar-nav mx-auto mb-2 mb-lg-0 d-flex">
            <li className="nav-item">
                <Link className="nav-link text-primary" to="/" aria-label="Home">
                    Home
                </Link>
            </li>
            <li className="nav-item">
                <Link className="nav-link text-primary" to="/allproduct" aria-label="All Products">
                    All Product
                </Link>
            </li>
            {!role && (
                <>
                    <li className="nav-item">
                        <Link className="nav-link text-primary" to="/signup" aria-label="Signup">
                            Signup
                        </Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link text-primary" to="/login" aria-label="Login">
                            Login
                        </Link>
                    </li>
                </>
            )}
            {role === "USER" && (
                <li className="nav-item">
                    <Link className="nav-link text-primary" to="/user-dashboard" aria-label="User Dashboard">
                        User
                    </Link>
                </li>
            )}
            {role === "ADMIN" && (
                <li className="nav-item">
                    <Link className="nav-link text-primary" to="/admin-dashboard" aria-label="Admin Dashboard">
                        Admin
                    </Link>
                </li>
            )}
            {role && (
                <>
                    <li className="nav-item">
                        <button className="nav-link btn btn-link text-primary" onClick={logout} aria-label="Logout">
                            Logout
                        </button>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link text-primary" to="/cart" aria-label="Cart">
                            Cart({cartItems.length})
                        </Link>
                    </li>
                </>
            )}
        </ul>
    );

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-warning sticky-top">
            <div className="container-fluid" style={{paddingLeft: '10%', paddingRight: '10%'}}>
                <Link className="navbar-brand d-flex align-items-center" to="/">
                    <h2 className="text-white mb-0">Medico</h2>
                </Link>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                        aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="d-flex justify-content-center flex-grow-1">
                    <SearchBar/>
                </div>
                <div className="collapse navbar-collapse" id="navbarNav">
                    {navList}
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
