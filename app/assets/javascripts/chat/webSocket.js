define(['angular'], function (angular) {

	var module = angular.module('WebSocket', [])
		.service('WebSocketService', WebSocketService);

	WebSocketService.$inject = ['$rootScope', '$location', '$timeout'];

	function WebSocketService($rootScope, $location, $timeout) {

		var vm = {};

		vm.socket = null;
		vm.callbacks = {};

		init();

		function init() {
			var webSocketAddress = "ws://" + $location.host() + ":9000/socket"

			console.log("WEBSOCKET connecting to", webSocketAddress);

			vm.socket = new WebSocket(webSocketAddress);

			vm.socket.onmessage = onmessage;

		}

		this.send = sendViaWs;
		this.on = on;

		function on(key, callback) {

			vm.callbacks[key] = callback;

		}

		function sendViaWs(message, callback) {
			waitForConnection(function () {
				vm.socket.send(message);
				if (typeof callback !== 'undefined') {
					callback();
				}
			}, 1000);
		}

		function waitForConnection(callback, interval) {
			if (vm.socket.readyState === 1) {
				callback();
			} else {
				$timeout(function () {
					waitForConnection(callback, interval);
				}, interval);
			}
		}

		function onmessage(msg) {
			$timeout(function () {
				if (msg && msg.data) {
					var parsedData = JSON.parse(msg.data);

					if(vm.callbacks[parsedData.messageType] != null){
						vm.callbacks[parsedData.messageType](parsedData)
					}
				}

			});
		}
	}

	return module;
});