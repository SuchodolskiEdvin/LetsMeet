import {createRouter, createWebHistory} from "vue-router";
import LoginView from "../views/LoginView.vue";
import MainView from "../views/MainView.vue";
import RegisterView from "../views/RegisterView.vue";
import EditProfile from "../views/EditProfileView.vue";

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