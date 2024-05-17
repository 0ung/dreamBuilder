package codehows.dream.dreambulider.controller;


import codehows.dream.dreambulider.dto.ReplyDTO.ReplyListViewResponse;
import codehows.dream.dreambulider.dto.ReplyDTO.ReplyResponseDTO;
import codehows.dream.dreambulider.service.NestedService;
import codehows.dream.dreambulider.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class TestViewController {

    private final ReplyService replyService;
    private final NestedService nestedService;

    @GetMapping("/reply")
    public String getReplys(Model model) {
        List<ReplyListViewResponse> replys = replyService.findAll().stream()
                .map(ReplyListViewResponse::new)
                .toList();
        model.addAttribute("replys", replys);

        return "test";
    }
}
