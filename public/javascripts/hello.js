angular.module("ChatApp", ['ngRoute', 'ngCookies']);

angular.module("ChatApp").controller("ChatController",
	function ($scope, $timeout, $location, $cookies) {

		// binding model for the UI
		var chat = this;
		chat.ws = null;
		chat.messages = [];
		chat.currentMessage = "";
		chat.username = "";
		chat.receiver = "";

		chat.register = register;

		init();

		function init() {
			// connect to websockets endpoint of our server
			chat.ws = new WebSocket("ws://" + $location.host() + ":9000/socket");

			// what to do when we receive message from the webserver
			chat.ws.onmessage = onmessage;

			chat.username = $cookies.get('username') ? $cookies.get('username') : "";
			if (chat.username && chat.username != "") {
				chat.register();
			}
		}

		// what happens when user enters message
		chat.sendMessage = function () {
			var text = chat.currentMessage;
			chat.messages.push(text);

			// send it to the server through websockets
			//case class TextMessage(to: ChatCoordinate, message: String, messageType: String = TextMessage.MSG_TYPE) extends ClientMessages
			sendViaWs(JSON.stringify({
				to: {
					segment: "individual",
					target: chat.receiver
				},
				message: text,
				messageType: "text_message"
			}));

			chat.currentMessage = "";
		};

		function register() {
			sendViaWs(JSON.stringify({
				uid: chat.username,
				messageType: "login"
			}));

			$cookies.put("username", chat.username);
		}


		function onmessage (msg) {
			$timeout(function () {
				if(msg && msg.data){
					var parsedData = JSON.parse(msg.data);
					chat.messages.push(parsedData.from + ":" + parsedData.message);
				}

			});
		}

		function sendViaWs(message, callback) {
			waitForConnection(function () {
				chat.ws.send(message);
				if (typeof callback !== 'undefined') {
					callback();
				}
			}, 1000);
		}

		function waitForConnection(callback, interval) {
			if (chat.ws.readyState === 1) {
				callback();
			} else {
				$timeout(function () {
					waitForConnection(callback, interval);
				}, interval);
			}
		}
	});
