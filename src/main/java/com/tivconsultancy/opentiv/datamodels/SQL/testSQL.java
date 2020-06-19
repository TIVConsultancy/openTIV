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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author Thomas Ziegenhein
 */
public class testSQL {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        PostgreSQL sql = new PostgreSQL();

        String SQL = "INSERT INTO piv.experimentalists VALUES ('JavaTest', 'HZDR')";
        String SQLSelect = "SELECT ident from piv.experiment";

        long id = 0;
        String script = "INSERT INTO actor(first_name,last_name) "
                + "VALUES(?,?)";

        try (Connection conn = sql.connect();
                PreparedStatement pstmt = conn.prepareStatement(SQL,
                                                                Statement.RETURN_GENERATED_KEYS)) {

            System.out.println("1" + pstmt);
            System.out.println("-----------------");

            int affectedRows = pstmt.executeUpdate();

            System.out.println("2" + pstmt);

            System.out.println("3" + affectedRows);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        List<String> lsIdent = sql.getColumnEntries("piv", "experiment", "ident");
        for(String s : lsIdent){
            System.out.println(s);
        }

    }

}
