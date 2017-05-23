define(['angular', 'angular-route'], function (angular) {

	'use strict';

	var module = angular.module('ParticipantsList', [])
		.directive('participantsList', participantsListDirective)
		.service("UserSubscriptionsService", UserSubscriptionsService);


	ParticipantsListController.$inject = ['WebSocketService', 'UserSubscriptionsService'];

	function ParticipantsListController(WebSocketService, UserSubscriptionsService) {

		var vm = this;

		vm.recipient = null;

		vm.selectDialog = selectDialog;

		init();

		function init() {
			vm.participants = [];
			vm.WebSocket = WebSocketService;

			UserSubscriptionsService.listIndividual().then(function (message) {
				if (message != null) {
					vm.participants = message.data;
				}
			});

			register();
		}


		function register() {

		}

		function selectDialog(user){
			if(user != null){
				vm.recipient = user;
			}
		}

	}


	participantsListDirective.$inject = [];

	function participantsListDirective() {
		return {
			restrict: "E",
			replace: true,
			templateUrl: 'assets/javascripts/chat/participants/participantsListTpl.html',
			scope: {
				recipient: "="
			},
			bindToController: true,
			controllerAs: "ctrl",
			controller: ParticipantsListController
		};
	}


	UserSubscriptionsService.$inject = ['$http'];

	function UserSubscriptionsService($http) {

		this.listIndividual = function () {
			return $http.get("/user/subscriptions/list");
		};

		this.subscribeToIndividual = function (user) {
			return $http.post("/user/subscriptions/subscribe", user);
		};

		this.unsubscribe = function (user) {
			return $http({
				method: 'DELETE',
				url: "/user/subscriptions/unsubscribe",
				params: {
					user: user.userId
				}
			});
		}

	}

	return module;

});
