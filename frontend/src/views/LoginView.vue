<template>
  <div class="login-container">
    <div class="login-panel-outer p-4 sm:py-4 md:px-8">
      <div class="login-panel-header" style="display: flex; justify-content: center; align-items: center;">
        <img src="@/assets/logo.png" alt="logo" class="max-w-full">
      </div>
      <div class="login-panel-content">
        <h1 class="py-3 text-center">Login</h1>
        <VeeForm @submit="login" v-slot="{ errors }">
          <div class="p-fluid">
            <div v-if="loginError" class="p-mb-5">{{ $t('message.login.loginError') }}</div>
            <div class="flex flex-column field relative">
              <label class="mb-2 inline-block" for="name"> Username </label>
              <InputText name="login" type="text" v-model="loginForm.login"/>

              <div class="p-fluid custom-password relative field">
                <label name="password" label="Hasło" :required="required"/>
                <span class="p-float-label p-input-icon-left">
                  <i :class="icon" class="icon" />
                  <Password id="password" name="password" v-model="loginForm.password">
                    <template #header>
                      <h6>Pick a password</h6>
                    </template>
                    <template #footer v-if="lowercase || uppercase || numeric
                        || charactersNumber || specialCharacter">
                        <p class="p-mt-2">Wymagania</p>
                        <ul class="p-pl-2 p-ml-2 p-mt-0" style="line-height: 1.5">
                            <li v-if="lowercase">Przynajmniej jedna mała litera</li>
                            <li v-if="uppercase">Przynajmniej jedna wielka litera</li>
                            <li v-if="numeric">Przynajmniej jedna cyfra</li>
                            <li v-if="specialCharacter">Przynajmniej 1 znak specjalny</li>
                            <li v-if="charactersNumber">Przynajmniej 8 znaków</li>
                        </ul>
                    </template>
                    <template #footer v-else>
                        <p class="p-mt-2">Hasło spełnia wymagania</p>
                    </template>
                  </Password>
                </span>
                <CustomValidationMessage :errorMessage="errorMessage" />
              </div>
<!--              <InputText name="password" v-model="loginForm.password"-->
<!--                  label="password" rules="required"/>-->
            </div>
            <div class="login-error" v-if="badCredentials">
              Niepoprawne dane logowania
            </div>
            <Button label="Zaloguj sie" class="mt-3" type="submit"
                :disabled="isDisabled(errors)"/>
          </div>
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
import Password from "primevue/password";
import {Form as VeeForm} from "vee-validate";
import {loginUsingPOST as loginRequest} from "@/swagger/vue-api-client";

export default {
  name: "LoginView",
  components: {
    InputText, Button, VeeForm, Password
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
