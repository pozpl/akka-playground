define(['angular'], function(angular){
	var module = angular.module('Avatar', [])
		.directive('avatar', avatarDirective);

	avatarDirective.$inject = [];

	function avatarDirective() {
		var colorMapping = {};

		return {
			restrict: "E",
			replace: true,
			scope: {
				user: "="
			},
			templateUrl: 'assets/javascripts/chat/avatar/avatarTpl.html',
			link: function($scope) {

				// http://stackoverflow.com/questions/1484506/random-color-generator-in-javascript
				function randomColor() {
					var letters = '0123456789ABCDEF'.split('');
					var color = '#';
					for (var i = 0; i < 6; i++ ) {
						color += letters[Math.floor(Math.random() * 16)];
					}
					return color;
				}

				var unwatch = $scope.$watch("user", function(user) {
					console.log("user", user);

					if (user) {
						$scope.initials = (user.name[0] || "A");

						if (!colorMapping[user.id]) colorMapping[user.id] = randomColor();
						$scope.color    = colorMapping[user.id];

						unwatch();
					}
				});
			}
		};
	}

	return module;
});
