/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

import constants.Emoji;
import setting.Prefix;
import constants.Global;
import command.Command;
import net.dv8tion.jda.core.EmbedBuilder;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class PrefixCommand extends Command {
    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0) {
            e.getChannel().sendMessage("Current prefix: `" + Prefix.getDefaultPrefix() + "`").queue();
        }
        
        else {
            e.getChannel().sendMessage(Emoji.ERROR + " setting Prefix is not supported.").queue();
        }
            
    }

    
}
