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
        var final_transcript = '';
        $scope.show = false;
        $scope.recording = false;
        rest.responsebody = [];
        //
        var recognition = new webkitSpeechRecognition();
        recognition.lang = "en-US";
        recognition.continuous = true;
        recognition.interimResults = true;
        recognition.onstart = function ()
        {
            $scope.recording = true;
            speech.src = '/page/assets/img/mic-animate.gif';
        };
        recognition.onend = function ()
        {
            $scope.recording = false;
            speech.src = '/page/assets/img/mic.gif';
        };
        recognition.onresult = function (event)
        {
            var interim_transcript = '';
            for (var i = event.resultIndex; i < event.results.length; ++i) {
                if (event.results[i].isFinal) {
                    final_transcript += event.results[i][0].transcript;
                } else {
                    interim_transcript += event.results[i][0].transcript;
                }
            }
            input_box.value = interim_transcript;
            input_box.value = final_transcript;
        };
        //
        $scope.send = function (force)
        {
            var query = $scope.keywords;
            if (!query)
            {
                query = input_box.value;
            }

            if (!query)
            {
                return;
            }
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
        };
        $scope.pressEnter = function (keyEvent) {
            $scope.show = false;
            if (keyEvent.which === 13)
                $scope.send(true);
        };
        $scope.toggleSpeech = function () {
            if ($scope.recording) {
                recognition.stop();
                return;
            } else
            {
                final_transcript = '';
                recognition.start();
            }
        };
    }]);