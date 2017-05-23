define(['angular', 'angular-route'], function(angular) {

	'use strict';

	var module = angular.module('sidebar', ['ngRoute']);
	module.directive('sidebar', sidebarDirective);

	

	SidebarController.$inject = [];

	function SidebarController() {

		// constructor() {
		// }
		//
		// currentUser() {
		// 	return Auth.getCurrentUser();
		// }
	}


	sidebarDirective.$inject = [];

	function sidebarDirective() {
		return {
			restrict: "E",
			replace: true,
			scope: {
				recipient: "="
			},
			templateUrl: 'assets/javascripts/chat/sidebar/sidebar.html',
			bindToController: true,
			controllerAs: "ctrl",
			controller: SidebarController
		};
	}


	return module;

});
