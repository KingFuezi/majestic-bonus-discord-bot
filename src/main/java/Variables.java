import net.dv8tion.jda.api.EmbedBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Variables {

    public static HashSet<String> persons = new HashSet<>();
    public static String payment="";
    public static String comment="Prämie";
    public static String messageId="";
    public static String activeButton="";
    public static final String prefix="!";

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
