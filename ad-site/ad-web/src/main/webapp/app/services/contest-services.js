app.service("ContestService", ['$http','$location','CommonFunctions',function($http, $location, CommonFunctions) {
    var self = this;
//    this.fetchBrandContests = function(brandId, success, error) {
//        self.getBrandContests(brandId, null, success, error);
//    };
//    this.getBrandContests= function(brandId,data, success, error) {
//        $http.get('/brand/' + brandId + '/contest', {params:data}).success(success);
//    };
    this.getContestsByUrl = function(success, error){
        var url = CommonFunctions.getTranslatedUrl();
        var params = $location.search() || {};
        $http.get(url, {params:params}).success(success);  
    };
    this.fetchContest = function(contestId, success, error) {
        $http.get('/contest/' + contestId).success(success);
    };
    this.registerContest = function(brandId, regModel, success, error) {
        $http.post('/brand/' + brandId + '/contest', regModel).success(success);
    };
    this.updateContest = function(contestId, regModel, success, error) {
        $http.post('/contest/' + contestId, regModel).success(success);
    };
    this.registerAnswer = function(contestId, regModel, success, error) {
        $http.post('/contest/' + contestId + '/answer', $.param(regModel),CommonFunctions.getPostHeader()).success(success).error(error);
    };
    this.getAnswers = function(contestId, success, error) {
        $http.get('/contest/' + contestId + '/answer').success(success);
    };
    this.updateWinner = function(contestId,answerId, winner, success, error) {
        $http.post('/contest/' + contestId + '/answer/'+answerId+"/state",$.param({winner: winner}),CommonFunctions.getPostHeader()).success(success);
    };
}]);