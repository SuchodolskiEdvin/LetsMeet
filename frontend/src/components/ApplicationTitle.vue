<template>
  <div class="tables-title">
    <h1>{{ title }}</h1>
  </div>
</template>

<script>
export default {
  name: "ApplicationTitle",

  props: {
    title: {
      type: String, required: true
    }
  }
}
</script>

<style scoped>
.tables-title {
  margin-left: 2%;
}
</style>