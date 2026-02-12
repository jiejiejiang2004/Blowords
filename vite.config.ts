import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue()
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))  // 新增
    }
  },
  server: {
    port: 8080,
    host: '0.0.0.0', // 允许局域网访问
    strictPort: true, // 端口被占用时，Vite 会自动尝试下一个可用端口（默认 false）
    open: true, // 启动开发服务器后自动打开浏览器
    proxy: {
      // 代理后端API请求
      '/api': {
        target: 'http://localhost:8080', // 后端地址
        changeOrigin: true,
        secure: false
      }
    }
  }
})
