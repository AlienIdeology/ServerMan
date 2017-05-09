/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package command;

import constants.Global;
import java.awt.Color;
import java.time.Instant;

import net.dv8tion.jda.core.EmbedBuilder;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author liaoyilin
 */
public abstract class Command {

    /**
     * command Responses and actions
     * @param args
     * @param e
     */
    public abstract void action(String[] args, MessageReceivedEvent e);
    
}
