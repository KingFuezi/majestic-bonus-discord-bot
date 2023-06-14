import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.EventListener;

public class Main implements EventListener {

    public static void main(String[] args) throws InterruptedException, LoginException, SQLException {

        SetVariablesFromEnv();

        JDA jda = JDABuilder.createDefault(Variables.botToken)
                .addEventListeners(new ButtonListener())
                .addEventListeners(new SlashInteractionListener())
                .addEventListeners(new StringSelectionInteractionListener())
                .addEventListeners(new MessageReceivedListener())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .build()
                .awaitReady();

        jda.upsertCommand("prämie","Prämie erstellen!").queue();
        jda.upsertCommand("set-channel","Channel auswählen!").queue();

        Database.CreateDB();
    }

    public static void SetVariablesFromEnv(){

        Dotenv dotenv = Dotenv.load();

        Variables.botToken=dotenv.get("BOT_TOKEN");
        Variables.factionName=dotenv.get("FACTION_NAME");

        Variables.jdbcConnectionString=dotenv.get("JDBC_CONNECTION_STRING");
        Variables.dbUser=dotenv.get("DATABASE_USER");
        Variables.dbPassword=dotenv.get("DATABASE_PASSWORD");


    }
}
