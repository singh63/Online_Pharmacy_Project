import {Navigate} from "react-router";

export const ProtectedRouteForAdmin = ({children}) => {
    const role = JSON.parse(localStorage.getItem('role'));

    if (role === "ADMIN") {
        return children;
    } else {
        return <Navigate to="/404"/>;
    }
};
