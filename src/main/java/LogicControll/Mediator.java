package LogicControll;

import App.Controllers.FXCustomController;

public interface Mediator {
    public void notify(FXCustomController controller, String message);
}
