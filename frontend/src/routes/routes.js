import {createRouter, createWebHistory} from "vue-router";

import LoginView from "../components/LoginView.vue";
import MainView from "../components/MainView.vue";
import RegisterView from "../components/RegisterView.vue";
import EditProfile from "../components/EditProfileView.vue";

// Widoki
const routes = [
	{
		path: "/",
		name: "login",
		component: LoginView
	},
	{
		path: "/register",
		name: "register",
		component: RegisterView
	},
	{
		path: "/home",
		name: "home",
		component: MainView,
		meta: {
			requiresAuth: true,
		}
	},
	{
		path: "/user/edit",
		name: "userEdit",
		component: EditProfile,
		meta: {
			requiresAuth: true,
		}
	}
];


const router = createRouter({
	history: createWebHistory(process.env.BASE_URL),
	routes,
});

export default router;