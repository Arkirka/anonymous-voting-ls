

var resource = Vue.resource('/clientEnter{/code}');

Vue.component('code-input', {
    data: function() {
      return {
          text: ''
      }

    },
    template:
        '<div class="container">' +
            '<div>' +
                '<h1 class="text-center mb-4 display-1" v-bind:class="classObject">Введите код доступа</h1>' +
            '</div>' +
            '<div class="col text-center mt-3">' +
                '<input type="text" class="col-4 fs-3 mb-2" placeholder="Введите код..." v-model="text" /><br>' +
                '<input type="button" class="btn-lg col-4 fs-3 btn btn-dark text-center mx-auto" value="Войти" @click="save" />' +
            '</div>' +
        '</div>',
        methods: {
          save: function() {
            var message =  { text: this.text };

            resource.get({code: message.text}).then(response => {
                var temp = response.bodyText;

                if(temp == "ok"){
                  resource.get({code: "setCookie"}).then(response => {
                    this.$cookie.set(response.$cookie);
                  });
                  window.location.href = 'clientVoting/';
                } if(temp == "used")
                  alert('Код доступа уже был использован. Повторное голосование невозможно.')
                else if(temp == "no")
                  alert('Неверный код доступа');

              });

            
          }
        }
    
});

Vue.component('messages-list', {
    props: ['messages'],
    template: '<div><div v-for="message in messages">{{message.text}}</div></div>',
    created: function(){
        messageApi.get().then(result =>
            console.log(result)
        )
    }
});

var app = new Vue({
  el: '#app',
  template: '<code-input />'
  
});