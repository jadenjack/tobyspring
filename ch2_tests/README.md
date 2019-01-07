# CH2. Tests
이번 챕터에서는 다음과 같은 과정을 거친다.
## 2.1 UserDaoTest 다시보기 및 문제점 진단
- 테스트의 유용성 : 내가 예상하고 의도했던 대로 코드가 정확히 동작하는지 확인할 수 있다.
- UserDaoTest 코드의 내용
> 1. main() 함수 사용
> 2. 테스트 대상의 오브젝트를 가져와 메소드를 호출
> 3. 사용할 입력 값을 직접 코드에 넣어줌.
> 4. 결과를 콘솔로 출력 후 눈으로 직접 확인

> 문제점
> 1. 수동 확인 작업의 번거로움 : 콘솔을 사람의 눈으로 직접 확인 후 정상 작동을 판단한다.
> 2. 실행 작업의 번거로움 : main()에서 테스트하기 때문에 여러개의 테스트 케이스에 대응하기 힘들다. 

## 2.2 UserDaoTest 개선
- 테스트 검증의 자동화 : if 조건문을 사용하여 결과를 컴퓨터가 판단하게 한다.
- 테스트의 효율적인 수행과 결과 관리 : JUnit을 사용하여 main() 함수로 테스트 하는 것의 한계점을 극복한다.

### 테스트 메소드
- main() 메소드로 만들어졌다는 것은 제어권을 직접 갅는다는 의미이다. 따라서 프레임워크에 적용하기 적합합지 않다.
- 테스트 메소드는 다음과 같은 조건을 따라야 한다.
> 1. @Test
> 2. public

- 검증 코드 전환  : 
if else로 검증을 하던 코드를 JUnit의 형식에 맞추어 assertThat으로 수정한다.

## 2.3 개발자를 위한 테스팅 프레임워크 JUnit
> 테스트 없이는 스프링도 의미가 없다.

- 테스트 결과의 일관성.<br>
> 1. 외부환경에 영향을 받지 않아야 한다.
> 2. 순서를 바꿔도 동일한 결과가 보장되어야 한다.

- 주의사항.
> 1. 예외 상황도 항상 테스트를 한다.<br>
@Test(expected=EmptyResultDataAccessException.class)
> 2. 성공하는 테스트만 골라서 만드는 실수를 하지 말자.<br>
로드 존슨 : "항상 네거티브 테스트를 먼저 만들라" <br> "내 PC에서는 잘 되는데" 라는 변명을 하지 말자.
> 3. 테스트 위주의 개발을 통해 검증된 코드를 만들자.
> 4. 테스트도 지속적인 리팩토링을 통해 직관적이고 효율적인 테스트를 만들자.

## 2.4 스프링 테스트의 적용
- JUnit은테스트 메소드를 실행할 때 마다 새로운 오브젝트를 만든다.<br>
 각 테스트가 서로 영향을 주지 않고 독립적으로 실행되기 위해.
 
- 픽스처(Fixture) : 테스트를 수행하는데 필요한 정보나 오브젝트
User 정보를 여러 테스트를 반복적으로 사용할 경우 다음과 같은 픽스처를 생성하는 것이 유용하다.
<pre><code>
private User user1, user2, user3;
@Before
public void setUp() {
    this.user1 = new User("a", "user1");
    this.user2 = new User("b", "user2");
}
</code></pre>

### 테스트를 위한 어플리케이션 컨텍스트 관리<br>
@Before 메소드가 테스트 메소드 개수만큼 반복되기 때문에 어플리케이션 컨텍스트도 세번 만들어진다.
어플리케이션 컨텍스트가 만들어질 때는 모든 싱글톤 빈 오브젝트를 초기화하므로 상당한 소요가 예상된다.
따라서 어플리케이션 컨텍스트는 테스트 전체가 공유하는 오브젝트를 만들기도 한다.<br><br>

