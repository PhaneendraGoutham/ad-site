app.directive('stSubmit', ['$parse',function($parse) {
    return {
        restrict : 'A',
        require : ['stSubmit','?^form'],

        controller : ['$scope',function($scope) {
            this.attempted = false;

            var formController = null;

            this.setAttempted = function() {
                this.attempted = true;
            };

            this.setFormController = function(controller) {
                formController = controller;
            };

            this.needsAttention = function(fieldModelController) {
                if (!formController)
                    return false;

                if (fieldModelController) {
                    return fieldModelController.$invalid && (this.attempted);
                } else {
                    return formController && formController.$invalid && (formController.$dirty || this.attempted);
                }
            };
        }],
        compile : function(cElement, cAttributes, transclude) {
            return {
                pre : function(scope, formElement, attributes, controllers) {

                    var submitController = controllers[0];
                    var formController = (controllers.length > 1) ? controllers[1] : null;

                    submitController.setFormController(formController);

                    scope.st = scope.st || {};
                    scope.st[attributes.name] = submitController;
                    scope.st.busy = [];
                },
                post : function(scope, formElement, attributes, controllers) {

                    var submitController = controllers[0];
                    var formController = (controllers.length > 1) ? controllers[1] : null;
                    var fn = $parse(attributes.stSubmit);

                    formElement.bind('submit', function() {
                        submitController.setAttempted();
                        if (!scope.$$phase)
                            scope.$apply();

                        if (!formController.$valid || scope.st.busy.length > 0)
                            return false;

                        formElement.find(':submit').attr('disabled', true);
                        scope.$apply(function() {
                            fn(scope, {
                                $event : event
                            });
                        });
                    });
                }
            };
        }
    };
}]);

app.directive('stError', function() {
    return {
        restrict : 'A',
        require : "^form",
        link : function(scope, htmlElem, attributes, formController) {
            var formChild = $("[name=" + formController.$name + "]").find("[name=" + attributes.stError + "]");
            var errorMessagesVar = attributes.stErrorMessagesVar ? attributes.stErrorMessagesVar : "errorMessages";

            scope.$watchCollection(formController.$name + "." + attributes.stError + ".$error", function(errors) {
                var isError = false;
                angular.forEach(errors, function(value, key) {
                    if (value) {
                        var errorArr = scope[errorMessagesVar][key];
                        var error = null;
                        if (errorArr) {
                            if (errorArr[attributes.stError]) {
                                error = errorArr[attributes.stError];
                            } else {
                                error = errorArr.def;
                            }
                            if (error.match("\{\{.*\}\}")) {
                                var errorParams = error.match("\{\{.*\}\}", "g");
                                $.each(errorParams, function(i, elem) {
                                    elem = elem.replace(new RegExp("\{", 'g'), "").replace(new RegExp("\}", 'g'), "");
                                    if (formChild.attr(elem)) {
                                        error = error.replace("\{\{" + elem + "\}\}", formChild.attr(elem));
                                    }
                                });
                            }
                            htmlElem.removeClass("hidden");
                            htmlElem.text(error);
                        }
                        isError = true;
                        return;
                    }
                });
                if (!isError) {
                    htmlElem.addClass("hidden");
                }
            }, true);
        }
    }
});

app.directive('stFieldMatch', ['$parse',function($parse) {
    return {
        require : 'ngModel',
        link : function(scope, field, attrs, c) {
            c.$setValidity('fieldMatch', false);
            scope.$watch(function() {
                return $parse(attrs.stFieldMatch)(scope) === c.$modelValue;
            }, function(currentValue) {
                c.$setValidity('fieldMatch', currentValue);
                c.$dirty = true;
            });
        }
    }
}]);

