<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useMediaQuery } from '@vueuse/core'
import PcAside from '@/components/PcAside.vue'

const router = useRouter()
const route = useRoute()

const isPC = useMediaQuery('(min-width: 750px)')
const currentTab = ref<string>((route.name as string) || 'home')
watch(
  () => route.name,
  (newName) => {
    if (newName) {
      currentTab.value = newName as string
    }
  }
)
</script>

<template>
  <!-- 条件侧边栏：PC端显示 -->
  <PcAside v-if="isPC" v-model="currentTab" />
  <main>
    <router-view />
  </main>
  <!-- 移动端底部导航 -->
  <van-tabbar v-if="!isPC" v-model="currentTab" active-color="pink" fixed>
    <van-tabbar-item name="home" icon="home-o" to="/home">首页</van-tabbar-item>
    <van-tabbar-item name="wordbooks" icon="records-o" to="/wordbooks">词书</van-tabbar-item>
    <van-tabbar-item name="statistics" icon="todo-list-o" to="/statistics">统计</van-tabbar-item>
    <van-tabbar-item name="user" icon="user-o" to="/user">我的</van-tabbar-item>
  </van-tabbar>
</template>

<style scoped lang="less">
@media screen and (min-width: 750px) {
  main {
    margin-left: 300px;
  }
}
</style>
