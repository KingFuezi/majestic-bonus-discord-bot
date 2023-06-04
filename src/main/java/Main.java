import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.EventListener;

public class Main implements EventListener {

    public static void main(String[] args) throws InterruptedException, LoginException {
        JDA jda = JDABuilder.createDefault("MTA2NjM4NDE4ODM4ODkzMzgwMg.GjYnd6.-TD2oKz968vOL_q1HV4VDhTISbucDL_OgD5l48")
                .addEventListeners(new ButtonListener())
                .addEventListeners(new SlashInteractionListener())
                .addEventListeners(new StringSelectionInteractionListener())
                .addEventListeners(new MessageReceivedListener())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .build()
                .awaitReady();

        jda.upsertCommand("prämie","Prämie vergeben!").queue();

    }


}
