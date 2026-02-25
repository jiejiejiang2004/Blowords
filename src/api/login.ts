import request from '@/utils/request'

// 登录接口
export const userLogin = (account: string, password: string) => {
    return request.post('/users/login', { account, password })
    // return {
    //     "code": 200,
    //     "msg": "ok",
    //     "data": {
    //         "uid": "u0001",
    //         "username": "jjj",
    //         "phoneNumber": "12345678910",
    //         "email": "12345@qq.com",
    //         "nickname": "jiejiejiang",
    //         "avatarUrl": "/avatar/u0001.png",
    //         "refreshToken": "ASDFGHJKLAKDJHFLKAS",
    //         "accessToken": "ASDFGHJKLAKDJHFLKAS"
    //     }
    // }
}

// 注册接口
export const userRegister = (user: {
    email: string,
    username: string,
    password: string,
    captcha: string
}) => {
    return request.post('/users/register', { ...user })
}

// 获取验证码
export const userCaptcha = (email: string) => {
    return request.post('/users/registerCaptcha', { email })
}
