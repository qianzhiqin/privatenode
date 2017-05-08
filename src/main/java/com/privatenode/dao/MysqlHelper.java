package com.privatenode.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class MysqlHelper {
    // ????????????
    private static Connection conn = null;
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;
    private static CallableStatement cs = null;

    private static String driver = "";
    private static String url = "";
    private static String userName = "";
    private static String password = "";

    private static Properties pp = null;
    private static InputStream fis = null;

    public static Connection getConn() {
        return conn;
    }

    public static PreparedStatement getPs() {
        return ps;
    }

    public static ResultSet getRs() {
        return rs;
    }

    public static CallableStatement getCs() {
        return cs;
    }

    static {
        try {
            pp = new Properties();
//            fis = new FileInputStream("db.properties");
            fis = MysqlHelper.class.getResourceAsStream("/db.properties");
            pp.load(fis);
            driver = pp.getProperty("jdbc.mysql.driver");
            url = pp.getProperty("jdbc.mysql.url");
            userName = pp.getProperty("jdbc.mysql.username");
            password = pp.getProperty("jdbc.mysql.password");

            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            fis = null;

        }
    }

    public static Connection getConnection() {
        try {
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void executeUpdateMultiParams(String[] sql,
                                                String[][] parameters) {
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            for (int i = 0; i < sql.length; i++) {
                if (parameters[i] != null) {
                    ps = conn.prepareStatement(sql[i]);
                    for (int j = 0; j < parameters[i].length; j++)
                        ps.setString(j + 1, parameters[i][j]);
                }
                ps.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e.getMessage());
        } finally {
            close(rs, ps, conn);
        }
    }

    public static void executeUpdate(String sql, String[] parameters) {
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            if (parameters != null)
                for (int i = 0; i < parameters.length; i++) {
                    ps.setString(i + 1, parameters[i]);
                }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            close(rs, ps, conn);
        }
    }

    // select
    public static ResultSet executeQuery(String sql, String[] parameters) {
        ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    ps.setString(i + 1, parameters[i]);
                }
            }
            rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {

        }
        return rs;
    }

    public static void callProc(String sql, String[] parameters) {
        try {
            conn = getConnection();
            cs = conn.prepareCall(sql);
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++)
                    cs.setObject(i + 1, parameters[i]);
            }
            cs.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            close(rs, cs, conn);
        }
    }

    public static CallableStatement callProcInput(String sql, String[] inparameters) {
        try {
            conn = getConnection();
            cs = conn.prepareCall(sql);
            if (inparameters != null)
                for (int i = 0; i < inparameters.length; i++)
                    cs.setObject(i + 1, inparameters[i]);
            cs.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {

        }
        return cs;
    }

    public static CallableStatement callProcOutput(String sql, Integer[] outparameters) {
        try {
            conn = getConnection();
            cs = conn.prepareCall(sql);
            if (outparameters != null)
                for (int i = 0; i < outparameters.length; i++)
                    cs.registerOutParameter(i + 1, outparameters[i]);
            cs.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {

        }
        return cs;
    }

    public static void close(ResultSet rs, Statement ps, Connection conn) {
        if (rs != null)
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        rs = null;
        if (ps != null)
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        ps = null;
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        conn = null;
    }
}