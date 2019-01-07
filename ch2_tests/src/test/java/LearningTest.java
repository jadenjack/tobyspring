import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.either;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LearningTest {
    static LearningTest testObject;

    @Test
    public void test1(){
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }

    @Test
    public void test2(){
        assertTrue(testObject == this);
        testObject = this;
    }

    @Test
    public void test3(){
        assertThat(testObject, either(is(nullValue())).or(is(this)));
    }
}
