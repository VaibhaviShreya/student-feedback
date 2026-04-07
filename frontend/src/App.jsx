import { Link, Navigate, Route, Routes } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import DashboardPage from './pages/DashboardPage'
import { useAuth } from './context/AuthContext'

function App() {
  const { token, logout, username } = useAuth()

  return (
    <>
      <nav className="nav">
        <h1>Student Feedback & Course Management</h1>
        <div>
          {token ? (
            <>
              <span>Hi, {username}</span>
              <button onClick={logout}>Logout</button>
            </>
          ) : (
            <Link to="/login">Login</Link>
          )}
        </div>
      </nav>

      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/" element={token ? <DashboardPage /> : <Navigate to="/login" />} />
      </Routes>
    </>
  )
}

export default App
