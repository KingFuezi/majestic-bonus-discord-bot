import net.dv8tion.jda.api.EmbedBuilder;

import java.util.HashSet;

public class Variables {

    public static HashSet<String> persons = new HashSet<>();
    public static String payment="";
    public static String comment="Prämie";

    public static String messageId="";
    public static String guildId="";
    public static String guildName="";
    public static String activeButton="";
    public static final String prefix="!";
    public static final String selectedChannel=null;

    public static String botToken="";
    public static String factionName="";

    public static String jdbcConnectionString = "jdbc:mysql://161.97.78.70:3306/s22792_DiscordBotServer";
    public static String dbUser = "u22792_tTKRyZ1RL2";
    public static String dbPassword = "Qb0qJ=reI=bX@y0+cCI2UW1E";



    public static EmbedBuilder CreateEmbed(){

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Mexican - Prämierung")
                .setDescription(":busts_in_silhouette: "+ Variables.persons.size() +" Personen \n ")
                .setThumbnail("https://cdn-icons-png.flaticon.com/512/2150/2150150.png");

        for (String p :Variables.persons) {
            embedBuilder.appendDescription("* "+p + " \n");
        }
        embedBuilder.appendDescription("\n :money_with_wings: Bezahlung pro Person\n");
        if (!Variables.payment.equals("")){
            embedBuilder.appendDescription("* " + Variables.payment+"\n");
        }

        embedBuilder.appendDescription("\n :pencil: Kommentar \n");
        embedBuilder.appendDescription("* " + Variables.comment);

        return embedBuilder;
    }

}
