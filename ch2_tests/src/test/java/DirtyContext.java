import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;

/*
실제로 돌아가는 코드는 아닙니다.
 */
@DirtiesContext
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class DirtyContext {
    @Autowired
    UserDao dao;

    @Before
    public void setUp(){
        DataSource dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost/testdb","id","pw",true);
        dao.setDataSource(dataSource);
    }
}
