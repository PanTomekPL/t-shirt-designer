package pl.net.kabala.confitura2015;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;

@Title("T-shirt Designer")
@Theme("valo")
@SpringUI
public class DesignerUI extends UI {

    EditForm editForm;
    ImageSource ImageSource;
    StreamResource resource;
    Image image;

    @Override
    protected void init(VaadinRequest request) {
        configureComponents();
        buildLayout();
    }


    private void configureComponents() {
        ImageSource = new ImageSource();
        resource = new StreamResource(ImageSource, Utils.makeImageFilename());
        editForm = new EditForm(resource);
    }

    private void buildLayout() {
        image = new Image(null, resource);
        HorizontalLayout mainLayout = new HorizontalLayout(editForm, image);
        setContent(mainLayout);
    }

    @WebServlet(urlPatterns = "/*")
    @VaadinServletConfiguration(ui = DesignerUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }


}
