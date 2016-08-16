/* 
 * Project   : Archives
 * Package   : server
 * File Name : ServerListener.java
 * Created By: Doug
 *    AKA    : Dougie Fresh
 * 
 * Created on   : Nov 18, 2007 5:36:17 PM
 * Last updated : ??/??/??
 */

package fresh;

import java.awt.*;

public interface ServerListener
{
	public boolean processClientCommand(ServerProcess sp, String s);

	public void removedClient(ServerProcess sp);

	public void SystemQuit();
}
