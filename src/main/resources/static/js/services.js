var cwaServices = angular.module('cwaServices', ['ngResource']);


cwaServices.factory('Teams', ['$resource',
   function($resource) {
      return $resource('http://209.124.175.190:9080/teams', {}, {
         query: {method:'GET',  isArray:true},
         create: { method: 'POST' }
   })
}]);

cwaServices.factory('TeamDetails', ['$resource',
   function($resource) {
      return $resource('http://209.124.175.190:9080/team/:teamId', {}, {
         show: { method: 'GET' }
   });
}]);


cwaServices.factory('TeamUpdate', ['$resource',
   function($resource) {
      return $resource('http://209.124.175.190:9080/team/update/:teamId', {}, {
         show: { method: 'GET' },
         update: {method:'PUT', params: {teamId: '@teamId'}},
         delete: {method:'DELETE', params:{teamId:'@teamId'}}
   });
}]);

cwaServices.factory('Pairing', ['$resource',
  
   function($resource) {
      return $resource('http://209.124.175.190:9080/pairing',{}, {
         generate: {method:'POST', isArray: false}
   });
}]);

cwaServices.factory('Matrix', ['$resource',
	function($resource) {
	   return $resource('http://209.124.175.190:9080/matrix',{}, {
		   query: {method:'GET', isArray:true}
    });
}]);

cwaServices.factory('MatrixAgeClasses', ['$resource',
     function($resource) {
        return $resource('http://209.124.175.190:9080/matrixAgeClasses',{}, {
           query: {method:'GET', isArray:true}
     });
}]);


