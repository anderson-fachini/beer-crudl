let beers = [];
let paging = {
  page: 0,
  size: 5,
  totalElements: 0,
  totalPages: 0,
  showing: 0
};
let beerColorsInfo;

if (localStorage.getItem('beerColorsInfo') == undefined) {
  axios
    .get('/api/v1/beers/colors-info')
    .then(function(response) {
      localStorage.setItem('beerColorsInfo', JSON.stringify(response.data));
      beerColorsInfo = response.data;
    })
    .catch(error => console.log(error));      
} else {
    beerColorsInfo = JSON.parse(localStorage.getItem('beerColorsInfo'));
}

function findbeer(beerId, fn) {
  let beerKey = findbeerKey(beerId);

  if (beerKey == undefined) {
    beerService.findById(beerId, function (data) {
      fn(data.data);
    });
  } else {
    fn(beers[beerKey]);
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

  async findById(id, fn) {
    axios
      .get('/api/v1/beers/' + id)
      .then(response => fn(response))
      .catch(error => console.log(error));
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
      .catch(error => {
       console.log(error); 
       })
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

function getBeerColorInfoById(colorId) {
    for (let i= 0; i < beerColorsInfo.length; i++) {
        let colorInfo = beerColorsInfo[i];
        
        if (colorInfo.id == colorId) {
            return colorInfo;
        }
    }
}

function getBeerColorDescription(colorId) {
    if (beerColorsInfo == undefined) {
        return colorId;
    }
    
    if (colorId == undefined) {
        return '';
    }
    
    return getBeerColorInfoById(colorId).description;
}

function getBeerColorHexColor(colorId) {
    if (beerColorsInfo == undefined) {
        return '';
    }
    
    return '#' + getBeerColorInfoById(colorId).hexColor;
}

function applyTextWhiteClass(colorId) {
    let middle = beerColorsInfo.length / 2;
    
    for (let i= 0; i < beerColorsInfo.length; i++) {
        let colorInfo = beerColorsInfo[i];
        
        if (colorInfo.id == colorId) {
            if (i >= middle) {
                return true;
            }
            return false;
        }
    }
}

var List = Vue.extend({
  template: '#beer-list',
  data: function () {
    return { beers: [], //
    	searchKey: '',  //
    	paging: paging,  //
    	filter: {} };
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
    resetfilters: function() {
        this.filter.name = '';
        this.filter.color = '';
        this.filter.alcoholicStrength = '';
        this.filter.temperature = '';
        this.filter.ingredients = '';
        this.filterbeers();
    },
    changepage: function(page) {
        event.preventDefault();
        
        this.paging.page=page-1;
        this.filterbeers();
    },
    getBeerColorDescription,
    getBeerColorHexColor
  }
  
});

var beer = Vue.extend({
  template: '#beer',
  data: function () {
    findbeer(this.$route.params.beer_id, data => {
        this.beer = data;
    });
    return { beer: this.beer || {} };
  },
  methods: {
    getBeerColorDescription,
    getBeerColorHexColor
  }
});

var beerEdit = Vue.extend({
  template: '#beer-edit',
  data: function () {
    findbeer(this.$route.params.beer_id, data => {
        this.beer = data;
    })
    return { beer: this.beer || {},
    beerColorsInfo: beerColorsInfo };
  },
  methods: {
    updatebeer: function () {
      beerService.update(this.beer.id, this.beer, r => router.push('/'))
    },
    getBeerColorDescription,
    getBeerColorHexColor,
    applyTextWhiteClass
  }
});

var beerDelete = Vue.extend({
  template: '#beer-delete',
  data: function () {
    findbeer(this.$route.params.beer_id, data => {
        this.beer = data;
    })
    return { beer: this.beer || {} };
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
    return { beer: {},
      beerColorsInfo: beerColorsInfo
    }
  },
  methods: {
    createbeer() {
      beerService.create(this.beer, r => router.push('/'))
    },
    getBeerColorDescription,
    getBeerColorHexColor,
    applyTextWhiteClass
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
}).$mount('#app');