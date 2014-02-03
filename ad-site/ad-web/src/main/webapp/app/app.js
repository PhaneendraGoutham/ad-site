var app = angular.module('spotnikApp', ['ngResource','ui.bootstrap','st-auth-module','blueimp.fileupload','st-common-module','seo']);

app.factory("BaseUrlInterceptor", function() {
    return {
        request : function(config) {
            if (config.url.substring(0, 3) != "app" && config.url.substring(0, 8) != "template" && !(config.url.match(".html"))) {
                config.url = "/api" + config.url;
            }
            return config;
        }
    }
})
.config(['$routeProvider','$httpProvider','stAuthInterceptorProvider','AuthProvider','CommonFunctionsProvider','$locationProvider','MetatagsCreatorProvider',
           

function($routeProvider, $httpProvider, stAuthInterceptorProvider, AuthProvider, CommonFunctionsProvider,$locationProvider,MetatagsCreatorProvider) {
    $locationProvider.html5Mode(false).hashPrefix('!');
    
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
            url : "/auth/brand/:brandId",
        },
        thisContest : {
            role : "company",
            url : "/auth/contest/:contestId",
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
        redirectTo : "/glowna"
    }).when("/glowna", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/ad/ad.html",
        reloadOnSearch : false,
        adSearchOptions : {
            search : false,
        },
        resolve : adSearchResolve,
    }).when("/reklamy/dodaj", {
        controller : 'AdRegistrationCtrl',
        templateUrl : "app/partials/ad/ad-registration.html",
        access : "user",
        resolve : {
            possibleBrands : ['$q','AdService',function($q, AdService) {
                return resolveFetcher($q, AdService.getPossibleBrands);
            }],
            possibleTags : ['$q','AdService',function($q, AdService) {
                return resolveFetcher($q, AdService.getPossibleTags);
            }],
        },
    }).when("/reklamy/:adId", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/ad/ad.html",
        reloadOnSearch : false,
        adSearchOptions : {
            search : false,
        },
        resolve : adSearchResolve,
        writeOGMetaTags: true,
    }).when("/poczekalnia", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/ad/ad.html",
        reloadOnSearch : false,
        adSearchOptions : {
            search : false,
        },
        resolve : adSearchResolve,
    }).when("/przegladaj", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/ad/search.html",
        reloadOnSearch : false,
        adSearchOptions : {},
        resolve : adSearchResolve,
    }).when("/losuj/", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/ad/search.html",
        reloadOnSearch : false,
        adSearchOptions : {
            search : false,
        },
        resolve : adSearchResolve,
    }).when("/uzytkownik/:userId/reklamy", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/ad/search.html",
        reloadOnSearch : false,
        resolve : adSearchResolve,
    }).when("/konkursy/:contestId/reklamy", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/ad/search.html",
        reloadOnSearch : false,
        adSearchOptions : {
            filters : {
                brands : false,
            }
        },
        resolve : adSearchResolve,
    }).when("/konkursy/:contestId/odpowiedzi", {
        controller : 'ContestAnswerListCtrl',
        templateUrl : "app/partials/contest/contest-answer.html",
        resolve : {
            answerBrowserWrapper : ['$q','ContestService','$route',function($q, ContestService, $route) {
                return resolveFetcher($q, ContestService.getAnswers, $route.current.params.contestId);
            }],
        },
    // access : "thisContest",
    }).when("/konkursy/:contestId/reklamy/dodaj", {
        controller : 'AdRegistrationCtrl',
        templateUrl : "app/partials/ad/ad-registration.html",
        resolve : {
            possibleBrands : ['$q','AdService','$route','Auth','ContestService',function($q, AdService, $route, Auth, ContestService) {
                var deferred = $q.defer();
                ContestService.fetchContest($route.current.params.contestId, function(contest) {
                    if (Auth.hasRole('company')) {
                        AdService.fetchWistiaProjectId(contest.brand.id, function(data) {
                            ad.brand.wistiaProjectId = data.response;
                            deferred.resolve(contest);
                        });
                    } else {
                        deferred.resolve(contest);
                    }
                });
                return deferred.promise;
                // return resolveFetcher($q, AdService.getPossibleBrands);
            }],
            possibleTags : ['$q','AdService',function($q, AdService) {
                return resolveFetcher($q, AdService.getPossibleTags);
            }],
        },
    }).when("/konkursy/:contestId/odpowiedzi/dodaj", {
        controller : 'ContestAnswerRegistrationCtrl',
        templateUrl : "app/partials/contest/contest-answer-registration.html",
        access : userRoles.user,
    }).when("/marki/:brandId/reklamy", {
        controller : 'AdSearchCtrl',
        templateUrl : "app/partials/ad/search.html",
        adSearchOptions : {
            filters : {
                brands : false,
            }
        },
        resolve : adSearchResolve,
    }).when("/marki/:brandId/reklamy/dodaj", {
        controller : 'AdRegistrationCtrl',
        templateUrl : "app/partials/ad/ad-registration.html",
        access : "thisBrand",
        resolve : {
            possibleBrands : ['$q','AdService','$route',function($q, AdService, $route) {
                var deferred = $q.defer();
                AdService.fetchWistiaProjectId($route.current.params.brandId, function(data) {

                    var possibleBrands = {};
                    possibleBrands.brand = {};
                    possibleBrands.brand.wistiaProjectId = data.response;
                    deferred.resolve(possibleBrands);
                });
                return deferred.promise;
                // return resolveFetcher($q, AdService.fetchWistiaProjectId,
                // $route.current.params.brandId, 'response');
            }],
            possibleTags : ['$q','AdService',function($q, AdService) {
                return resolveFetcher($q, AdService.getPossibleTags);
            }],
        },
    }).when("/reklamy/:parentId/odpowiedz", {
        controller : 'AdRegistrationCtrl',
        templateUrl : "app/partials/ad/ad-registration.html",
        access : "user",
        resolve : {
            possibleBrands : ['$q','AdService','$route','Auth',function($q, AdService, $route, Auth) {
                var deferred = $q.defer();
                AdService.getAdsByUrl(function(ad) {
                    if (Auth.hasRole('company')) {
                        AdService.fetchWistiaProjectId(ad.brand.id, function(data) {
                            ad.brand.wistiaProjectId = data.response;
                            deferred.resolve(ad);
                        });
                    } else {
                        deferred.resolve(ad);
                    }
                });
                return deferred.promise;
            }],
            possibleTags : ['$q','AdService',function($q, AdService) {
                return resolveFetcher($q, AdService.getPossibleTags);
            }],
        },
    }).when("/marki/:brandId/konkursy", {
        controller : 'ContestListCtrl',
        templateUrl : "app/partials/contest/contest-list.html",
        reloadOnSearch : false,
        resolve : {
            contestsBrowserWrapper : ['$q','ContestService','$route',function($q, ContestService, $route) {
                return resolveFetcher($q, ContestService.getContestsByUrl);
            }],

        }
    }).when("/marki/:brandId/statystyki",{
        controller: "BrandStatsCtrl",
        templateUrl: "app/partials/company/brand-stats.html",
        resolve: {
            stats : ['$q','CompanyService','$route',function($q, CompanyService, $route) {
                dateModel = {};
                dateModel.endDate = new Date();
                dateModel.startDate = new Date(dateModel.endDate.getFullYear(), dateModel.endDate.getMonth(), 1);
                var deferred = $q.defer();
                CompanyService.getBrandStats($route.current.params.brandId,dateModel, function(stats){
                    deferred.resolve(stats);
                });
                return deferred.promise;
            }],
        }
    })
    .when("/konkursy/:contestId", {
        controller : 'ContestCtrl',
        templateUrl : "app/partials/contest/contest.html",
        resolve : {
            contest : ['$q','ContestService','$route',function($q, ContestService, $route) {
                return resolveFetcher($q, ContestService.fetchContest, $route.current.params.contestId);
            }],
        }
    }).when("/konkursy/:contestId/edytuj", {
        controller : 'ContestRegistrationCtrl',
        templateUrl : "app/partials/contest/contest-registration.html",
        resolve : {
            contest : ['$q','ContestService','$route',function($q, ContestService, $route) {
                return resolveFetcher($q, ContestService.fetchContest, $route.current.params.contestId);
            }],
        },
        access : userRoles.thisContest,
    }).when("/uzytkownik/zaloguj", {
        controller : 'UserLoginCtrl',
        templateUrl : "app/partials/user/user-login.html",

    }).when("/marki/:brandId/konkursy/zarejestruj", {
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

    }).when("/uzytkownik/:userId/profil", {
        controller : 'UserProfileCtrl',
        templateUrl : "app/partials/user/profile.html",
        access : "user",
        resolve : {
            profileData : ['$q','$http','$rootScope','UserService',function($q, $http, $rootScope, UserService) {
                return resolveFetcher($q, UserService.fetchUserProfile, $rootScope.currentUser.id, 'response');
            }],
        }
    }).when("/uzytkownik/aktywuj/:token", {
        resolve : {
            activate : ['$q','$http','$route','UserService','$location','CommonFunctions',function($q, $http, $route, UserService, $location, CommonFunctions) {
                var deferred = $q.defer();
                UserService.activateUser($route.current.params.token, function(data) {
                    if (data.response) {
                        CommonFunctions.pushAlert("success", "Użytkownik został zaaktywowany pomyślnie. Możesz się teraz zalogować");
                        $location.path("/uzytkownik/zaloguj");
                    } else {
                        CommonFunctions.pushAlert("danger", "Ten kod aktywacyjny został użyty już wcześniej");
                        $location.path("/uzytkownik/zaloguj");
                    }
                    deferred.resolve();
                });
                return deferred.promise;
            }],
        },
    }).when("/partnerzy/:companyId/aktywuj", {
        controller : 'CompanyActivateCtrl',
        templateUrl : 'app/partials/confirm-page.html',
        access : userRoles.admin,
    }).when("/partnerzy/zarejestruj", {
        controller : 'CompanyRegistrationCtrl',
        templateUrl : "app/partials/company/company-registration.html",
        access : "user",
    }).when("/partnerzy/:companyId", {
        controller : 'BrandListCtrl',
        templateUrl : "app/partials/company/company.html",
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
    }).when("/partnerzy/:companyId/marki/zarejestruj", {
        controller : 'BrandRegistrationCtrl',
        templateUrl : "app/partials/company/brand-registration.html",
        access : "company",
        resolve : {
            brand : function() {
                return null;
            },

        }
    }).when("/marki/:brandId", {
        controller : 'BrandCtrl',
        templateUrl : "app/partials/company/brand.html",
        resolve : {
            brand : ['$q','CompanyService','$route',function($q, companyService, $route) {
                return resolveFetcher($q, companyService.fetchBrand, $route.current.params.brandId);
            }],
        }
    }).when("/marki/:brandId/edytuj", {
        controller : 'BrandRegistrationCtrl',
        templateUrl : "app/partials/company/brand-registration.html",
        access : "thisBrand",
        resolve : {
            brand : ['$q','CompanyService','$route',function($q, companyService, $route) {
                return resolveFetcher($q, companyService.fetchBrand, $route.current.params.brandId);
            }],
        }
    }).otherwise({
        redirectTo : '/'
    });

    AuthProvider.setProtectedResourcesList([{
        url : '/ad',
        method : 'POST',
        access : 'user',
    },{
        url : '/ad/*/rate',
        method : 'POST',
        access : 'user',
    },,{
        url : '/ad/*/state',
        method : 'POST',
        access : 'admin',
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
        url : '/company/*/activate',
        method : 'GET',
        access : 'admin',
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
    },{
        url : '/user/login/status',
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
//        "" : "ad?place=0",
        "przegladaj" : "ad",
        "marki" : "brand",
        "odpowiedz" : "",
    });
    MetatagsCreatorProvider.setDefaultMetatags({
       url : 'http://www.spotnik.pl/#!/glowna',
       description: 'Spotnik.pl - reklamy nie muszą być nudne!',
       type: 'website',
       image: 'http://www.spotnik.pl/resources/img/logo.png',
       site_name: 'Spotnik.pl',
    });

}]).run(['$rootScope','$location','Auth','CommonFunctions','$q', 'MetatagsCreator',function($rootScope, $location, Auth, CommonFunctions, $q,MetatagsCreator) {
    
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
