define(['angular', 'angular-route', 'moment',
'chat/messages/messagesListController'], function (angular, angularRote, moment, MessageListController) {

	'use strict';

	var module = angular.module('MessageList', [])
		.directive('messageList', messageListDirective)
		.directive('formattedMessage', formattedMessage)
		.filter('relativeDate', relativeDateFilter);


	/**
	 * We use MutationObserver which listens on child node add/remove changes
	 * to scroll to the bottom of the list if new messages are added.
	 *
	 * Additionally, if the user scrolled to a differend position of the list
	 * we won't scroll to the bottom.
	 **/
	messageListDirective.$inject = [];

	function messageListDirective() {
		return {
			restrict: "E",
			replace: true,
			templateUrl: 'assets/javascripts/chat/messages/messageListTpl.html',
			scope: {},
			bindToController: true,
			controllerAs: "ctrl",
			controller: MessageListController,
			link: function ($scope, element) {

				var alreadyAtBottom = true;

				function scrollToBottom() {
					if (alreadyAtBottom) {
						element.scrollTop(element.prop("scrollHeight"));
					}
				}

				function isAtBottom() {
					var scrollTop = element.scrollTop();
					var maxHeight = element.prop("scrollHeight") - element.prop("clientHeight");

					return scrollTop >= maxHeight;
				}

				// https://developer.mozilla.org/en/docs/Web/API/MutationObserver
				var observer = new window.MutationObserver(scrollToBottom);

				observer.observe(element[0], {childList: true});

				var throttledOnScrollHandler = _.throttle(function () {
					alreadyAtBottom = isAtBottom();
				}, 250);

				element.on("scroll", throttledOnScrollHandler);

				$scope.$on("$destroy", function () {
					element.off("scroll", throttledOnScrollHandler);
					observer.disconnect();
				});
			}
		};
	}


	formattedMessage.$inject = [];

	function formattedMessage() {
		return {
			restrict: "A",
			scope: {
				"formattedMessage" : "="
			},
			link: function($scope, element, attrs) {
				// var unwatch = $scope.$watch("formattedMessage", function(str) {
				// 	if (str) {
				// 		str = FormatMessageService.breakNewLine(str);
				// 		str = FormatMessageService.autoLink(str);
				// 		element.html(str);
				//
				// 		unwatch();
				// 	}
				// });
			}
		};
	}



	function relativeDateFilter() {
		return function(dateString) {
			return moment(dateString).fromNow();
		};
	}

	


	return module;

});
