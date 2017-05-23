define([], function () {

	MessageListController.$inject = ["WebSocketService"];

	function MessageListController(WebSocketService) {

		var vm = this;

		constructor();
		function constructor() {
			vm.messages = [];

			register();
		}

		function register() {
			WebSocketService.on('outbound_text_message', function(data){
				handleIncomingMessage(data);
			});

			WebSocketService.on('new_connection', function(data){
				handleNewConnection(data);
			});

			WebSocketService.on('user_disconnected', function(data){
				handleUserDisconnected(data);
			});
		}

		function handleIncomingMessage(data) {
			vm.messages.push({ message: data.message, user: data.from, created_at: data.created_at, type: "message" });
		}

		function handleUserDisconnected(data) {

		}

		function handleNewConnection(data) {

		}

	}

	return MessageListController;

});

