var SearchApp = angular.module('SearchApp', ['ngSanitize']);
SearchApp.config(['$httpProvider', function ($httpProvider) {
        $httpProvider.defaults.headers.common['Accept'] = 'application/json';
        $httpProvider.defaults.headers.common['Content-Type'] = 'application/json';
        delete $httpProvider.defaults.headers.common['X-Requested-With'];
        $httpProvider.defaults.headers.common['Accept-Language'] = 'en, en-US';
    }]);


var SearchController = SearchApp.controller('SearchController', ['$http', '$scope', '$sce',
    function ($http, $scope, $sce)
    {
        var rest = this;
        var origin = null;
        rest.responsebody = [];
        $scope.send = function (force)
        {
            var query = $scope.keywords;
            if (query)
            {
                origin = query;
                console.log(query);
                $http({method: 'GET', url: 'http://localhost:8080?q=' + query + '&c=' + force})
                        .then(
                                function (response)
                                {
                                    console.log(response.data);
                                    rest.responsebody = response.data;
                                },
                                function (response)
                                {
                                    $scope.open(response.data.message);
                                }
                        );
            }
        };
        $scope.pressEnter = function (keyEvent) {
            if (keyEvent.which === 13)
            $scope.send(true);
        };
    }]);