@RunWith는 JUnit 프레임워크의 테스트 실행 방법을 확장할 때 사용하는 어노테이션 이다.
@ContextConfiguration은 자동으로 만들어줄 어플리케이션 컨텍스트의 설정 파일 위치를 지정한 것이다.

<pre><code>
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(location="/applicationContext.xml")
public class UserDaoTest{
    @Autowired
    private Application Context context;
    ...
    
    @Before
    public void setUp(){
        this.dao = this.context.getBean("userDao", UserDao.class");
        ...
    }
}
</code></pre>

정말 컨텍스트를 공유하는지 다음 코드로 확인해보자
<pre><code>
@Before
public void setUp(){
    System.out.println(this.context);
    System.out.println(this);
}
</code></pre>
이와 같은 공유가 가능한 것은<br>
테스트 오브젝트가 만들어질 때마다 특별한 방법을 이용해 어플리케이션 컨텍스트 자신을 테스트 오브젝트의 특정 필드에 주입해주는 것이다.
<br><br>
또한 테스트 클래스 사이에서도 어플리케이션 컨텍스트를 공유하게 해준다.
<pre><code>
Class A : 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(location="/applicationContext.xml")   ┐
                                                            │
Class B :                                                   ├─ /applicationContext.xml 공유
@RunWith(SpringJUnit4ClassRunner.class)                     │
@ContextConfiguration(location="/applicationContext.xml")   ┘

</code></pre>

### 테스트 코드에 의한 DI
테스트 코드에 의한 DI를 이용하여 테스트용 DB에 연결해주는 DataSource를 만들자.
<pre><code>
@DirtiesContext
public class UserDaoTest{
    @Before
    public void setUp(){
        DataSource datasource = new SingleConnectionDataSource("jdbc:mysql://localhost/testdb", ... );
    }
}
</code></pre>

## 2.5 학습 테스트로 배우는 스프링
학습 테스트 : 다른 사람이 만든 API에 대해 학습할 목적으로 만드는 테스트<br>
다음과 같은 장점이 있다.
1. 다양한 조건에 따른 기능을 손쉽게 확인해볼 수 있다.
2. 학습 테스트 코드를 개발 중에 참고할 수 있다.
3. 프레임워크나 제품을 업그레이드할 때 호환성 검증을 도와준다.
4. 테스트 작성에 대한 좋은 훈련이 된다.
5. 새로운 기술을 공부하는 과정이 즐거워진다.

---
# Annotation 정리
- @Test : JUnit에게 테스트용 메소드임을 알려준다.
- @Before : @Test 메소드가 실행되기 전에 먼저 실행되어야 하는 메소드를 정의한다.
- @After : @Test 메소드가 실행된 후에 실행 된다. 모든 테스트 별로 각각 한번씩 실행 된다.
- @RunWith는 JUnit 프레임워크의 테스트 실행 방법을 확장할 때 사용하는 어노테이션 이다.
- @ContextConfiguration은 자동으로 만들어줄 어플리케이션 컨텍스트의 설정 파일 위치를 지정한 것이다.
- @Autowired가 붙은 인스턴스 변수가 있으면 컨텍스트 프레임워크가 일치하는 컨텍스트 내의 빈을 찾고, 일치하는 빈이 있으면 인스턴스 변수를 주입해준다. 일반적으로는 주입을 위해서는 생성자나 수정자 메소드를 이용하지만, 이때 메소드가 없어도 주입이 가능하다.
- @DirtiesContext : 테스트 메소드에서 어플리케이션 컨텍스트의 구성이나 상태를 변경한다는 것을 테스트 컨텍스트 프레임워크에 알려준다.


---
# Q&A
- Q. applicationContext.xml에는 context에 대한 bean 설정이 없는데 @autowired가 가능한가
> 스프링은 어플리케이션 컨텍스트를 초기화 할 때 자기자신도 빈으로 등록한다.