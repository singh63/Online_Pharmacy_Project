import React from 'react';
import '../admin/UserDetail.css';

const UserDetail = ({users}) => {
    return (
        <div className="container mt-5">
            {/* Heading Section */}
            <div className="d-flex justify-content-center align-items-center">
                <h1 className="display-4 text-primary font-weight-bold">All Users</h1>
            </div>

            {/* Table */}
            <div className="table-responsive mt-4">
                <table className="table table-striped table-bordered text-center">
                    <thead className="thead-dark">
                    <tr>
                        <th>S.No.</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Uid</th>
                        <th>Role</th>
                        <th>Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.map((value, index) => (
                        <tr key={index}>
                            <td>{index + 1}</td>
                            <td>{value.name}</td>
                            <td>{value.email}</td>
                            <td>{value.uid}</td>
                            <td>{value.role}</td>
                            <td>{value.date}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default UserDetail;
