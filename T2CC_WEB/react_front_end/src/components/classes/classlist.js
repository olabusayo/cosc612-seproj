import React, { useState, useContext, useEffect } from 'react';
import { getAll, isActive } from "../../service/Classservice"
import { AuthContext } from "../../providers/Auth";

const ClassList = () => {

    let user = useContext(AuthContext);
    const [classLists, setClassLists] = useState([]);
    const [userId, setUserId] = useState(0);

    useEffect(() => {
      getAll(user.uid).then(res => {
        if(Object.keys(res.data).length > 0) {
          let ClassListArray = res.data;
          var list = [];
          ClassListArray.map((item,index) => {
            if(isActive(item.data.term, item.data.year)) {
              list.push(
                <tr key={index}>
                  <td>{item.data.course_number}</td>
                  <td>{item.data.section}</td>
                  <td>{item.data.title}</td>
                  <td style={{width: "25%"}}> {item.data.term}</td>
                  <td>{item.data.year}</td>
                </tr>)
            }
            
          });

          setClassLists(list);
        }
      }); 
    }, [userId]);


    return(
      <section className="content">
        <div className="row">
          <div className="col-12">
            <div className="card">
              <div className="card-header">
              </div>
              <div className="card-body">
                <table id="example1" className="table table-bordered table-striped">
                  <thead>
                  <tr>
                   
                    <th style={{width: "20%"}}>Course</th>
                    <th style={{width: "10%"}}>Section</th>
                    <th>Title</th>
                    <th>Term</th>
                    <th>Year</th>
                    
                  </tr>
                  </thead>
                  <tbody>
                      {classLists}
                  </tbody>
                  <tfoot>
                  </tfoot>
                </table>
                </div>
              </div>
            </div>
          </div>
        </section>
    )
}

export default ClassList
