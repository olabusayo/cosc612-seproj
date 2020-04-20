import React from 'react'
import { AuthProvider } from "./providers/Auth";

import Application from "./functionalComp/Appication"

const App = () => {

    return (
        <AuthProvider> 
          <Application /> 
        </AuthProvider>
    )
}

export default App