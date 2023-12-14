import axios from "axios";

export var auth = {
	login(context, credentials) {
		localStorage.removeItem("access_token");
		axios({
			method: "post",
			url: process.env.VUE_APP_API_URL + "/oauth/token?grant_type=password" +
				"&username=" + credentials.email +
				"&password=" + credentials.password,
			auth: {username: "my-trusted-client", password: "secret"},
			headers: {"Content-type": "application/x-www-form-urlencoded"},
		}).then(response => {
			localStorage.setItem("access_token", response.data.access_token);
			context.$router.push("/app")

		}).catch(() => {});
	},

	logout(context) {
		localStorage.removeItem("access_token");
		context.$router.push("/");
	},
};