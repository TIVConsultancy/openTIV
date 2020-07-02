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

import com.tivconsultancy.opentiv.logging.TIVLog;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author TZ ThomasZiegenhein@TIVConsultancy.com +1 480 494 7254
 */
public class PostgreSQL implements SQLDatabase {

    private String url = "jdbc:postgresql://localhost/localpiv";
    private String user = "adminpiv";
    private String password = "default";

    public PostgreSQL() {

    }

    public PostgreSQL(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }
    
    public String getUser(){
        return user;
    }

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection connect() {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            TIVLog.tivLogger.log(Level.SEVERE, "Cannot connect to SQL on URL: " + url, e);
        }

        return conn;
    }

    public List<String> getColumnEntries(String schemaName, String tableName, String columnName) {
        String sqlSelect = "SELECT " + columnName + " from " + schemaName + "." + tableName;
        List<String> lsOut = new ArrayList<>();
        try (Connection conn = this.connect();
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
    
    public List<String> getColumnEntries(String schemaName, String tableName, String columnName, String sideCondition) {
        String sqlSelect = "SELECT " + columnName + " from " + schemaName + "." + tableName + " " +sideCondition;
        List<String> lsOut = new ArrayList<>();
        try (Connection conn = this.connect();
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

        try (Connection conn = this.connect();
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
        try (Connection conn = this.connect()) {
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
        try (Connection conn = this.connect();
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
        try (Connection conn = this.connect();) {

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
    
    public InputStream getBinaryStream(String sqlStatement) throws SQLException {
        BufferedImage img = null;
        try (Connection conn = this.connect();PreparedStatement pstmt = conn.prepareStatement(sqlStatement); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                try (InputStream is = rs.getBinaryStream(1)) {
                    return is;
                } catch (IOException ex) {
                    TIVLog.tivLogger.log(Level.INFO, "Cannot execute read from bitestream, try netx ...", ex);
                }
            }
        } catch (Exception e) {
            TIVLog.tivLogger.log(Level.SEVERE, "Cannot execute query: " +sqlStatement, e);
        }
        return null;
    }

}
