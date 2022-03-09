/*
 * This library is distributed under a modified BSD license.  See the included
 * LICENSE file for details.
 */
package org.fife.ui.rtextarea;

import javax.swing.*;
import java.awt.*;

/**
 * A dummy icon implementation for test purposes.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class EmptyTestIcon implements Icon {

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
    }

    @Override
    public int getIconWidth() {
        return 0;
    }

    @Override
    public int getIconHeight() {
        return 0;
    }

}
