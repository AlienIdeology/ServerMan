/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

import command.*;
import constants.Emoji;
import constants.Global;
import setting.Prefix;
import main.ServerMan;
import utility.UtilBot;
import utility.UtilString;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class StatusCommand extends Command{

    public final static String HELP = "This command is for getting this bot's status.\n"
                              + "Command Usage: `" + Prefix.getDefaultPrefix() + "status` or `" + Prefix.getDefaultPrefix() + "uptime`\n"
                              + "Parameter: `-h | null`";

    private String type = "";
    
    public StatusCommand(String invoke)
    {
        if("status".equals(invoke)) type = "status";
        else if("uptime".equals(invoke)) type = "uptime";
    }


    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0)
        {
            if("uptime".equals(type))
            {
                String uptime = UtilString.formatTime(System.currentTimeMillis() - ServerMan.timeStart);
                e.getChannel().sendMessage(Emoji.STOPWATCH + " ServerMan has been up for: " + uptime).queue();
            }

            else if("status".equals(type))
            {
                e.getChannel().sendMessage(UtilBot.postStatus(e.getJDA()).build()).queue();
            }
        }
    }
    
}
