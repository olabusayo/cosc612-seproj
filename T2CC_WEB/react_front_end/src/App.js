import React from 'react'
import { AuthProvider } from "./providers/Auth";

import Application from "./components/Appication"

const App = () => {

    return (
        <AuthProvider> 
          <Application /> 
        </AuthProvider>
    )
}

export default App