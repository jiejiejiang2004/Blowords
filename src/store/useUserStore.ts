import { defineStore } from 'pinia'
import type { UserInfo } from '@/utils/types'


export const useUserStore = defineStore('user', {
    state: () => ({
        uid: '',
        username: '',
        nickname: '',
        phoneNumber: '',
        email: '',
        avatarUrl: '',
        refreshToken: '',
        accessToken: ''
    }),
    getters: {
        isLogin(state) {
            return state.refreshToken !== ''
        }
    },
    actions: {
        login(user: UserInfo) {
            this.uid = user.uid
            this.username = user.username
            this.nickname = user.nickname
            this.phoneNumber = user.phoneNumber
            this.email = user.email
            this.avatarUrl = user.avatarUrl
            this.refreshToken = user.refreshToken
            this.accessToken = user.accessToken
        },
        logout() {
            this.uid = ''
            this.username = ''
            this.nickname = ''
            this.phoneNumber = ''
            this.email = ''
            this.avatarUrl = ''
            this.refreshToken = ''
            this.accessToken = ''
        }
    },
    persist: {
        key: 'bw-user'
    }
})