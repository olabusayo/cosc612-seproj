import React from 'react';
import Navbar from './Navbar';

const Home = () => {

        return (
            <div className="container">
                <Navbar />
                <div className="jumbotron mt-5">
                <div className="col-sm-8 mx-auto">
                    <h1 className="text-center">WELCOME to T2CC :</h1>
                </div>
                </div>
            </div>
        );

}

export default Home;