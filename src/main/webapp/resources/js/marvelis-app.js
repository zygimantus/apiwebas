angular.module("marvelisApp", ['ngMaterial', 'datatables', 'frontendServices', 'spring-security-csrf-token-interceptor'])
  .service("worldsService", [MyDataService])
  .controller('MarvelController', MarvelController)
  .controller('SwapiController', SwapiController)
  .controller('CharactersController', CharactersController)
  .controller('ComicsController', ComicsController)
  .controller('SeriesController', SeriesController)
  .config(function($mdThemingProvider) {
    $mdThemingProvider.theme('docs-dark')
      .primaryPalette('brown')
      .accentPalette('red');
    // .dark();
    console.log('configuring application');
  })
  .run(["$rootScope", "$window", startup]);

function MarvelController($scope, $templateRequest, $sce, $compile, $http, UserService) {
  var templateUrl = $sce.getTrustedResourceUrl('templates/tabs.html');

  $templateRequest(templateUrl).then(function(template) {
    $compile($("#tabsContent").html(template).contents())($scope);

    this.character = {
      id: ''
    };

    // TODO validation of keys...
    $scope.save = function() {
      $http.get('/api/marvel/settings/' + $scope.key.public + '/' + $scope.key.private).then(function(response) {
        alert(response);
      });
    };

    $scope.search = function(characterId) {
      $http.get('/api/marvel/characters/' + characterId).then(function(response) {
        var str = JSON.stringify(response.data, undefined, 4);
        $scope.data = $sce.trustAsHtml(syntaxHighlight(str));
      });
    };

  }, function() {
    // An error has occurred
  });

  $scope.logout = function() {
    UserService.logout();
  }

}

function SwapiController($scope, $compile, $http, $sce, $mdDialog, DTOptionsBuilder, DTColumnBuilder, UtilService) {
  var vm = this;
  vm.dtOptions = DTOptionsBuilder.newOptions()
    .withOption('ajax', {
      url: '/api/swapi/films',
      type: 'post',
      data: function(data) {
        return JSON.stringify(data);
      },
      dataType: "json",
      contentType: 'application/json;charset=UTF-8'
    })
    .withDataProp(function(json) {
      json.recordsTotal = json.count;
      json.recordsFiltered = json.count;
      return json.results;
    })
    .withOption('processing', true)
    .withOption('serverSide', true)
    .withOption('saveState', true)
    .withPaginationType('full_numbers')
    .withOption('order', [])
    .withDisplayLength(10);
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
              $scope.data = $sce.trustAsHtml(syntaxHighlight(JSON.stringify(data)));
              // $('#speciesInfo').append(syntaxHighlight(JSON.stringify(data)));
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

            function syntaxHighlight(json) {
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

function CharactersController($scope, $compile, $http, $sce, $mdDialog, DTOptionsBuilder, DTColumnBuilder, UtilService) {
  var vm = this;
  vm.message = '';
  vm.someClickHandler = someClickHandler;
  // FIXME still try to use csrf later...
  vm.dtOptions = DTOptionsBuilder.newOptions()
    .withOption('ajax', {
      url: '/api/marvel/characters',
      type: 'post',
      data: function(data) {
        return JSON.stringify(data);
      },
      dataType: "json",
      contentType: 'application/json;charset=UTF-8'
    })
    .withDataProp(function(json) {
      json.recordsTotal = json.response.total;
      json.recordsFiltered = json.response.total;
      return json.response.characters;
    })
    .withOption('processing', true)
    .withOption('serverSide', true)
    .withOption('saveState', true)
    .withPaginationType('full_numbers')
    .withOption('order', [2, 'asc'])
    .withOption('createdRow', createdRow)
    .withOption('rowCallback', rowCallback)
    // .withOption('drawCallback', )
    .withDisplayLength(10);
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

function ComicsController($scope, $compile, $http, $sce, $mdDialog, DTOptionsBuilder, DTColumnBuilder, UtilService) {
  var vm = this;
  vm.message = '';
  vm.someClickHandler = someClickHandler;
  // FIXME still try to use csrf later...
  vm.dtOptions = DTOptionsBuilder.newOptions()
    .withOption('ajax', {
      url: '/api/marvel/comics',
      type: 'post',
      data: function(data) {
        return JSON.stringify(data);
      },
      dataType: "json",
      contentType: 'application/json;charset=UTF-8'
    })
    .withDataProp(function(json) {
      json.recordsTotal = json.response.total;
      json.recordsFiltered = json.response.total;
      return json.response.comics;
    })
    .withOption('processing', true)
    .withOption('serverSide', true)
    .withOption('saveState', true)
    .withPaginationType('full_numbers')
    .withOption('rowCallback', rowCallback)
    .withOption('order', [2, 'asc'])
    .withDisplayLength(10);
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

function SeriesController($scope, $http, DTOptionsBuilder, DTColumnBuilder, ComicsService, UtilService) {
  var vm = this;
  vm.message = '';
  vm.someClickHandler = someClickHandler;
  // FIXME still try to use csrf later...
  vm.dtOptions = DTOptionsBuilder.newOptions()
    .withOption('ajax', {
      url: '/api/marvel/series',
      type: 'post',
      data: function(data) {
        return JSON.stringify(data);
      },
      dataType: "json",
      contentType: 'application/json;charset=UTF-8'
    })
    .withDataProp(function(json) {
      json.recordsTotal = json.response.total;
      json.recordsFiltered = json.response.total;
      return json.response.series;
    })
    .withOption('processing', true)
    .withOption('serverSide', true)
    .withOption('saveState', true)
    .withPaginationType('full_numbers')
    .withOption('rowCallback', rowCallback)
    .withOption('order', [2, 'asc'])
    .withDisplayLength(10);
  vm.dtColumns = [
    DTColumnBuilder.newColumn(null).withClass('details-control').withOption('defaultContent', ''),
    DTColumnBuilder.newColumn('id').withTitle('ID'),
    DTColumnBuilder.newColumn('title').withTitle('Title'),
    DTColumnBuilder.newColumn('description').withTitle('Description').withOption('sWidth', '50%'),
    DTColumnBuilder.newColumn('modified').withTitle('Modified'),
    // DTColumnBuilder.newColumn('type').withTitle('Type')
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
