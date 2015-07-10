package pl.net.kabala.confitura2015;

import com.vaadin.server.StreamResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Color;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;

public class ImageSource implements StreamResource.StreamSource {

    private static final int TEXT_START_POSITION_X = 227;
    private static final int TEXT_START_POSITION_Y = 190;
    private static final int MAX_VERTICAL_SPACE = 816;
    private static final int BREAKE_WIDTH = 540;
    private static final String IMAGE_FILE ="/static/t-shirt.jpg" ;

    private String text = "";
    private float fontSize = 18;
    private Font font = Utils.getDefaultFont();
    private Color color = Color.black;

    @Bean
    public InputStream getStream() {
        try {
            URL url = ImageSource.class.getResource(IMAGE_FILE);
            File img = new File(url.toURI());
            BufferedImage image = ImageIO.read(img);

            Graphics drawable = image.getGraphics();
            ((Graphics2D) drawable).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Hashtable<TextAttribute, Object> map = new Hashtable<>();
            map.put(TextAttribute.FONT, font.deriveFont(fontSize));
            map.put(TextAttribute.JUSTIFICATION, TextAttribute.JUSTIFICATION_FULL);
            drawable.setColor(color);

            Boolean hasVerticalSpace = true;
            float drawPosY = TEXT_START_POSITION_Y;
            String[] paragraphs = text.split("\n");
            for (String paragraphText : paragraphs) {
                if (StringUtils.isBlank(paragraphText)) {
                    paragraphText = " ";
                }
                AttributedString attributedString = new AttributedString(paragraphText, map);
                AttributedCharacterIterator paragraph = attributedString.getIterator();
                int paragraphStart = paragraph.getBeginIndex();
                int paragraphEnd = paragraph.getEndIndex();
                FontRenderContext frc = ((Graphics2D) drawable).getFontRenderContext();
                LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);
                lineMeasurer.setPosition(paragraphStart);

                while (lineMeasurer.getPosition() < paragraphEnd && hasVerticalSpace) {
                    TextLayout layout = lineMeasurer.nextLayout(BREAKE_WIDTH);
                    float drawPosX = TEXT_START_POSITION_X + (BREAKE_WIDTH - layout.getAdvance()) / 2;
                    drawPosY += layout.getAscent();
                    layout.draw((Graphics2D) drawable, drawPosX, drawPosY);
                    drawPosY += layout.getDescent() + layout.getLeading();
                    if (drawPosY > MAX_VERTICAL_SPACE){
                        hasVerticalSpace = false;
                    }
                }
                if (!hasVerticalSpace){
                    break;
                }
            }

            ByteArrayOutputStream imagebuffer = new ByteArrayOutputStream();
            ImageIO.write(image, "png", imagebuffer);

            return new ByteArrayInputStream(imagebuffer.toByteArray());
        } catch (IOException | URISyntaxException e) {
            return null;
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}