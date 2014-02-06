app.controller('UserLoginCtrl', ['$scope','ErrorFactory','Auth','$location',function($scope, ErrorFactory, Auth, $location) {
    init();
    function init() {
        $scope.loginModel = {};
    }
    $scope.login = function() {
        Auth.login($scope.loginModel, function() {
            $location.path("/");
        });
    }

}]);
app.controller('UserProfileCtrl', ['$scope','$location','profileData','$http','CommonFunctions','$routeParams','Auth',function($scope, $location, profileData, $http, CommonFunctions,$routeParams,Auth) {
    init();
    function init() {
        $scope.profileModel = profileData;
    }
    $scope.update = function() {
        $http.post('/user/' + $scope.currentUser.id + '/', $scope.profileModel).success(function() {
            CommonFunctions.pushAlert('success', 'Dane użytkownika zostały zaktualizowane');
        });
    }
    $scope.options = {
        url : "/api/user/" + $routeParams.userId + "/upload/image",
        type : "POST",
        dataType : "json",
        headers : {},
        autoUpload : true,
    };
    $.extend($scope.options.headers, Auth.getAuthHeader());
    $("#fileupload").bind('fileuploaddone', function(e, data) {
       Auth.changeUser({imageUrl: data.result.response});
    });
}]);

app.controller('PassRecoverCtrl', ['$scope','$location','$http','CommonFunctions','ErrorFactory','UserService',function($scope, $location, $http, CommonFunctions, ErrorFactory, UserService) {
    $scope.recoverModel = {};
    $scope.errorMessages = ErrorFactory.getErrorMessages({});
    $scope.recover = function() {
        UserService.recoverPassword($scope.recoverModel.email, function(data) {
            if (data.response)
                CommonFunctions.pushAlert('success', "Wysłaliśmy hasło na podany adres email: " + $scope.recoverModel.email);
            else
                CommonFunctions.pushAlert('danger', "Podany adres email: " + $scope.recoverModel.email + " nie istnieje w naszej bazie.");
            $location.path('/uzytkownik/zaloguj');
        });
    }
}]);

app.controller('PassChangeCtrl', ['$scope','$location','$http','CommonFunctions','ErrorFactory','$rootScope',function($scope, $location, $http, CommonFunctions, ErrorFactory, $rootScope) {
    $scope.changePassModel = {};
    $scope.errorMessages = ErrorFactory.getErrorMessages({});
    $scope.change = function() {
        $http.post('/user/' + $rootScope.currentUser.id + '/password', $scope.changePassModel).success(function(data) {
            if (data.response) {
                CommonFunctions.pushAlert('success', "Hasło zostało zmienione");
                $location.path('/');
            } else {
                CommonFunctions.pushAlert('danger', "Nieprawidłowe hasło");
                $scope.changePassModel.oldPassword = "";
            }
        });
    }
}]);
app.controller('UserRegistrationCtrl', ['$scope','ErrorFactory','$locale','$location','$http','CommonFunctions','$filter',function($scope, ErrorFactory, $locale, $location, $http, CommonFunctions, $filter) {
    init();
    function init() {
        $scope.regModel = {};
        $scope.errorMessages = ErrorFactory.getErrorMessages({
            pattern : {
                username : "Tylko litery i cyfry"
            },
            unique : {
                username : "Taki użytkownik już istnieje",
                email : "Taki adres email już istnieje"
            },
        });
        $scope.months = $locale.DATETIME_FORMATS.MONTH;

        $scope.register = function() {
            var data = angular.copy($scope.regModel);
            data.birthdate = ("0" + $scope.regModel.birthdate.year).slice(-4) + "-" + ("0" + $scope.regModel.birthdate.month).slice(-2) + "-" + ("0" + $scope.regModel.birthdate.day).slice(-2);
            $http({
                method : 'POST',
                url : '/user',
                data : data
            }).success(function(data) {
                CommonFunctions.pushAlert('success', "Użytkownik został zarejestrowany. Prosimy o potwierdzenie rejestracji poprzez kliknięcie w link który wysłaliśmy na podany adres email: " + $scope.regModel.mail);
                $location.path('/uzytkownik/zaloguj');
            });
        }
    }

}]);
app.controller('UserDropdownCtrl', ['$scope','Auth','$location',function($scope, Auth, $location) {
    $scope.logout = function() {
        Auth.logout();
        $location.path('/');
    }
    $scope.showProfile = function() {
        $location.path('/uzytkownik/' + $scope.currentUser.id + '/profil');
    }
}]);