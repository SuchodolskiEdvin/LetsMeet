<template>
  <v-main>
    <v-container fluid fill-height>
      <v-layout align-center justify-center>
        <v-flex xs12 sm8 md4>
          <v-card-text>
            <v-form>
              <v-text-field name="name" label="Name"
                  v-model="data.name"
                  type="text"></v-text-field>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-row>

              <v-col cols="6"/>
              <v-col offset="1" cols="5">
                <div style="float: right">
                  <v-btn color="primary" @click="create">
                    Stwórz
                  </v-btn>
                </div>
              </v-col>
            </v-row>
          </v-card-actions>
        </v-flex>
      </v-layout>
    </v-container>
  </v-main>
</template>

<script>
import {api} from "@/util/Api";

export default {
  name: 'CreateMeetVForm',

  data() {
    return {
      data: {
        name: "",
      },
      error_message: "",
    };
  },

  methods: {
    create() {
      api.post(this, "/meet", this.data, () => {
        this.$router.push("/app");
      });
    },
  },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>