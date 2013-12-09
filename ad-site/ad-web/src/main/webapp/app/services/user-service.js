app.service("UserService", ['$http','$route','$location','CommonFunctions',function($http, $route, $location, CommonFunctions) {
    this.fetchUserProfile = function(userId, success, error) {
        $http.get('/user/' + userId + "/profile").success(success);
    };
}]);