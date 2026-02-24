import { defineStore } from 'pinia'

export interface UserInfo {
    uid: string,
    username: string,
    nickname: string,
    phoneNumber: string,
    email: string,
    avatarUrl: string,
    token: string
}

export const useUserStore = defineStore('user', {
    state: () => ({
        uid: '',
        username: '',
        nickname: '',
        phoneNumber: '',
        email: '',
        avatarUrl: '',
        token: ''
    }),
    getters: {
        isLogin(state) {
            return state.token !== ''
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
            this.token = user.token
        },
        logout() {
            this.uid = ''
            this.username = ''
            this.nickname = ''
            this.phoneNumber = ''
            this.email = ''
            this.avatarUrl = ''
            this.token = ''
        }
    }
})