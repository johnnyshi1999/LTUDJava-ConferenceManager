package LogicControll;

import App.Controllers.*;
import App.Controllers.Dialogs.ConferenceDetailController;
import App.Controllers.Dialogs.LoginController;
import App.Controllers.Dialogs.RegisterController;
import App.Controllers.PaneController.*;
import Database.Hibernate.Entities.User;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class FXControllMediator implements Mediator{
    static private FXControllMediator mediator =null;
    AnchorPane homeParentPane;
    MainController mainController;
    ConferenceDetailController conferenceDetailController;
    LoginController loginController;
    RegisterController registerController;
    AttendListController attendListController;
    ProfileController profileController;
    ConferenceManageController conferenceManageController;
    UserManageController userManageController;

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

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
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

    public void setUserManageController(UserManageController userManageController) {
        this.userManageController = userManageController;
    }

    @Override
    public void notify(FXCustomController controller, String message) {
        if (controller == loginController) {
            if (message.equals("MainController logged in")) {
                LoginController newController = (LoginController) controller;
                User user = newController.getLoginUser();
                LogicController.getController().setCurrentUser(user);
                mainController.setLoggedInUser();
                if (conferenceDetailController != null) {
                    conferenceDetailController.refresh();
                }
            }
        }

        if (controller == registerController) {
            if (message.equals("MainController regiested")) {
                User user = ((RegisterController)controller).getRegisteredUser();
                LogicController.getController().setCurrentUser(user);
                mainController.setLoggedInUser();
                if (conferenceDetailController != null) {
                    conferenceDetailController.refresh();
                }

            }
        }

        if (controller == conferenceDetailController) {
            if (message.equals("Update Conference List")) {
//                mainController.updateConferenceList();
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

        if (controller == mainController) {
            mainController.updateConferenceList();
            ((MainController) mainController).homePane.setVisible(true);
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
        if (controller == userManageController) {
            ((UserManageController)controller).parent.setVisible(true);
        }
    }

    public void setParentPane(AnchorPane homeParentPane) {
        this.homeParentPane = homeParentPane;
    }


}
