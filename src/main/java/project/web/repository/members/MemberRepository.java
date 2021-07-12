package project.web.repository.members;

public interface MemberRepository {

    Member save(Member member);
    Member checkId(String id);
    Member loginCheck(String id, String pw);
    Member findById(String name, String phone);
    Member findByPw(String id, String name);
    Member findByMember(String id);
    void update(String id, Member infoEdit);
    void delete(String id);

   // Optional<Member> findByName(String name);
   // List<Member> findAll();
}
