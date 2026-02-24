<script setup lang="ts">
import { closeToast, showLoadingToast, showToast } from 'vant'
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { userCaptcha, userLogin, userRegister } from '@/api/login'
const router = useRouter()
const loginMode = ref<boolean>(true)

const account = ref<string>('')

const password = ref<string>('')
const email = ref<string>('')
const username = ref<string>('')
const captcha = ref<string>('')

const comfirmPassword = ref<string>('')

const captchaSecond = ref<number>(0)
const btnContent = computed<string>(() => {
  return captchaSecond.value <= 0 ? '获取验证码' : `验证码 ${captchaSecond.value}s`
})

const onLogin = async () => {
  console.log('login')
  showLoadingToast('登录中')
  const res = await userLogin(account.value, password.value)
  closeToast()
  console.log(res)
  router.push({ name: 'home' })
}
const onRegister = async () => {
  console.log('register')
  showLoadingToast('注册中')
  await userRegister({
    email: email.value,
    username: username.value,
    password: password.value,
    captcha: captcha.value,
  })
  closeToast()
  showToast('注册成功')
}
const getCaptcha = async () => {
  if (!emailReg.test(email.value)) {
    showToast('请输入正确的邮箱')
    return
  }
  console.log('getCaptcha')
  showLoadingToast('获取验证码')
  await userCaptcha(email.value)
  closeToast()
  showToast('已发送验证码')
  captchaSecond.value = 60
  let itv = setInterval(() => {
    captchaSecond.value--
    if (captchaSecond.value <= 0) {
      clearInterval(itv)
    }
  }, 1000)
}

const emailReg = /^[a-zA-Z0-9]+([._-][a-zA-Z0-9]+)*@[a-zA-Z0-9]+([.-][a-zA-Z0-9]+)*\.[a-zA-Z]{2,}$/
const usernameReg = /^[a-zA-Z0-9_]{1,16}$/
const captchaReg = /^[0-9]{6}$/
const passwordReg = /^[a-zA-Z0-9\+\-\*\/_=\.@!#$%^&?:;,]{8,16}$/
const alphabetReg = /[a-zA-Z]/
const labelWidth = '64px'
</script>

<template>
  <a href="https://github.com/jiejiejiang2004/Blowords" target="_blank">
    <img src="/blowords.svg" class="logo" alt="Blowords" />
  </a>
  <h1>Blowords</h1>
  <van-form @submit="onLogin" v-if="loginMode" class="login form">
    <van-cell-group inset>
      <van-field
        v-model="account"
        name="account"
        label="账户"
        placeholder="用户名/邮箱/手机号"
        label-width="40px"
        :rules="[{ required: true, message: '请填写登录账户' }]"
      />
      <van-field
        v-model="password"
        type="password"
        name="password"
        label="密码"
        placeholder="密码"
        label-width="40px"
        :rules="[{ required: true, message: '请填写密码' }]"
      />
    </van-cell-group>
    <div style="margin: 16px">
      <van-button round block type="primary" native-type="submit" color="#e16076"> 登录 </van-button>
      <van-button round block plain type="primary" color="#e16076" @click="loginMode = false"> 注册账号 </van-button>
    </div>
  </van-form>
  <van-form @submit="onRegister" v-else class="reg form">
    <van-cell-group inset>
      <van-field
        v-model="email"
        name="email"
        label="邮箱"
        placeholder="邮箱"
        :label-width="labelWidth"
        :rules="[
          { required: true, message: '请填写邮箱' },
          { pattern: emailReg, message: '请填写正确的邮箱格式' },
        ]"
      />
      <van-field
        v-model="username"
        name="username"
        label="用户名"
        placeholder="用户名"
        :label-width="labelWidth"
        :rules="[
          { required: true, message: '请填写用户名' },
          { pattern: alphabetReg, message: '至少包含一个英文字母' },
          { pattern: usernameReg, message: '只包含数字字母下划线且小于16字符' },
        ]"
      />
      <div class="captcha">
        <van-field
          v-model="captcha"
          name="captcha"
          label="验证码"
          placeholder="验证码"
          :label-width="labelWidth"
          :rules="[
            { required: true, message: '请填写验证码' },
            { pattern: captchaReg, message: '请填写正确的验证码格式' },
          ]"
        />
        <van-button type="primary" color="#e16076" @click="getCaptcha" :disabled="captchaSecond > 0">
          {{ btnContent }}
        </van-button>
      </div>
      <van-field
        v-model="password"
        name="password"
        label="密码"
        placeholder="密码"
        :label-width="labelWidth"
        :rules="[
          { required: true, message: '请填写密码' },
          {
            pattern: passwordReg,
            message: '密码为8-16位且只含字母数字特殊字符',
          },
        ]"
      />
      <van-field
        v-model="comfirmPassword"
        type="password"
        name="comfirmPassword"
        label="确认密码"
        placeholder="确认密码"
        :label-width="labelWidth"
        :rules="[{ required: true, message: '请确认密码' }]"
      />
    </van-cell-group>
    <div>
      <van-button round block type="primary" native-type="submit" color="#e16076"> 注册 </van-button>
      <a href="#" @click="loginMode = true">返回登录</a>
    </div>
  </van-form>
</template>

<style scoped lang="less">
.logo {
  margin: 80px auto 0;
  height: 100px;
  will-change: filter;
  transition: filter 300ms;

  &:hover {
    filter: drop-shadow(0 0 2em #05f1a6aa);
  }
}

h1 {
  color: pink;
  text-align: center;
  font-size: 50px;
  font-family: consolas, 'Courier New', Courier, monospace;
}

.form {
  max-width: 340px;

  button {
    font-size: 16px;
    width: 90%;
    margin: 10px auto 0;
  }
}

.login {
  margin: 20px auto 0;

  :deep(.van-field__control) {
    font-size: 16px;
  }

  :deep(.van-field) {
    min-height: 60px;
  }

  :deep(.van-field__label) {
    font-size: 16px;
  }
}

.reg {
  margin: 0 auto;

  .captcha {
    display: flex;

    button {
      height: 38px;
      width: 120px;
      font-size: 14px;
      padding: 0;
    }
  }

  a {
    margin-top: 10px;
    display: block;
    text-align: center;
    font-size: 18px;
    text-decoration: underline;
    color: rgb(253, 156, 172);
  }
}
</style>
