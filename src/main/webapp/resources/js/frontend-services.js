angular.module('frontendServices', [])
  .service("UtilService", function($http) {
    return {
      syntaxHighlight: function(json) {
        json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
        return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function(match) {
          var cls = 'number';
          if (/^"/.test(match)) {
            if (/:$/.test(match)) {
              cls = 'key';
            } else {
              cls = 'string';
            }
          } else if (/true|false/.test(match)) {
            cls = 'boolean';
          } else if (/null/.test(match)) {
            cls = 'null';
          }
          return '<span class="' + cls + '">' + match + '</span>';
        });
      },
      fillRow: function(getPath, row) {
        function createTable(data) {
          var table = $('<table class="table"></table>');
          $.each(data, function(i, list) {
            /* filling table */
            var $tr = $('<tr id="' + i + '">');
            $tr.append($('<td>').text(i));

            if (typeof list == 'string' || typeof list == 'number') {
              $tr.append($('<td>').text(list));
            } else {
              $tr.append($('<td>').append(createTable(list)));
            }
            table.append($tr);
          });
          return table;
        }

        $http.get('/api/marvel/' + getPath + '/' + row.data().id).then(function(data) {
          row.child(createTable(data.data.response)).show();
        });
      }
    }
  })
  .service('ComicsService', function($http, $q, DTOptionsBuilder, DTColumnBuilder) {
    this.init = function() {

    }
  })
  .service('MealService', ['$http', '$q', function($http, $q) {
    return {
      searchMeals: function(fromDate, fromTime, toDate, toTime, pageNumber) {
        var deferred = $q.defer();

        function prepareTime(time) {
          return time ? '1970/01/01 ' + time : null;
        }

        $http.get('/meal/', {
            params: {
              fromDate: fromDate,
              toDate: toDate,
              fromTime: prepareTime(fromTime),
              toTime: prepareTime(toTime),
              pageNumber: pageNumber
            }
          })
          .then(function(response) {
            if (response.status == 200) {
              deferred.resolve(response.data);
            } else {
              deferred.reject('Error retrieving list of meals');
            }
          });

        return deferred.promise;
      },

      deleteMeals: function(deletedMealIds) {
        var deferred = $q.defer();

        $http({
            method: 'DELETE',
            url: '/meal',
            data: deletedMealIds,
            headers: {
              "Content-Type": "application/json"
            }
          })
          .then(function(response) {
            if (response.status == 200) {
              deferred.resolve();
            } else {
              deferred.reject('Error deleting meals');
            }
          });

        return deferred.promise;
      },

      saveMeals: function(dirtyMeals) {
        var deferred = $q.defer();

        $http({
            method: 'POST',
            url: '/meal',
            data: dirtyMeals,
            headers: {
              "Content-Type": "application/json",
              "Accept": "text/plain, application/json"
            }
          })
          .then(function(response) {
            if (response.status == 200) {
              deferred.resolve();
            } else {
              deferred.reject("Error saving meals: " + response.data);
            }
          });

        return deferred.promise;
      }
    }
  }])
  .service('UserService', ['$http', '$q', function($http, $q) {
    return {
      getUserInfo: function() {
        var deferred = $q.defer();

        $http.get('/user')
          .then(function(response) {
            if (response.status == 200) {
              deferred.resolve(response.data);
            } else {
              deferred.reject('Error retrieving user info');
            }
          });

        return deferred.promise;
      },
      updateMaxCaloriesPerDay: function(maxCaloriesPerDay) {
        var deferred = $q.defer();

        $http.put('/user', maxCaloriesPerDay)
          .then(function(response) {
            if (response.status == 200) {
              deferred.resolve();
            } else {
              deferred.reject('Error saving max calories per day');
            }
          });

        return deferred.promise;
      },
      logout: function() {
        $http({
            method: 'POST',
            url: '/logout'
          })
          .then(function(response) {
            if (response.status == 200) {
              window.location.reload();
            } else {
              console.log("Logout failed!");
            }
          });
      }
    };
  }]);
