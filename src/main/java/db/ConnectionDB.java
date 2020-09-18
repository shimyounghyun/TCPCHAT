package db;

import java.sql.*;
import java.util.Map;

public class ConnectionDB {
    private Connection con;

    public ConnectionDB(String ip, String database){

    }

    public Statement getSTMT() {
        Statement stmt = null;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return stmt;
    }

    public PreparedStatement getPSTMT(String sql) {
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return pstmt;
    }

    public void close(Object o) {
        try {
            if(o instanceof Connection) {
                ((Connection)o).close();
            } else if(o instanceof Statement) {
                ((Statement)o).close();
            } else if(o instanceof PreparedStatement) {
                ((PreparedStatement)o).close();
            } else if(o instanceof ResultSet) {
                ((ResultSet)o).close();
            }
        } catch(Exception e) {}
    }

    public Connection getCon() {
        return con;
    }
}
