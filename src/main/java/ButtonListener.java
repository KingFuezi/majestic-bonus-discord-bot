import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        Variables.messageId=event.getMessage().getId();

        switch (event.getButton().getId()){
            case "downloadButton":

                File file=CreateCSV.writeDataLineByLine("Prämie.csv");

                event.reply("").addFiles(FileUpload.fromData(file)).setEphemeral(true).queue();

                break;
            case "addPersonButton":
                Variables.activeButton="addPersonButton";
                event.reply("ID der zu hinzufügenden Person:").setEphemeral(true).queue();
                break;
            case "removePersonButton":
                Variables.activeButton="removePersonButton";
                event.reply("ID der zu entfernenden Person:").setEphemeral(true).queue();
                break;
            case "editCommentButton":
                Variables.activeButton="editCommentButton";
                event.reply("Kommentar schreiben:").setEphemeral(true).queue();
                break;
            case "refreshButton":

                event.replyEmbeds(Variables.CreateEmbed().build())
                        .addActionRow(GetActiveChannels(event))
                        .addActionRow(event.getMessage().getActionRows().get(1).getComponents().get(0))
                        .addActionRow(event.getMessage().getActionRows().get(2).getComponents().get(0),
                                event.getMessage().getActionRows().get(2).getComponents().get(1),
                                event.getMessage().getActionRows().get(2).getComponents().get(2),
                                event.getMessage().getActionRows().get(2).getComponents().get(3),
                                event.getMessage().getActionRows().get(2).getComponents().get(4))
                        .queue();

                event.getMessage().getChannel().deleteMessageById(Variables.messageId).queue();

                break;
        }

    }

    public StringSelectMenu GetActiveChannels(ButtonInteractionEvent event){
        StringSelectMenu stringSelectMenu = StringSelectMenu.create("first")
                .setPlaceholder("Auswählen")
                .addOption("A","A")
                .build();

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

        return menu;
    }
}
