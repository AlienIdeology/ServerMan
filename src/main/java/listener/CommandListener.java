/* 
 * ServerMan by AlienIdeology
 * 
 * CommandListener
 * Deliver commands to CommandParser, then handle the command by calling the corisponding
 * Command Class.
 */
package listener;

import constants.Global;
import net.dv8tion.jda.core.entities.VoiceChannel;
import secret.PrivateConstant;
import setting.Prefix;
import main.*;
import static main.ServerMan.commands;
import constants.Emoji;
import setting.RateLimiter;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.regex.Pattern;

/**
 *
 * @author liaoyilin
 */
public class CommandListener extends ListenerAdapter {
    
    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        /**
         * Reject Commands from non Ayylien users
         */
        if(!e.getAuthor().getId().equals(Global.D_ID))
            return;
        
        /**
         * Detect commands.
         */
        if(!e.getMessage().getAuthor().getId().equals(e.getJDA().getSelfUser().getId()))
        {
            //Message from Guild that starts with Prefix or mention
            if (e.getChannelType() == ChannelType.TEXT &&
               (e.getMessage().getContent().startsWith(Prefix.getDefaultPrefix()) ||
                e.getMessage().getStrippedContent().startsWith("@" + e.getGuild().getSelfMember().getEffectiveName())))
            {
                try {
                    if(RateLimiter.isSpam(e)) return;
                    handleCommand(ServerMan.parser.parse(e.getMessage().getContent(), e));
                } catch (Exception ex) {
                    e.getChannel().sendMessage("An error occurred! Informed the owner.").queue();
                }
            }
             
            else if (e.getChannelType() == ChannelType.PRIVATE)
            {
                if(RateLimiter.isSpam(e)) return;
                handleCommand(ServerMan.parser.parsePrivate(e.getMessage().getContent(), e));
            }
        }
    }

    public static void handleCommand(CommandParser.CommandContainer cmd)
    {
        if(commands.containsKey(cmd.invoke)) {
            cmd.event.getChannel().sendTyping().queue(success -> 
            {
                MessageReceivedEvent e = cmd.event;
                try {
                    commands.get(cmd.invoke).action(cmd.args, e);
                } catch (NullPointerException npe) {
                    if(e.isFromType(ChannelType.PRIVATE))
                        e.getPrivateChannel().sendMessage(Emoji.ERROR + " This command is not supported in DM.").queue();
                    else
                        throw npe;
                } catch (PermissionException pe) {
                    e.getChannel().sendMessage(Emoji.ERROR + " I need the following permission to the command!\n"
                        +"`"+pe.getPermission().getName()+"`").queue();
                } catch (Exception ex) {
                    e.getChannel().sendMessage(Emoji.ERROR + " An error occurred! Informed the owner.").queue();
                }
            });
        }
    }

}
