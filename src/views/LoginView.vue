<script setup lang="ts">
</script>

<template>
  <a href="https://github.com/jiejiejiang2004/Blowords" target="_blank">
    <img src="/blowords.svg" class="logo" alt="Blowords" />
  </a>
  <h1>Blowords</h1>

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
  }
</style>
