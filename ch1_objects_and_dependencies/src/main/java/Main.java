public class Main {
    public static void main(String[] args){
        UserDao dao = new DaoFactory().userDao();

        User user = new User();
        user.setId("jadenjack");
        user.setName("장용준");
        user.setPassword("pw");

        try {
            dao.add(user);
            System.out.println("등록 성공");

            User user2 = dao.get(user.getId());
            System.out.println(user2.getName());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
