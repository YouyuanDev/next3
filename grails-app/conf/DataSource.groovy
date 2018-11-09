import org.springframework.core.io.support.PropertiesLoaderUtils   
import org.springframework.core.io.ClassPathResource   

def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("datasource.properties"))   
/*   
dataSource {
  pooled = true
  //driverClassName = "org.hsqldb.jdbcDriver"					//For HSQL
  //driverClassName = "org.postgresql.Driver"					//For PostgresSQL
  //username = "postgres"
  //password = "postgres"
  
  //driverClassName = "com.mysql.jdbc.Driver"
  //username = "next"
  //password = "KJTRwrf1r5AbSV"
  //pooled = false
  //configClass = org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsAnnotationConfiguration
    driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
   username = "sa"
   password = "sa"
	  def dialect = org.hibernate.dialect.SQLServerDialect.class;

}
hibernate {
  cache.use_second_level_cache = true
  cache.use_query_cache = true
  cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
  development {
    dataSource {
      //dbCreate = "create-drop" // one of 'create', 'create-drop','update'
      dbCreate = "update" // one of 'create', 'create-drop','update'
      //url = "jdbc:hsqldb:mem:devDB"							//For HSQL
      //url = "jdbc:postgresql://localhost:5432/next_devDb"		//For PostgresSQL
      //url = "jdbc:mysql://159.226.128.108:3306/next_devdb?useUnicode=true&characterEncoding=utf-8"
       url = "jdbc:sqlserver://localhost:1433;databaseName=next_devdb;selectMethod=cursor"
    	  logSql = true	  
    }
  }
  test {
    dataSource {
      dbCreate = "update"
      //url = "jdbc:hsqldb:mem:testDb"
      //url = "jdbc:postgresql://localhost:5432/next_testDb"
      // url = "jdbc:mysql://159.226.128.108:3306/next_testdb?useUnicode=true&characterEncoding=utf-8"
      url = "jdbc:sqlserver://localhost:1433;databaseName=next_devdb;selectMethod=cursor"
    	  logSql = true
    }
  }
  production {
    dataSource {
      dbCreate = "update"
      //url = "jdbc:hsqldb:file:prodDb;shutdown=true"
      //url = "jdbc:postgresql://localhost:5432/next_prodDb"
      //username="root"
      //url = "jdbc:mysql://159.226.128.108:3306/next_devdb?useUnicode=true&characterEncoding=utf-8"
      url = "jdbc:sqlserver://localhost:1433;databaseName=next_devdb;selectMethod=cursor"
    	  logSql = true
    }
  }
}

*/


dataSource {   
pooled = true  

 driverClassName = properties.getProperty("driverClass")
 username = properties.getProperty("username")
 password = properties.getProperty("password")
 url = properties.getProperty("url") 
 maxIdle= properties.getProperty("maxIdle") 
maxActive=properties.getProperty("maxActive") 
 
 //println "aaaaaaaaaaaaaaaaa url= ${url}"
 /*
driverClassName="net.sourceforge.jtds.jdbc.Driver"
username="sa"
password="sa"
url="jdbc:jtds:sqlserver://localhost:1433;DatabaseName=next_devdb;SelectMethod=cursor"
 */
}   
hibernate {   
    cache.use_second_level_cache=true  
    cache.use_query_cache=true  
    cache.provider_class='net.sf.ehcache.hibernate.EhCacheProvider'  
}   
// environment specific settings   
environments {   
development {   
   dataSource {   
      dbCreate = "update" // one of 'create', 'create-drop','update'   
      //url = "jdbc:jtds:sqlserver://localhost:1433;DataBaseName=next_devdb;SelectMethod=cursor"
      //logSql = true
   }   
}   
test {   
   dataSource {   
      dbCreate = "update"  
      //url = "jdbc:jtds:sqlserver://localhost:1433;DataBaseName=next_devdb;SelectMethod=cursor"
      //logSql = true
   }   
}   
production {   
   dataSource {   
      dbCreate = "update"  
      //url = "jdbc:jtds:sqlserver://localhost:1433;DataBaseName=next_devdb;SelectMethod=cursor"
      //logSql = true
   }   
}   
} 