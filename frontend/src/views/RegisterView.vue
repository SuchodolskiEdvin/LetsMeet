<template>
  <div class="register-panel-outer sm:py-4">
    <Toast/>
    <div class="text-center">
      <img src="@/assets/logo.png" alt="logo" class="max-w-full">
    </div>
    <div class="register-panel-content">
      <h1 class="py-3 text-center">Witamy!</h1>
      <VeeForm @submit="register" v-slot="{ errors }">
        <div class="mb-2">
          <label class="mb-2 inline-block" for="name">E-mail</label>
          <InputText name="email" type="text" v-model="registerForm.email"/>
        </div>
        <div class="mb-2">
          <label class="mb-2 inline-block" for="name">Hasło</label>
          <span class="p-float-label p-input-icon-left">
            <Password name="password" v-model="registerForm.password" toggleMask>
              <template #header>
                <h6>Pick a password</h6>
              </template>
              <template #footer v-if="loginErrorType.lowercase || loginErrorType.uppercase ||
                      loginErrorType.numeric || loginErrorType.charactersNumber || loginErrorType.specialCharacter">
                <p class="p-mt-2">Wymagania</p>
                <ul class="p-pl-2 p-ml-2 p-mt-0" style="line-height: 1.5">
                  <li v-if="loginErrorType.lowercase">Przynajmniej jedna mała litera</li>
                  <li v-if="loginErrorType.uppercase">Przynajmniej jedna wielka litera</li>
                  <li v-if="loginErrorType.numeric">Przynajmniej jedna cyfra</li>
                  <li v-if="loginErrorType.specialCharacter">Przynajmniej 1 znak specjalny</li>
                  <li v-if="loginErrorType.charactersNumber">Przynajmniej 8 znaków</li>
                </ul>
              </template>
              <template #footer v-else>
                <p class="p-mt-2">Hasło spełnia wymagania</p>
              </template>
            </Password>
          </span>
        </div>
        <div class="mb-2">
          <label class="mb-2 inline-block" for="name">Powtórz hasło</label>
          <span class="p-float-label p-input-icon-left">
            <Password name="confirmPassword" v-model="registerForm.confirmPassword" :feedback="false" toggleMask/>
          </span>
        </div>
        <div class="mb-2">
          <label class="mb-2 inline-block" for="name">Imię</label>
          <InputText name="firstName" type="text" v-model="registerForm.firstName"/>
        </div>
        <div class="mb-2">
          <label class="mb-2 inline-block" for="name">Nazwisko</label>
          <InputText name="lastName" type="text" v-model="registerForm.lastName"/>
        </div>
        <Button label="Zarejestruj się" class="mt-3" type="submit"
            :disabled="isDisabled(errors)"/>
      </VeeForm>
      <div class="text-center block my-3">
        Jesteś już na LetsMeet?
        <router-link :to="{name: 'login'}">
          Zaloguj się
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import Button from "primevue/button";
import {Form as VeeForm} from "vee-validate";
import Toast from "primevue/toast";
import Password from "primevue/password";

export default {
  name: 'RegisterView',
  components: {Button, VeeForm, Toast, Password},
  data() {
    return {
      registerForm: {
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        confirmPassword: "",
      },
      loginError: false,
      loginErrorType: {
        lowercase: true,
        uppercase: true,
        numeric: true,
        charactersNumber: true,
        specialCharacter: true,
      },
    };
  },

  methods: {
    isDisabled() {

    },

    register() {

    },
  },
}
</script>

<style scoped>

</style>