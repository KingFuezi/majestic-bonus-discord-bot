import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class StringSelectionInteractionListener extends ListenerAdapter {


    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {

        if (event.getValues().get(0).equalsIgnoreCase("foundNoMembers")){ //No Members in VoiceChannel got clicked

            Variables.messageId= event.getMessage().getId();
            event.editMessageEmbeds(Variables.CreateEmbed().build()).queue();
            return;


        } else if (!event.getValues().get(0).isEmpty() &&           //Voice Channel which is Stage Channel got clicked
                event.getValues().get(0).startsWith("STAGE")) {

            var allStageChannels = event.getGuild().getStageChannels();
            StageChannel selectedStageChannel=null;

            for (StageChannel channel: allStageChannels) {
                if (("STAGE "+channel.getName()).equalsIgnoreCase(event.getValues().get(0))){
                    selectedStageChannel=channel;
                }
            }
            var members=selectedStageChannel.getMembers();
            Variables.persons=new HashSet<>();

            for (Member m : members) {
                Variables.persons.add(m.getEffectiveName());
            }

        } else if (!event.getValues().get(0).isEmpty() &&           //Voice Channel which is Voice Channel got clicked
                event.getValues().get(0).startsWith("VOICE")){

            var allVoiceChannels = event.getGuild().getVoiceChannels();
            VoiceChannel selectedVoiceChannel=null;

            for (VoiceChannel channel: allVoiceChannels) {
                if (("VOICE "+channel.getName()).equalsIgnoreCase(event.getValues().get(0))){
                    selectedVoiceChannel=channel;
                }
            }
            var members=selectedVoiceChannel.getMembers();
            Variables.persons=new HashSet<>();

            for (Member m : members) {
                Variables.persons.add(m.getEffectiveName());
            }

        } else{                                                     //Money got selected
            Variables.payment=event.getValues().get(0);
        }

        Variables.messageId= event.getMessage().getId();
        event.editMessageEmbeds(Variables.CreateEmbed().build()).queue();
    }
}
