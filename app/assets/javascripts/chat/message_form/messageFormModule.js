define(['angular'], function (angular) {

	var module = angular.module('MessageForm', [])
		.directive('messageForm', messageFormDirective)
		.directive('submitFormOnReturn', submitFormOnReturn);


	messageFormDirective.$inject = [];

	function messageFormDirective() {
		return {
			restrict: "E",
			replace: true,
			scope: {},
			templateUrl: 'assets/javascripts/chat/message_form/messageFormTpl.html',
			bindToController: true,
			controllerAs: "ctrl",
			controller: MessageFormController
		};
	}

	MessageFormController.$inject = ["$http"];

	function MessageFormController($http) {

		this.sendMessage = function(message){

		}

	}

	function submitFormOnReturn() {
		var RETURN_KEY = 13;

		function shouldHandleKeydownEvent(event) {
			return event.keyCode === RETURN_KEY && !event.shiftKey && !event.altKey && !event.ctrlKey && !event.metaKey;
		}

		return {
			restrict: "A",
			link: function ($scope, element) {
				element.on("keydown", function (event) {
					if (shouldHandleKeydownEvent(event)) {
						element.closest("form").triggerHandler("submit");
						return false;
					}
				});
			}
		};
	}

	submitFormOnReturn.$inject = [];

	return module;
});
