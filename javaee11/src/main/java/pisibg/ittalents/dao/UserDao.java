package pisibg.ittalents.dao;

import org.springframework.stereotype.Component;

@Component
public class UserDao extends DAO {

    private static UserDao userDaoInstance = new UserDao();

    public static UserDao getInstance() {
        return userDaoInstance;
    }


}
