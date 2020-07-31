package App;

import App.Controllers.MainController;
import Database.Hibernate.HibernateUtils;
import LogicControll.FXControllMediator;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    //static List<Conference> conferenceList;
    static FXControllMediator mediator;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("iConference");
        MainController mainController = new MainController();
        loader.setController(mainController);
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                HibernateUtils.getSessionFactory().close();
            }
        });
    }


    public static void main(String[] args) {
        mediator = new FXControllMediator();
        launch(args);
    }
}
