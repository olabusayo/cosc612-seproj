import React, { useState, useContext, useEffect } from 'react';
import SidebarName from '../../helpers/sidebar.json';
import { Link, BrowserRouter as Router, Switch } from "react-router-dom";
import T2CC_logo_updated from "./T2CC_logo_updated.png"
import { logout, getTeacher } from "../../service/Userservice"
import { AuthContext } from "../../providers/Auth";

const Sidebar = () => {

    let user = useContext(AuthContext);
    const [teacher, setTeacher] = useState('');
    const [userId, setUserId] = useState(0);

    useEffect(() => {
      getTeacher(user.email).then(res => {
        setTeacher(res.data.fname);
      });
    }, [userId]);
    

    const logOut = () => {
      logout();
    };

    const sidebarArray = [];
    SidebarName.SIDEBAR.map((Sidebar,i) => {
      if(Sidebar.Active ==  true)
        sidebarArray.push(
          <li className="nav-item has-treeview" key={i}>
            <Link to={Sidebar.Url} className={i == 0 ? "nav-link active":"nav-link"}>
              <i className={Sidebar.Icon} ></i>
              <p>
                {Sidebar.Main_Moudle_Name}
              </p>
            </Link>
          </li>
       )
    })

    return(
      <aside className="main-sidebar sidebar-dark-primary elevation-4">
        <Link to="/dashboard" className="brand-link">
          <img src="dist/img/AdminLTELogo.png" alt="AdminLTE Logo" className="brand-image img-circle elevation-3"/>
          <span className="brand-text font-weight-light">T2CC</span>
        </Link>
        <div className="sidebar">
          <div className="user-panel mt-3 pb-3 mb-3 d-flex">
            <div className="image">
              <img src={T2CC_logo_updated} className="img-circle elevation-2" alt="User Image"/>
            </div>
            <div className="info">
              <a href="#" className="d-block" onClick={ logOut }>{teacher}</a>
            </div>
          </div>
          <nav className="mt-2">
            <ul className="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
              {sidebarArray}
            </ul>
          </nav>
        </div>
     </aside>
    )
}

export default Sidebar;
