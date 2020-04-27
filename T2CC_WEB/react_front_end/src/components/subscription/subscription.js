import React, { useState, useContext, useEffect } from 'react';
import { getSubs, approve, deny } from "../../service/Subscriptionservice"
import { AuthContext } from "../../providers/Auth";
import { useHistory } from "react-router-dom";

const Subscription = () => {

  let user = useContext(AuthContext);
  let history = useHistory();
  const [userId, setUserId] = useState(0);
  const [subs, setSubs] = useState([]);

  function getData() {
    getSubs(user.uid).then(res => {
      let subs = res.data;
      if(Object.keys(subs).length > 0) {
        var list = [];
        subs.map((item,index) => {
          let students = item.students;
          students.map((student, i) => {       
              list.push(
                {
                  class : item.class,
                  student : student
                }
              );
          });
        });
        console.log(list);
        setSubs(list);
      }
    });
  }

  useEffect(() => {
    getData();
  }, [userId]);

  const approveStudent = (class_id, student_email, id) => {

    const data = {
      class_id : class_id,
      student_email : student_email
    }

    approve(data)
    .then(() => {
      let list = subs.filter((item, j) => id !== j);
      setSubs(list);
    }).catch(error => {
      console.log(error.message);
    });
  }

  const denyStudent = (class_id, student_email, id) => {
    const data = {
      class_id : class_id,
      student_email : student_email
    }

    deny(data)
    .then(() => {
      let list = subs.filter((item, j) => id !== j);
      setSubs(list);
    }).catch(error => {
      console.log(error.message);
    });
  }

  return (
      
    <section className="content">
      <div className="row">
        <div className="col-md-11">
          <div className="box box-primary">
            <div className="box-header with-border">
              <h3 className="box-title">Subscription requests</h3>
            </div>
            <div className="box-body no-padding">
              <div className="table-responsive mailbox-messages">
                <table className="table table-stripped">
                  <tbody>    
                   {
                    subs.map((item, index) => 
                      <tr key={index}>
                        <td className="mailbox-subject"><b>{item.student.fname + " " + item.student.lname}</b> requested to join <em>{item.class.data.course_number + "-" + item.class.data.section}</em> </td>
                        <td className="mailbox-date">
                          <a href="#" onClick={() => approveStudent(item.class.id, item.student.email, index)}><i className="fa fa-check"></i> <span>Approve</span>  </a>                      
                        </td>
                        <td className="mailbox-date">    
                          <a href="#" onClick={() => denyStudent(item.class.id, item.student.email, index)}><i className="fa fa-times"></i> <span>Deny</span> </a>
                        </td>
                      </tr>
                    )
                   }
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

  );


}

export default Subscription;