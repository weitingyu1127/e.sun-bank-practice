// src/stores/authStore.js
import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    userId: localStorage.getItem('userId') || null  
  }),
  actions: {
    setUserId(id) {
      this.userId = id
      localStorage.setItem('userId', id)  
    },
    logout() {
      this.userId = null
      localStorage.removeItem('userId')
    }
  }
})