<template>
  <div class="form-container">
    <VeeForm @submit="editUser">
      <div class="col-12 md:col-4">
        <div class="flex flex-column field relative">
          <label class="mb-2 inline-block" for="email">E-mail</label>
          <InputText id="email" v-model.trim="user.email" required="true" autofocus/>
        </div>
        <div class="flex flex-column field relative">
          <label class="mb-2 inline-block" for="name">Imię</label>
          <InputText id="name" v-model.trim="user.firstName" required="true" autofocus/>
        </div>
        <div class="flex flex-column field relative">
          <label class="mb-2 inline-block" for="surname">Naziwsko</label>
          <InputText id="surname" v-model.trim="user.lastName" required="true" autofocus/>
        </div>
        <Toolbar>
          <template #end>
            <Button label="Zapisz" type="submit" icon="pi pi-check"/>
          </template>
        </Toolbar>
      </div>
    </VeeForm>
  </div>
</template>

<script>
import {Form as VeeForm} from "vee-validate";
import Toolbar from "primevue/toolbar";
import {createOrUpdateUserUsingPOST as editUser, getLoggedUserUsingGET as getUser} from "@/swagger/vue-api-client"

export default {
  name: "EditProfileView",

  components: {VeeForm, Toolbar},

  data() {
    return {
      user: {},
    }
  },

  methods: {
    editUser() {
      editUser({userDto: this.user}).then(() => {
        this.$router.push({name: "home"});
      }).catch((error) => {
        console.log(error);
      })
    }
  },

  created() {
    getUser().then((response) => {
      this.user = response.data;
    }).catch((error) => {
      console.log(error);
    })
  }
}

</script>

<style scoped>

</style>