import React, { useState, useContext, useEffect } from 'react';
import { getAll } from "../../service/Classservice"
import { AuthContext } from "../../providers/Auth";

const ClassList = () => {

    let user = useContext(AuthContext);
    const [classLists, setClassLists] = useState([]);
    const [userId, setUserId] = useState(0);

    useEffect(() => {
      getAll(user.email).then(res => {
        let ClassListArray = res.data;
        console.log(ClassListArray);
        var list = [];
        ClassListArray.map((item,index) =>
           list.push(<tr key={index}>
            
               <td>{item.course_number}</td>
               <td>{item.section}</td>
               <td>{item.title}</td>
               <td style={{width: "25%"}}> {item.term}</td>
               <td>{item.year}</td>
               <td>
                   <span style={{padding: "6px"}} className="badge badge-success">Active</span>
               </td>
             </tr>)
         );

         setClassLists(list);
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
                    <th style={{width: "10%"}}>Status</th>
                    
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
