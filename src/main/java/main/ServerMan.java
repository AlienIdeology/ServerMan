/* 
 * ServerMan by AlienIdeology
 * 
 * Main
 * Startup tasks, add commands, and bot configuration
 */
package main;

import constants.Global;
import secret.PrivateConstant;
import command.*;
import command.information.*;
import command.moderation.*;
import command.restricted.*;
import listener.*;
import setting.*;
import com.mashape.unirest.http.Unirest;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.exceptions.*;
import net.dv8tion.jda.core.entities.Game;

import java.util.HashMap;
import javax.security.auth.login.LoginException;
import java.io.IOException;

/**
 *
 * @author Alien Ideology <alien.ideology at alien.org>
 */
public class ServerMan {

    public static JDA jda;
    public static final CommandParser parser = new CommandParser();
    public static GuildScanner scanner;
    public static HashMap<String, Command> commands = new HashMap<String, Command>();
    public static long timeStart = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(PrivateConstant.BOT_TOKEN)
                    .addEventListener(new BotListener(), new MessageFilter(),
                        new GuildListener(), new CommandListener())
                    .setAutoReconnect(true)
                    .setEnableShutdownHook(false)
                    .setMaxReconnectDelay(300)
                    .buildBlocking();

            jda.getPresence().setGame(Game.of(Global.B_GAME_DEFAULT));

            startUp();
        } catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized static void startUp()
    {
        timeStart = System.currentTimeMillis();
        addCommands();
        scanner = new GuildScanner(getGuild())
            .scanAutoRole().
            postGuildStatus();
    }

    public static void shutdown() throws IOException
    {
        System.out.println("Bot Shut Down Successfully");
        Unirest.shutdown();
        jda.shutdown();
        System.exit(0);
    }

    public static Guild getGuild()
    {
        return jda.getGuildsByName("Music Hub", false).get(0);
    }
        
    private static void addCommands() {
        // Information Commands
        commands.put("invite", new InviteCommand());
        commands.put("list", new ListCommand());
        commands.put("l", new ListCommand());
        commands.put("prefix", new PrefixCommand());
        commands.put("ping", new PingCommand());
        commands.put("status", new StatusCommand("status"));
        commands.put("uptime", new StatusCommand("uptime"));
        commands.put("support", new SupportCommand());
        
        // Moderation Commands
        commands.put("prune", new PruneCommand());
        commands.put("kick", new KickCommand());
        commands.put("warn", new WarnCommand());
        commands.put("ban", new BanCommand());
        commands.put("unban", new UnbanCommand());
        commands.put("softban", new SoftBanCommand());
        
        //Restricted Commands
        commands.put("shutdown", new ShutDownCommand());
        commands.put("setNick", new PresenceCommand("setNick"));
        commands.put("setStatus", new PresenceCommand("setStatus"));
        commands.put("setGame", new PresenceCommand("setGame"));
        commands.put("source", new SourceCommand());
    }
}