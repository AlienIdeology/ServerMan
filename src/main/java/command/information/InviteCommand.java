/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command.information;

import constants.Emoji;
import constants.Global;
import setting.Prefix;
import command.Command;
import utility.UtilBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public class InviteCommand extends Command {

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        EmbedBuilder links = new EmbedBuilder();
        links.setAuthor(e.getMember().getEffectiveName() + ", some spicy links", Global.B_INVITE, e.getAuthor().getEffectiveAvatarUrl());
        links.setColor(UtilBot.randomColor());
        links.appendDescription(Emoji.INVITE + " **[Invite Link](" + Global.B_INVITE + "&guild_id=" + e.getGuild().getId() + ")  |  " +
                "[ServerMan Support Server]("+ Global.B_SERVER+")**");
        e.getChannel().sendMessage(links.build()).queue();
    }
}
