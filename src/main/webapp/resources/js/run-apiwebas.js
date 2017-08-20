///////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// Defines the javascript files that need to be loaded and their dependencies.
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////

require.config({
    paths: {
        angular: '../bower_components/angular/angular',
        ngAnimate: '../bower_components/angular-animate/angular-animate',
        ngAria: '../bower_components/angular-aria/angular-aria',
        ngMessages: '../bower_components/angular-messages/angular-messages',
        ngMaterial: '../bower_components/angular-material/angular-material',
        ngDatatables: 'angular-datatables.min',
        datatables: 'jquery.dataTables.min',
        csrfInterceptor: '../bower_components/spring-security-csrf-token-interceptor/dist/spring-security-csrf-token-interceptor.min',
        lodash: "../bower_components/lodash/dist/lodash",
        jQuery: "jquery.min",
        frontendServices: 'frontend-services',
        apiwebasApp: "apiwebas-app"
    },
    shim: {
        jQuery: {
            exports: "jQuery"
        },
        angular: {
            exports: "angular"
        },
        lodash: {
            exports: '_'
        },
        ngAnimate: {
            deps: ['angular']
        },
        ngAria: {
            deps: ['angular']
        },
        datatables: {
            deps: ['jQuery']
        },
        ngDatatables: {
            deps: ['angular', 'jQuery']
        },
        csrfInterceptor: {
            deps: ['angular']
        },
        ngMessages: {
            deps: ['angular']
        },
        ngMaterial: {
             deps: ['angular', 'ngAnimate', 'ngAria']
        },
        frontendServices: {
            deps: ['angular', 'lodash', 'csrfInterceptor', 'ngDatatables', 'datatables']
        },
        apiwebasApp: {
            deps: [ 'angular', 'jQuery', 'ngMessages', 'ngMaterial', 'frontendServices']
        }
    }
});

require(['apiwebasApp'], function () {

    angular.bootstrap(document.getElementById('apiwebasApp'), ['apiwebasApp']);

});
