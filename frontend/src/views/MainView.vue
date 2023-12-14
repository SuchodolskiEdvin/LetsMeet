<template>
    <div class="card">
      <DataTable :value="testData" paginator :rows="5" :rowsPerPageOptions="[5, 10, 20, 50]" tableStyle="min-width: 50rem">
        <Column field="name" header="Name" style="width: 25%"></Column>
        <Column field="country" header="Country" style="width: 25%"></Column>
        <Column field="company" header="Company" style="width: 25%"></Column>
        <Column field="status" header="Representative" style="width: 25%"></Column>
      </DataTable>
    </div>
</template>

<script>
// import { ref, onMounted } from 'vue';
// import { CustomerService } from '@/service/CustomerService';
//
// onMounted(() => {
//   CustomerService.getCustomersMedium().then((data) => (customers.value = data));
// });
//
// const customers = ref();

import {auth} from "@/util/Auth";

export default {

  name: "MainView",

  methods: {

    logOut() {
      auth.logout(this);
    },

    edit() {
      this.$router.push("/edit-profile");
    },

    editItem (item) {
      this.editedIndex = this.meets.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialog = true
    },

    deleteItem (item) {
      this.editedIndex = this.meets.indexOf(item)
      this.editedItem = Object.assign({}, item)
      this.dialogDelete = true
    },

    deleteItemConfirm () {
    },

    close () {
      this.dialog = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    closeDelete () {
      this.dialogDelete = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    closeInfo () {
      this.dialogInfo = false
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem)
        this.editedIndex = -1
      })
    },

    save () {
    },

    getData () {
    },

    saveDate (date) {
      this.$refs.menu.save(date)
    },

    getInfo (item) {
      this.editedIndex = this.meets.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogInfo = true;
    }

  },

  created() {
    this.getData ();

  },

  computed: {
    formTitle () {
      return this.editedIndex === -1 ? 'New Meet' : 'Edit Meet'
    },
  },

  watch: {
    dialog (val) {
      val || this.close()
    },
    dialogDelete (val) {
      val || this.closeDelete()
    },
    menu (val) {
      val && setTimeout(() => (this.activePicker = 'YEAR'))
    },
  },

  data () {
    return {
      testData: [{
          name: "Odin",
          company: "Companyny",
          conutry: "Lithuania",
          status: "Active",
        }, {
        name: "Edvin",
        company: "Google",
        conutry: "Estonia",
        status: "Blocked",
      }, {
        name: "Martyna",
        company: "Goldman Suchs",
        conutry: "Poland",
        status: "Not active",
      }],
      e6: [],
      e7: [],
      dialog: false,
      dialogDelete: false,
      dialogInfo: false,
      meets: [],
      participants: [{
        name: '',
        surname: '',
        email: '',
      }],
      headers: [
        {
          text: 'Name',
          align: 'start',
          sortable: true,
          value: 'name',
        },
        { text: 'Creator\'s name', value: 'creatorName' },
        { text: 'Creator\'s surname', value: 'creatorSurname' },
        { text: 'Creation date', value: 'creationDate' },
        { text: 'Last modification date', value: 'modificationDate' },
        { text: 'Date', value: 'date' },
        { text: 'Time', value: 'time'},
        { text: 'Actions', value: 'actions', sortable: false },
      ],
      editedIndex: -1,
      editedItem: {
        id: '',
        name: '',
        date: '',
        time: '',
        participants: [],
      },
      defaultItem: {
        id: '',
        name: '',
        date: '',
        time: '',
        participants: [],
      },
      activePicker: null,
      menu: false,
      users: [{
        name: '',
        surname: '',
        email: '',
      }],
    }
  },
}
</script>

<style scoped>
.v-toolbar--prominent:not(.v-toolbar--bottom) .v-toolbar__title {
  align-self: flex-start;
}
</style>