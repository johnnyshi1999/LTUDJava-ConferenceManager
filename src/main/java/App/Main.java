package App;

import App.Controllers.HomeController;
import Entities.Conference;
import Entities.User;
import Hibernate.HibernateUtils;
import LogicControll.FXControllMediator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class Main extends Application {
    //static List<Conference> conferenceList;
    static FXControllMediator mediator;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("iConference");
        HomeController homeController = new HomeController();
        loader.setController(homeController);
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }


    public static void main(String[] args) {
        mediator = new FXControllMediator();
        launch(args);
    }
}
