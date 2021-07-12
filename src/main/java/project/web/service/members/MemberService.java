package project.web.service.members;

import project.web.repository.members.Member;
import project.web.repository.product.Reservation;

import java.util.List;

public interface MemberService {

    Member join(Member member);
    int validateDuplicateMember(Member member);
    Member login(Member member);
    Member searchId(Member member);
    Member searchPw(Member member);
    Member memberInfo(String id);
    void memberInfoUpdate(String id, Member member);

    void memberDelete(String id);

    List<Reservation> reservationInfo(String id);

    Reservation reservationDetail(int num);


}
