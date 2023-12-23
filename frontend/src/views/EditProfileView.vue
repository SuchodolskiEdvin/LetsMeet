<template>
  <div>
    <VeeForm>
      <div class="field">
        <label for="email">E-mail</label>
        <InputText id="email" v-model.trim="user.email" required="true" autofocus/>
      </div>
      <div class="field">
        <label for="name">Imię</label>
        <InputText id="name" v-model.trim="user.name" required="true" autofocus/>
      </div>
      <div class="field">
        <label for="surname">Naziwsko</label>
        <InputText id="surname" v-model.trim="user.name" required="true" autofocus/>
      </div>

    </VeeForm>
  </div>
</template>

<script>
import {Form as VeeForm} from "vee-validate";
export default {
  name: "EditProfileView",

  components: {VeeForm},

  data() {
    return {
      user: {
        email: "",
        name: "",
        surname: ""
      },
    }
  },
}

</script>

<style scoped>

</style>