<template>
  <div class="card">
    <Toolbar class="mb-4">
      <template #end>
        <Button label="Dodaj" icon="pi pi-plus" severity="success" class="mr-2" @click="createMeet"/>
      </template>
    </Toolbar>

    <DataTable :value="meets" paginator stripedRows :rows="5" :rowsPerPageOptions="[5, 10, 20, 50]"
        lazy tableStyle="min-width: 50rem" v-model:rows="searchCriteria.page.limit"
        v-model:first="searchCriteria.page.offset" :total-records="totalRecords"
        @page="onPage" @sort="onPage">
      <Column v-for="col of columns" :key="col.value" :field="col.value" :header="col.text"/>
      <Column style="min-width: 8rem">
        <template #body="slotProps">
          <Button icon="pi pi-info-circle" rounded severity="info" class="mr-2" @click="getMeetInfo(slotProps.data)"/>
          <Button icon="pi pi-pencil" rounded class="mr-2" @click="editMeet(slotProps.data)" :disabled="isCreator(slotProps.data)"/>
          <Button icon="pi pi-trash" rounded severity="danger" @click="deleteMeet(slotProps.data)"/>
        </template>
      </Column>
    </DataTable>

    <Dialog v-model:visible="showAdditionalInformationMeetDialog" :style="{width: '450px'}"
        header="Informacja o spotkaniu" :modal="true">
      <div>
        <h2>Lista uczestników</h2>
        <ul>
          <li v-for="participant in this.editedMeet.participants" :key="participant.id">
            {{ participant.fullName }} (email: {{ participant.email }})
          </li>
        </ul>
      </div>
      <div>
        <h2>Link do spotkania ZOOM</h2>
        <ul>
          <a :href="this.editedMeet.zoomUrlJoinLink" v-text="this.editedMeet.zoomUrlJoinLink" />
        </ul>
      </div>
      <template #footer>
        <Button label="OK" icon="pi pi-check" text @click="closeShowAdditionalInformationMeetDialog"/>
      </template>
    </Dialog>

    <Dialog v-model:visible="createOrEditMeetDialog" :style="{width: '550px'}" header="Dane spotkania" :modal="true"
        class="p-fluid">
      <VeeForm class="mb-5">
        <div class="field flex-auto">
          <label for="name" class="mb-2">Nazwa</label>
          <InputText id="name" v-model.trim="editedMeet.name" required="true" autofocus/>
        </div>
        <div class="field flex-auto">
          <label for="date" class="mb-2">Data</label>
          <Calendar id="date" v-model="editedMeet.date" :minDate="new Date()"
              aria-describedby="date-error"/>
        </div>
        <div class="field flex-auto grid">
            <div class="col-6">
              <label for="timeStart">Godzina rozpoczęcia</label>
              <Dropdown v-model="editedMeet.timeStart" :options="time"
                  placeholder="HH:MM" class="mt-2"/>
            </div>
            <div class="col-6">
              <label for="duration">Czas trwania</label>
              <Dropdown v-model="editedMeet.duration" :options="duration"
                  placeholder="HH:MM" class="mt-2"/>
            </div>
        </div>
        <div class="field flex-auto">
          <label for="participants" class="mb-2">Uczestnicy spotkania</label>
          <MultiSelect v-model="editedMeet.participants" :options="participants" optionLabel="email" filter placeholder="Wybierz uczestników"
              :maxSelectedLabels="3" class="w-full" />
        </div>
        <div class="flex-auto">
          <Checkbox v-model="editedMeet.isOnline" :binary="true" />
          <label for="isOnline" class="mx-2">Utworzyć spotkanie online (ZOOM)</label>
        </div>
      </VeeForm>

      <template #footer>
        <Button label="Anuluj" icon="pi pi-times" text @click="closeCreateOrEditMeetDialog"/>
        <Button label="Zapisz" icon="pi pi-check" text @click="saveMeet"/>
      </template>
    </Dialog>

    <Dialog v-model:visible="deleteMeetDialog" :style="{width: '450px'}" header="Uwaga" :modal="true">
      <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem"/>
        <span v-if="editedMeet">Czy na pewno chcesz usunąć spotkanie <b>{{ editedMeet.name }}</b>?</span>
      </div>
      <template #footer>
        <Button label="Nie" icon="pi pi-times" text @click="closeDeleteMeetDialog"/>
        <Button label="Tak" icon="pi pi-check" text @click="deleteItemConfirm"/>
      </template>
    </Dialog>
  </div>
</template>

