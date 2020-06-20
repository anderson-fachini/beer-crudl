var beers = [];

function findbeer (beerId) {
  return beers[findbeerKey(beerId)];
}

function findbeerKey (beerId) {
  for (var key = 0; key < beers.length; key++) {
    if (beers[key].id == beerId) {
      return key;
    }
  }
}

var beerService = {
  findAll(fn) {
    axios
      .get('/api/v1/beers')
      .then(response => fn(response))
      .catch(error => console.log(error))
  },

  findById(id, fn) {
    axios
      .get('/api/v1/beers/' + id)
      .then(response => fn(response))
      .catch(error => console.log(error))
  },

  create(beer, fn) {
    axios
      .post('/api/v1/beers', beer)
      .then(response => fn(response))
      .catch(error => console.log(error))
  },

  update(id, beer, fn) {
    axios
      .put('/api/v1/beers/' + id, beer)
      .then(response => fn(response))
      .catch(error => console.log(error))
  },

  deletebeer(id, fn) {
    axios
      .delete('/api/v1/beers/' + id)
      .then(response => fn(response))
      .catch(error => console.log(error))
  }
}

var List = Vue.extend({
  template: '#beer-list',
  data: function () {
    return {beers: [], searchKey: ''};
  },
  computed: {
    filteredbeers() {
      return this.beers.filter((beer) => {
      	return beer.name.indexOf(this.searchKey) > -1
      	  || beer.description.indexOf(this.searchKey) > -1
      	  || beer.price.toString().indexOf(this.searchKey) > -1
      })
    }
  },
  mounted() {
    beerService.findAll(r => {
    	this.beers = r.data._embedded.beers; beers = r.data._embedded.beers;
    })
  }
});

var beer = Vue.extend({
  template: '#beer',
  data: function () {
    return {beer: findbeer(this.$route.params.beer_id)};
  }
});

var beerEdit = Vue.extend({
  template: '#beer-edit',
  data: function () {
    return {beer: findbeer(this.$route.params.beer_id)};
  },
  methods: {
    updatebeer: function () {
      beerService.update(this.beer.id, this.beer, r => router.push('/'))
    }
  }
});

var beerDelete = Vue.extend({
  template: '#beer-delete',
  data: function () {
    return {beer: findbeer(this.$route.params.beer_id)};
  },
  methods: {
    deletebeer: function () {
      beerService.deletebeer(this.beer.id, r => router.push('/'))
    }
  }
});

var Addbeer = Vue.extend({
  template: '#add-beer',
  data() {
    return {
      beer: {name: '', description: '', price: 0}
    }
  },
  methods: {
    createbeer() {
      beerService.create(this.beer, r => router.push('/'))
    }
  }
});

var router = new VueRouter({
	routes: [
		{path: '/', component: List},
		{path: '/beer/:beer_id', component: beer, name: 'beer'},
		{path: '/add-beer', component: Addbeer},
		{path: '/beer/:beer_id/edit', component: beerEdit, name: 'beer-edit'},
		{path: '/beer/:beer_id/delete', component: beerDelete, name: 'beer-delete'}
	]
});

new Vue({
  router
}).$mount('#app')