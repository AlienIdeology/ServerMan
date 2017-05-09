/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.moderation;

import command.Command;
import constants.Emoji;
import setting.Prefix;

import java.awt.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

/**
 *
 * @author liaoyilin
 */
public class UnbanCommand extends Command{
    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        if(args.length == 0 && e.getChannelType() != e.getChannelType().PRIVATE) 
        {
            e.getTextChannel().sendMessage(Emoji.ERROR + " You need to mention 1 or more members to unban!").queue();
        }
        
        else
        {
            try {
                Guild guild = e.getGuild();
                Member selfMember = guild.getSelfMember(); 

                //Check if the bot have permission to kick.
                if (!selfMember.hasPermission(Permission.BAN_MEMBERS))
                    e.getTextChannel().sendMessage(Emoji.ERROR + " I need to have **Ban Members* Permission to unban members.").queue();

                for (String userId : args)
                {
                    Member member = guild.getMemberById(userId);
                    boolean hasError = false;

                    try
                    {
                        guild.getController().unban(userId).queue();
                    } catch(Exception ex) {
                        hasError = true;
                        if (ex instanceof PermissionException)
                            {
                                PermissionException pe = (PermissionException) ex;
                                Permission missingPermission = pe.getPermission();

                                e.getTextChannel().sendMessage(Emoji.ERROR + " PermissionError unbanning: " + ex.getMessage()).queue();
                            }
                            else
                            {
                                e.getTextChannel().sendMessage(Emoji.ERROR + " Unknown error while unbanning: " + ex.getClass().getSimpleName() + ">: " + ex.getMessage()).queue();
                            }
                    }
                    if(hasError == false)
                        e.getTextChannel().sendMessage(Emoji.SUCCESS + " Succesfully unbanned! Join back!\n").queue();
                }
            } catch (Exception ex) {
                e.getTextChannel().sendMessage(Emoji.ERROR + " Cannot find this (these) member!\n").queue();
            }
            
        }
    }

    
}