<script>
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import Toolbar from "primevue/toolbar";
import Button from "primevue/button";
import MultiSelect from "primevue/multiselect";
import Calendar from "primevue/calendar";
import Checkbox from 'primevue/checkbox';
import Dropdown from 'primevue/dropdown';
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
  components: {DataTable, Column, Toolbar, Button, MultiSelect, Calendar, Dropdown, Checkbox, VeeForm},

  data() {
    return {
      columns: [
        {text: 'Nazwa', align: 'start', sortable: true, value: 'name'},
        {text: 'Twórca', sortable: true, value: 'creator.fullName'},
        {text: 'Data', sortable: true, value: 'date'},
        {text: 'Godzina rozpoczęcia', value: 'timeStart'},
        {text: 'Czas trwania', value: 'duration'},
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
        timeStart: '',
        duration: '',
        participants: [],
        isOnline: false,
      },
      participants: [],
      loggedUsersId: localStorage.getItem("id"),
      test: ['test1', 'test2'],
      time: ['12:00am', '12:15am', '12:30am', '12:45am', '01:00am', '01:15am', '01:30am', '01:45am',
        '02:00am', '02:15am', '02:30am', '02:45am', '03:00am', '03:15am', '03:30am', '03:45am',
        '04:00am', '04:15am', '04:30am', '04:45am', '05:00am', '05:15am', '05:30am', '05:45am',
        '06:00am', '06:15am', '06:30am', '06:45am', '07:00am', '07:15am', '07:30am', '07:45am',
        '08:00am', '08:15am', '08:30am', '08:45am', '09:00am', '09:15am', '09:30am', '09:45am',
        '10:00am', '10:15am', '10:30am', '10:45am', '11:00am', '11:15am', '11:30am', '11:45am',
        '12:00pm', '12:15pm', '12:30pm', '12:45pm', '01:00pm', '01:15pm', '01:30pm', '01:45pm',
        '02:00pm', '02:15pm', '02:30pm', '02:45pm', '03:00pm', '03:15pm', '03:30pm', '03:45pm',
        '04:00pm', '04:15pm', '04:30pm', '04:45pm', '05:00pm', '05:15pm', '05:30pm', '05:45pm',
        '06:00pm', '06:15pm', '06:30pm', '06:45pm', '07:00pm', '07:15pm', '07:30pm', '07:45pm',
        '08:00pm', '08:15pm', '08:30pm', '08:45pm', '09:00pm', '09:15pm', '09:30pm', '09:45pm',
        '10:00pm', '10:15pm', '10:30pm', '10:45pm', '11:00pm', '11:15pm', '11:30pm', '11:45pm'],
      duration: ['00:15', '00:30', '00:45', '01:00', '01:15', '01:30', '01:45', '02:00',
        '02:15', '02:30', '02:45', '03:00', '03:15', '03:30', '03:45', '04:00',
        '04:15', '04:30', '04:45', '05:00', '05:15', '05:30', '05:45', '06:00',
        '06:15', '06:30', '06:45', '07:00', '07:15', '07:30', '07:45', '08:00'],
    }
  },

  methods: {
    getAllParticipants() {
      getUsers()
          .then((response) => {
            this.participants = response.data.filter(participant => participant.id != this.loggedUsersId);
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
      getParticipants({id: this.editedMeet.id}).then((response) => {
        this.editedMeet.participants = response.data;
      }).catch((error) => {
        console.log(error);
      })
      this.showAdditionalInformationMeetDialog = true;
    },

    editMeet(item) {
      this.editedMeetIndex = this.meets.indexOf(item);
      this.editedMeet = Object.assign({}, item);
      this.editedMeet.participants = this.participants.filter(item => this.editedMeet.participantsId.includes(item.id));
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
      // Konwertowanie daty
      if (typeof this.editedMeet.date === 'object') {
        this.editedMeet.date = `${this.editedMeet.date.getFullYear()}-${(this.editedMeet.date.getMonth() + 1).toString().padStart(2, '0')}-${this.editedMeet.date.getDate().toString().padStart(2, '0')}`;
      }

      // Wybranie godziny z daty
      if (typeof this.editedMeet.time === "object") {
        const hours = this.editedMeet.time.getHours().toString();
        const minutes = this.editedMeet.time.getMinutes().toString();
        this.editedMeet.time = hours + ":" + minutes;
      }

      createOrEditMeet({meetDto: this.editedMeet}).then(() => {
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
        timeStart: "",
        duration: "",
        participants: [],
        isOnline: false,
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

    isCreator(item) {
      return this.loggedUsersId != item.creator.id;
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

}
</script>

<style scoped>
</style>
