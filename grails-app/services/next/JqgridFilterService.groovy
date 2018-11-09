package next

import org.codehaus.groovy.grails.commons.ApplicationHolder
import grails.converters.*

class JqgridFilterService {

  boolean transactional = true
  def grailsApplication = ApplicationHolder.application

  def filter(def params, Class filterClass) {
    return filter(params, filterClass, false)
  }

  def count(def params, Class filterClass) {
    return filter(params, filterClass, true)
  }
  def jqgridAdvFilter(def params, Class filterClass, Closure closure,boolean doCount) {
			beforePager(params)
			
      def criteria = filterClass.createCriteria()
      if(doCount){
        //return criteria.list(criteriaClosure).size()
		  
				return afterPager(params,criteria.list(closure).size())
      } else{
        return criteria.list(closure)
      }
	}
  
  
  
  
  
  
  def jqgridFilter(def params, Class filterClass, boolean doCount) {
    params.max = Math.min(params.rows ? params.int('rows') : 20, 100)
    //if(params.max<1) params.max=20;
    def filters = params['filters']
    def domainClass = ServiceUtils.resolveDomainClass(grailsApplication, filterClass)
    def max = params.max
    def page = params.page?.toInteger()
    if (page == 1) {
      params.offset = 0
    } else {
      params.offset = (page - 1) * params.max
    }

    params.sort = params.sidx ?: 'id'
    params.order = params.sord ?: 'asc'

    if (filters) {
      filters = JSON.parse(filters)
      def groupOp = filters.groupOp
      def rules = convertJqgridFilter(filters.rules)
      def c = filterClass.createCriteria()
      def criteriaClosure = {
        def mc = filterClass.getMetaClass()
        and {
          rules.each() {
            def propName = it.field
            def filterOp = it.op
            def data = it.data
            if (log.isDebugEnabled()) log.debug("\n=============================================================================.")
            if (log.isDebugEnabled()) log.debug("== ${propName}")
            log.info("${propName} ${filterOp} ${data}")
            // Skip associated property entries.  (They'll have a dot in them.)  We'll use the map instead later.
            if (!propName.contains(".")) {
              // If the filterOp is a Map, then the propName is an association (e.g. Book.author)
              if (filterOp instanceof Map && data instanceof Map) {
                // Are any of the values non-empty?
                if (filterOp.values().find {it.length() > 0} != null) {
                  if (log.isDebugEnabled()) log.debug("== Adding association ${propName}")
                  c."${propName}"() {
                    filterOp.each() {opEntry ->
                      def associatedDomainProp = ServiceUtils.resolveDomainProperty(grailsApplication, domainClass, propName)
                      def associatedDomainClass = associatedDomainProp.referencedDomainClass
                      def realPropName = opEntry.key
                      def realOp = opEntry.value
                      def realRawValue = data[realPropName]
                      //def realRawValue2 = rawValue2 != null ? rawValue2["${realPropName}To"] : null
                      //def thisDomainProp = ServiceUtils.resolveDomainProperty(grailsApplication, associatedDomainClass, realPropName)
                      def thisDomainProp = ServiceUtils.resolveDomainProperty(grailsApplication, associatedDomainClass, realPropName)

                      //log.debug("real prop name is ${realPropName}")
                      def val = this.parseValue(thisDomainProp, realRawValue, params)
                      //def val2 = this.parseValue(thisDomainProp, realRawValue2, filterParams)
                      //log.debug("val is ${val} and val2 is ${val2}")
                      this.addJQGridCriterion(c, realPropName, realOp, val, null)
                    }

                    if (!doCount && params.sort && params.sort.startsWith("${propName}.")) {
                      def parts = params.sort.split("\\.")
                      if (parts.size() == 2) {
                        associationList << propName
                        order(parts[1], params.order ?: 'asc')
                      }
                    }
                  } // end c.propName closure.
                } // end if any values not empty.
              } else {
                if (propName && data) {
                  log.debug("propName is ${propName}")
                  def thisDomainProp = ServiceUtils.resolveDomainProperty(grailsApplication, domainClass, propName)
                  def val = this.parseValue(thisDomainProp, data, params)
                  //def val2  = this.parseValue(thisDomainProp, data,params)
                  //if (log.isDebugEnabled()) log.debug("== propName is ${propName}, rawValue is ${rawValue}, val is ${val} of type ${val?.class} val2 is ${val2} of type ${val2?.class}")
                  this.addJQGridCriterion(c, propName, filterOp, val, null)
                }
              }
            }
            if (log.isDebugEnabled()) log.debug("==============================================================================='\n")
          } // end each op
        }//end and
        if (doCount) {
          c.projections {
            rowCount()
          }
        } else {
          if (params.offset) {
            firstResult(params.offset.toInteger())
          }
          if (params.max) {
            maxResults(params.max.toInteger())
          }
          if (params.sidx) {
            order(params.sidx, params.sord ?: 'asc')
          }
        }
      }//end closure
      def results = null
      if (doCount) {
        results = c.get(criteriaClosure)
        return pager(results, page, max)
      } else {
        results = c.list(criteriaClosure)
      }
      if (doCount && results instanceof List) {
        results = 0I
      }
      return results
    } else {
      if (doCount) {
        return pager(filterClass.count(), page, max)
      }


      return filterClass.list(params)
    }
  }

