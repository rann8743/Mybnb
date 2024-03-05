import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class MyJDBC {

    private static final String PROPERTY_FILEPATH = "db.properties";
    private static final String INITIALIZE_DB_FILEPATH = "mybnbDDL.txt";
    private static final String POPULATE_DATA_FILEPATH = "sampleData.txt";
    private static final String URL_PROPERTIES = "url";
    private static final String MYBNB_DB_NAME = "mybnb";
    private static final String CREATE_DB_QUERY = "CREATE DATABASE IF NOT EXISTS";
    private static final String SQL_FILE_DELIMITER = ";";

    /* Sets up database for MyBnB.
    *  If populateData is true, loads sample data from sampleData.txt into the database. */
    public static Connection initializeDB() throws SQLException, IOException {

        /* Get initial connection. */
        Properties properties = new Properties();
        properties.load(new FileInputStream(PROPERTY_FILEPATH));
        String url = properties.getProperty(URL_PROPERTIES);
        Connection connection = DriverManager.getConnection(url, properties);

        /* Create new database for MyBnB. */
        Statement statement = connection.createStatement();
        statement.executeUpdate(CREATE_DB_QUERY + " " + MYBNB_DB_NAME);
        statement.close();
        connection.close();

        /* Get new connection to the new database. */
        connection = DriverManager.getConnection(url + MYBNB_DB_NAME, properties);
        statement = connection.createStatement();

        /* Initialize database. */
        Scanner scanner = new Scanner(new FileInputStream(INITIALIZE_DB_FILEPATH));
        scanner.useDelimiter(SQL_FILE_DELIMITER);
        String query;
        while (scanner.hasNext())
        {
            query = scanner.next();
            statement.executeUpdate(query);
        }
        scanner.close();

        /* Populate sample data if first time setting up db. */
        query = "SELECT * FROM amenity WHERE name = ?";
        PreparedStatement pStatement = connection.prepareStatement(query);
        pStatement.setString(1, "wifi");
        ResultSet result = pStatement.executeQuery();
        if (!result.next())
        {
            scanner = new Scanner(new FileInputStream(POPULATE_DATA_FILEPATH));
            scanner.useDelimiter(SQL_FILE_DELIMITER);
            while (scanner.hasNext())
            {
                query = scanner.next();
                statement.executeUpdate(query);
            }
            scanner.close();
        }

        statement.close();

        return connection;
    }
}