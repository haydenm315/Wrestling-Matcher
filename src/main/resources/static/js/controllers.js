var cwaControllers = angular.module('cwaControllers',[]);


cwaControllers.controller('TeamListCtrl', function ($scope, Teams, TeamUpdate, $location) {
        $scope.deleteTeam = function(teamId) {
           TeamUpdate.delete({teamId: teamId}, function success(){$location.path('/teams');});
        }

        $scope.updateTeam = function(teamId) {
           $location.path('/team/update/' + teamId);
        }
        
        $scope.viewTeam = function(teamId) {
        	$location.path('/team/' + teamId);
        }
        $scope.teams = Teams.query();
     });

cwaControllers.controller('TeamDetailsCtrl', function($scope, $routeParams, TeamDetails, $location) {

    $scope.team = TeamDetails.show({teamId: $routeParams.teamId},  function(team) {});

});

cwaControllers.controller('TeamUpdateCtrl', function($scope, $routeParams, TeamUpdate, $location) {

      $scope.team = TeamUpdate.show({teamId: $routeParams.teamId},  function(team) {});

      $scope.addPlayer = function() {
         if (!$scope.team.players)
            $scope.team.players = [];
         $scope.team.players.push({});
      }

      $scope.updateTeam = function() {
         TeamUpdate.update($scope.team, function success() { $location.path('/teams');});
      }

      $scope.cancel = function() {
         $location.path('/teams');
      }
      //$http.get('http://172.28.12.234:8080/team?teamId=' + $scope.teamId).success(function(data) {
      //$http.get('http://172.28.12.234:8080/team/' + $scope.teamId).success(function(data) {
      //$scope.team = data;

     $scope.removePlayer = function(index) {
        $scope.team.players.splice(index, 1);
     };
});

cwaControllers.controller('TeamCreateCtrl',  function($scope, Teams, $location) {
   $scope.createTeam = function() { 
      Teams.create($scope.team, function success(){$location.path('/teams');});
   }

   $scope.addPlayer = function() {
      if (!$scope.team)
         $scope.team = {};
         if (!$scope.team.players)
            $scope.team.players = [];
      $scope.team.players.push({});
   }

   $scope.removePlayer = function(index) {
      $scope.team.players.splice(index, 1);
    };
});

cwaControllers.controller('PairTeamsCtrl', function ($rootScope, $scope, Teams,Pairing, TeamDetails, $location) {
	$scope.selectedTeam1Players = {};
	$scope.selectedTeam2Players = {};
	    
	
	$scope.updateTeam1 = function(item)
	{
		$scope.team1Players = TeamDetails.show({teamId: item.id},  function(team) {});
	}
	
	$scope.updateTeam2 = function(item)
	{
		$scope.team2Players = TeamDetails.show({teamId: item.id},  function(team) {});
	}
	
    $scope.generatePairing = function() {
       console.log(this.team1);
       console.log(this.team2);
       $rootScope.team1Name = this.team1.teamName;
       $rootScope.team2Name = this.team2.teamName;
       console.log($scope.selectedTeam1Players);
       var selectedTeam1PlayerIds = [];
       var selectedTeam2PlayerIds = [];
       
       for (var keyName in $scope.selectedTeam1Players)
       {
    	   selectedTeam1PlayerIds.push(keyName);
       }
       
       for (var keyName in $scope.selectedTeam2Players)
       {
    	   selectedTeam2PlayerIds.push(keyName);
       }
       
       console.log($scope.selectedTeam2Players);
       var response = Pairing.generate({teamId1: this.team1.id, teamId2: this.team2.id,
          team1Players: selectedTeam1PlayerIds, team2Players: selectedTeam2PlayerIds },
          //success callback
          function(response){
        	  $rootScope.matches = response.pairings;
        	  $rootScope.noMatches1 = response.noMatches1;
        	  $rootScope.noMatches2 = response.noMatches2;
      	  }
       );
       
       $location.path('/pairing/showPairing');
    }
    
    $scope.checkAllTeam1 = function(players) {
       if ($scope.isAllSelectedTeam1)
       {
    	   $scope.isAllSelectedTeam1 = true;
       }
       else
       {
           $scope.isAllSelectedTeam1 = false;	   
       }
       angular.forEach(players, function (item) { $scope.selectedTeam1Players[item.id] = $scope.isAllSelectedTeam1});       
    }
    
    $scope.checkAllTeam2 = function(players) {
        if ($scope.isAllSelectedTeam2)
        {
     	   $scope.isAllSelectedTeam2 = true;
        }
        else
        {
            $scope.isAllSelectedTeam2 = false;	   
        }
        angular.forEach(players, function (item) { $scope.selectedTeam2Players[item.id] = $scope.isAllSelectedTeam2});       
     }
    
    $scope.getMatches = function(){
    	return $rootScope.matches;
    }
    
    $scope.getNoMatches1 = function(){
    	return $rootScope.noMatches1;
    }
    
    $scope.getNoMatches2 = function(){
    	return $rootScope.noMatches2;
    }
    
    
    $scope.teams = Teams.query();
 });

cwaControllers.controller('MatrixCtrl', function ($rootScope, $scope, Matrix, MatrixAgeClasses, $location) {
   $scope.getMatrix = function() {
	   $location.path('/matrix/showMatrix');
   }	
   $rootScope.matrixAges = MatrixAgeClasses.query();
   $rootScope.matrixClasses = Matrix.query();

});