  private def pager(records, page, max) {
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
  private def beforePager(params){
    params.max = Math.min(params.rows ? params.int('rows') : 20, 100);
    //if(params.max<1) params.max=20;
    params.page = params.page ?: '1'
    def page = params.page?.toInteger()
    params.page=page
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
		def page = params.page
    def pager = [:]
    pager.max=max;                 
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
  private def noPager(records){
		def pager = [:]
		pager.records = records
		pager.page = 1
		pager.total = 1
    return pager
  }

  private def filter(def params, Class filterClass, boolean doCount) {
    if (log.isDebugEnabled()) log.debug("filtering... params = ${params.toMapString()}")
    //def filterProperties = params?.filterProperties?.tokenize(',')

    def filterParams = params.filter ? params.filter : params
    def filterOpParams = filterParams.op
    def associationList = []
    def domainClass = ServiceUtils.resolveDomainClass(grailsApplication, filterClass)

    //if (filterProperties != null) {
    if (filterOpParams != null && filterOpParams.size() > 0) {

      def c = filterClass.createCriteria()

      def criteriaClosure = {
        def mc = filterClass.getMetaClass()
        and {
          // First pull out the op map and store a list of its keys.
          def keyList = []
          keyList.addAll(filterOpParams.keySet())
          keyList = keyList.sort() // Sort them to get nested properties next to each other.

          if (log.isDebugEnabled()) log.debug("op Keys = ${keyList}")

          // op = map entry.  op.key = property name.  op.value = operator.
          // params[op.key] is the value
          keyList.each() {propName ->
            if (log.isDebugEnabled()) log.debug("\n=============================================================================.")
            if (log.isDebugEnabled()) log.debug("== ${propName}")

            // Skip associated property entries.  (They'll have a dot in them.)  We'll use the map instead later.
            if (!propName.contains(".")) {

              def filterOp = filterOpParams[propName]
              def rawValue = filterParams[propName]
              def rawValue2 = filterParams["${propName}To"]

              // If the filterOp is a Map, then the propName is an association (e.g. Book.author)
              if (filterOp instanceof Map && rawValue instanceof Map) {

                // Are any of the values non-empty?
                if (filterOp.values().find {it.length() > 0} != null) {

                  if (log.isDebugEnabled()) log.debug("== Adding association ${propName}")

                  c."${propName}"() {

                    filterOp.each() {opEntry ->
                      def associatedDomainProp = ServiceUtils.resolveDomainProperty(grailsApplication, domainClass, propName)
                      def associatedDomainClass = associatedDomainProp.referencedDomainClass
                      def realPropName = opEntry.key
                      def realOp = opEntry.value
                      def realRawValue = rawValue[realPropName]
                      def realRawValue2 = rawValue2 != null ? rawValue2["${realPropName}To"] : null
                      def thisDomainProp = ServiceUtils.resolveDomainProperty(grailsApplication, associatedDomainClass, realPropName)
//                                            log.debug("real prop name is ${realPropName}")
                      def val = this.parseValue(thisDomainProp, realRawValue, filterParams)
                      def val2 = this.parseValue(thisDomainProp, realRawValue2, filterParams)
//                                            log.debug("val is ${val} and val2 is ${val2}")

                      this.addCriterion(c, realPropName, realOp, val, val2)
                    }
                    if (!doCount && params.sort && params.sort.startsWith("${propName}.")) {
                      def parts = params.sort.split("\\.")
                      if (parts.size() == 2) {
                        associationList << propName
                        order(parts[1], params.order ?: 'asc')
                      }
                    }
                  } // end c.propName closure.
                } // end if any values not empty.
              } else {
                log.debug("propName is ${propName}")
                def thisDomainProp = ServiceUtils.resolveDomainProperty(grailsApplication, domainClass, propName)
                def val = this.parseValue(thisDomainProp, rawValue, filterParams)
                def val2 = this.parseValue(thisDomainProp, rawValue2, filterParams)
                if (log.isDebugEnabled()) log.debug("== propName is ${propName}, rawValue is ${rawValue}, val is ${val} of type ${val?.class} val2 is ${val2} of type ${val2?.class}")
                this.addCriterion(c, propName, filterOp, val, val2)
              }
            }
            if (log.isDebugEnabled()) log.debug("==============================================================================='\n")
          } // end each op
        } // end and

        if (doCount) {
          c.projections {
            rowCount()
          }
        } else {
          if (params.offset) {
            firstResult(params.offset.toInteger())
          }
          if (params.max) {
            maxResults(params.max.toInteger())
          }
          if (params.sort) {
            if (params.sort.indexOf('.') < 0) { // if not an association..
              order(params.sort, params.order ?: 'asc')
            } else {
              def parts = params.sort.split("\\.")
              if (!associationList.contains(parts[0])) {
                c."${parts[0]}" {
                  order(parts[1], params.order ?: 'asc')
                }
              }
            }
          }
        }
      } // end criteria
      def results = null
      if (doCount) {
        results = c.get(criteriaClosure)
      } else {
        results = c.list(criteriaClosure)

      }
      if (doCount && results instanceof List) {
        //println "Returning count of 0"
        results = 0I
      }
      return results
    } else {
      if (doCount) {
        return filterClass.count()//0I
      }
      return filterClass.list(params)
    }
  }

  private def addJQGridCriterion(def criteria, def propertyName, def op, def value, def value2) {
    log.info propertyName
    log.info value
    log.info op
    switch (op) {
      case 'eq':
        criteria.eq(propertyName, value)
        break
      case 'ne':
        criteria.ne(propertyName, value)
        break
      case 'lt':
        criteria.lt(propertyName, value)
        break
      case 'le':
        criteria.le(propertyName, value)
        break
      case 'gt':
        criteria.gt(propertyName, value)
        break
      case 'ge':
        criteria.ge(propertyName, value)
        break
      case 'cn':
        if (!value.startsWith('*')) value = "*${value}"
        if (!value.endsWith('*')) value = "${value}*"
        criteria.like(propertyName, value?.replaceAll("\\*", "%"))
        break
      case 'ILike':
        if (!value.startsWith('*')) value = "*${value}"
        if (!value.endsWith('*')) value = "${value}*"
        criteria.ilike(propertyName, value?.replaceAll("\\*", "%"))
        break
      case 'nc':
        if (!value.startsWith('*')) value = "*${value}"
        if (!value.endsWith('*')) value = "${value}*"
        criteria.not {
          criteria.like(propertyName, value?.replaceAll("\\*", "%"))
        }
        break
      case 'NotILike':
        if (!value.startsWith('*')) value = "*${value}"
        if (!value.endsWith('*')) value = "${value}*"
        criteria.not {
          criteria.ilike(propertyName, value?.replaceAll("\\*", "%"))
        }
        break
      case 'IsNull':
        criteria.isNull(propertyName)
        break
      case 'IsNotNull':
        criteria.isNotNull(propertyName)
        break
      case 'Between': //bw and ew
        criteria.between(propertyName, value, value2)
        break
      default:
        break
    } // end op switch
    //println "== addCriterion OUT =="
  }

  private def addCriterion(def criteria, def propertyName, def op, def value, def value2) {
    //println "== addCriterion IN =="
    switch (op) {
      case 'Equal':
        criteria.eq(propertyName, value)
        break
      case 'NotEqual':
        criteria.ne(propertyName, value)
        break
      case 'LessThan':
        criteria.lt(propertyName, value)
        break
      case 'LessThanEquals':
        criteria.le(propertyName, value)
        break
      case 'GreaterThan':
        criteria.gt(propertyName, value)
        break
      case 'GreaterThanEquals':
        criteria.ge(propertyName, value)
        break
      case 'Like':
        def valuestr = value.toString()
        if (!valuestr.startsWith('*')) valuestr = "*${value}"
        if (!valuestr.endsWith('*')) valuestr = "${value}*"
        criteria.like(propertyName, valuestr?.replaceAll("\\*", "%"))
        break
      case 'ILike':
        if (!value.startsWith('*')) value = "*${value}"
        if (!value.endsWith('*')) value = "${value}*"
        criteria.ilike(propertyName, value?.replaceAll("\\*", "%"))
        break
      case 'NotLike':
        if (!value.startsWith('*')) value = "*${value}"
        if (!value.endsWith('*')) value = "${value}*"
        criteria.not {
          criteria.like(propertyName, value?.replaceAll("\\*", "%"))
        }
        break
      case 'NotILike':
        if (!value.startsWith('*')) value = "*${value}"
        if (!value.endsWith('*')) value = "${value}*"
        criteria.not {
          criteria.ilike(propertyName, value?.replaceAll("\\*", "%"))
        }
        break
      case 'IsNull':
        criteria.isNull(propertyName)
        break
      case 'IsNotNull':
        criteria.isNotNull(propertyName)
        break
      case 'Between':
        criteria.between(propertyName, value, value2)
        break
      default:
        break
    } // end op switch
    //println "== addCriterion OUT =="
  }

  def parseValue(def domainProperty, def val, def params) {
    log.info "val=" + val
    log.info domainProperty
    if (val) {
      Class cls = domainProperty.referencedPropertyType
      String clsName = cls.simpleName.toLowerCase()
      log.info("domainProperty is ${domainProperty}, val is ${val}, clsName is ${clsName}")

      if (cls.isEnum()) {
        val = Enum.valueOf(cls, val.toString())
      } else if ("boolean".equals(clsName)) {
        val = val.toBoolean()
      } else if ("int".equals(clsName) || "integer".equals(clsName)) {
        val = val.toInteger()
      } else if ("long".equals(clsName)) {
        val = val.toLong()
      } else if ("double".equals(clsName)) {
        val = val.toDouble()
      } else if ("float".equals(clsName)) {
        val = val.toFloat()
      } else if ("short".equals(clsName)) {
        val = val.toShort()
      } else if ("bigdecimal".equals(clsName)) {
        val = val.toBigDecimal()
      } else if ("biginteger".equals(clsName)) {
        val = val.toBigInteger()
      } else if (java.util.Date.isAssignableFrom(cls)) {
        //val = ServiceUtils.parseDateFromDatePickerParams(domainProperty.name, params)
        log.info "must to implement the date value"
      }
    }
    return val
  }

  def convertJqgridFilter(rules) {
    def fiterRuls = new ArrayList()
    rules.each() {
      def propName = it.field
      if (propName.contains(".")) {
        def domainName = propName.substring(0, propName.indexOf("."))
        def propertyName = propName.substring(propName.indexOf(".") + 1, propName.length())
        def filterRule = getRuleItByField(fiterRuls, domainName)
        if (!filterRule) {
          filterRule = new HashMap()
          filterRule.op = new HashMap()
          filterRule.data = new HashMap()
          filterRule.op.put(propertyName, it.op)
          filterRule.data.put(propertyName, it.data)
          filterRule.field = domainName

          fiterRuls.add(filterRule)
        } else {
          log.info filterRule.op
          log.info it.op
          filterRule.op[0].put(propertyName, it.op)
          filterRule.data[0].put(propertyName, it.data)
        }
      } else {
        def filterRule = [:]
        filterRule.field = it.field
        filterRule.op = it.op
        filterRule.data = it.data
        fiterRuls.add(filterRule)
      }
    }
    log.info fiterRuls
    return fiterRuls
  }

  def getRuleItByField(rules, field) {
    rules.each() {
      if (it.field.equals(field)) {
        return it
      }
    }
  }
}