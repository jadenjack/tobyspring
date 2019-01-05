import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = applicationContext.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("jij5379");
        user.setName("장용준");
        user.setPassword("password");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
    }
}
