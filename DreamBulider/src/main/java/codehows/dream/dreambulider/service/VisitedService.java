package codehows.dream.dreambulider.service;

import codehows.dream.dreambulider.entity.Visited;
import codehows.dream.dreambulider.repository.VisitedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;


@Service
@RequiredArgsConstructor
public class VisitedService {

    private final VisitedRepository visitedRepository;
    public void saveVisit(String ipAddress) {
        LocalDate today = LocalDate.now();
        Optional<Visited> existingVisit = visitedRepository.findByVisitedIpAndVisitDate(ipAddress, today);
        if (existingVisit.isEmpty()) {
            Visited visited = new Visited();
            visited.setVisitedIp(ipAddress);
            visited.setVisitDate(today);
            visitedRepository.save(visited);
        }
    }

    /* 일간 방문자 수 조회 */
    public List<Map<String, Object>> getDailyVisits(LocalDate startDate) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            LocalDate date = startDate.plusDays(i);
            long count = visitedRepository.findAllByVisitDate(date).size();
            Map<String, Object> data = new HashMap<>();
            data.put("period", date.toString());
            data.put("visits", count);
            result.add(data);
        }
        return result;
    }
    /* 주간 방문자 수 조회 */
    public List<Map<String, Object>> getWeeklyVisits(LocalDate startDate) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            LocalDate startOfWeek = startDate.plusWeeks(i).with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
            LocalDate endOfWeek = startOfWeek.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
            long count = visitedRepository.findAllByVisitDateBetween(startOfWeek, endOfWeek).size();
            Map<String, Object> data = new HashMap<>();
            data.put("period", startOfWeek.toString() + " to " + endOfWeek.toString());
            data.put("visits", count);
            result.add(data);
        }
        return result;
    }

    /* 월간 방문자 수 조회 */
    public List<Map<String, Object>> getMonthlyVisits(LocalDate startDate) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            LocalDate startOfMonth = startDate.plusMonths(i).with(TemporalAdjusters.firstDayOfMonth());
            LocalDate endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth());
            long count = visitedRepository.findAllByVisitDateBetween(startOfMonth, endOfMonth).size();
            Map<String, Object> data = new HashMap<>();
            data.put("period", startOfMonth.toString() + " to " + endOfMonth.toString());
            data.put("visits", count);
            result.add(data);
        }
        return result;
    }
    /*
    public long countDailyVisits(LocalDate date) {
        return visitedRepository.findAllByVisitDate(date).size();
    }

    public long countWeeklyVisits(LocalDate date) {
        LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = date.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        return visitedRepository.findAllByVisitDateBetween(startOfWeek, endOfWeek).size();
    }

    public long countMonthlyVisits(LocalDate date) {
        LocalDate startOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
        return visitedRepository.findAllByVisitDateBetween(startOfMonth, endOfMonth).size();
    } */
}

