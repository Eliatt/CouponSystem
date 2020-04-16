package pool;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class ConnectionPool {

    private static ConnectionPool instance = null;
    private final int MAX_CONNECTIONS = 15;

    private Set<Connection> connections;

    private ConnectionPool() {
        connections = new HashSet<>();

        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            try {
                connections.add(DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/coupons" + "?user=root" +
                                "&password=root" + "&useUnicode=true" + "&useJDBCCompliantTimezoneShift=true" +
                                "&useLegacyDatetimeCode=false" + "&serverTimezone=UTC"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public synchronized Connection getConnection() {
        while (connections.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Iterator<Connection> iterator = connections.iterator();
        Connection connection = iterator.next();
        iterator.remove();
        return connection;
    }

    public synchronized void returnConnetion(Connection connection) {
        connections.add(connection);
        notifyAll();
    }

    public synchronized void closeConnection() {
        int counter = 0;
        while (counter < MAX_CONNECTIONS) {
            while (connections.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Iterator<Connection> iterator = connections.iterator();

            while (iterator.hasNext()) {
                try {
                    Connection connection = iterator.next();
                    iterator.remove();
                    connection.close();
                    counter++;
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}