app.controller('CompanyRegistrationCtrl', ['CompanyService','$location','$scope','$rootScope','ErrorFactory','CommonFunctions',function(CompanyService, $location, $scope, $rootScope, ErrorFactory, CommonFunctions) {
    init();
    function init() {
        $scope.regModel = {};
        $scope.regModel.address = {};
        $scope.errorMessages = ErrorFactory.getErrorMessages({});
        $scope.register = function() {
            CompanyService.register($scope.regModel, function(data) {
                CommonFunctions.pushAlert('success', "Dziękujemy za chęć współpracy z nami. W ciągu 24h otrzymasz od nas email z dalszymi instrukcjami.");
                $location.path("/");
            }, function(message, statusCode){
                if(statusCode == 443){
                    CommonFunctions.pushAlert('danger', "Już wcześniej wyraziłeś/łaś chęć współpracy z nami. Jeśli nie dostałeś/łaś od nas wiadomości w ciągu 24h od rejestracji skontaktuj się z nami.");
                }
            });

        };
    }
}]);
app.controller('CompanyActivateCtrl', ['$routeParams','CompanyService','$location','CommonFunctions','$scope',function($routeParams, CompanyService, $location, CommonFunctions, $scope) {
    init();
    function init() {
        $scope.message = "Czy chcesz aktywować firmę.";
        $scope.cancel = function(){
            $location.path("/");
        };
        $scope.ok = function() {
            CompanyService.activateCompany($routeParams.companyId, function(data) {
                if (data.response) {
                    CommonFunctions.pushAlert("success", "Firma została zaaktywowana pomyślnie.");
                    $location.path("/");
                } else {
                    CommonFunctions.pushAlert("success", "Firma już wcześniej została zaaktywowana.");
                    $location.path("/");
                }
            });
        };
    }
}]);
app.controller('BrandListCtrl', ['brands','$scope',function(brands, $scope) {
    init();
    function init() {
        $scope.brands = brands;

    }
}]);
app.controller('BrandCtrl', ['brand','$scope',function(brand, $scope) {
    init();
    function init() {
        $scope.brand = brand;

    }
}]);
app.controller('BrandRegistrationCtrl', ['$scope','ErrorFactory','$location','$routeParams','CompanyService','brand','Auth',function($scope, ErrorFactory, $location, $routeParams, CompanyService, brand, Auth) {
    init();
    function init() {
        $scope.regModel = {};
        $scope.errorMessages = ErrorFactory.getErrorMessages({});

        if (brand) {
            $scope.regModel.name = brand.name;
            $scope.regModel.description = brand.description;
            $scope.locationEdit = true;
            $scope.brand = brand;
            $scope.submitText = "Zaktualizuj";
            $scope.options = {
                url : "/api/brand/" + $routeParams.brandId + "/upload/image",
                type : "POST",
                dataType : "json",
                headers : {},
                autoUpload : true,
            };
            $.extend($scope.options.headers, Auth.getAuthHeader());
            $("#fileupload").bind('fileuploaddone', function(e, data) {
                $scope.brand.smallLogoUrl = data.result.small;
                $scope.brand.logoUrl = data.result.big;
            });
            $scope.register = function() {
                CompanyService.updateBrand($routeParams.brandId, $scope.regModel, function(data) {
                    $location.path("/marki/" + $routeParams.brandId);
                });
            };
        } else {
            $scope.register = function() {
                CompanyService.registerBrand($routeParams.companyId, $scope.regModel, function(data) {
                    $location.path("/marki/" + data.response + "/edytuj");
                });
            };
            $scope.submitText = "Dodaj markę";
        }

    }
}]);
