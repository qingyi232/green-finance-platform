import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 30000,
})

request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) config.headers.Token = token
    return config
  },
  (error) => Promise.reject(error)
)

request.interceptors.response.use(
  (response) => {
    const data = response.data
    if (data.code === 0) {
      return data
    } else if (data.code === 401) {
      ElMessage.error(data.msg || '请先登录')
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      localStorage.removeItem('role')
      setTimeout(() => { window.location.href = '/login' }, 800)
      return Promise.reject(data)
    } else {
      ElMessage.error(data.msg || '请求失败')
      return Promise.reject(data)
    }
  },
  (error) => {
    if (error.response?.status === 401) {
      ElMessage.error('登录已过期')
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      localStorage.removeItem('role')
      setTimeout(() => { window.location.href = '/login' }, 800)
    } else {
      ElMessage.error(error.response?.data?.msg || error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default request
