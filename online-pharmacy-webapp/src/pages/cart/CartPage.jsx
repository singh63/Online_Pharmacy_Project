import {useDispatch, useSelector} from "react-redux";
import Layout from "../../components/layout/Layout";
import {Trash} from "lucide-react";
import {decrementQuantity, deleteFromCart, incrementQuantity, resetCart} from "../../redux/cartSlice";
import toast from "react-hot-toast";
import {useEffect, useState} from "react";
import axios from "axios";
import BuyNowModal from "../../components/buyNowModal/BuyNowModal";
import {Navigate} from "react-router";
import "bootstrap/dist/css/bootstrap.min.css";
import {useNavigate} from "react-router-dom";

const CartPage = () => {
    const cartItems = useSelector((state) => state.cart);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const deleteCart = (item) => {
        dispatch(deleteFromCart(item));
        toast.success("Removed from cart");
    };

// Handle incrementing quantity
    const handleIncrement = async (productId) => {
        const response = await axios.get(`http://localhost:8080/api/public/products/${productId}`);
        const quantity = response.data.quantity;
        const item = cartItems.find((item) => item.productId === productId);
        if (item) {
            if (item.quantity < quantity) {
                dispatch(incrementQuantity({productId: productId, quantity: quantity}));
            } else {
                toast.error("Cannot exceed available quantity");
            }
        }
    };

// Handle decrementing quantity
    const handleDecrement = (productId) => {
        const item = cartItems.find((item) => item.productId === productId);
        if (item) {
            if (item.quantity > 1) {
                dispatch(decrementQuantity(productId));
            } else {
                toast.error("Quantity cannot be less than 1");
            }
        }
    };

    const cartItemTotal = cartItems.map((item) => item.quantity).reduce((prevValue, currValue) => prevValue + currValue, 0);

    const cartTotal = cartItems.map((item) => item.price * item.quantity).reduce((prevValue, currValue) => prevValue + currValue, 0);

    // user
    const user = JSON.parse(localStorage.getItem("user"));
    const cartId = JSON.parse(localStorage.getItem("cartId"));
    const token = JSON.parse(localStorage.getItem("token"));

    const buyNowFunction = async () => {
        try {
            await axios.post(`http://localhost:8080/api/users/${user}/carts/${cartId}/payments/UPI_QR/order`, null, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            dispatch(resetCart());
            toast.success("Order placed successfully");
            navigate("/user-dashboard");
        } catch (error) {
            console.log(error);
            toast.error("Failed to place order");
        }
    };

    return (
        <Layout>
            <div className="container mt-5">
                <h1 className="text-center mb-4">Shopping Cart</h1>
                <div className="row">
                    <div className="col-md-8">
                        <div className="card">
                            <div className="card-body">
                                <h3 className="card-title">{cartItems.length > 0 ? "Items in your shopping cart" : "No items in the cart"}</h3>
                                {cartItems.length > 0 && (
                                    cartItems.map((item) => {
                                        const {productId, productName, price, image, quantity, category} = item;
                                        return (
                                            <div key={productId} className="mb-3 d-flex align-items-start">
                                                <img src={image} alt={productName} className="img-fluid me-3"
                                                     style={{width: "100px", height: "100px", objectFit: "cover"}}/>
                                                <div className="flex-grow-1">
                                                    <h5 className="mb-1">{productName}</h5>
                                                    <p className="text-muted mb-1">{category}</p>
                                                    <p className="mb-1">₹{price}</p>
                                                    <div className="d-flex align-items-center">
                                                        <button onClick={() => handleDecrement(productId)}
                                                                className="btn btn-outline-secondary me-2">
                                                            -
                                                        </button>
                                                        <input type="text" className="form-control text-center"
                                                               value={quantity} readOnly/>
                                                        <button onClick={() => handleIncrement(productId)}
                                                                className="btn btn-outline-secondary ms-2">
                                                            +
                                                        </button>
                                                        <button onClick={() => deleteCart(item)}
                                                                className="btn btn-danger ms-3">
                                                            <Trash size={16} className="me-1"/>
                                                            Remove
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        );
                                    })
                                )}
                            </div>
                        </div>
                    </div>
                    { cartItems.length > 0 &&
                        <div className="col-md-4">
                            <div className="card">
                                <div className="card-body">
                                    <h2 className="card-title">Price Details</h2>
                                    <dl className="row mb-3">
                                        <dt className="col-sm-6">Price ({cartItemTotal} items)</dt>
                                        <dd className="col-sm-6">₹{cartTotal}</dd>
                                    </dl>
                                    <dl className="row mb-3">
                                        <dt className="col-sm-6">Delivery Charges</dt>
                                        <dd className="col-sm-6 text-success">Free</dd>
                                    </dl>
                                    <dl className="row mb-4 border-top pt-2">
                                        <dt className="col-sm-6">Total Amount</dt>
                                        <dd className="col-sm-6">₹{cartTotal}</dd>
                                    </dl>
                                    <div className="d-flex justify-content-between">
                                        <BuyNowModal buyNowFunction={buyNowFunction} />
                                    </div>
                                </div>
                            </div>
                        </div>
                    }
                </div>
            </div>
        </Layout>
    );
};

export default CartPage;
