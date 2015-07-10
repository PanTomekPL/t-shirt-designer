package pl.net.kabala.confitura2015;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextArea;

import java.awt.*;
import java.awt.Color;

public class EditForm extends FormLayout {
    TextArea shirtText = new TextArea("Tekst");
    NativeSelect fontSize = new NativeSelect("Rozmiar");
    NativeSelect fontFamily = new NativeSelect("Czcionka");
    ColorPicker colorPicker = new ColorPicker();
    Button apply = new Button("Zastosuj", this::apply);
    Button download = new Button("Pobierz");

    private StreamResource resource;

    public EditForm(StreamResource resource) {
        this.resource = resource;
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        fontSize.addItem(18f);
        fontSize.addItem(24f);
        fontSize.addItem(28f);
        fontSize.addItem(36f);
        fontSize.addItem(48f);
        fontSize.addItem(72f);
        fontSize.setNullSelectionAllowed(false);
        fontSize.setValue(36f);
        fontSize.setImmediate(true);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = ge.getAllFonts();
        for (Font font : fonts) {
            fontFamily.addItem(font);
            fontFamily.setItemCaption(font, font.getFontName());
        }
        fontFamily.setNullSelectionAllowed(false);
        fontFamily.setValue(fontFamily.getItemIds().iterator().next());

        colorPicker.setCaption("Kolor");
        colorPicker.setSwatchesVisibility(false);
        colorPicker.setHistoryVisibility(false);
        colorPicker.setTextfieldVisibility(false);
        colorPicker.setHSVVisibility(false);
        colorPicker.setColor(com.vaadin.shared.ui.colorpicker.Color.BLACK);

        FileDownloader fileDownloader = new FileDownloader(resource);
        fileDownloader.extend(download);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);

        HorizontalLayout actions = new HorizontalLayout(apply, download);
        actions.setSpacing(true);

        addComponents(shirtText, fontFamily, fontSize, colorPicker, actions);
    }

    public void apply(Button.ClickEvent event) {
        getUI().ImageSource.setText(shirtText.getValue());
        getUI().ImageSource.setFontSize((Float) fontSize.getValue());
        getUI().ImageSource.setFont((Font) fontFamily.getValue());
        getUI().ImageSource.setColor(new Color(colorPicker.getColor().getRGB()));
        getUI().image.requestRepaint();
        getUI().resource.setFilename(Utils.makeImageFilename());
    }

    @Override
    public DesignerUI getUI() {
        return (DesignerUI) super.getUI();
    }
}