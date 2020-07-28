package LogicControll;

import App.Controllers.*;
import App.Controllers.Dialogs.LoginController;
import Entities.User;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.List;

public class FXControllMediator implements Mediator{
    static private FXControllMediator mediator =null;
    AnchorPane homeParentPane;
    HomeController homeController;
    ConferenceDetailController conferenceDetailController;
    LoginController loginController;
    RegisterController registerController;
    AttendListController attendListController;
    ProfileController profileController;
    ConferenceManageController conferenceManageController;

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

    public void setAttendListController(AttendListController attendListController) {
        this.attendListController = attendListController;
    }

    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }

    public void setConferenceManageController(ConferenceManageController conferenceManageController) {
        this.conferenceManageController = conferenceManageController;
    }

    @Override
    public void notify(FXCustomController controller, String message) {
        if (controller == loginController) {
            if (message.equals("HomeController logged in")) {
                LoginController newController = (LoginController) controller;
                User user = newController.getLoginUser();
                LogicController.getController().setCurrentUser(user);
                homeController.setLoggedInUser();
                if (conferenceDetailController != null) {
                    conferenceDetailController.refresh();
                }
            }
        }

        if (controller == registerController) {
            if (message.equals("HomeController regiested")) {
                User user = ((RegisterController)controller).getRegisteredUser();
                LogicController.getController().setCurrentUser(user);
                homeController.setLoggedInUser();
                if (conferenceDetailController != null) {
                    conferenceDetailController.refresh();
                }

            }
        }

        if (controller == conferenceDetailController) {
            if (message.equals("Update Conference List")) {
                homeController.updateConferenceList();
                if (attendListController != null) {
                    attendListController.updateAttendanceList();
                }
            }
        }
    }

    public void setPaneVisible(FXCustomController controller) {
        List<Node> panes = homeParentPane.getChildren();
        for (int i = 0; i < panes.size(); i++) {
            panes.get(i).setVisible(false);
        }

        if (controller == homeController) {
            ((HomeController)homeController).homePane.setVisible(true);
        }

        if (controller == attendListController) {
            ((AttendListController)controller).parent.setVisible(true);
        }
        if (controller == profileController) {
            ((ProfileController)controller).parent.setVisible(true);
        }
        if (controller == conferenceManageController) {
            ((ConferenceManageController)controller).parent.setVisible(true);
        }
    }

    public void setParentPane(AnchorPane homeParentPane) {
        this.homeParentPane = homeParentPane;
    }


}
