<template>
  <Header/>

  <div class="layout-main">
    <div class="content">
      <router-view/>
    </div>
    <footer/>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";

export default {
  name: "LayoutSecured",

  components: {Header},

};

</script>

<style scoped>
@import "../assets/layout-secured.scss";
@import "../assets/responsive.scss";
</style>