var app = angular.module("apiwebasApp", ['ngMaterial', 'datatables', 'frontendServices', 'spring-security-csrf-token-interceptor'])
  .controller('ApiwebasController', ApiwebasController)
  .controller('SwapiController', SwapiController)
  .controller('CharactersController', CharactersController)
  .controller('ComicsController', ComicsController)
  .controller('SeriesController', SeriesController)
  .controller('SettingsController', SettingsController)
  .config(function($mdThemingProvider) {
    $mdThemingProvider.theme('docs-dark')
      .primaryPalette('brown')
      .accentPalette('red');
    // .dark();
  });

function ApiwebasController($scope, $templateRequest, $sce, $compile, $http, UtilService, UserService) {
  var templateUrl = $sce.getTrustedResourceUrl('templates/tabs.html');

  $templateRequest(templateUrl).then(function(template) {
    $compile($("#tabsContent").html(template).contents())($scope);

    this.character = {
      id: ''
    };

    var deckId = '';

    $http.get('/api/marvel/settings/').then(function(response) {
      $scope.key = {
        public: response.data[0],
        private: response.data[1]
      };
    });

    // TODO validation of keys...
    $scope.generate = function() {
      $http.get('/api/cards/deck').then(function(response) {

        deckId = response.data.deckId;

        var str = JSON.stringify(response, undefined, 4);
        $scope.data = $sce.trustAsHtml(UtilService.syntaxHighlight(str));

        $('#deckInfo').removeClass('hidden');
        $('#btnDrawCards').removeClass('hidden');
      });
    };

    $scope.drawCards = function() {
      $http.get('/api/cards/card/' + deckId + '/' + $scope.numberOfCards).then(function(response) {

        $('#deckInfo').addClass('hidden');
        $('#card').removeClass('hidden');

        Object.keys(response.data.cards).forEach(function(key) {

          $('<img />', {
            src: response.data.cards[key].image
          }).appendTo($('#card'));

        });
      });
    };

    $scope.save = function() {
      $http.get('/api/marvel/settings/' + $scope.key.public + '/' + $scope.key.private).then(function(response) {
        alert(response);
      });
    };

    $scope.search = function() {
      $http.get('/api/marvel/characters/' + $scope.character.id).then(function(response) {
        var str = JSON.stringify(response.data, undefined, 4);
        $scope.data = $sce.trustAsHtml(UtilService.syntaxHighlight(str));
      });
    };

    $scope.searchMovie = function() {
        $http.get('/api/tmdb/movie/' + $scope.movie.id).then(function(response) {
        var str = JSON.stringify(response.data, undefined, 4);
        $scope.data = $sce.trustAsHtml(UtilService.syntaxHighlight(str));
      });
    };

  }, function() {
    // An error has occurred
  });

  $scope.logout = function() {
    UserService.logout();
  }

}

function SettingsController($scope, $templateRequest, $sce, $compile, $http, UserService) {

  $http.get('/api/marvel/settings/').then(function(response) {
    $scope.key.public = response.data[0];
    $scope.key.private = response.data[1];
  });

}

