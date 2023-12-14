const path = require("path");
const fs = require("fs");
const swaggerGen = require("swagger-vue")

module.exports = function (grunt) {
	grunt.initConfig({
		pkg: grunt.file.readJSON("package.json"),
		"swagger-vue-codegen": {
			options: {
				swagger: "<%= pkg.swagger.definition %>",
				className: "<%= pkg.swagger.className %>",
				moduleName: "vue-<%= pkg.swagger.moduleName %>",
				dest: "src/swagger",
			},
			dist: {

			},
		},
	});

	grunt.registerMultiTask("swagger-vue-codegen", function () {
		const callback = this.async();
		const opt = this.options();
		const distFileName = path.join(opt.dest, opt.moduleName + ".js");

		fs.readFile(opt.swagger, (err, data) => {
			if (err) {
				grunt.log.error(err.toString());
				callback(false);
			} else {
				const parsedData = JSON.parse(data);

				const codeResult = swaggerGen({
					swagger: parsedData,
					moduleName: opt.moduleName,
					className: opt.className,
				});

				fs.writeFile(distFileName, codeResult, (err1) => {
					if (err1) {
						grunt.log.error(err1.toString());
						callback(false);
					} else {
						grunt.log.writeln("Generated " + distFileName + " from " + opt.swagger);
					}
				});
			}
		});
	});

	grunt.registerTask("vue", ["swagger-vue-codegen"]);

	grunt.loadNpmTasks('grunt-vue');

	// grunt.registerTask('default', ['vue:generateApiClient']);
};
