import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './CSS/index.css'
import App from './Components/App.jsx'
import LoginPage from './Components/LoginPage.jsx'
import Start from './Components/Start.jsx'

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <Start/>
    </StrictMode>,
)