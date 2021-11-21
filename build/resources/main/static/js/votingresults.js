var resource = Vue.resource('/votingResults{/code}');

Vue.component('theme-header', {
  data: function() {
        return {
            header: '111',
            code: ''
        }
      },
  props: ['message'],
  template: '<div class="row">' +
              '<div class="col-md-3"></div>' +
              '<h1 class="col-md-6 text-center display-3">{{header}}</h1>' + 
              '<h2 class="col-md-3 display-4">Код доступа: {{ code }}</h2>' +
            '</div>',
  created: function(){
        resource.get({code: this.message}).then(response => {
          var output = response.bodyText.split(';');

          var textHeader = output.shift();
          var textCode = output.pop();


          this.header = textHeader;
          this.code = textCode;
        });


  }
});

Vue.component('voting-progress-bars', {
  data: function() {
    return {
        yesProgress: '',
        noProgress: '',
        neutralProgress: '',
        brokenProgress: '', 
        yesProgressLabel: '0',
        noProgressLabel: '0',
        neutralProgressLabel: '0',
        brokenProgressLabel: '0',
        isPrinted : false
    }
  },
  template: '<div class="row align-items-center">' + 
              '<div class="col-3"></div>' +

              '<div class="col-5 fs-3 mt-3">' +
                  '<div class="text-end ">' +
                    '<label>За</label>' +
                    '<progress max="100" :value="yesProgress"> {{ yesProgress }}% </progress>' +
                    '<label>( {{ yesProgressLabel }} )</label><br>' +
                  '</div>' +
                  '<div class="fs-3 text-end">' +
                    '<label>Против</label>' +
                    '<progress max="100" :value="noProgress"> {{ noProgress }}% </progress>' +
                    '<label>( {{ noProgressLabel }} )</label><br>' +
                  '</div>' +
                  '<div class="fs-3 text-end">' +
                    '<label>Воздержались</label>' +
                    '<progress max="100" :value="neutralProgress"> {{ neutralProgress }}% </progress>' +
                    '<label>( {{ neutralProgressLabel }} )</label><br>' +
                  '</div>' +
                  '<div class="fs-3 text-end">' +
                    '<label>Испорченные биллютени</label>' +
                    '<progress max="100" :value="brokenProgress""> {{ brokenProgress }}% </progress>' +
                    '<label>( {{ brokenProgressLabel }} )</label><br>' +
                  '</div>' +
              '</div>' +

              '<div class="col-4"></div>' +

              '<div class="text-center fs-3 mt-3">' +
                '<input type="button" class="btn-lg fs-3 btn btn-dark mx-1" value="Обновить" @click="update" />' +
                '<input type="button"  class="btn-lg fs-3 btn btn-dark mx-1" value="Печать" @click="print" />' +
                '<input type="button" class="btn-lg fs-3 btn btn-dark mx-1" value="Закрыть голосование" @click="close" />' +
              '</div>' +

            '</div>',
  methods: {
    update: function() {

      var count = 0;
      resource.get({code: "voteyes"}).then(response => {
        count += Number(response.bodyText)
        this.yesProgressLabel = response.bodyText;
        this.yesProgress = response.bodyText;
      });
      resource.get({code: "voteno"}).then(response => {
        count += Number(response.bodyText)
        this.noProgressLabel = response.bodyText;
        this.noProgress = response.bodyText;
      });
      resource.get({code: "voteneutral"}).then(response => {
        count += Number(response.bodyText)
        this.neutralProgressLabel = response.bodyText;
        this.neutralProgress = response.bodyText;
      });
      resource.get({code: "votebroken"}).then(response => {
        count += Number(response.bodyText)
        this.brokenProgressLabel = response.bodyText;
        this.brokenProgress = response.bodyText;

        this.yesProgress = ((Number(this.yesProgress) / count) * 100).toString(10);
        this.noProgress = ((Number(this.noProgress)  / count) * 100).toString(10);
        this.neutralProgress = ((Number(this.neutralProgress)  / count) * 100).toString(10);
        this.brokenProgress = ((Number(this.brokenProgress)  / count) * 100).toString(10);
      });


    },
    print: function() {
      resource.get({code: "print"}).then(response => {
        alert("Отчёты успешно напечатаны!");
        this.isPrinted = true;
        window.open('zip', '_blank');
      });
        
    },
    close: function() {

      if(this.isPrinted){
        resource.get({code: "closevoting"}).then(response => {
          count += Number(response.bodyText)
          this.yesProgressLabel = response.bodyText;
          this.yesProgress = response.bodyText;
        });
        resource.delete({code: "closevoting"}).then(response => {
          // success callback
          alert("Голосование закрыто");
          window.location.href = window.location.origin + '/createVoting/';
        }, response => {
          // error callback
          alert("Error: delete error");
        });
      } else{
        alert("Для того, чтобы закрыть голосование, сначала нужно распечатать результаты голосования!");
      }
      
    }
  }
});

var app = new Vue({
  el: '#app',
  template: '<div>' + 
              '<theme-header message="getHeader"/>' +
              '<voting-progress-bars />' +
            '</div>'

});