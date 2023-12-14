<template>
  <div>
    TODO...
  </div>
<!--  <v-main>-->
<!--    <v-container fluid fill-height>-->
<!--      <v-layout align-center justify-center>-->
<!--        <v-flex md4>-->
<!--          <div>-->
<!--            <v-alert-->
<!--                v-if="error_message"-->
<!--                color="red"-->
<!--                outlined-->
<!--                text-->
<!--                type="error"-->
<!--            >-->
<!--              {{ error_message }}-->
<!--            </v-alert>-->
<!--          </div>-->

<!--          <v-card-text>-->
<!--            <v-form>-->
<!--              <v-text-field prepend-icon="person" name="email" label="E-mail"-->
<!--                  v-model="credentials.email"-->
<!--                  :rules="[rules.required]"-->
<!--                  type="text"></v-text-field>-->
<!--              <v-text-field prepend-icon="lock" name="password" label="Password"-->
<!--                  :append-icon="show1 ? 'visibility' : 'visibility_off'"-->
<!--                  :rules="[rules.required]"-->
<!--                  :type="show1 ? 'text' : 'password'"-->
<!--                  value="Pa"-->
<!--                  @click:append="show1 = !show1"-->
<!--                  v-model="credentials.password"-->
<!--                  @focus="magic_flag = true"-->
<!--                  @blur="magic_flag = false"-->
<!--                  v-popover:test.top-->
<!--              >-->
<!--              </v-text-field>-->
<!--              <v-text-field prepend-icon="lock" name="Confirm password" label="Confirm password"-->
<!--                  :append-icon="show2 ? 'visibility' : 'visibility_off'"-->
<!--                  :rules="[rules.required]"-->
<!--                  :type="show2 ? 'text' : 'password'"-->
<!--                  @click:append="show2 = !show2"-->
<!--                  v-model="credentials.confirmPassword">-->
<!--              </v-text-field>-->
<!--              <v-text-field name="name" label="Name"-->
<!--                  :rules="[rules.required]"-->
<!--                  v-model="credentials.name"-->
<!--                  type="text"></v-text-field>-->
<!--              <v-text-field name="surname" label="Surname"-->
<!--                  :rules="[rules.required]"-->
<!--                  v-model="credentials.surname"-->
<!--                  type="text">-->
<!--              </v-text-field>-->
<!--            </v-form>-->

<!--          </v-card-text>-->
<!--          <v-card-actions>-->
<!--            <v-row>-->

<!--              <v-flex class="justify-space-between d-flex ma-4">-->
<!--                <v-btn color="primary" @click="back">-->
<!--                  Back-->
<!--                </v-btn>-->
<!--                <v-spacer></v-spacer>-->
<!--                <v-btn color="primary" @click="reg">-->
<!--                  Save-->
<!--                </v-btn>-->
<!--              </v-flex>-->
<!--            </v-row>-->
<!--          </v-card-actions>-->
<!--        </v-flex>-->
<!--&lt;!&ndash;        <popover name="test" id="popover">&ndash;&gt;-->
<!--&lt;!&ndash;          <v-card-text class="input_container" v-if="magic_flag" :style="{width: '200px'}">&ndash;&gt;-->
<!--&lt;!&ndash;            <v-flex align-start justify-end>&ndash;&gt;-->
<!--&lt;!&ndash;              <h4>Password must meet the following requirements:</h4>&ndash;&gt;-->
<!--&lt;!&ndash;              <ul>&ndash;&gt;-->
<!--&lt;!&ndash;                <li v-bind:class="{ is_valid: passwordMeterParams.contains_eight_characters }">12 Characters</li>&ndash;&gt;-->
<!--&lt;!&ndash;                <li v-bind:class="{ is_valid: passwordMeterParams.contains_number }">Contains Number</li>&ndash;&gt;-->
<!--&lt;!&ndash;                <li v-bind:class="{ is_valid: passwordMeterParams.contains_uppercase }">Contains Uppercase and Lowercase</li>&ndash;&gt;-->
<!--&lt;!&ndash;                <li v-bind:class="{ is_valid: passwordMeterParams.contains_special_character }">Contains Special Character</li>&ndash;&gt;-->
<!--&lt;!&ndash;              </ul>&ndash;&gt;-->

