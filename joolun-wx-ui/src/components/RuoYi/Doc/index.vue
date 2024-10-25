<template>
  <div>
    <svg-icon icon-class="question" @click="goto" />
  </div>
</template>

<script setup>
const url = ref("https://gitee.com/joolun/JooLun-wx/wikis/Home");

function goto() {
  window.open(url.value);
}
</script>
