import java.math.BigInteger;
import java.sql.*;
import java.util.Map;

public class Database {

    public static void CreateDB() throws SQLException {

        Connection connection = DriverManager.getConnection(
                Variables.jdbcConnectionString,
                Variables.dbUser,
                Variables.dbPassword
        );


        connection.createStatement().execute("DROP TABLE IF EXISTS BonusDetail;");
        connection.createStatement().execute("DROP TABLE IF EXISTS Bonus;");
        connection.createStatement().execute("DROP TABLE IF EXISTS Person;");
        connection.createStatement().execute("DROP TABLE IF EXISTS Guild;");


        //create table Guild
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS Guild (" +
                "GuildId BIGINT NOT NULL," +
                "GuildName TINYTEXT NOT NULL," +
                "SelectedChannel TINYTEXT," +
                "PRIMARY KEY (GuildId));"
        );

        //create table Person
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS Person (" +
                "PersonId BIGINT NOT NULL," +
                "EffectiveName TINYTEXT," +
                "StaticId INT," +
                "PRIMARY KEY (PersonId));"
        );

        //create table Bonus
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS Bonus (" +
                "BonusId INT NOT NULL AUTO_INCREMENT," +
                "GuildId BIGINT NOT NULL," +
                "CreationDate DATETIME NOT NULL," +
                "CreatorId BIGINT NOT NULL," +
                "TotalPayment INT," +
                "Comment TINYTEXT," +
                "PRIMARY KEY (BonusId)," +
                "FOREIGN KEY (CreatorId) REFERENCES Person(PersonId)," +
                "FOREIGN KEY (GuildId) REFERENCES Guild(GuildId));"
        );


        //create table BonusDetail
        connection.createStatement().execute("CREATE TABLE IF NOT EXISTS BonusDetail (" +
                "DetailId INT NOT NULL AUTO_INCREMENT," +
                "BonusId INT NOT NULL," +
                "PersonId BIGINT NOT NULL," +
                "PaymentPerUser INT," +
                "PRIMARY KEY (DetailId)," +
                "FOREIGN KEY (BonusId) REFERENCES Bonus(BonusId)," +
                "FOREIGN KEY (PersonId) REFERENCES Person(PersonId));"
        );


        System.out.println("Verbindung erfolgreich hergestellt");
    }

    public static void InsertCurrentBonus() throws SQLException {

        Connection connection = DriverManager.getConnection(
                Variables.jdbcConnectionString,
                Variables.dbUser,
                Variables.dbPassword
        );



        PreparedStatement searchGuildId = connection.prepareStatement("SELECT * from Guild WHERE GuildId = ? LIMIT 1");
        searchGuildId.setLong(1,Long.parseLong(Variables.guildId));
        ResultSet resultSearchGuildId =searchGuildId.executeQuery();

        //if guild does not exist it gets created first
        if (!resultSearchGuildId.next()){
            PreparedStatement insertNewGuild=connection.prepareStatement(
                    "INSERT INTO Guild values (?,?,?)");
            insertNewGuild.setLong(1,Long.parseLong(Variables.guildId));
            insertNewGuild.setString(2,Variables.guildName);
            insertNewGuild.setString(3,Variables.selectedChannel);
            insertNewGuild.execute();
        }


        PreparedStatement searchCreatorId = connection.prepareStatement("SELECT * from Person WHERE PersonId = ? LIMIT 1");
        searchCreatorId.setLong(1,Variables.creator.getFirst());
        ResultSet resultSearchCreatorId =searchCreatorId.executeQuery();

        //if creator person does not exist it gets created first
        if (!resultSearchCreatorId.next()){
            PreparedStatement insertCreator = connection.prepareStatement(
                    "INSERT INTO Person values (?,?,?)"
            );
            insertCreator.setLong(1,Variables.creator.getFirst());
            insertCreator.setString(2,Variables.creator.getSecond());
            insertCreator.setInt(3,Integer.parseInt(Variables.creator.getSecond().split("\\|")[1].strip()));
            insertCreator.execute();
        }

        //Insert Bonus
        PreparedStatement insertBonus = connection.prepareStatement(
                "INSERT INTO Bonus(GuildId,CreationDate,CreatorId,TotalPayment,Comment) values (?,NOW(),?,?,?)"
        );
        insertBonus.setLong(1,Long.parseLong(Variables.guildId));
        insertBonus.setLong(2,Variables.creator.getFirst());
        insertBonus.setInt(3,Variables.persons.size() * Integer.parseInt(Variables.payment));
        insertBonus.setString(4,Variables.comment);
        insertBonus.execute();

        //Search for Bonus to get Id for BonusDetail
        ResultSet resultSearchBonusId = connection.createStatement().executeQuery("SELECT MAX(BonusId) from Bonus");
        resultSearchBonusId.next();

        for (Map.Entry<Long,String> person:Variables.persons.entrySet()) {

            PreparedStatement searchPerson = connection.prepareStatement(
                    "SELECT * from Person WHERE PersonId = ?"
            );
            searchPerson.setLong(1, person.getKey());
            ResultSet resultSearchPerson = searchPerson.executeQuery();


            //Insert person if the person does not exist
            if (!resultSearchPerson.next()){
                PreparedStatement insertPerson = connection.prepareStatement(
                        "INSERT INTO Person values (?,?,?)"
                );
                insertPerson.setLong(1,person.getKey());
                insertPerson.setString(2,person.getValue());
                insertPerson.setInt(3,Integer.parseInt(person.getValue().split("\\|")[1].strip()));
                insertPerson.execute();
            }

            //Insert BonusDetail
            PreparedStatement insertBonusDetail = connection.prepareStatement(
                    "INSERT INTO BonusDetail(BonusId,PersonId,PaymentPerUser) values (?,?,?)"
            );

            insertBonusDetail.setInt(1,resultSearchBonusId.getInt(1));
            insertBonusDetail.setLong(2,person.getKey());
            insertBonusDetail.setInt(3,Integer.parseInt(Variables.payment));
            insertBonusDetail.execute();
        }
    }
}
