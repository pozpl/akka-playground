angular.module("ChatApp", ['ngRoute']);

angular.module("ChatApp").controller("ChatController", function($scope, $timeout){
	// connect to websockets endpoint of our server
	var ws = new WebSocket("ws://localhost:9000/socket");

	// binding model for the UI
	var chat = this;
	chat.messages = [];
	chat.currentMessage = "";
	chat.username = "";
	chat.receiver = "";

	// what happens when user enters message
	chat.sendMessage = function() {
		var text = chat.username + ": " + chat.currentMessage;
		chat.messages.push(text);

		// send it to the server through websockets
		//case class TextMessage(to: ChatCoordinate, message: String, messageType: String = TextMessage.MSG_TYPE) extends ClientMessages
		ws.send(JSON.stringify({
			to: {
				segment: "individual",
				target: chat.receiver
			},
			message: text,
			messageType: "text_message"
		}));

		chat.currentMessage = "";
	};

	chat.register = function(){
		ws.send(JSON.stringify({
			uid: chat.username,
			messageType: "login"
		}))
	};
	
	// what to do when we receive message from the webserver
	ws.onmessage = function(msg) {
		$timeout(function(){
			chat.messages.push(msg.data);
		});
		// $scope.$digest();
	};
});
