package project.web.service.home;

import org.springframework.stereotype.Service;
import project.web.paging.PaginationInfo;
import project.web.repository.home.HomeRepository;
import project.web.repository.home.Notice;
import project.web.repository.home.Qna;

import java.util.Collections;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {

    private final HomeRepository homeRepository;

    public HomeServiceImpl(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

    /*
     공지사항 리스트
     */
    @Override
    public List<Notice> noticeAll(Notice params) {

        List<Notice> notices = Collections.emptyList();

        String table = "notice";

        int TotalCount = homeRepository.selectBoardTotalCount(table);
        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(TotalCount);

        params.setPaginationInfo(paginationInfo);

        if (TotalCount > 0) {

            notices = homeRepository.findByNoticeAll(params);
        }

        return notices;
    }

    /*
     공지사항 디테일
     */
    @Override
    public Notice noticeDetail(int num) {
        Notice notice = homeRepository.findByNotice(num);
        return notice;
    }

    /*
     조회수 증가
     */
    @Override
    public void inquiryAdd(String name, int num) {
        homeRepository.updateInquiry(name, num);
    }

    /*
     문의게시판 리스트
     */
    @Override
    public List<Qna> qnaAll(Qna params) {

        List<Qna> qnaList = Collections.emptyList();

        String table = "qna";

        int TotalCount = homeRepository.selectBoardTotalCount(table);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(TotalCount);

        params.setPaginationInfo(paginationInfo);

        if (TotalCount > 0) {

            qnaList = homeRepository.findByQnaAll(params);
        }
        return qnaList;
    }

    /*
     문의게시판 검색 결과리스트
     */
    @Override
    public List<Qna> qnaSearch(Qna params, String searchType, String searchKeyword) {

        List<Qna> qnaList = Collections.emptyList();

        String table = "qna";

        int TotalCount = homeRepository.selectBoardSearchTotalCount(table, searchType, searchKeyword);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(TotalCount);

        params.setPaginationInfo(paginationInfo);

        if (TotalCount > 0) {
            qnaList = homeRepository.findByQna(params, searchType, searchKeyword);
        }
        return qnaList;
    }

    /*
     문의게시판 디테일
     */
    @Override
    public Qna qnaDetail(int num) {
        Qna qna = homeRepository.findByQna(num);
        return qna;
    }

    /*
     문의게시판 글 작성
     */
    @Override
    public Qna qnaAdd(Qna qna) {
        Qna newQna = homeRepository.saveQna(qna);
        return newQna;
    }

    /*
     문의게시판 글 삭제
     */
    @Override
    public void qnaDelete(int num) {

        homeRepository.deleteQna(num);

    }


}
