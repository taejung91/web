package project.web.service.members;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.web.repository.members.Member;
import project.web.repository.members.MemberRepository;
import project.web.repository.product.Reservation;
import project.web.repository.product.ReservationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    /* public MemberServiceImpl(MemberRepository memberRepository) {
         this.memberRepository = memberRepository;
     }*/
    /*
      회원 가입
     */
    public Member join(Member member) {

        validateDuplicateMember(member);
        memberRepository.save(member);

        return member;
    }

    /*
     아이디 중복 체크
     */
    public int validateDuplicateMember(Member member) {

        Member checkId = memberRepository.checkId(member.getId());

        if (checkId != null) {

            return 1;
        }
        return 0;
    }

    /*
     로그인
     */
    public Member login(Member member) {
        Member loginMember = memberRepository.loginCheck(member.getId(), member.getPw());

        return loginMember;
    }

    /*
     아이디 찾기
   */
    public Member searchId(Member member) {
        Member searchMember = memberRepository.findById(member.getName(), member.getPhone());

        return searchMember;
    }

    /*
     비밀번호 찾기
     */
    public Member searchPw(Member member) {
        Member searchMember = memberRepository.findByPw(member.getId(), member.getName());

        return searchMember;
    }

    /*
     내 정보 보기
     */
    @Override
    public Member memberInfo(String id) {
        Member memberInfo = memberRepository.findByMember(id);
        return memberInfo;
    }

    /*
     내 정보 수정
     */
    @Override
    public void memberInfoUpdate(String id, Member member) {
        memberRepository.update(id, member);

    }

    /*
     회원탈퇴
     */
    @Override
    public void memberDelete(String id) {
        memberRepository.delete(id);

    }

    /*
    예약 상황
    */
    @Override
    public List<Reservation> reservationInfo(String id) {

        List<Reservation> reservations = reservationRepository.selectReservationAll(id);

        return reservations;
    }

    /*
     예약상황 상세정보
     */
    @Override
    public Reservation reservationDetail(int num) {

        Reservation reservation = reservationRepository.selectReservation(num);

        return reservation;
    }


}
