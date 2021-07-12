package project.web.repository.home;

import java.util.List;

public interface HomeRepository {

    List<Notice> findByNoticeAll(Notice params);
    Notice findByNotice(int num);
    void updateInquiry(String name, int num);

    List<Qna> findByQnaAll(Qna params);

    int selectBoardTotalCount(String table);
    int selectBoardSearchTotalCount(String table, String searchType, String Keyword);

    List<Qna> findByQna(Qna params, String searchType, String Keyword);
    Qna findByQna(int num);
    Qna saveQna(Qna qna);
    void deleteQna(int num);





}
