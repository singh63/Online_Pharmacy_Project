import {useContext} from "react";
import myContext from "../../context/myContext";
import "../admin/OrderDetail.css";

const OrderDetail = () => {
    const context = useContext(myContext);
    const {getAllOrder, orderDelete} = context;

    return (
        <div className="container mt-5">
            {/* Heading Section */}
            <div className="text-center mb-4">
                <h1 className="display-4 text-primary font-weight-bold">All Orders</h1>
            </div>

            {/* Table */}
            <div className="table-responsive">
                <table className="table table-bordered table-hover text-center">
                    <thead className="thead-dark">
                    <tr>
                        <th>S.No.</th>
                        <th>Order Id</th>
                        <th>Image</th>
                        <th>Title</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Quantity</th>
                        <th>Total Price</th>
                        <th>Status</th>
                        <th>Name</th>
                        <th>Address</th>
                        <th>Pincode</th>
                        <th>Phone Number</th>
                        <th>Email</th>
                        <th>Date</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    {getAllOrder.map((order) => (
                        <>
                            {order.cartItems.map((item, index) => {
                                const {id, productImageUrl, title, category, price, quantity} = item;
                                return (
                                    <tr key={index}>
                                        <td>{index + 1}</td>
                                        <td>{id}</td>
                                        <td>
                                            <img src={productImageUrl} alt="Product" className="img-fluid"
                                                 style={{maxWidth: "100px", height: "auto"}}/>
                                        </td>
                                        <td>{title}</td>
                                        <td>{category}</td>
                                        <td>₹{price.toFixed(2)}</td>
                                        <td>{quantity}</td>
                                        <td>₹{(price * quantity).toFixed(2)}</td>
                                        <td className={order.status === "Delivered" ? "text-success" : "text-warning"}>{order.status}</td>
                                        <td>{order.addressInfo.name}</td>
                                        <td>{order.addressInfo.address}</td>
                                        <td>{order.addressInfo.pincode}</td>
                                        <td>{order.addressInfo.mobileNumber}</td>
                                        <td>{order.email}</td>
                                        <td>{new Date(order.date).toLocaleDateString()}</td>
                                        <td onClick={() => orderDelete(order.id)}
                                            className="text-danger cursor-pointer">
                                            Delete
                                        </td>
                                    </tr>
                                );
                            })}
                        </>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default OrderDetail;
