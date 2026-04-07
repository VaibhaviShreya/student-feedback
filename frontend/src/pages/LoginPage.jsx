import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

function LoginPage() {
  const { login } = useAuth()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [mode, setMode] = useState('login')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const onSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await login(username, password, mode)
      navigate('/')
    } catch (err) {
      setError(err?.response?.data?.error || 'Authentication failed. Try another username or password.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="container">
      <form className="card" onSubmit={onSubmit}>
        <h2>{mode === 'login' ? 'Login' : 'Register'}</h2>
        <input value={username} onChange={(e) => setUsername(e.target.value)} placeholder="Username" required />
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password" required />
        {error && <p className="error">{error}</p>}
        <button type="submit" disabled={loading}>{loading ? 'Please wait...' : mode === 'login' ? 'Login' : 'Create Account'}</button>
        <button type="button" className="secondary" onClick={() => setMode(mode === 'login' ? 'register' : 'login')}>
          Switch to {mode === 'login' ? 'Register' : 'Login'}
        </button>
        <p>Admin: <strong>admin / admin123</strong> | Teacher: <strong>teacher / teacher123</strong></p>
      </form>
    </div>
  )
}

export default LoginPage
