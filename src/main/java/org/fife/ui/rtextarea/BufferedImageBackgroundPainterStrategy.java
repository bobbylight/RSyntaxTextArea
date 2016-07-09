/*
 * 01/22/2005
 *
 * BufferedImageBackgroundPainterStrategy.java - Renders an RTextAreaBase's
 * background as an image using a BufferedImage.
 *
 * This library is distributed under a modified BSD license.  See the included
 * RSyntaxTextArea.License.txt file for details.
 */
package org.fife.ui.rtextarea;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;


/**
 * A strategy for painting the background of an <code>RTextAreaBase</code>
 * as an image.  The image is always stretched to completely fill the
 * <code>RTextAreaBase</code>.<p>
 *
 * A <code>java.awt.image.BufferedImage</code> is used for rendering;
 * theoretically, for performance you should use
 * <code>java.awt.image.VolatileImage</code>; see
 * <code>org.fife.ui.RTextArea.VolatileImageBackgroundPainterStrategy</code>
 * for this.<p>
 *
 * You can set the scaling hint used when stretching/skewing the image
 * to fit in the <code>RTextAreaBase</code>'s background via the
 * <code>setScalingHint</code> method, but keep in mind the more
 * accurate the scaling hint, the less responsive your application will
 * be when stretching the window (as that's the only time the image's
 * size is recalculated).
 *
 * @author Robert Futrell
 * @version 0.1
 * @see org.fife.ui.rtextarea.ImageBackgroundPainterStrategy
 * @see org.fife.ui.rtextarea.VolatileImageBackgroundPainterStrategy
 */
public class BufferedImageBackgroundPainterStrategy
					extends ImageBackgroundPainterStrategy {

	private BufferedImage bgImage;


	/**
	 * Constructor.
	 *
	 * @param ta The text area whose background we'll be painting.
	 */
	public BufferedImageBackgroundPainterStrategy(RTextAreaBase ta) {
		super(ta);
	}


	/**
	 * Paints the image at the specified location.  This method assumes
	 * scaling has already been done, and simply paints the background
	 * image "as-is."
	 *
	 * @param g The graphics context.
	 * @param x The x-coordinate at which to paint.
	 * @param y The y-coordinate at which to paint.
	 */
	@Override
	protected void paintImage(Graphics g, int x, int y) {
		if (bgImage != null) {
			g.drawImage(bgImage, x,y, null);
		}
	}


	/**
	 * Rescales the displayed image to be the specified size.
	 *
	 * @param width The new width of the image.
	 * @param height The new height of the image.
	 * @param hint The scaling hint to use.
	 */
	@Override
	protected void rescaleImage(int width, int height, int hint) {

		Image master = getMasterImage();
		if (master!=null) {

			Map<RenderingHints.Key, Object> hints =
					new HashMap<RenderingHints.Key, Object>();
			switch (hint) {
				default:
				case Image.SCALE_AREA_AVERAGING:
				case Image.SCALE_SMOOTH:
					hints.put(RenderingHints.KEY_INTERPOLATION,
							RenderingHints.VALUE_INTERPOLATION_BICUBIC);
					hints.put(RenderingHints.KEY_RENDERING,
							RenderingHints.VALUE_RENDER_QUALITY);
					hints.put(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
			}

			bgImage = createAcceleratedImage(width, height);
			Graphics2D g = bgImage.createGraphics();
			g.addRenderingHints(hints);
			g.drawImage(master, 0,0, width,height, null);
			g.dispose();

		}
		else {
			bgImage = null;
		}
	}


	private BufferedImage createAcceleratedImage(int width, int height) {
		GraphicsConfiguration gc= getRTextAreaBase().getGraphicsConfiguration();
		BufferedImage image = gc.createCompatibleImage(width, height);
		return image;
	}


}