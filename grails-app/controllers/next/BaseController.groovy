package next

import grails.converters.*

class BaseController {
  def jqgridFilterService

  public def beforePager(params){
    params.max = Math.min(params.rows ? params.int('rows') : 20, 100)
    params.page = params.page ?: '1'
    def page = params.page?.toInteger()
    if (page == 1) {
      params.offset = 0
    } else {
      params.offset = (page - 1) * params.max
    }
    params.sort = params.sidx ?: 'id'
    params.order = params.sord ?: 'asc'
  }
  public def afterPager(params,records){
    def max = params.max
    def pager = [:]
    if (records > 0) {
      def int size = records / max
      int mod = records % max
      if (mod != 0)
        size++;
      def total = records == 0 ? 1 : size
      pager.records = records
      pager.page = params['page']
      pager.total = total
    } else {
      pager.records = 0
      pager.page = 1
      pager.total = 1
    }
    return pager
  }
  public def prePager(params, records) {
    params.max = Math.min(params.rows ? params.int('rows') : 20, 100)
    params.page = params.page ?: '1'
    def page = params.page?.toInteger()
    if (page == 1) {
      params.offset = 0
    } else {
      params.offset = (page - 1) * params.max
    }
    params.sort = params.sidx ?: 'id'
    params.order = params.sord ?: 'asc'

    def max = params.max
    def pager = [:]
    if (records > 0) {
      def int size = records / max
      int mod = records % max
      if (mod != 0)
        size++;
      def total = records == 0 ? 1 : size

      pager.records = records
      pager.page = page
      pager.total = total
    } else {
      pager.records = 0
      pager.page = 1
      pager.total = 1
    }
    return pager
  }
}
