/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.moderation;

import command.Command;
import constants.Emoji;
import constants.Global;
import setting.Prefix;

import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 *
 * @author liaoyilin
 */
public class BanCommand extends Command{
    private final int delDays = 7;

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0) {
            e.getTextChannel().sendMessage(Emoji.ERROR + " You need to mention 1 or more members to ban!").queue();
        }
        
        else
        {
            Guild guild = e.getGuild();
            Member selfMember = guild.getSelfMember(); 
            
            //Check if the bot have permission to kick.
            if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {
                e.getTextChannel().sendMessage(Emoji.ERROR + " I need to have **Ban Members** Permission to ban members.").queue();
                return;
            } else if(e.getMember().hasPermission(Permission.BAN_MEMBERS)) {
                e.getTextChannel().sendMessage(Emoji.ERROR + " You need to have **Ban Members** Permission to ban members.").queue();
                return;
            }

            List<User> mentionedUsers = e.getMessage().getMentionedUsers();
            
            for (User user : mentionedUsers)
            {
                Member member = guild.getMember(user);
                if(!selfMember.canInteract(member))
                {
                    e.getTextChannel().sendMessage(Emoji.ERROR + " Cannot ban member: " + member.getEffectiveName()
                                      + ", they are in a higher role than I am!").queue();
                    return;
                }
                
                guild.getController().ban(member, delDays).queue(
                    success -> e.getTextChannel().sendMessage(Emoji.SUCCESS + " Banned " + member.getEffectiveName() + "! Don't come back!\n").queue(),
                    error -> 
                    {
                        if (error instanceof PermissionException)
                        {
                            PermissionException pe = (PermissionException) error;
                            Permission missingPermission = pe.getPermission();
                            
                            e.getTextChannel().sendMessage(Emoji.ERROR + " I do not have the permission to ban " + member.getEffectiveName()
                                            + "\nRequired permission: `" + missingPermission.getName() +"`").queue();
                        }
                        else
                        {
                            e.getTextChannel().sendMessage(Emoji.ERROR + " Unknown error while banning " + member.getEffectiveName()
                                    + ": <" + error.getClass().getSimpleName() + ">: " + error.getMessage()).queue();
                        }
                    });
            }
        }
    }

    
}
