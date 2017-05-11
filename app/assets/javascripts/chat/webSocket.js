define(['angular'], function (angular) {

	var module = angular.module('WebSocket', [])
		.factory('WebSocket', WebSocket);

	WebSocket.$inject = ['$rootScope'];

	function WebSocket($rootScope) {

		init();

		function init() {
			// var host = window.location.origin;
			// console.log("WEBSOCKET connecting to", host);
			//
			// this.socket = new WebSocket("ws://" + host + ":9000/socket");
			//
			// this.socket.on('connect', function () {
			//
			// });

		}

		function on(key, callback) {
			// this.socket.on(key, function (data) {
			// 	console.log("on", key, data)
			// 	$rootScope.$apply(function () {
			// 		callback(data)
			// 	});
			// });
		}

		return {
			on: on
		}
	}

	return module;
});