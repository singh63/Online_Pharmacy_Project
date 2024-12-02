import {useContext} from "react";
import Layout from "../../components/layout/Layout";
import myContext from "../../context/myContext";
import Loader from "../../components/loader/Loader";

const UserDashboard = () => {
    // user
    const userEmail = JSON.parse(localStorage.getItem("user"));
    const Role = JSON.parse(localStorage.getItem("role"));
    const firstName = JSON.parse(localStorage.getItem("firstName"));
    const lastName = JSON.parse(localStorage.getItem("lastName"));

    const context = useContext(myContext);
    const {loading, getAllOrder} = context;

    return (
        <Layout>
            <div className="container my-5">
                {/* Top  */}
                <div className="mb-4 p-4 border border-secondary rounded bg-light">
                    {/* image  */}
                    <div className="text-center mb-3">
                        <img src="https://cdn-icons-png.flaticon.com/128/2202/2202112.png" alt=""
                             className="img-fluid"/>
                    </div>
                    {/* text  */}
                    <div>
                        {/* Name  */}
                        <h1 className="text-center h4">
                            <span className="fw-bold">Name: </span>
                            {firstName + " " + lastName}
                        </h1>

                        {/* Email  */}
                        <h1 className="text-center h4">
                            <span className="fw-bold">Email: </span>
                            {userEmail}
                        </h1>

                        {/* Role  */}
                        <h1 className="text-center h4">
                            <span className="fw-bold">Role: </span>
                            {Role}
                        </h1>
                    </div>
                </div>

                {/* Bottom  */}
                <div>
                    {/* Order Details Section */}
                    <div className="mb-4">
                        <h2 className="h3 mb-4">Order Details</h2>

                        <div className="text-center mb-3">{loading && <Loader/>}</div>

                        {/* Orders List */}
                        {getAllOrder
                            .filter((obj) => obj.userid === '' /* match userId here */)
                            .map((order, orderIndex) => {
                                return (
                                    <div key={orderIndex} className="mb-4 border border-secondary rounded bg-light p-3">
                                        {order.cartItems.map((item, itemIndex) => {
                                            const {id, date, quantity, price, title, productImageUrl, category} = item;
                                            const {status} = order;
                                            return (
                                                <div key={itemIndex}
                                                     className="d-flex flex-column flex-md-row mb-4 border border-secondary rounded bg-light">
                                                    {/* Left Column */}
                                                    <div className="p-3 border-end">
                                                        <div className="row">
                                                            <div className="col-6 mb-2">
                                                                <div className="fw-semibold">Order Id</div>
                                                                <div className="text-muted">#{id}</div>
                                                            </div>

                                                            <div className="col-6 mb-2">
                                                                <div className="fw-semibold">Date</div>
                                                                <div className="text-muted">{date}</div>
                                                            </div>

                                                            <div className="col-6 mb-2">
                                                                <div className="fw-semibold">Total Amount</div>
                                                                <div className="text-muted">₹ {price * quantity}</div>
                                                            </div>

                                                            <div className="col-6 mb-2">
                                                                <div className="fw-semibold">Order Status</div>
                                                                <div
                                                                    className={`text-muted ${status === "pending" ? "text-danger" : "text-success"}`}>{status}</div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    {/* Right Column */}
                                                    <div className="flex-grow-1">
                                                        <ul className="list-unstyled">
                                                            <li className="d-flex justify-content-between align-items-start py-3">
                                                                <div className="d-flex">
                                                                    <img className="img-fluid me-3 rounded border"
                                                                         src={productImageUrl} alt="Product" style={{
                                                                        maxHeight: "150px",
                                                                        objectFit: "contain"
                                                                    }}/>
                                                                    <div>
                                                                        <p className="fw-bold mb-1">{title}</p>
                                                                        <p className="text-muted mb-1">{category}</p>
                                                                        <p className="text-muted mb-1">x {quantity}</p>
                                                                    </div>
                                                                </div>
                                                                <div className="text-end">
                                                                    <p className="fw-bold mb-1">₹ {price}</p>
                                                                </div>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </div>
                                            );
                                        })}
                                    </div>
                                );
                            })}
                    </div>
                </div>
            </div>
        </Layout>
    );
};

export default UserDashboard;
