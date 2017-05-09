/*
 * 
 * ServerMan, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) ServerMan
 */
package command.information;

import command.Command;
import constants.Global;
import constants.Emoji;
import setting.Prefix;
import system.AIPages;
import utility.UtilBot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class ListCommand extends Command {

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if (args.length > 0) {
            try {
                int page = 1;
                if(null == args[0])
                    e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid list type. "
                            + "`=list server, member, role, or channel`").queue();
                else {
                    if(args.length > 1)
                        page = Integer.parseInt(args[1]);
                }
                
                switch (args[0]) {
                    case "member":
                    case "mem":
                        listMember(e,page);
                        break;
                    case "role":
                    case "roles":
                        listRole(e,page);
                        break;
                    case "channel":
                    case "channels":
                        listChannel(e,page);
                        break;
                    default:
                        e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid list type. "
                                + "`=list server, member, role, or channel`").queue();
                        break;
                }
            } catch (IllegalArgumentException  | IndexOutOfBoundsException ex) {
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid page number.").queue();
                return;
            }
        }
    }

    
    public void listMember(MessageReceivedEvent e, int page)
    {
        List<Member> memsList = UtilBot.getMemberList(e);
        AIPages pages = new AIPages(memsList, 10);

        List<Member> members = pages.getPage(page);
        String output = "```md\n\n[Member List](Total: " + memsList.size() + ")\n\n";

        int index = (page-1) * 10+1;
        for(int i = 0; i < members.size(); i++) {
            Member mem = members.get(i);
            String role = mem.getRoles().isEmpty() ? mem.getGuild().getPublicRole().getName() : members.get(i).getRoles().get(0).getName();
            output += (i+index) + ". " + mem.getEffectiveName() + " #" + mem.getUser().getDiscriminator()
                   + " <Role: " + role + ">\n\n";
        }

        output += "--------\n\n# Page(s): " + page + " / " + pages.getPages() + "  (Sorted by role position and alphabetical order)\n\n"
                + "# Use " + Prefix.getDefaultPrefix() + "list member [Page Number] to show more pages.```";
        e.getChannel().sendMessage(output).queue();
    }
    
    public void listRole(MessageReceivedEvent e, int page)
    {
        List<Role> roleList = UtilBot.getRoleList(e);
        AIPages pages = new AIPages(roleList, 10);

        List<Role> roles = pages.getPage(page);
        String output = "```md\n\n[Role List](Total: " + roleList.size() + ")\n\n";
        
        int index = (page-1) * pages.getPageSize()+1;
        for(int i = 0; i < roles.size(); i++) {
            if(!roles.get(i).isPublicRole()) {
                output += (i+index) + ". " + roles.get(i).getName();
                output += roles.get(i).getColor() == null ?  " <Color: None>\n\n" : " <Color: " + UtilBot.getHexCode(roles.get(i).getColor()) + ">\n\n";
            }
        }

        output += "--------\n\n# Page(s): " + page + " / " + pages.getPages() + "  (Sorted by role position)\n\n"
                + "# Use " + Prefix.getDefaultPrefix() + "list role [Page Number] to show more pages.```";
        e.getChannel().sendMessage(output).queue();
    }
    
    public void listChannel(MessageReceivedEvent e, int page)
    {
        List<Channel> tcList = UtilBot.getTextChannelList(e);
        List<Channel> vcList = UtilBot.getVoiceChannelList(e);
        List<Channel> chanList = new ArrayList();
        chanList.addAll(tcList);
        chanList.addAll(vcList);

        AIPages pages = new AIPages(chanList, 10);

        List<Channel> chans = pages.getPage(page);
        String output = "```md\n\n[Channel List](Total: " + chanList.size() + ")\n\n";
        
        int index = (page-1) * pages.getPageSize()+1;
        for(int i = 0; i < chans.size(); i++) {
            if(i==0 && page == 1)
                output += "/* Text Channel(s): " + tcList.size() + " *\n\n";
            if(i==tcList.size()-((page-1)*10))
                output += "/* Voice Channel(s): " + vcList.size() + " *\n\n";
            
            output += (i+index) + ". " + chans.get(i).getName() + " <Members: " + chans.get(i).getMembers().size() + ">\n\n";
        }

        output += "--------\n\n# Page(s): " + page + " / " + pages.getPages() + "  (Sorted by channel position)\n\n"
                + "# Use " + Prefix.getDefaultPrefix() + "list channel [Page Number] to show more pages.```";
        e.getChannel().sendMessage(output).queue();
    }
    
}
