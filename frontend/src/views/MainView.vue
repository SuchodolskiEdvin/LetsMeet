<template>
  <div class="card">
    <Toolbar class="mb-4">
      <template #end>
        <Button label="New" icon="pi pi-plus" severity="success" class="mr-2" @click="createMeet"/>
      </template>
    </Toolbar>

    <DataTable :value="meets" paginator stripedRows :rows="5" :rowsPerPageOptions="[5, 10, 20, 50]"
        lazy tableStyle="min-width: 50rem" v-model:rows="searchCriteria.page.limit"
        v-model:first="searchCriteria.page.offset" :total-records="totalRecords"
        @page="onPage" @sort="onPage">
      <Column v-for="col of columns" :key="col.value" :field="col.value" :header="col.text"/>
      <Column style="min-width: 8rem" header="Actions">
        <template #body="slotProps">
          <Button icon="pi pi-info-circle" rounded severity="info" class="mr-2" @click="getMeetInfo(slotProps.data)"/>
          <Button icon="pi pi-pencil" rounded class="mr-2" @click="editMeet(slotProps.data)"/>
          <Button icon="pi pi-trash" rounded severity="danger" @click="deleteMeet(slotProps.data)"/>
        </template>
      </Column>
    </DataTable>

    <Dialog v-model:visible="showAdditionalInformationMeetDialog" :style="{width: '450px'}"
        header="Informacja o spotkaniu" :modal="true">
      <div class="confirmation-content">
        <h2>Lista uczestników</h2>
        <ul>
          <li v-for="participant in this.editedMeet.participants" :key="participant.id">
            {{ participant.fullName }} (email: {{ participant.email }})
          </li>
        </ul>
      </div>
      <template #footer>
        <Button label="OK" icon="pi pi-check" text @click="closeShowAdditionalInformationMeetDialog"/>
      </template>
    </Dialog>

    <Dialog v-model:visible="createOrEditMeetDialog" :style="{width: '450px'}" header="Product Details" :modal="true"
        class="p-fluid">
      <VeeForm>
        <div class="field">
          <label for="name">Nazwa</label>
          <InputText id="name" v-model.trim="editedMeet.name" required="true" autofocus/>
        </div>
        <div class="field flex-auto">
          <label for="date">Data i godzina spotkania</label>
          <Calendar id="date" v-model="editedMeet.date" :class="{ 'p-invalid': errorMessage }"
              aria-describedby="date-error"/>
        </div>
        <div class="field flex-auto">
          <label for="time" class="block mb-2">Czas</label>
          <Calendar id="time" v-model="editedMeet.time" timeOnly/>
        </div>
      </VeeForm>

      <div class="field">
        <label for="participants" class="mb-3">Uczestnicy spotkania</label>
        <Dropdown id="participants" v-model="participants" :options="statuses" optionLabel="label"
            placeholder="Add participants">
          <!--          <template #value="slotProps">-->
          <!--            <div v-if="slotProps.value && slotProps.value.value">-->
          <!--              <Tag :value="slotProps.value.value" :severity="getStatusLabel(slotProps.value.label)"/>-->
          <!--            </div>-->
          <!--            <div v-else-if="slotProps.value && !slotProps.value.value">-->
          <!--              <Tag :value="slotProps.value" :severity="getStatusLabel(slotProps.value)"/>-->
          <!--            </div>-->
          <!--            <span v-else>-->
          <!--							{{ slotProps.placeholder }}-->
          <!--						</span>-->
          <!--          </template>-->
        </Dropdown>
      </div>

      <template #footer>
        <Button label="Anuluj" icon="pi pi-times" text @click="closeCreateOrEditMeetDialog"/>
        <Button label="Zapisz" icon="pi pi-check" text @click="saveMeet"/>
      </template>
    </Dialog>

    <Dialog v-model:visible="deleteMeetDialog" :style="{width: '450px'}" header="Confirm" :modal="true">
      <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem"/>
        <span v-if="editedMeet">Czy na pewno chcesz usunąć spotkanie <b>{{ editedMeet.name }}</b>?</span>
      </div>
      <template #footer>
        <Button label="Nie" icon="pi pi-times" text @click="deleteItemConfirm"/>
        <Button label="Tak" icon="pi pi-check" text @click="deleteMeet"/>
      </template>
    </Dialog>
  </div>
</template>

<script>
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import Toolbar from "primevue/toolbar";
import Button from "primevue/button";
import Dropdown from "primevue/dropdown";
import Calendar from "primevue/calendar";
import {Form as VeeForm} from "vee-validate";
import {
  getUsersUsingGET as getUsers,
  searchMeetCountUsingPOST as searchMeetCount,
  searchMeetUsingPOST as searchMeets,
  createOrUpdateMeetUsingPOST as createOrEditMeet,
  deleteMeetUsingDELETE as deleteMeet,
  getParticipantsUsingGET as getParticipants,
} from "@/swagger/vue-api-client"
import {ToastUtils} from "@/util/ToastUtils";

