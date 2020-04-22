import React, { useContext } from 'react';
import { logout } from "../service/Userservice"
import { AuthContext } from "../providers/Auth";

const Navbar = () => {
    let user = useContext(AuthContext);

    const logOut = () => {
        logout();
    };

    return (
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark rounded">
            <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExample10" aria-controls="navbarsExample10" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon" />
            </button>

            <div className="collapse navbar-collapse justify-content-md-center" id="navbarsExample10">
                <ul className="navbar-nav">
                    <li className="nav-item">
                        <a  className="nav-link" href="#">{ user.email }</a>
                    </li>
                    <li className="nav-item">
                    <a className="nav-link" href="#" onClick={ logOut }>Disconnect</a>
                    </li>
                </ul>
            </div>
        </nav>
    );
}

export default Navbar;