<!--&lt;!&ndash;              <v-progress-linear&ndash;&gt;-->
<!--&lt;!&ndash;                  :value="progress"&ndash;&gt;-->
<!--&lt;!&ndash;                  :color="color"&ndash;&gt;-->
<!--&lt;!&ndash;                  rounded&ndash;&gt;-->
<!--&lt;!&ndash;                  height="7"&ndash;&gt;-->
<!--&lt;!&ndash;              ></v-progress-linear>&ndash;&gt;-->
<!--&lt;!&ndash;            </v-flex>&ndash;&gt;-->
<!--&lt;!&ndash;          </v-card-text>&ndash;&gt;-->
<!--&lt;!&ndash;        </popover>&ndash;&gt;-->


<!--      </v-layout>-->
<!--    </v-container>-->
<!--  </v-main>-->
</template>

<script>
// import {api} from "@/util/Api";
// import {auth} from "@/util/Auth";
// import passwordMeter from "vue-simple-password-meter";

export default {
  name: 'RegisterView',
  // components: {passwordMeter},
  // data() {
  //   return {
  //     credentials: {
  //       name: "",
  //       surname: "",
  //       email: "",
  //       password: "",
  //       confirmPassword: "",
  //     },
  //     passwordMeterParams: {
  //       contains_eight_characters: false,
  //       contains_number: false,
  //       contains_uppercase: false,
  //       contains_special_character: false,
  //     },
  //     rules: {
  //       required: value => !!value || 'Required. ',
  //     },
  //     value: '',
  //     error_message: "",
  //     show1: false,
  //     show2: false,
  //     score: 0,
  //     strength: null,
  //     magic_flag: false,
  //   };
  // },
  //
  // computed: {
  //   progress () {
  //     let score = 0;
  //     let length = this.credentials.password.length;
  //
  //     if(this.credentials.password.toUpperCase() !== this.credentials.password) {
  //       score += 26;
  //     }
  //     if(this.credentials.password.toLowerCase() !== this.credentials.password) {
  //       score += 26;
  //     }
  //
  //     if(/\d/.test(this.credentials.password)) {
  //       score += 10;
  //     }
  //
  //     if(/[ `!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?~]/.test(this.credentials.password)){
  //       score += 32;
  //     }
  //
  //     score = length * Math.log(score) / Math.log(2);
  //     if(score >= 100)
  //       score = 100;
  //
  //     // eslint-disable-next-line vue/no-side-effects-in-computed-properties
  //     this.passwordMeterParams.contains_eight_characters = length >= 12;
  //
  //     // eslint-disable-next-line vue/no-side-effects-in-computed-properties
  //     this.passwordMeterParams.contains_uppercase = this.credentials.password.toUpperCase() !== this.credentials.password &&
  //         this.credentials.password.toLowerCase() !== this.credentials.password;
  //
  //     // eslint-disable-next-line vue/no-side-effects-in-computed-properties
  //     this.passwordMeterParams.contains_number = /\d/.test(this.credentials.password);
  //
  //     // eslint-disable-next-line vue/no-side-effects-in-computed-properties
  //     this.passwordMeterParams.contains_special_character = /[ `!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?~]/.test(this.credentials.password);
  //
  //     return score;
  //   },
  //   color () {
  //     return ['error', 'warning', 'success'][Math.floor(this.progress / 40)]
  //   },
  // },
  //
  // methods: {
  //   reg() {
  //     if(this.credentials.name === "" ||
  //         this.credentials.surname === "" ||
  //         this.credentials.email === "" ||
  //         this.credentials.password === "" ||
  //         this.credentials.confirmPassword === "") {
  //       this.lackOfData();
  //     } else {
  //       api.post(this, "/user", this.credentials, () => {
  //         auth.login(this, this.credentials);
  //       }, errorStatus => {
  //         if (errorStatus === 406) {
  //           this.passwordError();
  //         } else if (errorStatus === 409) {
  //           this.accountAlreadyExists();
  //         } else if (errorStatus === 403) {
  //           this.incorrectEmail();
  //         }
  //       });
  //     }
  //   },
  //
  //   back() {
  //     this.$router.push("/");
  //   },
  //
  //   passwordError() {
  //     if (this.credentials.password != this.credentials.confirmPassword) {
  //       this.error_message = "Podane hasła nie zgadzają się."
  //     } else {
  //       this.error_message = "Podane hasło jest zbyt słabe lub nie spełnia wymienione warunki."
  //     }
  //   },
  //
  //   accountAlreadyExists() {
  //     this.error_message = "Dany użytkownik już istnieje."
  //   },
  //
  //   incorrectEmail() {
  //     this.error_message = "Błąd w etykiecie E-mail."
  //   },
  //
  //   lackOfData() {
  //     this.error_message = "Brak danych."
  //   },
  // },
}
</script>

<style scoped>

</style>