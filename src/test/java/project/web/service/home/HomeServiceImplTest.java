package project.web.service.home;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.web.repository.home.HomeRepository;
import project.web.repository.home.Qna;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class HomeServiceImplTest {

    @Autowired
    HomeService homeService;
    @Autowired
    HomeRepository homeRepository;

    @Test
    void 문의글_작성(){

        //given
        Qna qna = new Qna();
        qna.setTitle("문의글1");
        qna.setContent("문의합니다");
        qna.setPw("1234");
        qna.setWriter("회원");

        Qna newQna = homeService.qnaAdd(qna);

        //when
        Qna qna2 = homeRepository.findByQna(newQna.getNum());

        //then
        Assertions.assertThat(qna2.getNum()).isEqualTo(newQna.getNum());
    }



}