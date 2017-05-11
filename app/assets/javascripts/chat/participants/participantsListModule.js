define(['angular', 'angular-route'], function (angular) {

	'use strict';

	var module = angular.module('ParticipantsList', [])
		.directive('participantsList', participantsListDirective);


	ParticipantsListController.$inject = [];

	function ParticipantsListController(WebSocket) {

		var vm = this;

		init();

		function init() {
			vm.participants = [];
			vm.WebSocket = WebSocket;

			register();
		}


		function register() {

		}

		

	}


	participantsListDirective.$inject = [];

	function participantsListDirective() {
		return {
			restrict: "E",
			replace: true,
			templateUrl: 'assets/javascripts/chat/participants/participantsListTpl.html',
			scope: true,
			bindToController: true,
			controllerAs: "ctrl",
			controller: ParticipantsListController
		};
	}

	return module;

});
