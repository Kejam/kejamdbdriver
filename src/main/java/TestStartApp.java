import lombok.extern.slf4j.Slf4j;
import ru.kejam.db.driver.KejamDatabaseDriver;

import java.sql.*;

@Slf4j
public class TestStartApp {
    public static final String select = "create table start_select(\n" +
            "      id int,\n" +
            "      name String\n" +
            ");";


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        log.info(KejamDatabaseDriver.getVersion());
        DriverManager.drivers().forEach(
                k -> {
                    try {
                        log.info("Found driver with name {}", String.valueOf(k.getParentLogger().getName()));
                    } catch (SQLFeatureNotSupportedException throwables) {
                        throwables.printStackTrace();
                    }
                }
        );
        Class.forName("ru.kejam.db.driver.KejamDatabaseDriver");
        try (Connection con = DriverManager
                .getConnection("jdbc:kejamdbsql://localhost:9013/", "kejam", "kejam");
             final PreparedStatement preparedStatement = con.prepareStatement(select)) {
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                log.info("Get row {}", resultSet.getRow());
            }
        }
    }
}
