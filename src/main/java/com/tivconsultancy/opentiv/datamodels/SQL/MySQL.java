/*
 * Copyright 2020 TIVConsultancy.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tivconsultancy.opentiv.datamodels.SQL;

import com.tivconsultancy.opentiv.imageproc.primitives.ImageInt;
import com.tivconsultancy.opentiv.logging.TIVLog;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class MySQL implements SQLDatabase {

    private final String computerName;
    private String url;
    private String user = "Crystalline";
    private String password = "enillatsyrC01";
    private String reactor;

    public MySQL(String reactor) throws UnknownHostException {
        this.computerName = InetAddress.getLocalHost().getHostName();
        this.url = "jdbc:sqlserver://;servername="+computerName+"\\CRYSTALLINE;databaseName=virtualreactor"+reactor;        
        this.reactor = reactor;
    }

    public MySQL(String url, String user, String password, String reactor) throws UnknownHostException {
        this.computerName = InetAddress.getLocalHost().getHostName();
        this.url = "jdbc:sqlserver://;servername="+computerName+"\\CRYSTALLINE;databaseName=virtualreactor"+reactor;        
        this.url = url;
        this.user = user;
        this.password = password;
        this.reactor = reactor;
    }

    public String getUser() {
        return user;
    }

    /**
     * Connect to the MySQL database
     *
     * @param reactor
     * @return a Connection object
     */
    public Connection connect(String reactor) {
        //addLibraryPath("D:\\Sync\\TIVConsultancy\\_Customers\\Technobis\\Project3\\mySQLDatabase\\sqljdbc_8.4\\enu\\auth\\x64");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://;servername="+computerName+"\\CRYSTALLINE;databaseName=virtualreactor" + reactor + ";user=" + user + ";password=" + password);
        } catch (Exception e) {
            TIVLog.tivLogger.log(Level.SEVERE, "Cannot connect to SQL on URL: " + url, e);
        }
        return conn;
    }

    /**
     * Connect to the MySQL database
     *
     * @param reactor
     * @return a Connection object
     */
    public boolean ableToConnect(String reactor) {
        try {
            DriverManager.getConnection("jdbc:sqlserver://;servername="+computerName+"\\CRYSTALLINE;databaseName=virtualreactor" + reactor + ";user=" + user + ";password=" + password);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getColumnEntries(String tableName, String columnName) {
        return getColumnEntries("dbo", tableName, columnName);
    }

    public List<String> getColumnEntries(String schemaName, String tableName, String columnName) {
        String sqlSelect = "SELECT " + columnName + " from " + schemaName + "." + tableName;
        List<String> lsOut = new ArrayList<>();
        try (Connection conn = this.connect(this.reactor);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlSelect);) {

            while (rs.next()) {
                lsOut.add(rs.getString(columnName));
            }

        } catch (SQLException ex) {
            TIVLog.tivLogger.log(Level.SEVERE, "Cannot execute query: " + sqlSelect, ex);
        }
        return lsOut;
    }

    public ImageInt getRawImage(String id) throws SQLException {
        byte[] a = getBinaryStream("SELECT RawImage FROM dbo.RawImageData WHERE ID = " + id, "RawImage");
        int width = 640;
        int height = 480;
        ImageInt img = new ImageInt(height, width, a);
        return img;
    }

    public List<String> getColumnEntries(String schemaName, String tableName, String columnName, String sideCondition) {
        String sqlSelect = "SELECT " + columnName + " from " + schemaName + "." + tableName + " " + sideCondition;
        List<String> lsOut = new ArrayList<>();
        try (Connection conn = this.connect(this.reactor);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlSelect);) {

            while (rs.next()) {
                lsOut.add(rs.getString(columnName));
            }

        } catch (SQLException ex) {
            TIVLog.tivLogger.log(Level.SEVERE, "Cannot execute query: " + sqlSelect, ex);
        }
        return lsOut;
    }

    public int performStatement(String sqlStatement) {

        try (Connection conn = this.connect(this.reactor);
                PreparedStatement pstmt = conn.prepareStatement(sqlStatement,
                                                                Statement.RETURN_GENERATED_KEYS)) {

            int affectedRows = pstmt.executeUpdate();
            return affectedRows;

        } catch (SQLException ex) {
            TIVLog.tivLogger.log(Level.SEVERE, "Cannot execute query: " + sqlStatement, ex);
        }
        return -1;
    }

    @Override
    public String getStatus() {
        try (Connection conn = this.connect(this.reactor)) {
            if (conn == null) {
                return "cannot establish connection";
            } else {
                try {
                    return conn.getCatalog() + "," + conn.getSchema();
                } catch (SQLException ex) {
                    TIVLog.tivLogger.log(Level.SEVERE, "Connection to SQL database has errors", ex);
                    return "error";
                }
            }

        } catch (SQLException ex) {
            TIVLog.tivLogger.log(Level.SEVERE, "Cannot check status", ex);
        }

        return null;

    }

    @Override
    public List<String> getcolumValues(String sqlSelect, String columnName) {
        List<String> lsOut = new ArrayList<>();
        try (Connection conn = this.connect(this.reactor);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlSelect);) {

            while (rs.next()) {
                lsOut.add(rs.getString(columnName));
            }

        } catch (SQLException ex) {
            TIVLog.tivLogger.log(Level.SEVERE, "Cannot execute query: " + sqlSelect, ex);
        }

        return lsOut;
    }

    @Override
    public void performStatements(List<String> sqlStatements) {
        try (Connection conn = this.connect(this.reactor);) {

            for (String sqlStatement : sqlStatements) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlStatement,
                                                                     Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.executeUpdate();
                } catch (Exception e) {
                    throw new SQLException(e);
                }
            }

        } catch (SQLException ex) {
            TIVLog.tivLogger.log(Level.SEVERE, "Cannot execute query list", ex);
        }
    }

    public byte[] getBinaryStream(String sqlStatement, String Column) throws SQLException {
        BufferedImage img = null;
        try (Connection conn = this.connect(this.reactor); PreparedStatement pstmt = conn.prepareStatement(sqlStatement); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Blob blob = rs.getBlob(Column);
                byte[] a = blob.getBytes(1, (int) blob.length());
                InputStream is = new ByteArrayInputStream(a);

                return a;
//                try (InputStream is = blob.getBinaryStream()) {
//                    return is;
//                } catch (IOException ex) {
//                    TIVLog.tivLogger.log(Level.INFO, "Cannot execute read from bitestream, try netx ...", ex);
//                }
            }
        } catch (Exception e) {
            TIVLog.tivLogger.log(Level.SEVERE, "Cannot execute query: " + sqlStatement, e);
        }
        return null;
    }

    public static void addLibraryPath(String pathToAdd) throws Exception {
        final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        usrPathsField.setAccessible(true);

        //get array of paths
        final String[] paths = (String[]) usrPathsField.get(null);

        //check if the path to add is already present
        for (String path : paths) {
            if (path.equals(pathToAdd)) {
                return;
            }
        }

        //add the new path
        final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
        newPaths[newPaths.length - 1] = pathToAdd;
        usrPathsField.set(null, newPaths);
    }

    @Override
    public Connection connect() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
