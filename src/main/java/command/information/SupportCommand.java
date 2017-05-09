/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

import command.Command;
import constants.Emoji;
import constants.Global;
import setting.Prefix;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class SupportCommand extends Command{

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0) 
        {
            String msg = Emoji.INVITE + " Join this server for music!\n"
                                        + Global.B_SERVER;
            
            e.getChannel().sendMessage(msg).queue();
        }
    }

    
}
