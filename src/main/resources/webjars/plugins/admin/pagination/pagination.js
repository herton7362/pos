define(function() {
    return {
        props: {
            currentPage: {
                type: [Number, String],
                default: 1
            },
            pagerSize: {
                type: [Number, String],
                default: 5
            },
            pageSize: {
                type: [Number, String],
                default: 15
            },
            count: {
                type: [Number, String],
                default: 0
            }
        },
        template: '<div class="row">\n' +
        '              <div class="col-sm-5">\n' +
        '                  <div class="dataTables_info" role="status" aria-live="polite">\n' +
        '                      共 {{count}} 条记录\n' +
        '                  </div>\n' +
        '              </div>\n' +
        '              <div class="col-sm-7">\n' +
        '                  <div class="dataTables_paginate paging_simple_numbers">\n' +
        '                      <ul class="pagination">\n' +
        '                          <li class="paginate_button previous" :class="{\'disabled\':innerProps.currentPage<=1}" @click="_goToPage(innerProps.currentPage - 1)">\n' +
        '                              <a href="javascript:;" tabindex="0">上一页</a>\n' +
        '                          </li>\n' +
        '                          <template v-if="innerProps.currentPage - ((pagerSize - 1) / 2) > 1">\n' +
        '                              <li class="paginate_button button_sm" @click="_goToPage(1)">\n' +
        '                                  <a href="#" tabindex="0">1</a>\n' +
        '                              </li>\n' +
        '                              <li class="paginate_button button_sm">\n' +
        '                                  <a href="#" tabindex="0">...</a>\n' +
        '                              </li>\n' +
        '                          </template>\n' +
        '                          <li class="paginate_button button_sm" :class="{\'active\':i==innerProps.currentPage}" v-for="i in page" @click="_goToPage(i)"><a href="javascript:;">{{i}}</a></li>\n' +
        '                          <template v-if="innerProps.currentPage + ((pagerSize - 1) / 2) < totalPage">\n' +
        '                              <li class="paginate_button button_sm">\n' +
        '                                  <a href="#" tabindex="0">...</a>\n' +
        '                              </li>\n' +
        '                              <li class="paginate_button button_sm" @click="_goToPage(totalPage)">\n' +
        '                                  <a href="#" tabindex="0">{{totalPage}}</a>\n' +
        '                              </li>\n' +
        '                          </template>\n' +
        '                          <li class="paginate_button next" :class="{\'disabled\':innerProps.currentPage>=page}" @click="_goToPage(innerProps.currentPage + 1)">\n' +
        '                              <a href="#" tabindex="0">下一页</a>\n' +
        '                          </li>\n' +
        '                      </ul>\n' +
        '                  </div>\n' +
        '              </div>\n' +
        '          </div>',
        watch: {
            count: function(val) {
                this.totalPage = parseInt(val / this.pageSize) + 1;
                var i, l,
                    currentPage = this.innerProps.currentPage;
                if(this.totalPage < this.pagerSize) {
                    this.page = [];
                    for(i = 1,l = this.totalPage; i <= l; i++) {
                        this.page.push(i);
                    }

                } else {
                    if(this.pagerSize % 2 === 0) {
                        console.error('attached the param pagerSize is even, please change to odd');
                    }
                    var step1 = (this.pagerSize - 1) / 2;
                    var step2 = step1;
                    if(currentPage - step1 < 1) {
                        step2 += 1 - (currentPage - step1);
                        step1 -= 1 - (currentPage - step1);
                    }
                    if(currentPage + step2 > this.totalPage) {
                        step1 += currentPage + step2 - this.totalPage;
                        step2 -= currentPage + step2 - this.totalPage;
                    }
                    this.page = [];
                    for(i = currentPage - step1,l = currentPage + step2; i <= l; i++) {
                        this.page.push(i);
                    }
                }
            },
            currentPage: function(val) {
                this.innerProps.currentPage = val;
            }
        },
        data: function() {
            return {
                innerProps: {
                    currentPage: this.currentPage
                },
                page: [],
                totalPage: 1
            }
        },
        methods: {
            _goToPage: function(val) {
                if(val <= 0 || val > this.totalPage || this.innerProps.currentPage === val) {
                    return;
                }
                this.innerProps.currentPage = val;
                this.$emit('go', val);
                var step1 = (this.pagerSize - 1) / 2;
                var step2 = step1;
                if(this.innerProps.currentPage - step1 > 1){
                	this.page = [];
                    for(i = this.innerProps.currentPage - 2,l = this.innerProps.currentPage + 2; i <= l; i++) {
                        this.page.push(i);
                    };
                    if(this.innerProps.currentPage + this.pagerSize  > this.totalPage){
                    	this.page = [];
                        for(i = this.totalPage - this.pagerSize,l = this.totalPage; i <= l; i++) {
                            this.page.push(i);
                        }
                    }
                    
                }else{
                   	this.page = [];
                    for(i = 1; i <= this.pagerSize; i++) {
                        this.page.push(i);
                    }
                }
            }
        }
    };
});