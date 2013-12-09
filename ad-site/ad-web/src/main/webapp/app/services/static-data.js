app.factory('ErrorFactory', function () {
    this.errorMessages = {
        pattern: {def:"Niewłaściwe znaki"},
        minlength: { def: 'Min {{data-ng-minlength}} znaków' },
        maxlength: { def: 'Max {{data-ng-maxlength}} znaków' },
        fieldMatch: { def: 'Pola nie są takie same' },
        email: { def: 'Niepoprawny email' },
        dateValid: { def: 'Niepoprawna data' },
        min: { def: 'Podana liczba musi być większa od {{data-min}}' },
        max: { def: 'Podana liczba musi być mniejsza od {{data-max}}' },
    }
    this.getErrorMessages = function(customErrorMessages){
        return $.extend(true, this.errorMessages, customErrorMessages);
    }
    return this;
});