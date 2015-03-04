package com.conferenceengineer.server.datamodel;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Utility routines.
 */
public final class DBUtils {

    /**
     * Close a prepared statement and discard any exceptions.
     *
     * @param ps The prepared statement.
     */
    public static void close(PreparedStatement ps) {
        try {
            ps.close();
        } catch(SQLException e) {
            // Ignore
        }
    }

}
