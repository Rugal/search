function stringStartsWith(string, prefix) {
    return string.slice(0, prefix.length) === prefix;
}

var SearchApp = angular.module('SearchApp', ['ngSanitize', 'ngAnimate']);
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
        $scope.show = false;
        rest.responsebody = [];
        $scope.send = function (force)
        {
            var query = $scope.keywords;
            if (query)
            {
                console.log(query);
                $http({method: 'GET', url: 'http://localhost:8080?q=' + query + '&c=' + force})
                        .then(
                                function (response)
                                {
                                    rest.responsebody = response.data;
                                    if (stringStartsWith(response.data.message, 'Correct with'))
                                    {
                                        $scope.show = true;
                                    }
                                },
                                function (response)
                                {
                                    $scope.open(response.data.message);
                                }
                        );
            }
        };
        $scope.pressEnter = function (keyEvent) {
            $scope.show = false;
            if (keyEvent.which === 13)
                $scope.send(true);
        };
    }]);