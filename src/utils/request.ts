import axios from "axios"
import { showFailToast } from "vant"
import { useUserStore } from "@/store/useUserStore"

const instance = axios.create({
    baseURL: '/api/v1',
    timeout: 3000
})

// 添加请求拦截器
instance.interceptors.request.use((config) => {
    // 在发送请求之前做些什么
    const userStore = useUserStore()
    if (userStore.accessToken) {
        config.headers.Authorization = 'Bearer ' + userStore.accessToken
    }
    return config
}, (error) => {
    // 对请求错误做些什么
    showFailToast('请求错误')
    return Promise.reject(error)
})

// 添加响应拦截器
instance.interceptors.response.use((response) => {
    // 2xx 范围内的状态码都会触发该函数。
    // 对响应数据做点什么
    const res = response.data
    console.log(res)
    return res
}, (error) => {
    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    const res = error.response.data
    console.log(res)
    showFailToast(res?.msg ?? '网络错误')
    return Promise.reject(error)
})

export default instance