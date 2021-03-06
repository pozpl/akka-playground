define(['angular', 'angular-route'], function(angular) {

	'use strict';

	var module = angular.module('app.chat', ['ngRoute']);

	module.config(['$routeProvider', function ($routeProvider) {
		$routeProvider.when('/', {
			templateUrl: 'assets/javascripts/chat/mainLayoutTpl.html',
			controller: 'MainController',
			controllerAs: "ctrl"
		});

		$routeProvider.otherwise({
			redirectTo: '/'
		});
		
	}]);

	module.controller('MainController', MainController);

	MainController.$inject = [];

	function MainController(){

		var vm = this;
		vm.recipient = null;

	}

	return module;
	
});
