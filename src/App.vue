<script setup lang="ts">
  import HelloWorld from '@/components/HelloWorld.vue'
</script>

<template>

  <a href="https://github.com/jiejiejiang2004/Blowords" target="_blank">
    <img src="/blowords.svg" class="logo" alt="Blowords logo" />
  </a>

  <HelloWorld msg="Ciallo~ Blowords" />
</template>

<style scoped lang="less">


  .logo {
    margin: 10rem auto 0;
    height: 6em;
    will-change: filter;
    transition: filter 300ms;

    &:hover {
      filter: drop-shadow(0 0 2em #05f1a6aa);
    }
  }


</style>
