/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.restricted;

import command.Command;
import constants.Emoji;
import constants.Global;
import setting.Prefix;
import main.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class ShutDownCommand extends Command{

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0) 
        {
            if(Global.D_ID.equals(e.getAuthor().getId()))
            {
                e.getChannel().sendMessage(Emoji.SUCCESS + " Shutting down...").queue();

                try {
                    Thread.sleep(2000);
                    ServerMan.shutdown();
                } catch (InterruptedException ite) {

                } catch (IOException ex) {
                    Logger.getLogger(ShutDownCommand.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(!"-h".equals(args[0])) 
                e.getChannel().sendMessage(Emoji.ERROR + " This command is for **Bot Owner** only!").queue();
                
        }
    }
}
