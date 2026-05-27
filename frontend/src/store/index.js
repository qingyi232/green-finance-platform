import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user') || 'null'),
    role: localStorage.getItem('role') || '',
  }),
  getters: {
    isLogin: (state) => !!state.token,
    isAdmin: (state) => state.role === '管理员',
  },
  actions: {
    setLogin(token, user) {
      this.token = token
      this.user = user
      this.role = user?.role || ''
      localStorage.setItem('token', token)
      localStorage.setItem('user', JSON.stringify(user))
      localStorage.setItem('role', user?.role || '')
    },
    updateUser(user) {
      this.user = user
      localStorage.setItem('user', JSON.stringify(user))
    },
    logout() {
      this.token = ''
      this.user = null
      this.role = ''
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      localStorage.removeItem('role')
    },
  },
})
