/*
 * Copyright and authors: see LICENSE.txt in base repository.
 *
 * This software is a web portal for pipeline execution on distributed systems.
 *
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.insalyon.creatis.vip.warehouse.mysql;
import fr.insalyon.creatis.vip.core.server.dao.DAOException;
import fr.insalyon.creatis.vip.core.server.dao.mysql.PlatformConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.logging.Level;


import org.apache.log4j.Logger;
/**
 *
 * @author cervenansky
 */
public class MedImgWarehouseDataSQL implements MedImgWarehouseData{
 
       private final static Logger logger = Logger.getLogger(MedImgWarehouseDataSQL.class);
     private Connection connection;

    public void XnatDB() throws DAOException
    {
        connection = PlatformConnection.getInstance().getConnection();
    }
    
    //email: name in the Medical Images Warehouse site
    //nickname: name of the Medical Images Warehouse site
    // url: url of the Medical Images Warehouse site
    // site: "name" of the Medical Images Warehouse site
    // type : xnat-midas-other

    public void recordSite(String email, String nickname, String url, String site, String type) throws DAOException
    {
        try {
           
            connection = PlatformConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO MedImgWarehouse(email, nickname, url, site, session, type) "
                    + "VALUES (?, ?, ?, ?, ?)");

            ps.setString(1, email);
            ps.setString(2, nickname);
            ps.setString(3, url);
            ps.setString(4, site);
            ps.setString(5, "");
            ps.setString(6, type);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
   
    // email: email of user in VIP
    // url: url of the Medical Images Warehouse site
    // nickname: name of user in the Medical Images Warehouse site
       @Override
    public void recordNickname(String email, String url, String nickname) throws DAOException
    {
         try {
           
            connection = PlatformConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO MedImgWarehouse(email, url, nickname) "
                    + "VALUES (?, ?, ?)");

            ps.setString(1, email);
            ps.setString(2, url);
            ps.setString(3, nickname);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
    
     @Override  
    public ArrayList<String> getSites(String email) throws DAOException
    {
        ArrayList results = new ArrayList<String>();
        try
        {
         connection = PlatformConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT nickname, url,site,type FROM MedImgWarehouse "
                    + "WHERE email=?");

            ps.setString(1, email);
          
            ResultSet rs = ps.executeQuery();

            while (rs.next()) 
                results.add(rs.getString("nickname")+"#"+rs.getString("url")+"#"+rs.getString("site")+"#"+rs.getString("type"));
            ps.close();
            System.out.println("resultats : " +results );
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
        return results;
        
    }
       
    //user: name in the Medical Images Warehouse site
    //label: name of the Medical Images Warehouse site
       @Override
    public String getNickName(String email, String site) throws DAOException
    {
        String nick= "";
        try {
           
            connection = PlatformConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT nickname FROM MedImgWarehouse "
                    + "WHERE email=? ,  site=?");

            ps.setString(1, email);
            ps.setString(2, site);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) 
                nick = rs.getString("nickname");
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
        return nick;
    }
    
        //user: name in the Medical Images Warehouse site
 
     //user: name in the Medical Images Warehouse site
    //label: name of the Medical Images Warehouse site
    // jsession: jsessionid for the user and the label
       @Override
    public void setSession(String nickname, String url, String jsession) throws DAOException
    {
           try {
               System.out.println(nickname+ "  " + url +"  "+ jsession);
               connection = PlatformConnection.getInstance().getConnection();
               PreparedStatement ps = connection.prepareStatement("UPDATE "
                            + "MedImgWarehouse "
                            + "SET session=? "
                            + "WHERE nickname=? AND url=?");
               ps.setString(1,jsession);
               ps.setString(2, nickname);
               ps.setString(3, url);
               ps.executeUpdate();
               ps.close();
           } catch (SQLException ex) {
               java.util.logging.Logger.getLogger(MedImgWarehouseDataSQL.class.getName()).log(Level.SEVERE, null, ex);
           }
    }
    
     //email: name in Medical Images Warehouse site
    //url: url of the Medical Images Warehouse site
       @Override
     public String getSession(String nickname, String url) throws DAOException
    {
        String jses = "";
        try {
            System.out.println(nickname + "  url "+url);
               PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "session FROM "
                    + "MedImgWarehouse WHERE nickname=? AND url=?");
               ps.setString(1, nickname);
               ps.setString(2, url);
               ResultSet rs = ps.executeQuery();

            while (rs.next()) 
                jses = rs.getString("session");
            ps.close();
           } catch (SQLException ex) {
               java.util.logging.Logger.getLogger(MedImgWarehouseDataSQL.class.getName()).log(Level.SEVERE, null, ex);
           }
            return jses;
    }

     //user: name in the Medical Images Warehouse site
    //label: name of the Medical Images Warehouse site
       @Override
     public String getURL(String email, String site) throws DAOException
    {
        String url = "";
        try {
               PreparedStatement ps = connection.prepareStatement("SELECT "
                    + "url FROM "
                    + "MedImgWarehouse WHERE email=?, site=?");
               ps.setString(1, email);
               ps.setString(2, site);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) 
                url = rs.getString("url");
            
           } catch (SQLException ex) {
               java.util.logging.Logger.getLogger(MedImgWarehouseDataSQL.class.getName()).log(Level.SEVERE, null, ex);
           }
            return url;
    }
    
    // jsession: jsessionid for the user and the label
      //url: url of the Medical Images Warehouse site
    //suri: starting uri of data
     //furi: finishing uri of data
       @Override
     public void setData(String jsession, String url, String suri, String furi) throws DAOException
    {
           try {
               connection = PlatformConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO MedImgWhouseDown(jsession, url, start_uri, finish_uri) "
                    + "VALUES (?, ?, ?, ?, ?)");

            ps.setString(1, jsession);
            ps.setString(2, url);
            ps.setString(3, suri);
            ps.setString(4, furi);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }
     
       @Override
    public void updateData(String jsession, String suri) throws DAOException
    {
        try {
               connection = PlatformConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM MedImgWhouseDown "
                    + "WHERE jsession=?, start_uri=?");

            ps.setString(1, jsession);
            ps.setString(2, suri);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException(ex);
        }
    }

    @Override
    public void recordSite(String user, String label, String url) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
