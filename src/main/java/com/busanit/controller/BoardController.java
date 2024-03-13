package com.busanit.controller;

import com.busanit.domain.BoardAttachVO;
import com.busanit.domain.BoardVO;
import com.busanit.domain.PageDTO;
import com.busanit.domain.PagingHandler;
import com.busanit.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/board/*") /* board로 시작하는 것들을 모두 mapping 시킴 */
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 리스트

   /* public void list(Model model) {
        model.addAttribute("list", boardService.getList());
    }*/
   @GetMapping("/list")
    public void list(PagingHandler paging, Model model) {
        model.addAttribute("list", boardService.getList(paging));

        int total = boardService.getTotal(paging); /* getTotal BoardService에 메서드 추가  */

        model.addAttribute("pageMaker", new PageDTO(paging, total));
    }

    // 등록
    @GetMapping("/register")
    public void register() {
    }

    @PostMapping ("/register")
    public String register(BoardVO board, RedirectAttributes rttr) {

       System.out.println("========================");

       if(board.getAttachList() != null) {
           board.getAttachList().forEach(attach -> System.out.println(attach));
       }

        System.out.println("========================");

        boardService.register(board);

        rttr.addFlashAttribute("result", board.getBno()); /* 입력된 pk 값을 RedirectAttributes를 적어서 rttr.addFlashAttribute 통해 bno 값 반환 */

        return "redirect:/board/list";
    }


    // 상세, 수정
    @GetMapping({"/get", "/modify"}) /* 두 개 이상 Mapping 시 {} 중괄호 사용 */
    public void get(Long bno, @ModelAttribute("paging") PagingHandler paging, Model model) {   /* @ModelAttribute("paging") PagingHandler paging 추가 (페이지 2에서 상세보기 후 다시 리스트 돌아올 때 2번 페이지 유지를 위한 추가 부분 -- 3월 7일 */
        model.addAttribute("board", boardService.get(bno)); /* 호출된 값을 board에 담아서 get, modify로 각각 보냄 */
    }


    // 수정 (더 추가할 부분이 있어 PostMapping 사용
    @PostMapping("/modify")
    public String modify(BoardVO board, @ModelAttribute("paging") PagingHandler paging, RedirectAttributes rttr) {  /* @ModelAttribute("paging") PagingHandler paging 추가 (페이지 2에서 수정 후 다시 리스트 돌아올 때 2번 페이지 유지를 위한 추가 부분 -- 3월 7일 */
        if(boardService.modify(board)) {
            rttr.addFlashAttribute("result","success"); /* true 반환 시 result success 문자열 반환 */
        }

        return "redirect:/board/list" + paging.getListLink(); /* 실패 시 그대로 /board/list 반환 되고 검색한 항목 그대로 유지 */
    }


    // 삭제
    @PostMapping("/remove")
    public String remove(Long bno,  @ModelAttribute("paging") PagingHandler paging, RedirectAttributes rttr) {

        // 첨부파일 리스트 조회
        List<BoardAttachVO> attachList = boardService.getAttachList(bno.intValue());

        // 삭제 처리 성공 시
        if(boardService.remove(bno)) {

            // DB에서 삭제 성공 시 실제 파일도 삭제 진행
            deleteFiles(attachList);

            rttr.addFlashAttribute("result", "success"); /* true 반환 시 result success 문자열 반환 */
        }

        return "redirect:/board/list" + paging.getListLink(); /* 실패 시 /board/list 반환 */
    }

    // 첨부파일 리스트 조회
    @GetMapping(value = "/getAttachList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<BoardAttachVO>> getAttachList(int bno) {
        return new ResponseEntity<>(boardService.getAttachList(bno), HttpStatus.OK);
    }


    private void deleteFiles(List<BoardAttachVO> attachList) {
        if(attachList == null || attachList.size() == 0) {
            return;
        }

        attachList.forEach(attach -> {
            try {
                Path file = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\" + attach.getUuid() + "_" + attach.getFileName());

                // 실제 파일 삭제
                Files.deleteIfExists(file);

                // 파일 타입이 이미지일 경우 썸네일 파일도 같이 삭제
                if (Files.probeContentType(file).startsWith("image")) {
                    Path thumbNail = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\s_" + attach.getUuid() + "_" + attach.getFileName());

                    //실제 썸네일 파일 삭제
                    Files.delete(thumbNail);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
