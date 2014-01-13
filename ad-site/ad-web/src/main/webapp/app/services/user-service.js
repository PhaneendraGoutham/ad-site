app.service("UserService", ['$http','$route','$location','CommonFunctions',function($http, $route, $location, CommonFunctions) {
    this.fetchUserProfile = function(userId, success, error) {
        $http.get('/user/' + userId + "/profile").success(success);
    };
    this.activateUser = function(token, success, error){
      $http.get("/user/activate/" + token).success(success);  
    };
    this.recoverPassword = function(email, success, error){
        $http.post('/user/password/recover', $.param({mail: email}), CommonFunctions.getPostHeader()).success(success);
    };
}]);