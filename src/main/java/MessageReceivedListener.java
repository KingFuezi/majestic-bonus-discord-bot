import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class MessageReceivedListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        String messageReceived=event.getMessage().getContentRaw();

        if (event.getMessage().getContentRaw().startsWith(Variables.prefix)){

            switch (messageReceived.substring(1).toLowerCase()){
                case "channel":
                    break;
            }
            return;
        }


        if(!event.getMessage().getAuthor().isBot()) {

            switch (Variables.activeButton){
                case "downloadButton":
                    break;
                case "addPersonButton":
                    SplitAndAddOrRemovePeople(messageReceived,event,true);
                    break;
                case "removePersonButton":
                    SplitAndAddOrRemovePeople(messageReceived,event,false);
                    break;
                case "editCommentButton":
                    Variables.comment = event.getMessage().getContentRaw();
                    break;
            }
            //deletes the user input
            event.getMessage().getChannel().deleteMessageById(event.getMessage().getChannel().getLatestMessageId()).queue();

            //edits the embed
            event.getGuildChannel().editMessageEmbedsById(Variables.messageId, Variables.CreateEmbed().build()).queue();

            Variables.activeButton="";
        }

    }
    public void SplitAndAddOrRemovePeople(String input,MessageReceivedEvent event,boolean addPerson){
        String seperator="";
        if (input.contains(";")){
            seperator=";";
        } else if (input.contains(",")) {
            seperator=",";
        }
        if (!seperator.equals("")){
            String[] people= input.split(seperator);
            for (String person:people) {
                if (addPerson){
                    AddPerson(person,event);
                }else{
                    RemovePerson(person,event);
                }
            }
        }
        if (addPerson){
            AddPerson(input,event);
        }else{
            RemovePerson(input,event);
        }

    }

    public void RemovePerson(String input, MessageReceivedEvent event){

        if (input.strip().equalsIgnoreCase("alle")||input.strip().equalsIgnoreCase("all")){
            Variables.persons=new HashMap<>();
            return;
        }

        var members=event.getGuild().getMembers();

        for (Member member:members) {
            if (member.getEffectiveName().contains("|")){
                if (member.getEffectiveName().split("\\|")[1].strip().equals(input.strip())){
                    Variables.persons.remove(member.getIdLong());
                }
            } else if (member.getEffectiveName().equalsIgnoreCase(input)) {
                Variables.persons.remove(member.getIdLong());
            }
        }
    }

    public void AddPerson(String input, MessageReceivedEvent event){

        var members=event.getGuild().getMembers();

        for (Member member:members) {
            if (member.getEffectiveName().equalsIgnoreCase(input)){
                Variables.persons.put(member.getIdLong(),member.getEffectiveName());
                return;
            }
            if (member.getEffectiveName().contains("|")){
                if (member.getEffectiveName().split("\\|")[1].strip().equals(input.strip())){
                    Variables.persons.put(member.getIdLong(),member.getEffectiveName());
                    return;
                }
            }
        }
    }
}
