import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private ApplicationContext context;

    private UserDao dao;
    private User user1,user2,user3;

    @Before
    public void setUp(){
        this.dao = this.context.getBean("userDao",UserDao.class);
        System.out.println(this.context);
        System.out.println(this);
    }
    @Before
    public void makeMockUsers(){
        user1 = new User("name1","id1", "pw1");
        user2 = new User("name2","id2", "pw2");
        user3 = new User("name3","id3", "pw3");
    }
    @Test
    public void addAndGet() throws SQLException,ClassNotFoundException {

        dao.deleteAll();
        assertThat(dao.getCount(),is(0));

        dao.add(user1);
        assertThat(dao.getCount(),is(1));
    }
    @Test(expected = EmptyResultDataAccessException.class)
    public void emptyUserGet() throws SQLException, ClassNotFoundException{
        dao.deleteAll();
        assertThat(dao.get("woet4aok4t"),is(0));
    }
    @Test(expected = EmptyResultDataAccessException.class)
    public void nullUserGet() throws SQLException, ClassNotFoundException{
        User user = new User("VeryStrangeName","VeryStrangeId","VeryStrangePassword");
        assertThat(dao.get(user.getId()),is(0));
    }
}
