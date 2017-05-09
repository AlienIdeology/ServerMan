package setting;

import constants.Emoji;
import main.ServerMan;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.GuildController;
import secret.PrivateConstant;
import utility.UtilBot;

import java.time.Instant;

/**
 * Created by liaoyilin on 5/8/17.
 */
public class GuildScanner {

    Guild guild;
    Role autoRole;

    public GuildScanner(Guild guild)
    {
        this.guild = guild;
        autoRole = guild.getRoleById(PrivateConstant.AUTOROLE);
    }

    /**
     * Automatically scan through members and add the auto role
     * @return
     */
    public GuildScanner scanAutoRole()
    {
        GuildController controller = guild.getController();
        for(Member mem : guild.getMembers()) {
            if(!mem.getRoles().contains(autoRole) && !mem.getUser().isBot() && !mem.getUser().isFake()) {
                controller.addRolesToMember(mem, autoRole).queue();
                System.out.println("Auto role added to " + mem.getEffectiveName());
            }
        }
        return this;
    }

    /**
     * Add the auto role to a member
     * @param mem the member to be added
     * @return
     */
    public GuildScanner addAutoRole(Member mem) {
        mem.getGuild().getController().addRolesToMember(mem, autoRole);
        return this;
    }

    /**
     * Automatically post the status to the information channel
     * @return
     */
    public GuildScanner postGuildStatus() {
        Guild server = ServerMan.getGuild();
        int online = 0, idle = 0, dnd = 0, offline = 0;
        int bot_playing = 0, human_listening = 0;

        for(Member mem : server.getMembers()) {
            switch (mem.getOnlineStatus()) {
                case ONLINE:
                    online++;
                    break;
                case IDLE:
                    idle++;
                    break;
                case DO_NOT_DISTURB:
                    dnd++;
                    break;
                case OFFLINE:
                case UNKNOWN:
                    offline++;
                    break;
                default:
                    break;
            }

            GuildVoiceState vs = mem.getVoiceState();

            if(mem.getUser().isBot() && vs.inVoiceChannel() && !vs.isMuted()) {
                bot_playing++;
            } else if(!mem.getUser().isFake() && vs.inVoiceChannel() && !vs.isDeafened()) {
                human_listening++;
            }
        }

        EmbedBuilder status = new EmbedBuilder()
            .setAuthor("Server Live Status", null, server.getIconUrl())
            .setThumbnail(server.getIconUrl()).setColor(UtilBot.randomColor()).setTimestamp(Instant.now())
            .setFooter("Music Hub Server Manager", server.getSelfMember().getUser().getEffectiveAvatarUrl())
            .addField(Emoji.ABC+" Name", server.getName(), true)
            .addField(Emoji.SPY+" Owner", server.getOwner().getEffectiveName(), true)
            .addField("Region", server.getRegion().getName(), true)
            .addField("Members", server.getMembers().size()+"", true)
            .addField("Status", Emoji.GUILD_ONLINE+" `"+online+"` online\n"+Emoji.GUILD_IDLE+" `"+idle
                +"` idle\n"+Emoji.GUILD_DND+" `"+dnd+"` dnd\n"+Emoji.GUILD_OFFLINE+" `"+offline+"` offline", true)
            .addField("Voice Channel", Emoji.NOTES+" `"+bot_playing+"` playing\n"
                +Emoji.GLOBE+" `"+human_listening+"` listening", true);

        server.getTextChannelById(PrivateConstant.INFO).editMessageById(PrivateConstant.STATUS_MSG, status.build()).queue();
        return this;
    }

}
