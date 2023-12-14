<template>
  <div class="simple-layout-wrapper flex align-items-center justify-content-center h-screen">
    <div class="main p-fluid w-full sm:w-10 md:w-8 lg:w-6 xl:w-3">
      <router-view />
    </div>
  </div>
</template>

<script>
  export default {
    name: "LayoutPublic",

  };

</script>

<style lang="scss">
  @import "../assets/layout-public";
</style>