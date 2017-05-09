/**
 * The app module, as both AngularJS as well as RequireJS module.
 * Splitting an app in several Angular modules serves no real purpose in Angular 1.2.
 * (Hopefully this will change in the near future.)
 * Splitting it into several RequireJS modules allows async loading. We cannot take full advantage
 * of RequireJS and lazy-load stuff because the angular modules have their own dependency system.
 */
define([
	'angular',
	'angular-sanitize',
	'angular-route',
	'./chat/chatController'
], function (angular) {
	'use strict';

	// We must already declare most dependencies here (except for common), or the submodules' routes
	// will not be resolved
	var app = angular.module('app', [
		'ngSanitize' ,
		'ngRoute',
		'app.chat',
		'sidebar'
	]);


	// app.config(['$locationProvider', function ($locationProvider) {
	// 	if(window.history && history.pushState) {
	// 		$locationProvider.html5Mode(true);
	// 	}
	// }]);
	//
	//
	// app.config(['$httpProvider', function ($httpProvider) {
	// 	$httpProvider.defaults.headers.common = {
	// 		'X-Requested-With' : 'XMLHttpRequest'
	// 	};
	// }]);
	//
	// app.run(function($rootScope) {
	// 	$rootScope.$on('$routeChangeError', function(event, current, previous, rejection) {
	// 		console.log('failed to change route', event, current, previous, rejection);
	// 	});
	// });

	
	return app;
});
