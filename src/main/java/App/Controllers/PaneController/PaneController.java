package App.Controllers.PaneController;

import App.Controllers.FXCustomController;
import LogicControll.FXControllMediator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class PaneController extends FXCustomController {
    Pane parent;

    public PaneController(Pane parent, String fxmlFile) {
        this.parent = parent;
        loader = new FXMLLoader(getClass().getResource(fxmlFile));
        loader.setController(this);
    }
    @Override
    public void load() {
        try {
            Pane pane = loader.load();
            parent.getChildren().clear();
            parent.getChildren().add(pane);
            ((FXControllMediator)mediator).setPaneVisible(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setControllerToMediator() {

    }
}
