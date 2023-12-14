<template>
  <div>
    <div class="login-panel-outer p-4 sm:py-4 md:px-8">
      <div class="login-panel-header text-center">
        <img src="@/assets/logo.png" alt="logo" class="max-w-full">
      </div>
      <div class="login-panel-content">
        <h1 class="py-3 text-center">some text</h1>
        <VeeForm @submit="login" v-slot="{ errors }">
          <div v-if="loginError" class="p-mb-5">{{ $t('message.login.loginError') }}</div>
          <InputText name="login" v-model="loginForm.login" label="login"
              rules="required"/>
          <InputText name="password" v-model="loginForm.password"
              label="password" rules="required"/>
          <div class="login-error" v-if="isBlocked">
            Zbyt dużo nieudanych prób logowania. Logowanie zablokowane na 24 godziny
          </div>
          <div class="login-error" v-if="badCredentials">
            Niepoprawne dane logowania
          </div>
          <Button label="Zaloguj sie" class="mt-3" type="submit"
              :disabled="isDisabled(errors)"/>
        </VeeForm>
<!--        <router-link :to="{name: 'remindPassword'}" class="text-center block my-3">-->
<!--          remind password-->
<!--        </router-link>-->
<!--        <div class="text-center">-->
<!--          pls register - -->
<!--          <router-link :to="{name: 'register'}" class="text-center block my-2">-->
<!--            register-->
<!--          </router-link>-->
<!--        </div>-->
      </div>
    </div>
  </div>
</template>

<script>
import InputText from "primevue/inputtext";
import Button from "primevue/button";
import {Form as VeeForm} from "vee-validate";
import {loginUsingPOST as loginRequest} from "@/swagger/vue-api-client";

export default {
  name: "LoginView",
  components: {
    InputText, Button, VeeForm,
  },

  data() {
    return {
      loginError: false,
      loginForm: {
        login: "",
        password: "",
      },
      isBlocked: false,
      badCredentials: false,
    };
  },

  methods: {
    isDisabled(errors) {
      return !(errors && Object.keys(errors).length === 0
          && Object.getPrototypeOf(errors) === Object.prototype);
    },
    login() {
      loginRequest({authenticationRequest: this.loginForm})
          .then((response) => {
            localStorage.setItem("token", response.data.token);
            localStorage.setItem("role", response.data.role);
            localStorage.setItem("fullName", response.data.fullName);
            if (this.$route.query.next) {
              this.$router.push(this.$route.query.next);
            } else {
              this.$router.push({name: "home"});
            }
          })
          .catch((error) => {
            switch (error.code) {
              case "ERR_BAD_RESPONSE":
                this.isBlocked = true;
                this.badCredentials = false;
                break;
              case "ERR_BAD_REQUEST":
                this.badCredentials = true;
                break;
              default:
            }
          });
    },
  },
};
</script>

<style lang="scss" scoped>
  @import "assets/login";
</style>
