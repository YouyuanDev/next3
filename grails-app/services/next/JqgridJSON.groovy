package next

import grails.converters.*

class JqgridJSON {
  def json = [:]

  JqgridJSON(pager) {
    json.page = pager.page
    json.total = pager.total
    json.records = pager.records
    json.max=pager.max

    def rows = new ArrayList()
    json.rows = rows
  }

  def addRow(i, cell) {
	 if(json.max>0)
	 {
		 if(i<(json.page-1)*json.max) return;
		 if(i>=json.page*json.max) return;
	 }
    def row = [:]
    row.id = i
    row.cell = cell
    json.rows.add(row)
  }

  def json() {
    return json
  }
}
