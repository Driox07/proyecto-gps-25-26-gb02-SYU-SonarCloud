package com.gb02.syumsvc.model.dao.postgresql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection ;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.postgresql.Driver;

public class PostgresqlConnector {
    
    private static String url;
    private static String usr;
    private static String psw;

    private static String driver = "org.postgresql.Driver";

    private static Connection connection;

    public static Connection connect(){
        if(connection == null){
            System.out.println("Connecting to syuDB...");
            try{
                loadCredentials();
                Class.forName(driver);
                connection = DriverManager.getConnection(url, usr, psw);
                connection.setAutoCommit(false);
                url = "";
                usr = "";
                psw = "";
                System.out.println("Connection to syuDB established.");
            }catch(ClassNotFoundException e){
                System.err.println("PostgreSQL driver not found.");
            }catch(SQLException e){
                System.err.println("Error connecting to syuDB");
            }
        }
        return connection;
    }

    private static boolean disconnect() {
        System.out.println("Disconnecting from syuDB...");
        try{
            connection.close();
            System.out.println("Disconnected from syuDB.");
            connection = null;
            return true;
        }catch(Exception e){
            System.err.println("Error disconnecting from syuDB.");
            connection = null;
            return false;
        }
    }

    private static boolean loadCredentials(){
        try{
            InputStream is = new FileInputStream(new File(System.getProperty("user.home")+"/asee/dbcred.properties"));
            Properties props = new Properties();
            props.load(is);
            String ip = props.getProperty("ip");
            String port = props.getProperty("port");
            String dbn = props.getProperty("dbn");
            usr = props.getProperty("usr");
            psw = props.getProperty("psw");
            url = String.format("jdbc:postgresql://%s:%s/%s", ip, port, dbn);
            return true;
        }catch(FileNotFoundException e){
            System.err.println("Credentials file not found at " + System.getProperty("user.home")+"/asee/");
            return false;
        }catch(Exception e){
            System.err.println("Error loading database credentials.");
            return false;
        }
    }

}
