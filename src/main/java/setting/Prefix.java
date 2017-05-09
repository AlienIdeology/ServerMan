/* 
 * ServerMan by AlienIdeology
 * 
 * Prefix
 * Accessing prefix
 */
package setting;

import main.*;


/**
 *
 * @author liaoyilin
 */
public class Prefix {
    public static String DIF_PREFIX = "music ";

    public synchronized static String getDefaultPrefix()
    {
        return DIF_PREFIX;
    }
}