function SwapiController($scope, $compile, $http, $sce, $mdDialog, DTOptionsBuilder, DTColumnBuilder, DatatablesService, UtilService) {
  var vm = this;

  vm.dtOptions = DatatablesService.initDatatablesOptions(DTOptionsBuilder, '/api/swapi/films', []);

  vm.dtColumns = [
    DTColumnBuilder.newColumn(null).notSortable().withClass('details-control').withOption('defaultContent', ''),
    DTColumnBuilder.newColumn('episodeId').withTitle('Episode ID'),
    DTColumnBuilder.newColumn('title').withTitle('Title'),
    DTColumnBuilder.newColumn('director').withTitle('Director'),
    DTColumnBuilder.newColumn('producer').withTitle('Producer'),
    DTColumnBuilder.newColumn('release_date').withTitle('Release Date')
  ];

  vm.dtInstance = {};
  vm.dtInstanceCallback = dtInstanceCallback;

  function dtInstanceCallback(dtInstance) {
    vm.dtInstance = dtInstance;
  }

  $('#swapi').on('click', 'td.details-control', function() {
    var tr = $(this).closest('tr');
    var row = vm.dtInstance.DataTable.row(tr);

    if (row.child.isShown()) {
      // This row is already open - close it
      row.child.hide();
      tr.removeClass('shown');
    } else {
      // Open this row
      row.child(row.data().openingCrawl).show();
      tr.addClass('shown');

      // add table with links
      row.child(createTable(row.data().speciesUrls)).show();
    }

    function createTable(data) {
      var table = $('<table class="table"></table>');
      $.each(data, function(i, list) {
        /* filling table */
        var $tr = $('<tr id="' + i + '">');
        $tr.append($('<td>').text(i));

        if (typeof list == 'string' || typeof list == 'number') {
          var a = $('<a />');
          a.attr('href', list);
          a.text(list);
          a.click(function(ev) {

            ev.preventDefault();
            var speciesId = this.href.replace(/\D+/g, '');
            $http.get('/api/swapi/species/' + speciesId).then(function(response) {

              data = response;

              $mdDialog.show({
                  controller: DialogController,
                  templateUrl: 'templates/dialogSpecies.html',
                  parent: angular.element(document.body),
                  // targetEvent: ev,
                  clickOutsideToClose: true,
                  fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
                })
                .then(function(answer) {
                  $scope.status = 'You said the information was "' + answer + '".';
                }, function() {
                  $scope.status = 'You cancelled the dialog.';
                });
            });

            function DialogController($scope, $mdDialog, $sce) {
              // $scope.data = data;
              $scope.data = $sce.trustAsHtml(UtilService.syntaxHighlight(JSON.stringify(data)));
              $scope.hide = function() {
                $mdDialog.hide();
              };
              $scope.cancel = function() {
                $mdDialog.cancel();
              };
              $scope.answer = function(answer) {
                $mdDialog.hide(answer);
              };
            }

          });

          $tr.append($('<td>').append(a));
        } else {
          $tr.append($('<td>').append(createTable(list)));
        }
        table.append($tr);
      });
      return table;
    }
  });
}

function CharactersController($scope, $compile, $http, $sce, $mdDialog, DTOptionsBuilder, DTColumnBuilder, DatatablesService, UtilService) {
  var vm = this;
  vm.message = '';
  vm.someClickHandler = someClickHandler;
  // FIXME still try to use csrf later...

  vm.dtOptions = DatatablesService.initDatatablesOptions(DTOptionsBuilder, '/api/marvel/characters', [2, 'asc']);

  vm.dtColumns = [
    DTColumnBuilder.newColumn(null).withOption('defaultContent', '').renderWith(actionsHtml),
    DTColumnBuilder.newColumn('id').withTitle('ID'),
    DTColumnBuilder.newColumn('name').withTitle('Name'),
    DTColumnBuilder.newColumn('description').withTitle('Description').withOption('sWidth', '50%'),
    DTColumnBuilder.newColumn('modified').withTitle('Modified')
  ];

  showCharInfo = function(ev, id) {
    var characterId = id;
    var data;
    $http.get('/api/marvel/characters/' + characterId).then(function(response) {
      data = response.data.response;

      $mdDialog.show({
          controller: DialogController,
          templateUrl: 'templates/dialogChar.html',
          parent: angular.element(document.body),
          // targetEvent: ev,
          clickOutsideToClose: true,
          fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
        })
        .then(function(answer) {
          $scope.status = 'You said the information was "' + answer + '".';
        }, function() {
          $scope.status = 'You cancelled the dialog.';
        });
    });

    function DialogController($scope, $mdDialog) {
      $scope.data = data;
      $scope.hide = function() {
        $mdDialog.hide();
      };
      $scope.cancel = function() {
        $mdDialog.cancel();
      };
      $scope.answer = function(answer) {
        $mdDialog.hide(answer);
      };
    }
  }

  function createdRow(row, data, dataIndex) {
    // Recompiling so we can bind Angular directive to the DT
    $compile(angular.element(row).contents())($scope);
  }

  function actionsHtml(data, type, full, meta) {
    return '<md-button class="md-primary md-raised" onclick="showCharInfo(event,' + data.id + ')">Show</md-button>';
    // return '<md-button class="md-primary btn btn-primary" onclick="alert(\'aa\')">Show</md-button>';
    // return '<md-button class="md-primary btn btn-primary" ng-click="$scope.charContr.showAdvanced($event)">Show</md-button>';
  }

  $scope.dtInstanceCallback = function(instance) {
    $scope.dtInstance = instance;
  }

  function someClickHandler(info) {
    vm.message = info.id + ' - ' + info.name;
  }

  function rowCallback(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
    $('td', nRow).unbind('click');
    $('td', nRow).bind('click', function() {
      $scope.$apply(function() {
        vm.someClickHandler(aData);
      });
    });
    return nRow;
  }
}