export default {
  name: "MainView",
  components: {DataTable, Column, Toolbar, Button, Dropdown, Calendar, VeeForm},

  data() {
    return {
      columns: [
        {text: 'Name', align: 'start', sortable: true, value: 'name',},
        {text: 'Creator', value: 'creatorsFullName'},
        {text: 'Creation date', value: 'creationDate'},
        {text: 'Last modification date', value: 'modificationDate'},
        {text: 'Date', value: 'date'},
        {text: 'Time', value: 'time'},
      ],
      meets: [],
      totalRecords: 0,
      showAdditionalInformationMeetDialog: false,
      createOrEditMeetDialog: false,
      deleteMeetDialog: false,
      searchCriteria: {
        page: {
          limit: 10,
          offset: 0,
          sortField: null,
          sortOrder: null,
        },
      },
      editedMeetIndex: -1,
      editedMeet: {
        id: '',
        name: '',
        date: '',
        time: '',
        participants: [],
      },
      participants: [],
    }
  },

  methods: {
    getAllParticipants() {
      getUsers()
          .then((response) => {
            this.participants = response.data;
          })
          .catch((error) => {
            console.log(error);
          });
    },

    createMeet() {
      this.createOrEditMeetDialog = true;
      this.editedMeet = this.clearEditedMeet();
    },

    getMeetInfo(item) {
      this.editedMeetIndex = this.meets.indexOf(item);
      this.editedMeet = Object.assign({}, item);
      getParticipants({id :this.editedMeet.id}).then((response) => {
        this.editedMeet.participants = response.data;
      }).catch((error) => {
        console.log(error);
      })
      this.showAdditionalInformationMeetDialog = true;
    },

    editMeet(item) {
      this.editedMeetIndex = this.meets.indexOf(item);
      this.editedMeet = Object.assign({}, item);
      this.createOrEditMeetDialog = true;
    },

    deleteMeet(item) {
      this.editedIndex = this.meets.indexOf(item);
      this.editedMeet = Object.assign({}, item);
      this.deleteMeetDialog = true;
    },

    deleteItemConfirm() {
      deleteMeet({id: this.editedMeet.id}).then(() => {
        this.search();
        this.closeDeleteMeetDialog();
      }).catch((error) => {
        console.log(error)
      });
    },

    closeShowAdditionalInformationMeetDialog() {
      this.editedMeet = Object.assign({}, this.clearEditedMeet())
      this.editedIndex = -1
      this.showAdditionalInformationMeetDialog = false;
    },

    closeCreateOrEditMeetDialog() {
      this.editedMeet = Object.assign({}, this.clearEditedMeet())
      this.editedIndex = -1
      this.createOrEditMeetDialog = false;
    },

    closeDeleteMeetDialog() {
      this.editedMeet = Object.assign({}, this.clearEditedMeet())
      this.editedIndex = -1
      this.deleteMeetDialog = false;
    },

    saveMeet() {
      createOrEditMeet({meetDTO: this.editedMeet}).then(() => {
        const detailMessage = this.editedIndex > -1 ? "Zapisano dane spotkania" : "Utworzono nowe spotkanie";
        this.createOrEditMeetDialog = false;
        this.search();
        ToastUtils.addToast(this, {
          severity: "success",
          summary: "Sukces",
          detail: detailMessage,
        });
      }).catch((error) => {
            console.log(error)
          }
      );

      this.closeCreateOrEditMeetDialog();
    },

    clearEditedMeet() {
      return {
        id: null,
        name: "",
        date: "",
        time: ""
      }
    },

    search() {
      this.onPage(this.getFirstPage());
      this.updateTotalRecords();
    },

    onPage(event) {
      this.searchCriteria.page.offset = event.first;
      this.searchCriteria.page.limit = event.rows;
      this.searchCriteria.page.sortField = event.sortField;
      this.searchCriteria.page.sortOrder = event.sortOrder;

      searchMeets({searchCriteria: this.searchCriteria}).then((response) => {
        this.meets = response.data;
      });
    },

    updateTotalRecords() {
      searchMeetCount({searchCriteria: this.searchCriteria}).then((response) => {
        this.totalRecords = response.data;
      });
    },

    getFirstPage() {
      return {
        first: this.searchCriteria.page.offset,
        rows: this.searchCriteria.page.limit,
      };
    },
  },

  created() {
    this.search();
    this.getAllParticipants();
  },

  computed: {
    formTitle() {
      return this.editedIndex === -1 ? 'New Meet' : 'Edit Meet'
    },
  },

  watch: {
    // dialog(val) {
    //   val || this.close()
    // },
    // dialogDelete(val) {
    //   val || this.closeDelete()
    // },
    // menu(val) {
    //   val && setTimeout(() => (this.activePicker = 'YEAR'))
    // },
  },

}
</script>

<style scoped>
</style>