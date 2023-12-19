/* eslint-disable */

export const ToastUtils = {

	async addToast(ctx, data, focus = true) {
		if (!data) {
			return;
		}

		data.life = 60 * 1000; // 1 minute in ms

		await ctx.$toast.add(data);

		const toasts = document.getElementsByClassName("p-toast-icon-close");
		if (toasts.length === 0) {
			return;
		}
		if (focus) {
			toasts[toasts.length - 1].focus();
		}
	},

	clearToasts(ctx) {
		ctx.$toast.removeAllGroups();
	}

};
