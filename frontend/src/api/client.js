import axios from 'axios'

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

const client = axios.create({
  baseURL: API_URL
})

export const setToken = (token) => {
  if (token) {
    client.defaults.headers.common.Authorization = `Bearer ${token}`
  } else {
    delete client.defaults.headers.common.Authorization
  }
}

export default client
