import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.HashMap;


public class SlashInteractionListener extends ListenerAdapter  {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){

        Variables.payment="";
        Variables.persons=new HashMap<>();
        Variables.comment="Prämie";

        //select menu with A,A as placeholder (need 1 Option to create)
        StringSelectMenu stringSelectMenu = StringSelectMenu.create("first")
                .setPlaceholder("Auswählen")
                .addOption("A","A")
                .build();

        //if command is /prämie
        if (event.getInteraction().getName().equalsIgnoreCase("prämie")){

            //gets all voice channels
            var voiceChannels = event.getGuild().getVoiceChannels();
            var stageChannels=event.getGuild().getStageChannels();

            //iterate through all voice channels
            for (int i=0;i<voiceChannels.size();i++){

                // gets the amount of members in current channel
                var membersInChannel = voiceChannels.get(i).getMembers().size();

                //if member count is at least 1
                if (membersInChannel>0){

                    stringSelectMenu = stringSelectMenu
                            .createCopy()
                            .addOption(
                                    voiceChannels.get(i).getName() + " - " + membersInChannel + " Member",
                                    "VOICE "+voiceChannels.get(i).getName())
                            .build();

                }
            }
            for (StageChannel channel:stageChannels) {
                var membersInStageChannel = channel.getMembers().size();
                if (membersInStageChannel>0){
                    stringSelectMenu=stringSelectMenu
                            .createCopy()
                            .addOption(channel.getName() + " - " + membersInStageChannel + " Member",
                                    "STAGE "+channel.getName())
                            .build();
                }
            }

            if (stringSelectMenu.getOptions().size()==1){
                stringSelectMenu=stringSelectMenu.createCopy().addOption("Keine Member in Talks gefunden","foundNoMembers").build();
            }

            StringSelectMenu menu = StringSelectMenu.create("prämie")
                    .setPlaceholder("Channel auswählen")
                    .addOptions(stringSelectMenu.getOptions().subList(1,stringSelectMenu.getOptions().size()))
                    .build();

            StringSelectMenu payment = StringSelectMenu.create("payment")
                    .setPlaceholder("Bezahlung pro Person auswählen")
                    .addOption("15k","15000")
                    .addOption("20k","20000")
                    .addOption("25k","25000")
                    .addOption("30k","30000")
                    .addOption("35k","35000")
                    .addOption("40k","40000")
                    .addOption("45k","45000")
                    .addOption("50k","50000")
                    .addOption("60k","60000")
                    .addOption("75k","75000")
                    .addOption("100k","100000")
                    .addOption("150k","150000")
                    .addOption("200k","200000")
                    .build();

            Button downloadbutton = Button.secondary("downloadButton", "Download CSV").withEmoji(Emoji.fromFormatted("⬇"));
            Button addPersonButton = Button.success("addPersonButton", "Person hinzufügen").withEmoji(Emoji.fromFormatted("⬆"));
            Button removePersonButton = Button.danger("removePersonButton", "Person entfernen").withEmoji(Emoji.fromFormatted("⬇"));
            Button editCommentButton = Button.secondary("editCommentButton", "Kommentar ändern").withEmoji(Emoji.fromFormatted("🔄"));
            Button refreshButton = Button.success("refreshButton", "Refresh").withEmoji(Emoji.fromFormatted("🔄"));

            event.replyEmbeds(ticketEmbed().build())
                    .addActionRow(menu)
                    .addActionRow(payment)
                    .addActionRow(downloadbutton,addPersonButton,removePersonButton,editCommentButton,refreshButton)
                    //.setEphemeral(true) //TODO
                    .queue();
        }
    }

    private EmbedBuilder ticketEmbed(){

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Mexican - Prämierung")
                .setDescription(":busts_in_silhouette: " + Variables.persons.size() + " Personen \n\n :money_with_wings: Bezahlung pro Person \n\n :pencil: Kommentar \n - "+Variables.comment)
                .setThumbnail("https://cdn-icons-png.flaticon.com/512/2150/2150150.png");
        return embedBuilder;
    }
}
