package mina;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.util.SessionLog;

public class ImageServerIoHandler extends IoHandlerAdapter {
	   private final static String characters = "mina rocks abcdefghijklmnopqrstuvwxyz0123456789";

	    public static final String INDEX_KEY = ImageServerIoHandler.class.getName() + ".INDEX";

	    public void sessionOpened(IoSession session) throws Exception {
	        session.setAttribute(INDEX_KEY, new Integer(0));
	    }

	    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
	        SessionLog.warn(session, cause.getMessage(), cause);
	    }

	    public void messageReceived(IoSession session, Object message) throws Exception {
	        ImageRequest request = (ImageRequest) message;
	        String text1 = generateString(session, request.getNumberOfCharacters());
	        String text2 = generateString(session, request.getNumberOfCharacters());
	        BufferedImage image1 = createImage(request, text1);
	        BufferedImage image2 = createImage(request, text2);
	        ImageResponse response = new ImageResponse(image1, image2);
	        session.write(response);
	    }

	    private BufferedImage createImage(ImageRequest request, String text) {
	        BufferedImage image = new BufferedImage(request.getWidth(), request.getHeight(), BufferedImage.TYPE_BYTE_INDEXED);
	        Graphics graphics = image.createGraphics();
	        graphics.setColor(Color.YELLOW);
	        graphics.fillRect(0,0,image.getWidth(),image.getHeight());
	        Font serif = new Font("serif", Font.PLAIN, 30);
	        graphics.setFont(serif);
	        graphics.setColor(Color.BLUE);
	        graphics.drawString(text, 10, 50);
	        return image;
	    }

	    private String generateString(IoSession session, int length) {
	        Integer index = (Integer) session.getAttribute(INDEX_KEY);
	        StringBuffer buffer = new StringBuffer(length);
	        while (buffer.length() < length) {
	            buffer.append(characters.charAt(index));
	            index++;
	            if (index >= characters.length()) {
	                index = 0;
	            }
	        }
	        session.setAttribute(INDEX_KEY, index);
	        return buffer.toString();
	    }
}
