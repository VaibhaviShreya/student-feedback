import { createContext, useContext, useEffect, useMemo, useState } from 'react'
import client, { setToken } from '../api/client'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setAuthToken] = useState(localStorage.getItem('token'))
  const [username, setUsername] = useState(localStorage.getItem('username'))

  useEffect(() => {
    setToken(token)
  }, [token])

  const login = async (usernameInput, password, mode = 'login') => {
    // Avoid sending stale/invalid JWT while trying to login/register.
    setToken(null)
    const { data } = await client.post(`/auth/${mode}`, { username: usernameInput, password })
    localStorage.setItem('token', data.token)
    localStorage.setItem('username', data.username)
    setAuthToken(data.token)
    setUsername(data.username)
    setToken(data.token)
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    setAuthToken(null)
    setUsername(null)
    setToken(null)
  }

  const value = useMemo(() => ({ token, username, login, logout }), [token, username])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  return useContext(AuthContext)
}
