let beers = [];
let paging = {
  page: 0,
  size: 5,
  totalElements: 0,
  totalPages: 0,
  showing: 0
};

function findbeer(beerId) {
  let beerKey = findbeerKey(beerId);

  if (beerKey == undefined) {
    beerService.findById(beerId, function (data) {
      let beer = data;
      return beer.data;
    });
  } else {
    return beers[beerKey];
  }
}

function findbeerKey(beerId) {
  for (var key = 0; key < beers.length; key++) {
    if (beers[key].id == beerId) {
      return key;
    }
  }
}

var beerService = {
  findAll(filter, fn) {
    let uri = '/api/v1/beers?orderBy=createdAt&size=' + paging.size + '&page=' + paging.page;
    
    let props = ['name', 'color', 'temperature', 'ingredients', 'alcoholicStrength'];
    
    props.forEach(prop => {
        if (filter[prop] != undefined) {
            uri += '&'+prop+'='+filter[prop];
        } 
    });
    
    axios
      .get(uri)
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
      .then(function (response) {
        let files = document.getElementById('add-photo').files;

        if (files.length > 0) {
          let file = files[0];

          const formData = new FormData();
          formData.append('beer_id', response.data.id);
          formData.append('photo', file, file.name);

          return axios.post('/api/v1/uploadFile', formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          });
        }
      })
      .then(response => fn(response))
      .catch(error => console.log(error))
  },

  update(id, beer, fn) {
    axios
      .put('/api/v1/beers/' + id, beer)
      .then(function (response) {
        let files = document.getElementById('edit-photo').files;

        if (files.length > 0) {
          let file = files[0];

          const formData = new FormData();
          formData.append('beer_id', beer.id);
          formData.append('photo', file, file.name);

          return axios.post('/api/v1/uploadFile', formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          });
        }
      })
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

function callFindAll(filter, _this) {
    beerService.findAll(filter, r => {
      this.beers = r.data._embedded.beers;
      beers = r.data._embedded.beers;

      this.paging.totalElements = r.data.page.totalElements;
      paging.totalElements = r.data.page.totalElements;

      this.paging.totalPages = r.data.page.totalPages;
      paging.totalPages = r.data.page.totalPages;

      this.paging.showing = r.data._embedded.beers.length;
      paging.showing = r.data._embedded.beers.length;
    })
}

var List = Vue.extend({
  template: '#beer-list',
  data: function () {
    return { beers: [], //
    	searchKey: '',  //
    	paging: paging,  //
    	filter: {} //
    	};
  },
  computed: {
    filteredbeers() {
      return this.beers.filter((beer) => {
        return beer.name.indexOf(this.searchKey) > -1
          || beer.color.indexOf(this.searchKey) > -1
          || beer.ingredients.toString().indexOf(this.searchKey) > -1
      })
    }
  },
  mounted() {
    let filter = this.filter;
    
    beerService.findAll(filter, r => {
      this.beers = r.data._embedded.beers;
      beers = r.data._embedded.beers;

      this.paging.totalElements = r.data.page.totalElements;
      paging.totalElements = r.data.page.totalElements;

      this.paging.totalPages = r.data.page.totalPages;
      paging.totalPages = r.data.page.totalPages;

      this.paging.showing = r.data._embedded.beers.length;
      paging.showing = r.data._embedded.beers.length;
    })
  },
  methods: {
    filterbeers: function () {
      let filter = this.filter;
      
      beerService.findAll(filter, r => {
          this.beers = r.data._embedded.beers;
          beers = r.data._embedded.beers;
    
          this.paging.totalElements = r.data.page.totalElements;
          paging.totalElements = r.data.page.totalElements;
    
          this.paging.totalPages = r.data.page.totalPages;
          paging.totalPages = r.data.page.totalPages;
    
          this.paging.showing = r.data._embedded.beers.length;
          paging.showing = r.data._embedded.beers.length;
      });
    },
    changepage: function(page) {
        event.preventDefault();
        
        this.paging.page=page-1;
        this.filterbeers();
    }
  }
  
});

var beer = Vue.extend({
  template: '#beer',
  data: function () {
    let beer = findbeer(this.$route.params.beer_id);
    return { beer: beer };
  }
});

var beerEdit = Vue.extend({
  template: '#beer-edit',
  data: function () {
    return { beer: findbeer(this.$route.params.beer_id) };
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
    return { beer: findbeer(this.$route.params.beer_id) };
  },
  methods: {
    deletebeer: function () {
      beerService.deletebeer(this.beer.id, r => router.push('/'))
    }
  }
});

var addbeer = Vue.extend({
  template: '#add-beer',
  data() {
    return {
      beer: { name: '', description: '', price: 0 }
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
    { path: '/', component: List },
    { path: '/beer/:beer_id', component: beer, name: 'beer' },
    { path: '/add-beer', component: addbeer },
    { path: '/beer/:beer_id/edit', component: beerEdit, name: 'beer-edit' },
    { path: '/beer/:beer_id/delete', component: beerDelete, name: 'beer-delete' }
  ]
});

new Vue({
  router
}).$mount('#app')