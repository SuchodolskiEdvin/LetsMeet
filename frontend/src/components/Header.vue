<template>
  <Menubar :model="items">
    <template #start>
      <svg width="35" height="40" viewBox="0 0 35 40" fill="none" xmlns="http://www.w3.org/2000/svg" class="h-2rem">
        <path d="..." fill="var(--primary-color)"/>
        <path d="..." fill="var(--text-color)"/>
      </svg>
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
        <button @click="logout" class="p-button p-button-danger p-mr-2">
          <span :class="lastItem.icon"/>
          <span class="ml-2">{{ lastItem.label }}</span>
        </button>
        <Avatar label="E" class="mr-2" style="background-color:#1E88E5; color: #ffffff" shape="circle"/>
      </div>
    </template>
  </Menubar>
</template>

<script>
import Menubar from "primevue/menubar";
import Avatar from "primevue/avatar";

export default {
  name: "MenuBar",
  components: {Menubar, Avatar},

  data() {
    return {
      items: [{
        label: 'Home',
        icon: 'pi pi-home',
        url: '/home'
      }, {
        label: 'EditProfile',
        icon: 'pi pi-user',
        url: '/user/edit'
      }],
      lastItem: {
        label: 'Logout',
        icon: 'pi pi-sign-out'
      }
    };
  },

}

</script>


<style scoped>

</style>