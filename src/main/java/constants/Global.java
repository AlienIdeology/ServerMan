/* 
 * ServerMan by AlienIdeology
 * 
 * Constants
 * All informations and links about the bot and developer
 */
package constants;

import main.ServerMan;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import net.dv8tion.jda.core.Permission;
import secret.PrivateConstant;

/**
 *
 * @author liaoyilin
 */
public class Global {
    //main
    public final static String VERSION = "[0.0.1]";
    
    //Icon
    public final static String I_INFO = "https://maxcdn.icons8.com/Share/icon/Very_Basic//info1600.png";
    public final static String I_HELP = "https://maxcdn.icons8.com/Share/icon/Programming//help1600.png";
    
    //Bot
    //-Bot Global
    public final static String B_NAME = ServerMan.jda.getSelfUser().getName();
    public final static String B_AVATAR = ServerMan.jda.getSelfUser().getEffectiveAvatarUrl();
    public final static String B_DISCRIMINATOR = ServerMan.jda.getSelfUser().getDiscriminator();
    public final static String B_ID = ServerMan.jda.getSelfUser().getId();
    public final static String B_GAME_DEFAULT = "Music (Not really)";
    
    //-Bot Links
    public final static String B_INVITE = "https://discordapp.com/oauth2/authorize?client_id="+ PrivateConstant.BOT_ID+"&scope=bot&permissions=368573567";
    public final static String B_SERVER = "https://discord.gg/UMCqtZN";
    public final static String B_GITHUB = "https://github.com/AlienIdeology/ServerMan/";

    //-Bot Server
    public final static String B_SERVER_ID = "293928413029466114";
    public final static String B_SERVER_ERROR = "294487318797090816";
    public final static String B_SERVER_STATUS = "310453321972449290";
    public final static String B_SERVER_STATUS_MSG = "310458751645908992";
    
    //Link
    public final static String LYRICSURL = "https://genius.com/";
    
    //Permissions
    public final static Collection<Permission> PERM_MOD = new ArrayList<Permission> 
            (Arrays.asList(new Permission[] {Permission.ADMINISTRATOR, Permission.MANAGE_SERVER}));
    
    //Bot Developer ID
    public final static String D_ID = "248214880379863041";    
    
}
