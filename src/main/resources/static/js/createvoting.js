var resource = Vue.resource('/createVoting{/code}');

Vue.component('text-input-section', {
  data: function() {
    return {
        text: ''
    }
  },
  template:
      '<div class="container">' +
        '<div class="col text-center mt-3">' +
          '<textarea class="col-5 fs-3 mb-1" placeholder="Введите тему обсуждения, которая будет вынесена на голосовании..." v-model="text" rows="3" /><br>' +
          '<input type="button" class="btn-lg col-5 fs-3 btn btn-dark text-center mx-auto" value="Создать" @click="save" />' +
        '</div>' +
      '</div>',
  methods: {
    save: function() {
      resource.update({code: "createQuestion"}, this.text).then(result => {
        if(result.body == "NO_CONTENT")
          alert('Ошибка')
        else if(result.body == "OK"){
          alert('Nice boy')
          window.location.href = window.location.origin + '/votingResults/';
        }
        
      }); 
    }}
});

var app = new Vue({
  el: '#app',
  template: '<text-input-section />'

});