package App.Controllers;

import LogicControll.FXControllMediator;
import LogicControll.Mediator;
import javafx.fxml.FXMLLoader;

public abstract class FXCustomController {
    protected FXMLLoader loader;
    protected Mediator mediator;

    protected FXCustomController() {
        mediator = FXControllMediator.getMediator();
        setControllerToMediator();
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    abstract public void load();

    abstract protected void setControllerToMediator();
}
