<!-- src/components/LoginPage.vue -->
<template>
  <div class="login-container">
    <h2>{{ isLogin ? '登入' : '註冊' }}</h2>

    <div v-if="!isLogin">
      <input v-model="registerAccount" placeholder="帳號" />
      <input v-model="registerName" placeholder="姓名" />
      <input v-model="registerEmail" placeholder="Email" />
      <input v-model="registerPaymentAccount" placeholder="付款帳號綁定" />
      <button @click="register">註冊</button>
    </div>

    <div v-else>
      <input v-model="loginAccount" placeholder="帳號" />
      <button @click="login">登入</button>
    </div>

    <div class="toggle" @click="toggleMode">
      {{ isLogin ? '還沒註冊？' : '已有帳號？' }}
      <span style="color: blue; text-decoration: underline; cursor: pointer">
        {{ isLogin ? '點我註冊' : '點我登入' }}
      </span>
    </div>

    <div class="message" :class="{ error: isError }">{{ message }}</div>
  </div>
</template>

<script>
import { useAuthStore } from '@/stores/authStore' 
import { callApi } from '@/utils/api'

export default {
  name: 'LoginPage',
  data() {
    return {
      isLogin: true,
      registerAccount: '',
      registerName:'',
      registerEmail: '',
      registerPaymentAccount: '',
      loginAccount: '',
      users: [],
      message: '',
      isError: false
    };
  },
  methods: {
    toggleMode() {
      this.isLogin = !this.isLogin;
      this.message = '';
    },
    async register() {
      if (!this.registerAccount || !this.registerName || !this.registerEmail || !this.registerPaymentAccount) {
        this.setMessage('請輸入完整資料', true);
        return;
      }

      const { success, message } = await callApi('/api/register', 'POST', {
        userid: this.registerAccount,
        username: this.registerName,
        email: this.registerEmail,
        account: this.registerPaymentAccount
      });

      this.setMessage(message, !success);
    },
    async login() {
      if (!this.loginAccount) {
        this.setMessage('請輸入帳號', true);
        return;
      }

      const { success, message } = await callApi('/api/login', 'POST', {
        userid: this.loginAccount
      });

      if (success) {
        const authStore = useAuthStore();
        authStore.setUserId(this.loginAccount);
        this.setMessage(message);
        setTimeout(() => this.$router.push("/home"), 1000);
      } else {
        this.setMessage(message, true);
      }
    },
    setMessage(msg, isError = false) {
      this.message = msg;
      this.isError = isError;
    }
  },
}
</script>

<style scoped>
.login-container {
  width: 300px;
  margin: 100px auto;
  padding: 30px;
  background: #f9f9f9;
  border-radius: 10px;
  text-align: center;
}
input {
  display: block;
  margin: 10px auto;
  padding: 8px;
  width: 90%;
}
button {
  padding: 8px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
.toggle {
  margin-top: 15px;
  color: #666;
  cursor: pointer;
}
.message {
  margin-top: 10px;
  color: green;
}
.error {
  color: red;
}
</style>
