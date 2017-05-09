/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.moderation;

import constants.Emoji;
import constants.Global;
import setting.Prefix;
import command.Command;

import java.util.List;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 *
 * @author liaoyilin
 */
public class PruneCommand extends Command{
    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0)
        {
            e.getChannel().sendMessage(Emoji.ERROR + " You must add a number after Prune command to delete an amount of messages.\n"
                                         + "Use `" + Prefix.getDefaultPrefix() + "prune -h` for help.").queue();
        }
        
        else
        {
            TextChannel chan = e.getTextChannel();
            /*if (!e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE) ||
                !e.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_HISTORY)) {
                chan.sendMessage(Emoji.ERROR + " I do not have the `Manage Message` and `Message History` Permission!").queue();
                return;
            }*/
            
            //Parse String to int, detect it the input is valid.
            Integer msgs = new Integer(0);
            try {
                msgs = Integer.parseInt(args[0]);
            } catch (NumberFormatException nfe) {
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter a valid number.").queue();
            }
            
            if(msgs <= 1 || msgs > 100) {
                e.getChannel().sendMessage(Emoji.ERROR + " Please enter a number between **2 ~ 100**.").queue();
                return;
            }
            
            //Delete command call
            e.getTextChannel().deleteMessageById(e.getMessage().getId()).complete();
            
            chan.getHistory().retrievePast(msgs).queue((List<Message> mess) -> {
                try {
                    e.getTextChannel().deleteMessages(mess).queue(
                            success ->
                                    chan.sendMessage(Emoji.SUCCESS + " `" + args[0] + "` messages deleted.")
                                            .queue(message -> {
                                                message.delete().queueAfter(2, TimeUnit.SECONDS);
                                            }),
                            error -> chan.sendMessage(Emoji.ERROR + " An Error occurred!").queue());
                } catch (IllegalArgumentException iae) {
                    e.getChannel().sendMessage(Emoji.ERROR + " Cannot delete messages older than 2 weeks.").queue();
                } catch (PermissionException pe) {
                    throw pe;
                }
            });
            
        }
    }

    
}
