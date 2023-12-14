import 'primevue/resources/themes/lara-light-green/theme.css'

import { createApp } from 'vue'
import App from './App.vue'
import PrimeVue from "primevue/config";
import AutoComplete from "primevue/dropdown";
import Button from "primevue/button";
import Dialog from 'primevue/dialog';
import Dropdown from "primevue/dropdown";
import InputText from 'primevue/inputtext';
import Toast from 'primevue/toast';
import ToastService from 'primevue/toastservice';
import router from "@/routes/routes";
import axios from "axios";
import store from "@/store/store";
import VueAxios from "vue-axios";
import {setDomain, isTokenValidUsingGET as isTokenValid} from "./swagger/vue-api-client";

setDomain(process.env.VUE_APP_API_URL);

router.beforeEach((to, from, next) => {
	if (to.matched.some((record) => record.meta.requiresAuth)) {
		isTokenValid().then(() => {
			next();
		}, () => {
			localStorage.removeItem("token");
			next({name: "login", query: {next: to.fullPath}});
		});
	} else {
		next();
	}
});

const app = createApp(App);
app.component('AutoComplete', AutoComplete)
	.component('Button', Button)
	.component('Dialog', Dialog)
	.component('Dropdown', Dropdown)
	.component('InputText', InputText)
	.component('Toast', Toast);

app.use(PrimeVue)
	.use(ToastService)
	.use(router)
	.use(store)
	.use(VueAxios, axios);

// axios.defaults.baseURL = process.env.VUE_APP_API_URL;

app.axios.interceptors.request.use((config) => {
	if (localStorage.getItem("token") !== null) {
		config.headers.Authorization = "Bearer " + localStorage.getItem("token");
	}

	return config;
});

app.config.globalProperties.$axios = axios;

app.mount('#app');
