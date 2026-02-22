<script setup lang="ts">
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  const router = useRouter()
  const currentTab = ref<string>('/home')

  const changePage = (target: string) => {
    currentTab.value = target
    router.push(target)
  }
</script>

<template>

  <!-- 条件侧边栏：PC端显示 -->
  <aside class="pc-sidebar">
    <div class="sidebar-header">
      <img src="/blowords.svg" class="logo" alt="Blowords" />
      <h2>Blowords</h2>
    </div>
    <ul class="sidebar-menu">
      <li :class="{ active: currentTab === '/home' }" @click="changePage('/home')">
        <van-icon name="home-o" />
        <span>首页</span>
      </li>
      <li :class="{ active: currentTab === '/wordbooks' }" @click="changePage('/wordbooks')">
        <van-icon name="records-o" />
        <span>词书</span>
      </li>
      <li :class="{ active: currentTab === '/statistics' }" @click="changePage('/statistics')">
        <van-icon name="todo-list-o" />
        <span>统计</span>
      </li>
      <li :class="{ active: currentTab === '/user' }" @click="changePage('/user')">
        <van-icon name="user-o" />
        <span>我的</span>
      </li>
    </ul>
  </aside>

</template>

<style scoped lang="less">
  @theme-bg: white;
  @theme-primary: rgb(254, 173, 186);

  .pc-sidebar {
    width: 300px;
    height: 100%;
    position: fixed;
    left: 0;
    background-color: @theme-bg;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.06);

    .sidebar-header {
      display: flex;
      padding: 20px 20px;
      border-bottom: 1px solid #f0f0f0;

      .logo {
        height: 40px;
      }

      h2 {
        margin-left: 10px;
        color: @theme-primary;
        font-size: 40px;
        line-height: 40px;
      }
    }

    .sidebar-menu {
      padding: 16px 0;

      li {
        display: flex;
        align-items: center;
        padding: 12px 0 12px 60px;
        margin: 4px 8px;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.5s;

        &:hover {
          background-color: @theme-bg;
          color: @theme-primary;
        }

        &.active {
          background-color: #e6f7ff;
          color: @theme-primary;
          font-weight: 500;
        }

        .van-icon {
          margin-right: 12px;
          font-size: 1em;
        }
      }
    }
  }
</style>
