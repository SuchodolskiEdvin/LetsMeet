import {createRouter, createWebHistory} from "vue-router";
import LoginView from "../views/LoginView.vue";
import MainView from "../views/MainView.vue";
import RegisterView from "../views/RegisterView.vue";
import EditProfile from "../views/EditProfileView.vue";
import LayoutPublic from "@/views/layout/LayoutPublic.vue";
import LayoutSecured from "@/views/layout/LayoutSecured.vue";

// Widoki
const routes = [
	{
		component: LayoutPublic,
		path: "/",
		children: [
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
		],
	},
	{
		component: LayoutSecured,
		path: "/",
		meta: {
			requiresAuth: true,
		},
		children: [
			{
				path: "/home",
				name: "home",
				component: MainView
			},
			{
				path: "/user/edit",
				name: "userEdit",
				component: EditProfile
			}
		],
	},
];

const router = createRouter({
	history: createWebHistory(process.env.BASE_URL),
	routes,
});

export default router;