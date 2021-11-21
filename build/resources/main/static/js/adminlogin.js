var resource = Vue.resource('/adminLogin{/code}');

Vue.component('auth-section', {
  data: function() {
    return {
        loginText: '',
        passwordText: ''
    }
  },
  template:
        '<div class="container">' +
          '<div class="col text-center mt-3">' +
            '<input class="col-4 fs-3 mb-1" type="text" placeholder="Логин" v-model="loginText" /><br>' +
            '<input class="col-4 fs-3 mb-2" type="password" placeholder="Пароль" v-model="passwordText" /><br>' +
            '<input type="button" class="btn-lg col-4 fs-3 btn btn-dark text-center mx-auto" value="Войти" @click="save" />' +
          '</div>' +
        '</div>',
  methods: {
    save: function() {
      var authData = this.loginText + ';' + this.passwordText

      resource.update({code: "authed"}, authData).then(result => {
        if(result.body == "NOT_ACCEPTABLE"){
          alert('Неверный логин или пароль')
        } else if(result.body == "NO_CONTENT")
          alert('Ошибка')
        else if(result.body == "OK"){
          window.location.href = window.location.origin + '/createVoting/';
        }
        
      }); 
    }}
});

var app = new Vue({
  el: '#app',
  template: '<auth-section />'

});