import React, { useState, useContext, useEffect } from 'react';
import { AuthContext } from "../../providers/Auth";
import { useHistory } from "react-router-dom";
import { getAll } from "../../service/Classservice"
import { create, saveDraft } from "../../service/Messageservice"

const CreateMessage = () => {

  let user = useContext(AuthContext);
  let history = useHistory();

  const [userId, setUserId] = useState(0);
  const [classTitle, setClassTitle] = useState('');
  const [content, setContent] = useState('');
  const [error, setError] = useState('');
  const [classes, setClasses] = useState([]);



  useEffect(() => {
    getClasses();
  }, [userId]);

  const getClasses = () => {
    getAll(user.uid).then(res => {
      if(Object.keys(res.data).length > 0) {
        const classList = [];
        const data = res.data;
        console.log(data);
        data.map((myclass, i) => {
          classList.push(<option value={myclass.id}>{myclass.data.course_number}</option>);
        })
        setClasses(classList);
      }
    });  
  }
  

  const handleSubmit = (e) => {
    e.preventDefault();
    const { value } = e.currentTarget;

    if(!classTitle) {
      setError("Please fill in the required fields");
    } else if(content.length >= 250 ) {
      setError("The message should not exceed 250 characters");
    } else {

      const message = {
        class_id : classTitle, 
        content : content, 
        teacher_id: user.uid
      };

      if(value.localeCompare("Send") === 0) {
        create(message)
        .then(res => {
          history.push("/");
        })
        .catch(error => {
          setError("Something went wrong! The message was not sent.");
        });
      } 

      // saveDraft(message)
      // .then(res => {
      //   history.push("/");
      // })
      // .catch(error => {
      //   setError("Something went wrong! The message was not sent.");
      // });
      
    }
  }
  
  const handleInputChange = (event) => {
    const {name, value} = event.currentTarget;

    if(name === 'classTitle'){
      setClassTitle(value);
    }
    else if(name === 'content'){
      setContent(value);
    }

  }

  const cancel = () => {
    setContent('');
  }

    return(
      <section className="content">
        <form>
          <div className="row">
            <div className="col-md-6">
              <div className="card card-primary">
                <div className="card-header">
                  <h3 className="card-title">Create a new Message</h3>
                  <div className="card-tools">
                    <button type="button" className="btn btn-tool" data-card-widget="collapse" data-toggle="tooltip" title="Collapse">
                      <i className="fas fa-minus"></i>
                    </button>
                  </div>
                </div>
                <div className="card-body">
                  <p className="error-class">{error}</p>

                  <div class="form-group">
                    <label htmlFor="inputStatus">Class</label><span className="error-class">*</span>
                    <select class="form-control custom-select" name="classTitle" onChange={e => handleInputChange(e)}>
                      <option selected disabled>Choose a class</option>
                      { classes }
                    </select>
                  </div>

                  <div className="form-group">
                    <label>Message Content</label>
                    <textarea placeholder="Type the message content here" name="content" value={content} className="form-control" rows="4" onChange={e => handleInputChange(e)}></textarea>
                  </div>
                  
                </div>
              </div>
            </div>
          </div>
          <div className="row">
            <div className="col-1">
              <button type="submit" onClick={e => handleSubmit(e)} value="Send" className="btn btn-primary" >Send</button>
            </div>
            <div className="col-11">
              <button type="button" onClick={cancel} value="Send" className="btn btn-primary" >Cancel</button>
            </div>
          </div>
     </form>
   </section>
  )
}

export default CreateMessage
