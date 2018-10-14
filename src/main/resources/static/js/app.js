var cwaApp = angular.module('cwaApp',[
		'ngRoute',
		'cwaControllers',
        'cwaServices',
        'datetime'
                
		]);

cwaApp.config(['$routeProvider',
		function($routeProvider) {
			$routeProvider.
				when('/teams', {
				   templateUrl: 'partials/team-list.html',
				   controller: 'TeamListCtrl'
				}).
                when('/teams/createTeam', {
                   templateUrl: 'partials/team-create.html',
                   controller: 'TeamCreateCtrl'
                }).
                when('/team/update/:teamId', {
                   templateUrl: 'partials/team-update.html',
                   controller: 'TeamUpdateCtrl'
                }).
                when('/team/:teamId', {
                    templateUrl: 'partials/team-details.html',
                    controller: 'TeamDetailsCtrl'
                 }).
                when('/match/createMatch', {
                   templateUrl: 'partials/pairing-team-selection.html',
                   controller: 'PairTeamsCtrl'
                }).
                when('/pairing/showPairing', {
                   templateUrl: 'partials/pairing-show.html',
                   controller: 'PairTeamsCtrl'
                }).
                when('/matrix/showMatrix', {
                   templateUrl: 'partials/matrix-show.html'
                }).
                
				otherwise({
					redirectTo: '/teams'
				});
		}]);


angular.module('cwaApp')
.directive("formatDate", function(){
  return {
   require: 'ngModel',
    link: function(scope, elem, attr, modelCtrl) {
      modelCtrl.$formatters.push(function(modelValue){
        return new Date(modelValue);
      })
    }
  }
})