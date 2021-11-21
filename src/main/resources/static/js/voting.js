var resource = Vue.resource('/clientVoting{/code}');

Vue.component('theme-header', {
  data: function() {
        return {
            header: '111'
        }
      },
  props: ['message'],
  template: '<div class="text-center mb-3"> <h3 class="display-3">{{header}}</h3> </div>',
  created: function(){
        resource.get({code: this.message}).then(response => {
          var textHeader = response.bodyText;

          this.header = textHeader;
          

        });
  }
});

Vue.component('checkboxes', {
  data: function() {
    return {
      selectedVariants:[]
    }
  },
  template: '<div class="row justify-content-md-center fs-3">' +
                  '<div class="col-8 col-sm-5 col-md-3 col-lg-3 col-xxl-2 my-3 mx-auto">' +
                    '<div class="container-fluid">' +
                      '<input type="checkbox" class="form-check-input my-2" name="first" value="yes" v-model="selectedVariants">' +
                      '<label class="form-check-label " for="first">За</label><br>'  +
                    '</div>' +
                    '<div class="container-fluid">' +
                      '<input type="checkbox" class="form-check-input my-2" name="second" value="no" v-model="selectedVariants">' +
                      '<label class="form-check-label my-1" for="second">Против</label><br>'  +
                    '</div>' +
                    '<div class="container-fluid">' +
                      '<input type="checkbox" class="form-check-input my-2" name="third" value="neutral" v-model="selectedVariants">' +
                      '<label  class="form-check-label my-1" for="third">Воздержусь</label><br>'  +
                    '</div>' +
                    '<div class="container-fluid">' +
                        '<input type="button" class="btn-lg btn btn-dark fs-3 my-1" value="Проголосовать" @click="save" />' +
                    '</div>' +
                  '</div>' +
            '</div>',
  methods: {
    save: function() {
      var selectedVariantsString = ''
      for (let index = 0; index < this.selectedVariants.length; index++) {
        selectedVariantsString = selectedVariantsString + this.selectedVariants[index] + ';'; 
      }
      resource.update({code: "toVote"}, selectedVariantsString).then(result => {
        alert('Вы проголосовали')
        window.location.href = '/';
      }); 

      /*resource.save({code: "toVote"}, { selectedVariantsString }).then(response => {
        // success callback
        alert('Вы проголосовали')

      }, response => {
        // error callback
      }); */
    }
  }
});

var app = new Vue({
  el: '#app',
  template: '<div>' + 
              '<theme-header message="getHeader"/>' +
              '<checkboxes/>' +
            '</div>'

});