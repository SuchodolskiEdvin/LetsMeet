<template>
  <div class="login-container">
    <div class="login-panel-outer sm:py-4">
      <div class="login-panel-header">
        <img src="@/assets/logo.png" alt="logo" class="max-w-full">
      </div>
      <div class="login-panel-content">
        <h1 class="py-3 text-center">Witam ponownie</h1>
        <VeeForm @submit="login" v-slot="{ errors }">
          <div class="p-fluid">
            <div v-if="loginError" class="p-mb-5"> Error </div>
            <div class="flex flex-column field relative">
              <div class="mb-2">
                <label class="mb-2 inline-block" for="name">E-mail</label>
                <InputText name="login" type="text" v-model="loginForm.login"/>
              </div>
              <div class="p-fluid custom-password relative field">
                <label :required="required"> Hasło </label>
                <span class="p-float-label p-input-icon-left">
                  <Password id="password" name="password" v-model="loginForm.password" :feedback="false" toggleMask/>
                </span>
              </div>
            </div>
            <div class="login-error" v-if="badCredentials">
              Niepoprawne dane logowania
            </div>
            <Button label="Zaloguj sie" class="mt-3" type="submit"
                :disabled="isDisabled(errors)"/>
          </div>
        </VeeForm>
        <div class="text-center block my-3">
          Nie posiadasz konta?
          <router-link :to="{name: 'register'}">
            Zarejestruj się
          </router-link>
        </div>
        <Divider>
          OR
        </Divider>
        <div class="text-center">
          Nie pamiętasz hasła?
          <router-link :to="{name: 'register'}" class="text-center">
            Zresetuj hasło
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import InputText from "primevue/inputtext";
import Button from "primevue/button";
import Password from "primevue/password";
import Divider from "primevue/divider";
import {Form as VeeForm} from "vee-validate";
import {loginUsingPOST as loginRequest} from "@/swagger/vue-api-client";

export default {
  name: "LoginView",
  components: {
    InputText, Button, VeeForm, Password, Divider
  },

  data() {
    return {
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

  .login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
  }

  .p-fluid {
    width: 300px;
    margin: auto;
  }
</style>
