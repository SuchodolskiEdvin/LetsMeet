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
            <Password name="password" v-model="registerForm.password" toggleMask @input="checkPassword"
                promptLabel="Wprowadź hasło" weak-label="Słabe" medium-label="Średnie" strong-label="Mocne!">
              <template #footer v-if="showPasswordRules">
                <p class="p-mt-2">Wymagania</p>
                <ul class="p-pl-2 p-ml-2 p-mt-0" style="line-height: 1.5">
                  <li v-if="loginErrorType.lowercase">Przynajmniej jedna mała litera</li>
                  <li v-else class="condition_fulfilled">Przynajmniej jedna mała litera</li>
                  <li v-if="loginErrorType.uppercase">Przynajmniej jedna wielka litera</li>
                  <li v-else class="condition_fulfilled">Przynajmniej jedna wielka litera</li>
                  <li v-if="loginErrorType.numeric">Przynajmniej jedna cyfra</li>
                  <li v-else class="condition_fulfilled">Przynajmniej jedna cyfra</li>
                  <li v-if="loginErrorType.specialCharacter">Przynajmniej 1 znak specjalny</li>
                  <li v-else class="condition_fulfilled">Przynajmniej 1 znak specjalny</li>
                  <li v-if="loginErrorType.charactersNumber">Przynajmniej 8 znaków</li>
                  <li v-else class="condition_fulfilled">Przynajmniej 8 znaków</li>
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
import {createOrUpdateUserUsingPOST as registerRequest} from "@/swagger/vue-api-client"
import {ToastUtils} from "@/util/ToastUtils";

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
      registerRequest({userDto: this.registerForm})
          .then(() => {
            ToastUtils.addToast(this, {
              severity: "success",
              summary: "Sukces",
              detail: "Dziękujemy za rejestrację.",
            });
            this.$router.push({name: "login"});
          })
          .catch((error) => {
            if (error.response && error.response.status === 409) {
              ToastUtils.addToast(this, {
                severity: "error",
                summary: "Błąd",
                detail: "Użytkownik o podanym adresie e-mail już istnieje",
              });
            } else {
              ToastUtils.addToast(this, {
                severity: "error",
                summary: "Błąd",
                detail: "Wystąpił nieoczekiwany błąd, skontaktuj się z administratorem systemu",
              });
            }
          });
    },

    checkPassword() {
      this.loginErrorType.specialCharacter = !(/(?=.*[!@#$%^&*()[\]{};:+=|,<.>`~£§?/'\\-_"])/.test(this.registerForm.password));
      this.loginErrorType.uppercase = !(/(?=.*[A-ZŻŹĆŃÓŁĘĄŚ])/.test(this.registerForm.password));
      this.loginErrorType.lowercase = !(/(?=.*[a-zżźćńółęąś])/.test(this.registerForm.password));
      this.loginErrorType.numeric = !(/(?=.*\d)/.test(this.registerForm.password));
      this.loginErrorType.charactersNumber = !(/(?=.*[a-zA-ZżźćńółęąśZŻŹĆŃÓŁĘĄŚ]).{8,}$/.test(this.registerForm.password));
    }
  },

  computed: {
    showPasswordRules() {
      return Object.values(this.loginErrorType).some(error => error);
    },
  },
}
</script>

<style scoped>
.condition_fulfilled {
  text-decoration: line-through;
}
</style>