/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.moderation;

import command.Command;
import constants.Emoji;
import setting.Prefix;

import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 *
 * @author liaoyilin
 */
public class KickCommand extends Command{
    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0) {
            e.getTextChannel().sendMessage(Emoji.ERROR + " You need to mention 1 or more members to kick!").queue();
        }
        
        else if(e.getChannelType() != e.getChannelType().PRIVATE)
        {
            Guild guild = e.getGuild();
            Member selfMember = guild.getSelfMember(); 
            
            //Check if the bot have permission to kick.
            if (!selfMember.hasPermission(Permission.KICK_MEMBERS)) {
                e.getTextChannel().sendMessage(Emoji.ERROR + " I need to have **Kick Members* Permission to kick members.").queue();
                return;
            }else if(e.getMember().hasPermission(Permission.KICK_MEMBERS)) {
                e.getTextChannel().sendMessage(Emoji.ERROR + " You need to have **Kick Members** Permission to ban members.").queue();
                return;
            }
            
            List<User> mentionedUsers = e.getMessage().getMentionedUsers();
            
            for (User user : mentionedUsers)
            {
                Member member = guild.getMember(user);
                if(!selfMember.canInteract(member))
                {
                    if(mentionedUsers.size() > 1)
                    {
                        e.getTextChannel().sendMessage(Emoji.ERROR + " Cannot kick member: " + member.getEffectiveName()
                                      + ". They are in a higher role than I am!").queue();
                        return;
                    }
                    else
                    {
                        e.getTextChannel().sendMessage(Emoji.ERROR + " Cannot kick member: " + member.getEffectiveName()
                                      + ". the person is in a higher role than I am!").queue();
                        return;
                    }
                }
                
                guild.getController().kick(member).queue(
                    success -> e.getTextChannel().sendMessage(Emoji.SUCCESS + " Kicked " + member.getEffectiveName() + "! Bye!").queue(),
                    error -> 
                    {
                        if (error instanceof PermissionException)
                        {
                            PermissionException pe = (PermissionException) error;
                            Permission missingPermission = pe.getPermission();
                            
                            e.getTextChannel().sendMessage(Emoji.ERROR + " PermissionError kicking " + member.getEffectiveName()
                                            + ": " + error.getMessage()).queue();
                        }
                        else
                        {
                            e.getTextChannel().sendMessage(Emoji.ERROR + " Unknown error while kicking " + member.getEffectiveName()
                                    + ": <" + error.getClass().getSimpleName() + ">: " + error.getMessage()).queue();
                        }
                    });
            }
        }
    }
    

    
}
