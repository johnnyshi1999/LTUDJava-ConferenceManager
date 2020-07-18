package DAO;

public class DAOUtils {
    private static UserDAO userDAO = null;

    public static UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }
}
