import React, { useState, useContext } from 'react';
import { add, getYear } from "../../service/Classservice"
import { AuthContext } from "../../providers/Auth";
import { useHistory } from "react-router-dom";

const AddClass = () => {

  let user = useContext(AuthContext);
  let history = useHistory();

  const [classtitle, setClasstitle] = useState('');
  const [courseno, setCourseno] = useState('');
  const [section, setSection] = useState('');
  const [term, setTerm] = useState('');
  const [year, setYear] = useState('');
  const [error, setError] = useState('');

  const createClass = () => {
    const myclass = {
      title : classtitle, 
      course_number : courseno, 
      section : section, 
      term : term, 
      year : year,
      teacher_id : user.uid
    };

    add(myclass)
    .then((res) => {
      history.push("/");
    })
    .catch(error => {
      setError("Something went wrong! The class was not created.");
    });
  };

  const years = getYear();

  const handleSubmit = (e) => {
    e.preventDefault();

    if(!classtitle || !courseno || !section || !term || !year) {
      setError("Please fill in the required fields");
    } else if(section < 1 || section > 10) {
      setError("Please enter a valid section (01 to 10)");
    } 
    else {
      createClass();
    }
  }

  const handleInputChange = (event) => {
    const {name, value} = event.currentTarget;

    if(name === 'classtitle') {
      setClasstitle(value);
    }
    else if(name === 'courseno'){
      setCourseno(value);
    }
    else if(name === 'section'){
      setSection(value);
    }
    else if(name === 'term'){
      setTerm(value);
    }
    else if(name === 'year'){
      setYear(value);
    }
  }

    return(
      <section className="content">
        <form onSubmit={e => handleSubmit(e)}>
          <div className="row">
            <div className="col-md-6">
              <div className="card card-primary">
                <div className="card-header">
                  <h3 className="card-title">Create a new class</h3>
                  <div className="card-tools">
                    <button type="button" className="btn btn-tool" data-card-widget="collapse" data-toggle="tooltip" title="Collapse">
                      <i className="fas fa-minus"></i>
                    </button>
                  </div>
                </div>
                <div className="card-body">
                  <p className="error-class">{error}</p>
                  <div className="form-group">
                    <label>Class title</label><span className="error-class">*</span>
                    <input placeholder="Class title" title type="text" name="classtitle" className="form-control" onChange={e => handleInputChange(e)}/>
                  </div>
                  <div className="form-group">
                    <label>Course number</label><span className="error-class">*</span>
                    <input type="text" placeholder="Course number" name="courseno" className="form-control" rows="4" onChange={e => handleInputChange(e)}/>
                  </div>
                  <div className="form-group">
                    <label>Section</label><span className="error-class">*</span>
                    <input placeholder="Section" type="number" name="section" className="form-control" onChange={e => handleInputChange(e)}/>
                  </div>
                  <div class="form-group">
                    <label htmlFor="inputStatus">Term</label><span className="error-class">*</span>
                    <select class="form-control custom-select" name="term" onChange={e => handleInputChange(e)}>
                      <option selected disabled>Select one</option>
                      <option value="Fall">Fall</option>
                      <option value="Spring">Spring</option>
                      <option value="Summer">Summer</option>
                    </select>
                  </div>
                  <div class="form-group">
                    <label htmlFor="inputStatus">Year</label><span className="error-class">*</span>
                    <select class="form-control custom-select" name="year" onChange={e => handleInputChange(e)}>
                      <option selected disabled>Select one</option>
                      <option value="2020">{years[0]}</option>
                      <option value="2020">{years[1]}</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="row">
            <div className="col-12">
              <input type="submit" value="create" className="btn btn-primary float-left"/>
            </div>
          </div>
     </form>
   </section>
  )
}

export default AddClass
