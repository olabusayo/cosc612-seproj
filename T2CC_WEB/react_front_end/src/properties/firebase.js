import firebase from "firebase/app";
import "firebase/auth";

var firebaseConfig = {
    apiKey: "AIzaSyBNZ_caxR10UUAy93GUCfmnoF9kBvDPMKY",
    authDomain: "cosc612-student-andriod.firebaseapp.com",
    databaseURL: "https://cosc612-student-andriod.firebaseio.com",
    projectId: "cosc612-student-andriod",
    storageBucket: "cosc612-student-andriod.appspot.com",
    messagingSenderId: "848345663956",
    appId: "1:848345663956:web:7da2d038b7e76b05f82e41",
    measurementId: "G-Y2BJBKG6EX"
};

firebase.initializeApp(firebaseConfig);

export const auth = firebase.auth();
