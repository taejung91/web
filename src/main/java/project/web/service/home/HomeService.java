package project.web.service.home;

import project.web.repository.home.Notice;
import project.web.repository.home.Qna;

import java.util.List;

public interface HomeService {

    List<Notice> noticeAll(Notice params);
    Notice noticeDetail(int num);
    void inquiryAdd(String name, int num);

    List<Qna> qnaAll(Qna params);

    List<Qna> qnaSearch(Qna params, String searchType, String searchKeyword);
    Qna qnaDetail(int num);
    Qna qnaAdd(Qna qna);

    void qnaDelete(int num);
}
