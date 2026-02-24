import { createWebHistory, createRouter } from 'vue-router'


const routes = [
    {
        path: '/',
        redirect: '/home',
        component: () => import('@/views/LayoutView.vue'),
        children: [
            {
                path: '/home',
                name: 'home',
                component: () => import('@/views/HomeView.vue')
            },
            {
                path: '/wordbooks',
                name: 'wordbooks',
                component: () => import('@/views/WordBooksView.vue')
            },
            {
                path: '/statistics',
                name: 'statistics',
                component: () => import('@/views/StatisticsView.vue')
            },
            {
                path: '/user',
                name: 'user',
                component: () => import('@/views/UserView.vue')
            },
        ]
    },
    {
        path: '/login',
        component: () => import('@/views/LoginView.vue')
    },
    {
        path: '/learn',
        component: () => import('@/views/LearnWordsView.vue')
    },
    {
        path: '/review',
        component: () => import('@/views/ReviewWordsView.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router