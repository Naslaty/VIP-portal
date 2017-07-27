package fr.insalyon.creatis.vip.social.server.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import fr.insalyon.creatis.vip.core.client.bean.Group;
import fr.insalyon.creatis.vip.core.client.bean.User;
import fr.insalyon.creatis.vip.core.client.view.CoreConstants;
import fr.insalyon.creatis.vip.core.client.view.user.UserLevel;
import fr.insalyon.creatis.vip.core.client.view.util.CountryCode;
import fr.insalyon.creatis.vip.core.server.business.Server;
import fr.insalyon.creatis.vip.core.server.dao.CoreDAOFactory;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;

public class PlatformConnectionSocial {
    private final static Logger logger = Logger.getLogger(PlatformConnection.class);
    private static PlatformConnectionSocial instance;
    private boolean firstExecution;
    private Connection connection;

    public synchronized static PlatformConnectionSocial getInstance() throws DAOException {

        if (instance == null) {
            instance = new PlatformConnectionSocial();
        }
        return instance;
    }

    private PlatformConnectionSocial() throws DAOException {

        firstExecution = true;
        System.out.println("firstExecution: "+firstExecution);
        connect();
        System.out.println("connection: "+connection);
        createTables();
    }
    
    private void connect() {

        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/vip");
            connection = ds.getConnection();
            connection.setAutoCommit(true);          

        } catch (SQLException ex) {
            logger.error(ex);
        } catch (NamingException ex) {
            logger.error(ex);
        }
    }
    
    private void createTables() {
    	System.out.println("in createsTables()");
        if (firstExecution) {
        	System.out.println("in createsTables(1)");
            logger.info("Configuring VIP database.");
            System.out.println("in createsTables(2)");

            createTable("VIPSocialGroupMessage",
                    "id bigint(20) NOT NULL AUTO_INCREMENT, "
                    + "sender varchar(255) DEFAULT NULL, "
                    + "groupname varchar(255) DEFAULT NULL, "
                    + "title varchar(255) DEFAULT NULL, "
                    + "message text, "
                    + "posted timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                    + "PRIMARY KEY (id),"
                    + "FOREIGN KEY (email) REFERENCES VIPUsers (email)"
                    + "ON DELETE CASCADE ON UPDATE CASCADE,"
                    + "FOREIGN KEY (groupname) REFERENCES VIPGroups (groupname)"
                    + "ON DELETE CASCADE ON UPDATE CASCADE");
            System.out.println("in createsTables(3)");

            createTable("VIPSocialMessage",
                    "id bigint(20) NOT NULL AUTO_INCREMENT, "
                    + "sender varchar(255) DEFAULT NULL, "
                    + "title varchar(255) DEFAULT NULL, "
                    + "message text, "
                    + "posted timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                    + "PRIMARY KEY(groupname) ,"
                    +"FOREIGN KEY (sender) REFERENCES VIPUsers (email) ON DELETE CASCADE");
            System.out.println("in createsTables(4)");
            firstExecution = false;
        }
    }
    
    public Connection getConnection() {
        return connection;
    }

    /**
     * Creates a table in the platform database.
     *
     * @param name Table name
     * @param columnsDefinition SQL syntax to define columns
     * @return
     */
    public boolean createTable(String name, String columnsDefinition) {

        try {
            Statement stat = connection.createStatement();
            stat.executeUpdate("CREATE TABLE " + name + " ("
                    + columnsDefinition + ") ENGINE=InnoDB");

            logger.info("Table " + name + " successfully created.");
            return true;

        } catch (SQLException ex) {
            if (!ex.getMessage().contains("already exists")) {
                logger.error(ex);
            }
            return false;
        }
    }
}