function ComicsController($scope, $compile, $http, $sce, $mdDialog, DTOptionsBuilder, DTColumnBuilder, DatatablesService, UtilService) {
  var vm = this;
  vm.message = '';
  vm.someClickHandler = someClickHandler;
  // FIXME still try to use csrf later...

  vm.dtOptions = DatatablesService.initDatatablesOptions(DTOptionsBuilder, '/api/marvel/comics', [2, 'asc']);

  vm.dtColumns = [
    DTColumnBuilder.newColumn(null).withOption('defaultContent', '').renderWith(actionsHtml),
    DTColumnBuilder.newColumn('id').withTitle('ID'),
    DTColumnBuilder.newColumn('title').withTitle('Title'),
    DTColumnBuilder.newColumn('issueNumber').withTitle('Issue Number'),
    DTColumnBuilder.newColumn('pageCount').withTitle('Page Count'),
    DTColumnBuilder.newColumn('modified').withTitle('Modified'),
    DTColumnBuilder.newColumn('format').withTitle('Format')
  ];

  showComicInfo = function(ev, id) {
    var comicId = id;
    var data;
    $http.get('/api/marvel/comics/' + comicId).then(function(response) {
      data = response.data.response;

      $mdDialog.show({
          controller: DialogController,
          templateUrl: 'templates/dialogChar.html',
          parent: angular.element(document.body),
          // targetEvent: ev,
          clickOutsideToClose: true,
          fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
        })
        .then(function(answer) {
          $scope.status = 'You said the information was "' + answer + '".';
        }, function() {
          $scope.status = 'You cancelled the dialog.';
        });
    });

    function DialogController($scope, $mdDialog) {
      $scope.data = data;
      $scope.hide = function() {
        $mdDialog.hide();
      };
      $scope.cancel = function() {
        $mdDialog.cancel();
      };
      $scope.answer = function(answer) {
        $mdDialog.hide(answer);
      };
    }
  }

  function createdRow(row, data, dataIndex) {
    // Recompiling so we can bind Angular directive to the DT
    $compile(angular.element(row).contents())($scope);
  }

  function actionsHtml(data, type, full, meta) {
    return '<md-button class="md-primary md-raised" onclick="showComicInfo(event,' + data.id + ')">Show</md-button>';
    // return '<md-button class="md-primary btn btn-primary" onclick="alert(\'aa\')">Show</md-button>';
    // return '<md-button class="md-primary btn btn-primary" ng-click="$scope.charContr.showAdvanced($event)">Show</md-button>';
  }

  $scope.dtInstanceCallback = function(instance) {
    $scope.dtInstance = instance;
  }

  $('#comics').on('click', 'td.details-control', function() {
    var tr = $(this).closest('tr');
    var row = vm.dtInstance.DataTable.row(tr);

    if (row.child.isShown()) {
      // This row is already open - close it
      row.child.hide();
      tr.removeClass('shown');
    } else {
      // Open this row
      UtilService.fillRow(vm.dtInstance.id, row);
      // row.child(format(row.data())).show();
      tr.addClass('shown');
    }
    // modification after creation

  });

  function someClickHandler(info) {
    vm.message = info.id + ' - ' + info.name;
  }

  function rowCallback(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
    $('td', nRow).unbind('click');
    $('td', nRow).bind('click', function() {
      $scope.$apply(function() {
        vm.someClickHandler(aData);
      });
    });
    return nRow;
  }
}

