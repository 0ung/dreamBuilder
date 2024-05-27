package codehows.dream.dreambulider.controller;


import codehows.dream.dreambulider.service.VisitedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/visits")
public class VisitedController {

    @Autowired
    private VisitedService visitedService;

    /* 일간 방문자 수 조회 */
    @GetMapping("/daily")
    public List<Map<String, Object>> getDailyVisits(@RequestParam(value = "date", defaultValue = "#{T(java.time.LocalDate).now().toString()}") String date) {
        LocalDate localDate = LocalDate.parse(date);
        return visitedService.getDailyVisits(localDate);
    }

    /* 주간 방문자 수 조회 */
    @GetMapping("/weekly")
    public List<Map<String, Object>> getWeeklyVisits(@RequestParam(value = "date", defaultValue = "#{T(java.time.LocalDate).now().toString()}") String date) {
        LocalDate localDate = LocalDate.parse(date);
        return visitedService.getWeeklyVisits(localDate);
    }

    /* 월간 방문자 수 조회 */
    @GetMapping("/monthly")
    public List<Map<String, Object>> getMonthlyVisits(@RequestParam(value = "date", defaultValue = "#{T(java.time.LocalDate).now().toString()}") String date) {
        LocalDate localDate = LocalDate.parse(date);
        return visitedService.getMonthlyVisits(localDate);
    }

    /*
    @GetMapping("/daily")
    public long getDailyVisits(@RequestParam(value = "date", defaultValue = "#{T(java.time.LocalDate).now().toString()}") String date) {
        LocalDate localDate = LocalDate.parse(date);
        return visitedService.countDailyVisits(localDate);
    }

    @GetMapping("/weekly")
    public long getWeeklyVisits(@RequestParam(value = "date", defaultValue = "#{T(java.time.LocalDate).now().toString()}") String date) {
        LocalDate localDate = LocalDate.parse(date);
        return visitedService.countWeeklyVisits(localDate);
    }

    @GetMapping("/monthly")
    public long getMonthlyVisits(@RequestParam(value = "date", defaultValue = "#{T(java.time.LocalDate).now().toString()}") String date) {
        LocalDate localDate = LocalDate.parse(date);
        return visitedService.countMonthlyVisits(localDate);
    }*/
}
