package fr.insalyon.creatis.vip.core.server.dao.mysql;

import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import fr.insalyon.creatis.vip.core.client.bean.TermsOfUse;

public class TermsUseDataTest {
	
	@Before
	public void setUp(){
    	try {
    		// Create initial context
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
            InitialContext ic = new InitialContext();
            
            ic.createSubcontext("java:");
            ic.createSubcontext("java:comp");
            ic.createSubcontext("java:comp/env");
            ic.createSubcontext("java:comp/env/jdbc");
                       
            DataSource ds = dataSource();
            
            ic.bind("java:comp/env/jdbc/vip", ds);
    	}catch (NamingException ex){
    		
    	}
	}
	
    public static javax.sql.DataSource dataSource(){
    	org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();  	
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/vip");
        ds.setUsername("vipUser");
        ds.setPassword("vip");
        ds.setMaxActive(100);
        ds.setMaxIdle(50);
        System.out.println("datasource");
        return ds;
    }
    
	@Test
	public void test() throws Exception{
		
		Connection connection = PlatformConnection.getInstance().getConnection();
		
		TermsUseData tud = new TermsUseData();		
		TermsOfUse tou = new TermsOfUse();
		tou.setId(1);
		Timestamp time = new Timestamp(System.currentTimeMillis());

		tou.setDate(time);
		tou.setText("blebleble");
		
		tud.add(tou);
		
		Statement statement = connection.createStatement();
//		ResultSet rset = statement.executeQuery("SELECT date FROM VIPTermsOfuse ORDER BY idTermsOfuse DESC LIMIT 1");
		
		System.out.println("last term of use update was: "+ time);
//		rset.next();
		System.out.println("result found in database is: "+tud.getLastUpdateTermsOfUse());
		//assertThat("the last entry did not correspond to the waited value", tud.getLastUpdateTermsOfUse(), is(time));
	}

}
