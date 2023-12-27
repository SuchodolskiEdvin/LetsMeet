<template>
  <Menubar :model="headers">
    <template #start>
<!--      Potencjalna ikonka aplikacji  -->
    </template>
    <template #item="{ item, props, hasSubmenu, root }">
      <router-link :to="item.url" class="flex align-items-center" v-bind="props.action">
        <span :class="item.icon"/>
        <span class="ml-2">{{ item.label }}</span>
        <Badge v-if="item.badge" :class="{ 'ml-auto': !root, 'ml-2': root }" :value="item.badge"/>
        <span v-if="item.shortcut"
            class="ml-auto border-1 surface-border border-round surface-100 text-xs p-1">{{ item.shortcut }}</span>
        <i v-if="hasSubmenu"
            :class="['pi pi-angle-down text-primary', { 'pi-angle-down ml-2': root, 'pi-angle-right ml-auto': !root }]"></i>
      </router-link>
    </template>
    <template #end>
      <div class="flex align-items-center gap-2">
        <Avatar label="E" class="mr-2" style="background-color:#1E88E5; color: #ffffff" shape="circle"/>
        <button @click="logout" class="p-button p-button-danger p-mr-2">
          <span :class="lastItem.icon"/>
          <span class="ml-2">{{ lastItem.label }}</span>
        </button>
      </div>
    </template>
  </Menubar>
</template>

<script>
import Menubar from "primevue/menubar";
import Avatar from "primevue/avatar";
import Badge from "primevue/badge";

export default {
  name: "MenuBar",
  components: {Menubar, Badge, Avatar},

  data() {
    return {
      headers: [{
        label: 'Home',
        icon: 'pi pi-home',
        url: '/home'
      }, {
        label: 'Profile',
        icon: 'pi pi-user',
        url: '/user/edit'
      }],
      lastItem: {
        label: 'Logout',
        icon: 'pi pi-sign-out'
      }
    };
  },

  methods: {
    logout() {
      localStorage.removeItem("token");
      this.$router.push({name: "login"});
    },
  },

}

</script>


<style scoped>

</style>