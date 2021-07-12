package project.web.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.web.repository.members.Member;
import project.web.repository.members.MemberRepository;
import project.web.service.members.MemberService;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{

        //given

        Member member = new Member();
        member.setId("sss");
        member.setPw("123");
        member.setName("서태중");
        member.setAddr("서울시 중랑구");
        member.setPhone("010-111-234");
        member.setBirth("1991-04-12");

        //when

        Member newMember = memberService.join(member);

        //then

        Member member2 = memberRepository.findById(newMember.getName(), newMember.getPhone());
        assertEquals(member.getId(), member2.getId());

    }

    @Test
    void 아이디_찾기(){

        //given
        Member member = new Member();
        member.setId("sss");
        member.setPw("123");
        member.setName("서태중");
        member.setAddr("서울시 중랑구");
        member.setPhone("010-111-234");
        member.setBirth("1991-04-12");

        //when
        Member newMember = memberService.join(member);
        Member findId = memberService.searchId(newMember);

        //then
        assertEquals(newMember.getId(), findId.getId());
    }

    @Test
    void 아이디_중복체크(){

        //given
        Member member1 = new Member();
        member1.setId("sss");

        Member member2 = new Member();
        member2.setId("sss");

        //when
        memberService.join(member1);
        int result = memberService.validateDuplicateMember(member2);

        //then
        // 중복 : 1 가능 : 0
        assertThat(result).isEqualTo(1);
    }

    @Test
    void 로그인(){

        //given
        Member member1 = new Member();
        member1.setId("sss");
        member1.setPw("123");

        Member newMember = memberService.join(member1);

        //when
        Member loginMember = memberService.login(newMember);

        //then
        assertThat(loginMember.getId()).isEqualTo(newMember.getId());
    }
    @Test
    void 비밀번호찾기(){
        //given
        Member member = new Member();
        member.setId("sss");
        member.setPw("111");
        member.setName("서태중");
        Member newMember = memberService.join(member);

        //when
        Member searchPw = memberService.searchPw(newMember);

        //then
        assertThat(searchPw.getPw()).isEqualTo(newMember.getPw());
    }
    @Test
    void 내정보보기(){
        //given
        Member member = new Member();
        member.setId("sss");
        member.setPw("123");
        member.setName("서태중");
        member.setAddr("서울시 중랑구");
        member.setPhone("010-111-234");
        member.setBirth("1991-04-12");
        Member newMember = memberService.join(member);

        //when
        Member memberInfo = memberService.memberInfo(newMember.getId());

        //then
        assertThat(memberInfo.getId()).isEqualTo(newMember.getId());
        assertThat(memberInfo.getPw()).isEqualTo(newMember.getPw());
        assertThat(memberInfo.getName()).isEqualTo(newMember.getName());
        assertThat(memberInfo.getAddr()).isEqualTo(newMember.getAddr());
        assertThat(memberInfo.getPhone()).isEqualTo(newMember.getPhone());
        assertThat(memberInfo.getBirth()).isEqualTo(newMember.getBirth());
    }

    @Test
    void 내정보수정(){
        //given
        Member member = new Member();
        member.setId("sss");
        member.setPw("123");
        member.setName("서태중");
        member.setAddr("서울시 중랑구");
        member.setPhone("010-111-234");
        member.setBirth("1991-04-12");
        Member newMember = memberService.join(member);

        //when
        Member memberEdit = new Member();
        memberEdit.setPw("222");
        memberEdit.setName("중태서");
        memberEdit.setAddr("서울");
        memberEdit.setPhone("010222");
        memberEdit.setBirth("19910515");

        memberService.memberInfoUpdate(newMember.getId(), memberEdit);

        Member memberInfo = memberService.memberInfo(newMember.getId());

        //then
        assertThat(memberInfo.getName()).isEqualTo("중태서");
    }

    @Test
    void 회원탈퇴(){
        //given
        Member member = new Member();
        member.setId("sss");
        member.setPw("123");
        Member newMember = memberService.join(member);

        //when
        memberService.memberDelete(newMember.getId());
        Member memberInfo = memberService.memberInfo(newMember.getId());

        //then


    }




}