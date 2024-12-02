/* eslint-disable react/prop-types */
import {Navigate} from "react-router";

export const ProtectedRouteForUser = ({children}) => {
    const role = JSON.parse(localStorage.getItem("role"));

    if (role === null || role === "USER") {
        return children;
    } else {
        return <Navigate to="/admin-dashboard"/>;
    }
};
