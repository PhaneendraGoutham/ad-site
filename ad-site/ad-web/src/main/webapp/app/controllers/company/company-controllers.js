app.controller('CompanyRegistrationCtrl',['CompanyService', '$location','$scope', '$rootScope', 'ErrorFactory', function(CompanyService, $location, $scope, $rootScope, ErrorFactory){
	init();
	function init(){
		$scope.regModel = {};
		$scope.regModel.address= {};
	    $scope.errorMessages = ErrorFactory.getErrorMessages({});
		$scope.register = function(){
			CompanyService.register($scope.regModel, function(data){
				$rootScope.currentUser.companyId = data.response;
				$rootScope.currentUser.roles.push("company");
				$location.path("/company/"+data.response);
			});
			
		};
	}
}]);
app.controller('BrandListCtrl',['brands','$scope', function(brands,$scope){
	init();
	function init(){
		$scope.brands = brands;
		
	}
}]);
app.controller('BrandCtrl',['brand','$scope', function(brand,$scope){
	init();
	function init(){
		$scope.brand = brand;
		
	}
}]);
app.controller('BrandRegistrationCtrl',['$scope','ErrorFactory','$location','$routeParams','CompanyService', 'brand','Auth', function($scope,ErrorFactory,$location,$routeParams,CompanyService, brand, Auth){
	init();
	function init(){
		$scope.regModel = {};
		$scope.errorMessages = ErrorFactory.getErrorMessages({});

		if(brand){
			$scope.regModel.name = brand.name;
			$scope.regModel.description = brand.description;
			$scope.locationEdit = true;
			$scope.brand = brand;
			$scope.submitText = "Zaktualizuj";
			$scope.options ={
				url: "/brand/"+$routeParams.brandId+"/upload/image",
				type: "POST",
				dataType: "json",
				headers:{},
				autoUpload:true,
			};
			$.extend($scope.options.headers, Auth.getAuthHeader());
			$("#fileupload").bind('fileuploaddone', function (e, data) {
				$scope.brand.smallLogoUrl = data.result.small;
				$scope.brand.logoUrl = data.result.big;
			});
			$scope.register = function(){
				CompanyService.updateBrand($routeParams.brandId, $scope.regModel, function(data){
					$location.path("/marki/"+$routeParams.brandId);
				});
			};
		}else{
			$scope.register = function(){
				CompanyService.registerBrand($routeParams.companyId, $scope.regModel, function(data){
					$location.path("/marki/"+data.response+"/edytuj");
				});
			};
			$scope.submitText = "Dodaj markę";
		}




	}
}]);
