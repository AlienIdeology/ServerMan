/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

//Set to SUPPORT PRIVATE CHANNEL.

import constants.Emoji;
import setting.Prefix;
import constants.Global;
import command.Command;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.EmbedBuilder;

        
/**
 *
 * @author liaoyilin
 */
public class PingCommand extends Command {

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0) 
        {
            long time = System.currentTimeMillis();
            String respond = Emoji.PING + " Pong.\n";
            e.getChannel().sendMessage(respond).queue((Message m) ->
                    m.editMessage(respond+"Current ping `%d` ms", System.currentTimeMillis() - time).queue());
        }
    }

    
}
