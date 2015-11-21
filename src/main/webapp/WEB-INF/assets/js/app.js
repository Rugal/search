/* global angular */
var SearchApp = angular.module('SearchApp', ['ui.bootstrap']);
SearchApp.config(['$httpProvider', function ($httpProvider) {
        $httpProvider.defaults.headers.common['Accept'] = 'application/json';
        $httpProvider.defaults.headers.common['Content-Type'] = 'application/json';
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
        $httpProvider.defaults.headers.common['Accept-Language'] = 'en, en-US';
    }]);


var SearchController = SearchApp.controller('SearchController', ['$http', '$scope', '$log',
    function ($http, $log, $scope) {
        var rest = this;
        rest.responsebody = [];
        $scope.send = function (query, force)
        {
            $http({method: 'GET', url: 'http://localhost:8080?q=' + query + '&c=' + force})
                    .then(
                            function (response)
                            {
                                rest.responsebody = response.data;
                            },
                            function (response)
                            {
                                $scope.open(response.data.message);
                            }
                    );
        };
    }]
        );