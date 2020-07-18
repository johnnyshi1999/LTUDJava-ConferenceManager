package App;

import App.Controllers.HomeController;
import Entities.Conference;
import Entities.User;
import Hibernate.HibernateUtils;
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
    static List<Conference> conferenceList;

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
        SessionFactory factory = HibernateUtils.getSessionFactory();

        Session session = factory.getCurrentSession();

        try {
            // Tất cả các lệnh hành động với DB thông qua Hibernate
            // đều phải nằm trong 1 giao dịch (Transaction)
            // Bắt đầu giao dịch
            session.getTransaction().begin();

            // Tạo một câu lệnh HQL query object.
            // Tương đương với Native SQL:
            // Select e.* from EMPLOYEE e order by e.EMP_NAME, e.EMP_NO

            String sql = "Select e from " + User.class.getName() + " e";

            // Tạo đối tượng Query.
            Query<User> query = session.createQuery(sql);

            // Thực hiện truy vấn.
            List<User> list = query.getResultList();


            for (User emp : list) {
                System.out.println(emp.getUsername());
            }

            // Commit dữ liệu
            session.getTransaction().commit();
            //factory.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Rollback trong trường hợp có lỗi xẩy ra.
            session.getTransaction().rollback();
        }

        launch(args);
    }
}
