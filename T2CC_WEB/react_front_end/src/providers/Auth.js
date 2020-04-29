import React, { useEffect, useState } from "react";
import { auth } from "../properties/firebase";


export const AuthContext = React.createContext();

export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [userId, setUserId] = useState(0);

  useEffect(() => {
    auth.onAuthStateChanged(setCurrentUser);
  }, [userId])

  return (
    <AuthContext.Provider value={currentUser}>
      {children}
    </AuthContext.Provider>
  );
};

