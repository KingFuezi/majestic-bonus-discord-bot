import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static void CreateDB() throws SQLException {

        Connection connection = DriverManager.getConnection(
                Variables.jdbcConnectionString,
                Variables.dbUser,
                Variables.dbPassword
        );

        /* DROPS EVERY TABLE
        connection.createStatement().execute("DROP TABLE IF EXISTS Person;");
        connection.createStatement().execute("DROP TABLE IF EXISTS BonusDetail;");
        connection.createStatement().execute("DROP TABLE IF EXISTS Bonus;");
        connection.createStatement().execute("DROP TABLE IF EXISTS Guild;");
         */

        //create table Guild
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS Guild (" +
                "GuildId INT NOT NULL," +
                "GuildName TINYTEXT NOT NULL," +
                "SelectedChannel TINYTEXT," +
                "PRIMARY KEY (GuildId));"
        );

        //create table Bonus
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS Bonus (" +
                "BonusId INT NOT NULL AUTO_INCREMENT," +
                "GuildId INT NOT NULL," +
                "CreationDate DATETIME NOT NULL," +
                "TotalPayment INT," +
                "Comment TINYTEXT," +
                "PRIMARY KEY (BonusId)," +
                "FOREIGN KEY (GuildId) REFERENCES Guild(GuildId));"
        );

        //create table BonusDetail
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS BonusDetail (" +
                "DetailId INT NOT NULL AUTO_INCREMENT," +
                "BonusId INT NOT NULL," +
                "PaymentPerUser INT," +
                "PRIMARY KEY (DetailId)," +
                "FOREIGN KEY (BonusId) REFERENCES Bonus(BonusId));"
        );

        //create table Person
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS Person (" +
                "PersonId INT NOT NULL," +
                "DetailId INT NOT NULL," +
                "EffectiveName TINYTEXT," +
                "StaticId INT," +
                "FOREIGN KEY (DetailId) REFERENCES BonusDetail(DetailId)," +
                "PRIMARY KEY (PersonId));"
        );

        System.out.println("Verbindung erfolgreich hergestellt");
    }
}
