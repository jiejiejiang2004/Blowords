import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'

import router from '@/router'

import '@/styles/reset.less'
import '@/styles/global.less'

import 'vant/lib/index.css'
import '@vant/touch-emulator'

import 'amfe-flexible'

const app = createApp(App)
app.use(router)
app.use(createPinia())
app.mount('#app')
