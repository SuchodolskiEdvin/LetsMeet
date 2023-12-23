<template>
  <div class="layout-wrapper">
    <Header/>

    <div class="layout-main">
      <div class="content">
        <router-view/>
      </div>
      <footer/>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";

export default {
  name: "LayoutSecured",

  components: {Header},

  computed: {
    containerClass() {
      return ["layout-wrapper", {
        "layout-overlay": this.layoutMode === "overlay",
        "layout-static": this.layoutMode === "static",
        "layout-static-sidebar-inactive": this.staticMenuInactive && this.layoutMode === "static",
        "layout-overlay-sidebar-active": this.overlayMenuActive && this.layoutMode === "overlay",
        "layout-mobile-sidebar-active": this.mobileMenuActive,
      }];
    },
  },
};

</script>

<style scoped>
@import "../assets/layout-secured.scss";
@import "../assets/responsive.scss";
</style>