app.service("CompanyService", ['$http','$location','CommonFunctions',function($http, $location, CommonFunctions) {
    this.register = function(model, success, error) {
        $http.post("/company", model), success(success);
    };
    this.fetchUserCompanies = function(userId, success, error) {
        $http.get("/user/" + userId + "/company").success(success);
    };
    this.fetchCompanyBrands = function(companyId, success, error) {
        $http.get('/company/' + companyId + '/brand').success(success);
    };
    this.fetchBrands = function(success, error) {
        $http.get('/brand').success(success);
    };
    this.fetchBrand = function(brandId, success, error) {
        $http.get('/brand/' + brandId).success(success);
    };
    this.registerBrand = function(companyId, regModel, success, error) {
        $http.post('/company/' + companyId + '/brand', regModel, CommonFunctions.getPostHeader()).success(success);
    };
    this.updateBrand = function(brandId, regModel, success, error) {
        $http.post('/brand/' + brandId, regModel, CommonFunctions.getPostHeader()).success(success);
    };
}]);