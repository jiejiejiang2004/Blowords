<script setup lang="ts">
  import { ref } from 'vue'

  defineProps<{ msg: string }>()
  let a = 0
  console.log(a)


  const count = ref(0)
</script>

<template>
  <h1>{{ msg }}</h1>

</template>

<style scoped>
  h1 {
    color: pink;
    text-align: center;
  }
</style>