app.directive('stDateValidation', ['$parse',function($parse) {
    return {
        require : 'ngModel',
        link : function(scope, field, attrs, c) {
            c.$setValidity('dateValid', false);
            var regx = /^(?:(?:31(\/|-|\.)(?:0?[13578]|1[02]))\1|(?:(?:29|30)(\/|-|\.)(?:0?[1,3-9]|1[0-2])\2))(?:(?:1[6-9]|[2-9]\d)?\d{2})$|^(?:29(\/|-|\.)0?2\3(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\d|2[0-8])(\/|-|\.)(?:(?:0?[1-9])|(?:1[0-2]))\4(?:(?:1[6-9]|[2-9]\d)?\d{2})$/i;
            scope.$watchCollection(attrs.stDateValidation, function(currentValue) {
                if (currentValue && currentValue.day && currentValue.month && currentValue.year) {
                    var date = ("0" + currentValue.day).slice(-2) + "." + ("0" + currentValue.month).slice(-2) + "." + currentValue.year;
                    var valid = regx.test(date);
                    c.$setValidity("dateValid", valid);
                    c.$dirty = true;
                } else {
                    c.$setValidity("dateValid", false);
                    c.$dirty = true;
                }

            });
        }
    }
}]);

app.filter('stErrorMessagesFilter', function() {
    return function(items, field) {
        var result = {};
        angular.forEach(items, function(value, key) {
            if (value == field)
                result[key] = value;
        });
        return result;
    }
});

app.directive('stActive', ['$location',function($location) {
    return {
        restrict : 'A',
        link : function(scope, elem, attrs) {
            // var menuElems = elem.children('li');
            scope.$on('$routeChangeSuccess', function(event, prev, current) {
                $.each(elem.children('li'), function(i, value) {
                    var li = $(value);
                    var a = li.children('a');
                    if (a && $(a).attr('href')) {
                        var href = $(a).attr('href').replace('#!', '');
                        if (href === $location.path()) {
                            li.addClass('active');
                        } else {
                            li.removeClass('active');
                        }
                    }
                });
            });
        }
    }
}]);

app.directive('stEnsureUnique', ['Auth','$timeout',function(Auth, $timeout) {
    return {
        restrict : 'A',
        require : 'ngModel',
        link : function(scope, elem, attrs, ngModel) {

            var stop_timeout;
            scope.$watch(function() {
                return ngModel.$modelValue;
            }, function(value) {
                if (value) {
                    $timeout.cancel(stop_timeout);
                    stop_timeout = $timeout(function() {
                        scope.st.busy.push(attrs.name);
                        Auth[attrs["name"] + "Exist"](value, function(exist) {
                            var isValid = !exist;
                            ngModel.$setValidity('unique', isValid);
                            ngModel.$dirty = true;
                            scope.st.busy.splice(scope.st.busy.indexOf(attrs.name), 1);
                        });
                    }, 200);
                }
            })
        }
    }
}]);
app.directive('numbersOnly', function() {
    return {
        require : 'ngModel',
        link : function(scope, element, attrs, modelCtrl) {
            modelCtrl.$parsers.push(function(inputValue) {
                if (inputValue == undefined)
                    return ''
                var transformedInput = inputValue.replace(/[^0-9]/g, '');
                if (transformedInput != inputValue) {
                    modelCtrl.$setViewValue(transformedInput);
                    modelCtrl.$render();
                }

                return transformedInput;
            });
        }
    };
});

app.directive('stValidFormElem', function() {
    return {
        restrict : 'A',
        require : '^form',
        replace : true,
        transclude : true,
        scope : true,
        templateUrl : 'app/partials/valid-form-elem.html',
        compile : function(cElement, cAttributes, transclude) {
            return {
                pre : function(scope, elem, attrs, controllers) {
                    scope.stForm = scope[controllers.$name];
                    scope.stFormName = controllers.$name;
                    scope.stLabel = attrs['stLabel'];
                    scope.stName = attrs['stName'];
                    scope.stIcon = attrs['stIcon'];
                    scope.stInputGroup = scope.$eval(attrs['stInputGroup']);
                    if (scope.stInputGroup === undefined)
                        scope.stInputGroup = true;
                }
            }
        }
    }
});

app.directive('ngFocus', ['$parse',function($parse) {
    return function(scope, element, attr) {
        var fn = $parse(attr['ngFocus']);
        element.bind('focus', function(event) {
            scope.$apply(function() {
                fn(scope, {
                    $event : event
                });
            });
        });
    }
}]);

// app.filter('matchStringFilter', function(){
// return function(textToMatch, text){
// if(testToMatch.match(text)){
// return true;
// }
// return false;
// } ;
// });
