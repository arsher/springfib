(function () {

'use strict';


  angular.module('FibonacciApp', ['ngRoute'])

  .config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
      $locationProvider.hashPrefix('!');
      // routes
      $routeProvider
        .when("/login", {
          templateUrl: "./views/login.html",
          controller: "LoginController"
        })
        .when("/", {
          templateUrl: "./views/fibonacci.html",
          controller: "FibonacciController"
        })
        .otherwise({
           redirectTo: '/login'
        });
    }
  ])
  .run(['$rootScope', '$location', 'AuthService',
    function ($rootScope, $location, AuthService) {
        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            if ($location.path() !== '/login' && !AuthService.isAuthenticated()) {
                $location.path('/login');
            }
        });
    }]);

  angular.module('FibonacciApp').factory('AuthService', ['$rootScope', '$http',
    function(rootScope, http){
        var srv = {};

        srv.isAuthenticated = function() {
            return rootScope.authenticated;
        };

        srv.login = function(username, password) {
            return http({
              method: 'POST',
              url: '/login',
              data: $.param({username: username, password: password}),
              headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).then(function() {
                rootScope.authenticated = true;
            });
        };

        return srv;
  }]);

  angular.module('FibonacciApp').factory('FibonacciService', ['AuthService', '$http',
      function(rootScope, http){
          var srv = {};

          srv.generate = function(n) {
              return http({
                method:'GET',
                url: '/fibonacci',
                params: {number: n}
              }).then(function(res) {
                return res.data;
              }, function(res) {
                throw res.data;
              });
          };

          return srv;
    }]);

  angular.module('FibonacciApp').controller('LoginController', [
    '$scope', 'AuthService', '$location',
    function($scope, AuthService, $location) {
      $scope.login = function() {
        $scope.busy = true;
        AuthService.login($scope.user, $scope.pass)
            .then(function() {
                $scope.busy = false;
                $location.path('/');
            }, function() {
                $scope.error = 'Invalid username or password';
                $scope.busy = false;
            });
      };
    }
  ]);

  angular.module('FibonacciApp').controller('FibonacciController', [
      '$scope', 'FibonacciService', '$location',
      function($scope, FibonacciService, $location) {
        $scope.generate = function() {
          $scope.busy = true;
          FibonacciService.generate($scope.number)
            .then(function(data) {
                $scope.result = data;
                $scope.busy = false;
            }, function(error) {
                $scope.error = error.message;
                $scope.busy = false;
            });
        };
      }
    ]);
}());