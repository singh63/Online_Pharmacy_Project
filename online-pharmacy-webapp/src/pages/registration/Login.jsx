import {useContext, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import myContext from "../../context/myContext";
import toast from "react-hot-toast";
import axios from "axios";
import Loader from "../../components/loader/Loader";
import {useDispatch} from "react-redux";
import {initializeCart} from "../../redux/cartSlice";

const Login = () => {
    const context = useContext(myContext);
    const {loading, setLoading} = context;
    const dispatch = useDispatch();

    const navigate = useNavigate();

    const [userLogin, setUserLogin] = useState({
        email: "",
        password: "",
    });

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

    /**========================================================================
     *                          User Login Function
     *========================================================================**/

    const userLoginFunction = async () => {
        // validation
        if (userLogin.email === "" || userLogin.password === "") {
            toast.error("All Fields are required");
            return;
        }

        setLoading(true);
        try {

            const response = await axios.post("http://localhost:8080/api/login", userLogin);
            const {"jwt-token": token, role, firstName, lastName, user, userId, cartId} = response.data;

            // Store the token and user info in local storage
            localStorage.setItem("token", JSON.stringify(token));
            localStorage.setItem("role", JSON.stringify(role));
            localStorage.setItem("firstName", JSON.stringify(firstName));
            localStorage.setItem("lastName", JSON.stringify(lastName));
            localStorage.setItem("user", JSON.stringify(user));
            localStorage.setItem("userId", JSON.stringify(userId));
            localStorage.setItem("cartId", JSON.stringify(cartId));

            // Clear the login form
            setUserLogin({
                email: "",
                password: "",
            });

            initializeCartState();

            toast.success("Login Successfully");
            setLoading(false);

            // Navigate based on the user's role
            if (role === "ADMIN") {
                navigate("/admin-dashboard");
            } else {
                navigate("/");
            }
        } catch (error) {
            console.error(error);
            setLoading(false);
            toast.error("Login Failed");
        }
    };


    return (
        <div className="d-flex justify-content-center align-items-center vh-100">
            {loading && <Loader/>}
            <div className="bg-light px-4 py-5 border border-secondary rounded shadow-sm"
                 style={{maxWidth: "400px", width: "100%"}}>
                <h2 className="text-center text-primary mb-4">Login</h2>

                <div className="mb-3">
                    <input type="email" name="email" placeholder="Email Address" value={userLogin.email}
                           onChange={(e) => setUserLogin({...userLogin, email: e.target.value})}
                           className="form-control"/>
                </div>

                <div className="mb-4">
                    <input type="password" placeholder="Password" value={userLogin.password}
                           onChange={(e) => setUserLogin({...userLogin, password: e.target.value})}
                           className="form-control"/>
                </div>

                <div className="mb-4">
                    <button type="button" onClick={userLoginFunction} className="btn btn-primary w-100">
                        Login
                    </button>
                </div>

                <div className="text-center">
                    <p>
                        Don't have an account?{" "}
                        <Link className="text-primary" to="/signup">
                            Sign up
                        </Link>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default Login;
