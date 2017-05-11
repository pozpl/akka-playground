define([], function () {

	MessageListController.$inject = ["WebSocket"];

	function MessageListController(WebSocket) {

		constructor();
		function constructor() {
			this.messages = [];

			register();
		}

		function register() {
			WebSocket.on('incoming_message', function(data){
				handleIncomingMessage(data);
			});

			WebSocket.on('new_connection', function(data){
				handleNewConnection(data);
			});

			WebSocket.on('user_disconnected', function(data){
				handleUserDisconnected(data);
			});
		}

		function handleIncomingMessage(data) {
		}

		function handleUserDisconnected(data) {

		}

		function handleNewConnection(data) {

		}

	}

	return MessageListController;

});

