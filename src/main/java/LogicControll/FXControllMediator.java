package LogicControll;

import App.Controllers.*;
import Entities.User;

public class FXControllMediator implements Mediator{
    static private FXControllMediator mediator =null;
    HomeController homeController;
    ConferenceDetailController conferenceDetailController;
    LoginController loginController;
    RegisterController registerController;

    public static FXControllMediator getMediator() {
        if (mediator == null) {
            mediator = new FXControllMediator();
        }
        return mediator;
    }

    public void setRegisterController(RegisterController registerController) {
        this.registerController = registerController;
    }

    public void setConferenceDetailController(ConferenceDetailController conferenceDetailController) {
        this.conferenceDetailController = conferenceDetailController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setHomeController(HomeController homeController) {
        this.homeController = homeController;
    }

    @Override
    public void notify(FXCustomController controller, String message) {
        if (controller == loginController) {
            if (message.equals("HomeController logged in")) {
                LoginController newController = (LoginController) controller;
                User user = newController.getLoginUser();
                homeController.setLoggedInUser();
                LogicController.getController().setCurrentUser(user);
            }
        }

        if (controller == registerController) {
            if (message.equals("HomeController regiested")) {
                User user = ((RegisterController)controller).getRegisteredUser();
                homeController.setLoggedInUser();
                LogicController.getController().setCurrentUser(user);
            }
        }

        if (controller == conferenceDetailController) {
            if (message.equals("Update Conference List")) {
                homeController.updateConferenceList();
                homeController.setVisible("homePane");
            }
        }
    }
}