function SeriesController($scope, $http, DTOptionsBuilder, DTColumnBuilder, DatatablesService, UtilService) {
  var vm = this;
  vm.message = '';
  vm.someClickHandler = someClickHandler;
  // FIXME still try to use csrf later...

  vm.dtOptions = DatatablesService.initDatatablesOptions(DTOptionsBuilder, '/api/marvel/series', [2, 'asc']);

  vm.dtColumns = [
    DTColumnBuilder.newColumn(null).withClass('details-control').withOption('defaultContent', ''),
    DTColumnBuilder.newColumn('id').withTitle('ID'),
    DTColumnBuilder.newColumn('title').withTitle('Title'),
    DTColumnBuilder.newColumn('startYear').withTitle('Start Year'),
    DTColumnBuilder.newColumn('endYear').withTitle('End Year'),
    DTColumnBuilder.newColumn('modified').withTitle('Modified')
  ];

  vm.dtInstance = {};
  vm.dtInstanceCallback = dtInstanceCallback;

  function dtInstanceCallback(dtInstance) {
    vm.dtInstance = dtInstance;
  }

  $('#series').on('click', 'td.details-control', function() {
    var tr = $(this).closest('tr');
    var row = vm.dtInstance.DataTable.row(tr);

    if (row.child.isShown()) {
      // This row is already open - close it
      row.child.hide();
      tr.removeClass('shown');
    } else {
      // Open this row
      UtilService.fillRow(vm.dtInstance.id, row);
      // row.child(format(row.data())).show();
      tr.addClass('shown');
    }
    // modification after creation
    var imagesTable = $('#images').children()[1].firstChild;


  });

  function someClickHandler(info) {
    vm.message = info.id + ' - ' + info.name;
  }

  function rowCallback(nRow, aData, iDisplayIndex, iDisplayIndexFull) {
    $('td', nRow).unbind('click');
    $('td', nRow).bind('click', function() {
      $scope.$apply(function() {
        vm.someClickHandler(aData);
      });
    });
    return nRow;
  }
}

$(document).ready(function() {
  // Setup - add a text input to each footer cell
  $('#dataTableComic tfoot th').each(function() {
    var title = $(this).text();
    var value = $(this);
    if (title === "Type") {
      var option = $('<select id="selectType"></select>');
      value.html(option);
      $.getJSON('../api/marvel/series/type', function(data) {
        Object.keys(data).forEach(function(key) {
          option.append('<option value=' + data[key] + '>' + data[key] + '</option>');
        });
      });
    } else if (title === "Details") {
      // Do nothing
    } else {
      $(this).html('<input type="text" class="inputForSearch" id="search_' + title + '" />');
    }
  });

  // DataTable
  var table = $('#dataTableComic').DataTable();

  // Apply the search
  table.columns().every(function() {
    var that = this;

    $('input', this.footer()).on('keyup change', function() {
      if (that.search() !== this.value) {
        that
          .search(this.value)
          .draw();
      }
    });
    $('#selectType', this.footer()).on('change', function() {
      if (that.search() !== this.value) {
        that
          .search(this.value)
          .draw();
      }
    });
  });
});

app.run(function($rootScope) {
  // ...
});
