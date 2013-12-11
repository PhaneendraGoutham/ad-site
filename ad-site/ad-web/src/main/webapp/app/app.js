var app = angular.module('spotnikApp', ['ngResource','ui.bootstrap','st-auth-module','blueimp.fileupload','st-common-module']);

app.factory("BaseUrlInterceptor", function() {
    return {
        request : function(config) {
            if (config.url.substring(0, 3) != "app" && config.url.substring(0, 8) != "template") {
                config.url = "/api" + config.url;
            }
            return config;
        }
    }
}).config(['$routeProvider','$httpProvider','stAuthInterceptorProvider','AuthProvider','CommonFunctionsProvider',

function($routeProvider, $httpProvider, stAuthInterceptorProvider, AuthProvider, CommonFunctionsProvider) {

    function resolveFetcher($q, _function, param, returnObjectName) {
        var deferred = $q.defer();
        var success = function(data) {
            if (returnObjectName) {
                deferred.resolve(data[returnObjectName]);
            } else {
                deferred.resolve(data);
            }
        };
        if (param)
            _function(param, success);
        else
            _function(success);
        return deferred.promise;
    }

    var userRoles = {
        admin : "admin",
        public : "public",
        user : "user",
        company : "company",
        contest : "contest",
        thisBrand : {
            role : "company",
            url : "/auth/brand/:id",
        },
        thisContest : {
            role : "company",
            url : "/auth/contest/:id",
        }
    };

    var adSearchResolve = {
        possibleTags : ['$q','AdService',function($q, AdService) {
            var deferred = $q.defer();
            var searchOptions = AdService.getSearchOptions();
            if (searchOptions.search && searchOptions.filters.tagList) {
                AdService.fetchPossibleTags(deferred);
            } else {
                deferred.resolve([]);
            }
            return deferred.promise;
        }],
        possibleBrands : ['$q','AdService',function($q, AdService) {
            var deferred = $q.defer();
            var searchOptions = AdService.getSearchOptions();
            if (searchOptions.search && searchOptions.filters.brandList) {
                AdService.fetchPossibleBrands(deferred);
            } else {
                deferred.resolve([]);
            }
            return deferred.promise;
        }],
        adBrowserWrapper : ['$q','AdService',function($q, AdService) {
            return resolveFetcher($q, AdService.getAdsByUrl);
        }],
    };

    $routeProvider.when("/", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/add/ad.html",
        reloadOnSearch : false,
        adSearchOptions : {
            search : false,
        },
        resolve : adSearchResolve,
    }).when("/reklamy/dodaj", {
        controller : 'AdRegistrationCtrl',
        templateUrl : "app/partials/add/ad-registration.html",
        access : "user",
        resolve : {
            possibleBrands : ['$q','AdService',function($q, AdService) {
                return resolveFetcher($q, AdService.getPossibleBrands);
            }],
            possibleTags : ['$q','AdService',function($q, AdService) {
                return resolveFetcher($q, AdService.getPossibleTags);
            }],
        },
    }).when("/reklamy/:id", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/add/ad.html",
        reloadOnSearch : false,
        adSearchOptions : {
            search : false,
        },
        resolve : adSearchResolve,
    }).when("/poczekalnia", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/add/ad.html",
        reloadOnSearch : false,
        adSearchOptions : {
            search : false,
        },
        resolve : adSearchResolve,
    }).when("/przegladaj", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/add/search.html",
        reloadOnSearch : false,
        adSearchOptions : {},
        resolve : adSearchResolve,
    }).when("/losuj", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/add/search.html",
        reloadOnSearch : false,
        adSearchOptions : {
            search : false,
        },
        resolve : adSearchResolve,
    }).when("/uzytkownik/:id/reklamy", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/add/search.html",
        reloadOnSearch : false,
        resolve : adSearchResolve,
    }).when("/konkursy/:id/reklamy", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/add/search.html",
        reloadOnSearch : false,
        adSearchOptions : {
            filters : {
                brands : false,
            }
        },
        resolve : adSearchResolve,
    }).when("/konkursy/:id/odpowiedzi", {
        controller : 'ContestAnswerListCtrl',
        templateUrl : "app/partials/contest/contest-answer.html",
        resolve : {
            answerBrowserWrapper : ['$q','ContestService','$route',function($q, ContestService, $route) {
                return resolveFetcher($q, ContestService.getAnswers, $route.current.params.id);
            }],
        },
    // access : "thisContest",
    }).when("/konkursy/:contestId/reklamy/dodaj", {
        controller : 'AdRegistrationCtrl',
        templateUrl : "app/partials/add/ad-registration.html",
        resolve : {
            possibleBrands : ['$q','AdService','$route',function($q, AdService, $route) {
                return resolveFetcher($q, AdService.getPossibleBrands);
            }],
            possibleTags : ['$q','AdService',function($q, AdService) {
                return resolveFetcher($q, AdService.getPossibleTags);
            }],
        },
    }).when("/konkursy/:id/odpowiedzi/dodaj", {
        controller : 'ContestAnswerRegistrationCtrl',
        templateUrl : "app/partials/contest/contest-answer-registration.html",
        access : userRoles.user,
    }).when("/marki/:id/reklamy", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/add/search.html",
        adSearchOptions : {
            filters : {
                brands : false,
            }
        },
        resolve : adSearchResolve,
    }).when("/marki/:brandId/reklamy/dodaj", {
        controller : 'AdRegistrationCtrl',
        templateUrl : "app/partials/add/ad-registration.html",
        access : "thisBrand",
        resolve : {
            possibleBrands : ['$q','AdService','$route',function($q, AdService, $route) {
                return resolveFetcher($q, AdService.fetchWistiaProjectId, $route.current.params.brandId, 'response');
            }],
            possibleTags : ['$q','AdService',function($q, AdService) {
                return resolveFetcher($q, AdService.getPossibleTags);
            }],
        },
    }).when("/reklamy/:adId/odpowiedz", {
        controller : 'AdRegistrationCtrl',
        templateUrl : "app/partials/add/ad-registration.html",
        access : "user",
        resolve : {
            possibleBrands : ['$q','AdService','$route',function($q, AdService, $route) {
                return resolveFetcher($q, AdService.getAdsByUrl);
            }],
            possibleTags : ['$q','AdService',function($q, AdService) {
                return resolveFetcher($q, AdService.getPossibleTags);
            }],
        },
    }).when("/marki/:id/konkursy", {
        controller : 'ContestListCtrl',
        templateUrl : "app/partials/contest/contest-list.html",
        reloadOnSearch : false,
        resolve : {
            contestsBrowserWrapper : ['$q','ContestService','$route',function($q, ContestService, $route) {
                return resolveFetcher($q, ContestService.getContestsByUrl);
            }],

        }
    }).when("/konkursy/:id", {
        controller : 'ContestCtrl',
        templateUrl : "app/partials/contest/contest.html",
        resolve : {
            contest : ['$q','ContestService','$route',function($q, ContestService, $route) {
                return resolveFetcher($q, ContestService.fetchContest, $route.current.params.id);
            }],
        }
    }).when("/konkursy/:id/edytuj", {
        controller : 'ContestRegistrationCtrl',
        templateUrl : "app/partials/contest/contest-registration.html",
        resolve : {
            contest : ['$q','ContestService','$route',function($q, ContestService, $route) {
                return resolveFetcher($q, ContestService.fetchContest, $route.current.params.id);
            }],
        },
        access : userRoles.thisContest,
    }).when("/uzytkownik/zaloguj", {
        controller : 'UserLoginCtrl',
        templateUrl : "app/partials/user/user-login.html",

    }).when("/marki/:id/konkursy/zarejestruj", {
        controller : 'ContestRegistrationCtrl',
        templateUrl : "app/partials/contest/contest-registration.html",
        access : "company",
        resolve : {
            contest : function() {
                return null;
            },
        }
    }).when("/uzytkownik/zaloguj", {
        controller : 'UserLoginCtrl',
        templateUrl : "app/partials/user/user-login.html",

    }).when("/uzytkownik/zarejestruj", {
        controller : 'UserRegistrationCtrl',
        templateUrl : "app/partials/user/user-registration.html",

    }).when("/uzytkownik/haslo/przypomnij", {
        controller : 'PassRecoverCtrl',
        templateUrl : "app/partials/user/password-recovery.html",
    }).when("/uzytkownik/haslo/zmien", {
        controller : 'PassChangeCtrl',
        templateUrl : "app/partials/user/user-change-password.html",

    }).when("/uzytkownik/:id/profil", {
        controller : 'UserProfileCtrl',
        templateUrl : "app/partials/user/profile.html",
        access : "user",
        resolve : {
            profileData : ['$q','$http','$rootScope','UserService',function($q, $http, $rootScope, UserService) {
                if ($rootScope.currentUser.companyId) {
                    return resolveFetcher($q, UserService.fetchUserProfile, $rootScope.currentUser.id, 'response');
                }
            }],
        }
    }).when("/partnerzy/zarejestruj", {
        controller : 'CompanyRegistrationCtrl',
        templateUrl : "app/partials/company/company-registration.html",
        access : "user",
    }).when("/partnerzy/:id", {
        controller : 'BrandListCtrl',
        templateUrl : "app/partials/company/brand-list.html",
        access : "company",
        resolve : {
            brands : ['$q','CompanyService','$rootScope',function($q, companyService, $rootScope) {
                if ($rootScope.currentUser.companyId) {
                    return resolveFetcher($q, companyService.fetchCompanyBrands, $rootScope.currentUser.companyId);
                }
            }]
        }
    }).when("/marki", {
        controller : 'BrandListCtrl',
        templateUrl : "app/partials/company/brand-list.html",
        resolve : {
            brands : ['$q','CompanyService','$rootScope',function($q, companyService, $rootScope) {
                return resolveFetcher($q, companyService.fetchBrands);
            }]
        }
    }).when("/partnerzy/:id/marki/zarejestruj", {
        controller : 'BrandRegistrationCtrl',
        templateUrl : "app/partials/company/brand-registration.html",
        access : "company",
        resolve : {
            brand : function() {
                return null;
            },

        }
    }).when("/marki/:id", {
        controller : 'BrandCtrl',
        templateUrl : "app/partials/company/brand.html",
        resolve : {
            brand : ['$q','CompanyService','$route',function($q, companyService, $route) {
                return resolveFetcher($q, companyService.fetchBrand, $route.current.params.id);
            }],
        }
    }).when("/marki/:id/edytuj", {
        controller : 'BrandRegistrationCtrl',
        templateUrl : "app/partials/company/brand-registration.html",
        access : "thisBrand",
        resolve : {
            brand : ['$q','CompanyService','$route',function($q, companyService, $route) {
                return resolveFetcher($q, companyService.fetchBrand, $route.current.params.id);
            }],
        }
    }).otherwise({
        redirectTo: '/'
    });

    AuthProvider.setProtectedResourcesList([{
        url : '/ad',
        method : 'POST',
        access : 'user',
    },{
        url : '/ad/*/rate',
        method : 'POST',
        access : 'user',
    },{
        url : '/user/*/password',
        method : 'POST',
        access : 'user',
    },{
        url : '/user/*/profile',
        method : 'GET',
        access : 'user',
    },{
        url : '/user/*/',
        method : 'POST',
        access : 'user',
    },{
        url : '/company',
        method : 'POST',
        access : 'user',
    },{
        url : '/company/*/brand',
        method : 'GET',
        access : 'company',
    },{
        url : '/company/*/brand',
        method : 'POST',
        access : 'company',
    },{
        url : '/brand/*',
        method : 'POST',
        access : 'company',
    },{
        url : '/brand/*/contest',
        method : 'POST',
        access : 'company',
    },{
        url : 'contest/*',
        method : 'POST',
        access : 'company',
    },{
        url : '/contest/*/answer',
        method : 'POST',
        access : 'user',
    },{
        url : '/contest/*/answer',
        method : 'GET',
        access : 'contest',
    },{
        url : '/auth/*',
        method : 'GET',
        access : 'user',
    }]);
    AuthProvider.setUserRoles(userRoles);
    stAuthInterceptorProvider.setLoginUrl("/uzytkownik/zaloguj");
    $httpProvider.interceptors.push('BaseUrlInterceptor');
    $httpProvider.interceptors.push('stAuthInterceptor');

    // $httpProvider.defaults.headers.post["Content-Type"] =
    // "application/x-www-form-urlencoded";
    CommonFunctionsProvider.setWordMappings({
        "uzytkownik" : "user",
        "konkursy" : "contest",
        "reklamy" : "ad",
        "losuj" : "ad/rand",
        "poczekalnia" : "ad?place=1",
        "glowna" : "ad?place=0",
        "" : "ad?place=0",
        "przegladaj" : "ad",
        "marki" : "brand",
        "odpowiedz": "",
    });

}]).run(['$rootScope','$location','Auth','CommonFunctions','$q',function($rootScope, $location, Auth, CommonFunctions, $q) {
    function handleAuthorizationResponse(authorized) {
        if (!authorized) {
            if (Auth.isLoggedIn()) {
                CommonFunctions.pushAlertByType("accessDenied");
            } else {
                CommonFunctions.pushAlertByType("login");
                $location.path('/uzytkownik/zaloguj');
            }
            return $q.reject();
        }
    }
    $rootScope.$on("$routeChangeStart", function(event, next, current) {
        if (next.$$route && next.$$route.access) {
            var pPromise = Auth.isAuthorized(next.$$route.access, next.params);
            var promise = $q.when(pPromise);
            promise.then(handleAuthorizationResponse, handleAuthorizationResponse);
            next.resolve = next.resolve || {};
            angular.extend(next.resolve, {
                __authentication__ : function() {
                    return promise;
                }
            });
        }
    });

}])
