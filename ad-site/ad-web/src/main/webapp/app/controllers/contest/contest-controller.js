app.controller('ContestListCtrl', ['contestsBrowserWrapper','$scope',"$location",'ContestService','$routeParams',function(contestsBrowserWrapper, $scope, $location, ContestService, $routeParams) {
    init();
    function init() {
        $scope.filters = $location.search();
        $scope.contests = contestsBrowserWrapper.contests;
        $scope.total = contestsBrowserWrapper.total;
        $scope.selectPage = function(page) {
            $scope.filters.page = page;
            $location.search($scope.filters);
        };
        $scope.$on('$routeUpdate', function(e) {
            $scope.filters = $location.search();
            getContests();
        });
    }
    function getContests() {
        ContestService.getContestsByUrl(function(data) {
            $scope.total = data.total;
            $scope.contests = data.contests;
        });
    }
}]);
app.controller('ContestAnswerListCtrl', ['answerBrowserWrapper','$scope','$routeParams','ContestService',function(answerBrowserWrapper, $scope, $routeParams, ContestService) {
    init();
    function init() {
        $scope.contestAnswers = answerBrowserWrapper.contestAnswers;
        $scope.total = answerBrowserWrapper.total;
        $scope.currentPage = 1;
        $scope.selectPage = function(page) {
            $scope.filters.page = page;
            $scope.search();
        };
        $scope.pressWinner = function(answer) {
            ContestService.updateWinner($routeParams.contestId, answer.id, !answer.winner, function(data) {
                answer.winner = !answer.winner;
            });
        };
    }
}]);
app.controller('ContestAnswerRegistrationCtrl', ['ContestService','$scope','$routeParams','ErrorFactory',"$location",'CommonFunctions',function(ContestService, $scope, $routeParams, ErrorFactory, $location, CommonFunctions) {
    init();
    function init() {
        $scope.regModel = {};
        $scope.errorMessages = ErrorFactory.getErrorMessages({});
        $scope.submit = function() {
            ContestService.registerAnswer($routeParams.contestId, $scope.regModel, function(data) {
                CommonFunctions.pushAlert('success', "Twoja odpowiedź została zarejestrowana");
                $location.path("/konkursy/" + $routeParams.contestId +"/");
            }, function(message, statusCode) {
                if (statusCode == 442) {
                    CommonFunctions.pushAlert('danger', "Konkurs już się zakończył");
                } else if (statusCode == 443) {
                    CommonFunctions.pushAlert('danger', "Już wcześniej dodałeś/aś odpowiedź");
                }
            });
        };
    }
}]);
app.controller('ContestCtrl', ['contest','$scope',function(contest, $scope) {
    init();
    function init() {
        $scope.contest = contest;

    }
}]);
app.controller('ContestRegistrationCtrl', ['$scope','ErrorFactory','$location','$routeParams','ContestService','$filter','contest',function($scope, ErrorFactory, $location, $routeParams, ContestService, $filter, contest) {
    init();
    function prepareDateToSend() {
        var data = angular.copy($scope.regModel);
        data.finishDate = new Date(data.finishDate.date.getFullYear(), data.finishDate.date.getMonth(), data.finishDate.date.getDate(), data.finishDate.time.getHours(), data.finishDate.time.getMinutes(), data.finishDate.time.getSeconds());
        data.scoresDate = new Date(data.scoresDate.date.getFullYear(), data.scoresDate.date.getMonth(), data.scoresDate.date.getDate(), data.scoresDate.time.getHours(), data.scoresDate.time.getMinutes(), data.scoresDate.time.getSeconds());
        return data;
    }
    function init() {
        $scope.regModel = {};
        $scope.regModel.scoresDate = {};
        $scope.regModel.finishDate = {};
        $scope.minDate = new Date();
        $scope.errorMessages = ErrorFactory.getErrorMessages({
            'dateValid' : {
                scoresDate : "Data musi być poźniejsza",
                finishDate : "Data musi być poźniejsza"
            }
        });
        if (contest) {
            $scope.regModel.name = contest.name;
            $scope.regModel.description = contest.description;
            $scope.regModel.finishDate.date = new Date(contest.finishDate);
            $scope.regModel.finishDate.time = new Date(contest.finishDate);
            $scope.regModel.scoresDate.date = new Date(contest.scoresDate);
            $scope.regModel.scoresDate.time = new Date(contest.scoresDate);
            $scope.regModel.type = contest.type;
            $scope.register = function() {
                var data = prepareDateToSend();
                ContestService.updateContest($routeParams.contestId, data, function(responseData) {
                    $location.path("/konkursy/" + $routeParams.contestId + "/" + data.name.replace(/ /g, '-'));
                });
            };
        } else {
            $scope.regModel.finishDate.time = new Date();
            $scope.regModel.scoresDate.time = new Date();
            $scope.register = function() {
                var data = prepareDateToSend();
                ContestService.registerContest($routeParams.brandId, data, function(responseData) {
                    $location.path("/konkursy/" + responseData.response + "/" + data.name.replace(/ /g, '-'));
                });
            };

        }
        $scope.minDate = new Date();

        $scope.$watchCollection('regModel.finishDate', function(finishDate) {
            if (finishDate.date && finishDate.time && $scope.contestRegForm.finishDate) {
                var date = new Date(finishDate.date.getFullYear(), finishDate.date.getMonth(), finishDate.date.getDate(), finishDate.time.getHours(), finishDate.time.getMinutes(), finishDate.time.getSeconds());
                if (date <= new Date(new Date() + 15*60000)) {
                    $scope.contestRegForm.finishDate.$setValidity("dateValid", false);
                } else {
                    $scope.contestRegForm.finishDate.$setValidity("dateValid", true);
                }
            }
        });
        $scope.$watchCollection('regModel.finishDate', function(scoresDate) {
            if (scoresDate.date && scoresDate.time && $scope.contestRegForm.finishDate) {
                var date = new Date(scoresDate.date.getFullYear(), scoresDate.date.getMonth(), scoresDate.date.getDate(), scoresDate.time.getHours(), scoresDate.time.getMinutes(), scoresDate.time.getSeconds());
                if (date <=new Date() + 15*60000) {
                    $scope.contestRegForm.scoresDate.$setValidity("dateValid", false);
                } else {
                    $scope.contestRegForm.scoresDate.$setValidity("dateValid", true);
                }
            }
        });
    }
}]);