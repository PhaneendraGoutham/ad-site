var test = angular.module('st-auth-module-test', ['ngMockE2E']);
test.run(function ($httpBackend) {
    var user = { displayName: "maniekstasz", imageUrl: "asdf", roles: ["user"], authToken:"asdfasfasewqrzxdxzcivqqrqwevrwqejdjfl", id:"11" };
    var ad = { title: "Bmw reklama", videoUrl: "http://youtu.be/4KVVDdnGtdM", user: { displayName: "maniekstasz", imageUrl: "resources/static/no-user.gif" }, creationDate: "1382523642", description: "adsfasdfasdfasdfasdfasfsdfasdf", rank:'3.75' };
    var tags = [{ "name": "napoje", "selected": false, id: 5 }, { "name": "piwa", "selected": false, id: 2 }, { "name": "soki", "selected": false, id: 1 }, ];
    var brands = [{ "name": "Coca cola", "selected": false, id: 2 }, { "name": "Coca cola", "selected": false, id: 2 }, { "name": "BMW", "selected": false, id: 3 }]
    $httpBackend.whenGET(/app\/partials\/.*/).passThrough();
      $httpBackend.whenPOST('/user/username').passThrough();
    $httpBackend.whenPOST('/user/email').passThrough();
    $httpBackend.whenGET(/\/ad.*/).passThrough();
    $httpBackend.whenGET(/\/uzytkownik\/11\/reklamy/).respond([ad]);
    $httpBackend.whenGET('/tag').respond(tags);
    $httpBackend.whenGET('/brand').respond(brands);
    // returns the current list of phones

    $httpBackend.whenGET('http://localhost:8080/user/username').passThrough();
    $httpBackend.whenPOST('/user/email').passThrough();
    $httpBackend.whenPOST('/user/login').respond(user);
    $httpBackend.whenPOST('/ad/rate').respond({ rank: '4.75' });
    $httpBackend.whenPOST('/user/password/recover').respond({});
    $httpBackend.whenPOST('/user/password/change').respond({});
    $httpBackend.whenPOST('/user').respond(true);
  
 
   
   
});