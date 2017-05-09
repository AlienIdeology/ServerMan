/*
 * 
 * ServerMan, a Discord bot made by AlienIdeology
 * 
 * 
 * 2017 (c) ServerMan
 */
package listener;

import constants.Emoji;
import constants.Global;
import main.ServerMan;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.*;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.user.UserOnlineStatusUpdateEvent;
import secret.PrivateConstant;

import java.awt.*;
import java.time.Instant;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utility.UtilBot;
import utility.UtilString;

import javax.rmi.CORBA.Util;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class GuildListener extends ListenerAdapter {

    @Override
    public void onGenericGuild(GenericGuildEvent event) {
        if(UtilBot.isValidGuild(event.getGuild())) {
            ServerMan.scanner.postGuildStatus();
        }
    }

    @Override
    public void onUserOnlineStatusUpdate(UserOnlineStatusUpdateEvent event) {
        if(UtilBot.isValidGuild(event.getGuild())) {
            ServerMan.scanner.postGuildStatus();
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(UtilBot.isValidGuild(event.getGuild()))
        {
            User user = event.getAuthor();
            if(user.isBot() && (!user.getId().equals(PrivateConstant.BOT_ID) && !user.getId().equals(PrivateConstant.AIBOT_ID)))
            {
                if(!event.getChannel().getId().equals(PrivateConstant.SONG) && !event.getChannel().getId().equals(PrivateConstant.VC_LOG))
                {
                    event.getMessage().delete().queue();
                    event.getChannel().sendMessage(Emoji.EYES+" I'm sorry, bots can only talk in "+
                            ServerMan.getGuild().getTextChannelById(PrivateConstant.SONG).getAsMention()+" or "
                            +ServerMan.getGuild().getTextChannelById(PrivateConstant.VC_LOG).getAsMention()+".").queue();
                }
            }
        }
    }

    /**
     * Guild listener
     */
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if(!event.getGuild().getId().equals(PrivateConstant.ID))
            event.getGuild().leave().queue();
    }

    @Override
    public void onGuildBan(GuildBanEvent event) {
        User banne = event.getUser();
        EmbedBuilder notif = new EmbedBuilder().setColor(Color.RED)
                .setAuthor(banne.getName()+" Banned", Global.B_SERVER, banne.getEffectiveAvatarUrl())
                .appendDescription("Thor has use his banner to ban "+banne.getAsMention()+"... RIP")
                .setFooter("ID: "+banne.getId(), null).setTimestamp(Instant.now());
        ServerMan.getGuild().getTextChannelById(PrivateConstant.LOG).sendMessage(notif.build()).queue();
    }

    @Override
    public void onGuildUnban(GuildUnbanEvent event) {
        User salvation = event.getUser();
        EmbedBuilder notif = new EmbedBuilder().setColor(Color.CYAN)
                .setAuthor(salvation.getName()+" UnBanned", Global.B_SERVER, salvation.getEffectiveAvatarUrl())
                .appendDescription(salvation.getAsMention()+" has earned his/her salvation!")
                .setFooter("ID: "+salvation.getId(), null).setTimestamp(Instant.now());
        ServerMan.getGuild().getTextChannelById(PrivateConstant.LOG).sendMessage(notif.build()).queue();
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member newcommer = event.getMember();
        ServerMan.scanner.addAutoRole(newcommer);

        newcommer.getUser().openPrivateChannel().queue((PrivateChannel pm) -> {
            pm.sendMessage(Emoji.INVITE + "Welcome to Music Hub!\n"
                            + "Have a great time here by enjoying musics! You're welcome to chat and share songs you love.\n"
                            + "Btw, read #introduction channel for more information. If you got any questions, pin @Ayylien:tm:.\n"
                            + "Also, if you are a music producer or programmer, pm @Ayylien:tm:, maybe he'll got you a cool role! "+Emoji.FACE_BLUSH).queue();
        });

        EmbedBuilder notif = new EmbedBuilder().setColor(UtilBot.randomColor())
            .setAuthor(newcommer.getEffectiveName()+" Joined", Global.B_SERVER, newcommer.getUser().getEffectiveAvatarUrl())
                .appendDescription("Hi "+newcommer.getAsMention()+"^")
                .setFooter("ID: "+newcommer.getUser().getId(), null).setTimestamp(Instant.now());
        ServerMan.getGuild().getTextChannelById(PrivateConstant.LOG).sendMessage(notif.build()).queue();
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        Member leaver = event.getMember();
        EmbedBuilder notif = new EmbedBuilder().setColor(UtilBot.randomColor())
                .setAuthor(leaver.getEffectiveName()+" Left", Global.B_SERVER, leaver.getUser().getEffectiveAvatarUrl())
                .appendDescription("Bye bye "+leaver.getAsMention())
                .setFooter("ID: "+leaver.getUser().getId(), null).setTimestamp(Instant.now());
        ServerMan.getGuild().getTextChannelById(PrivateConstant.LOG).sendMessage(notif.build()).queue();
    }

    /**
     * Voice Channel listener
     */
    
    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
        //Log bot playing music
        if(e.getMember().getUser().isBot() && !e.getMember().getVoiceState().isGuildMuted()) {
            VoiceChannel vc = e.getChannelJoined();
            ServerMan.getGuild().getTextChannelById(PrivateConstant.VC_LOG).sendMessage(
                    Emoji.NOTES + " **" + e.getMember().getEffectiveName() + "** is now playing music in `"
                            + vc.getName() + "` for `" + vc.getMembers().size() + "` people.").queue();
            return;
        }

        //Log event
        ServerMan.getGuild().getTextChannelById(PrivateConstant.VC_LOG).sendMessage(
                "`"+UtilString.formatDurationToString(System.currentTimeMillis())+"` **"
                    +e.getMember().getEffectiveName()+"** connected to `"+e.getChannelJoined().getName()+"`").queue();
    }
    
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
        if(e.getMember().getUser().isBot()) return;
        ServerMan.getGuild().getTextChannelById(PrivateConstant.VC_LOG).sendMessage(
                "`"+UtilString.formatDurationToString(System.currentTimeMillis())+"` **"
                        +e.getMember().getEffectiveName()+"** disconnected from `"+e.getChannelLeft().getName()+"`").queue();
    }

    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
        if(e.getMember().getUser().isBot()) return;
        ServerMan.getGuild().getTextChannelById(PrivateConstant.VC_LOG).sendMessage(
                "`"+UtilString.formatDurationToString(System.currentTimeMillis())+"` **"
                        +e.getMember().getEffectiveName()+"** moved from ~~`"+e.getChannelLeft().getName()
                        +"`~~ to `"+e.getChannelJoined().getName()+"`").queue();
    }
    
}
