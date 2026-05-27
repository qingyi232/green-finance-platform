package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lly.entity.*;
import com.lly.mapper.*;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;;

@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired private ResourceMapper resourceMapper;
    @Autowired private EvaluationApplyMapper evalMapper;
    @Autowired private ListingMapper listingMapper;
    @Autowired private DemandMapper demandMapper;
    @Autowired private LoanApplyMapper loanMapper;
    @Autowired private LegalConsultMapper consultMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private FinanceProductMapper financeProductMapper;
    @Autowired private FarmProductMapper farmProductMapper;

    @GetMapping("/overview")
    public R overview() {
        // 统计资源总面积
        BigDecimal totalArea = BigDecimal.ZERO;
        for (Resource r : resourceMapper.selectList(new QueryWrapper<Resource>().eq("status", "已收储"))) {
            if (r.getAreaMu() != null) totalArea = totalArea.add(r.getAreaMu());
        }
        BigDecimal totalCarbon = BigDecimal.ZERO;
        for (Resource r : resourceMapper.selectList(new QueryWrapper<Resource>().eq("status", "已收储"))) {
            if (r.getCarbonStorage() != null) totalCarbon = totalCarbon.add(r.getCarbonStorage());
        }
        BigDecimal totalLoanAmount = BigDecimal.ZERO;
        for (LoanApply la : loanMapper.selectList(new QueryWrapper<LoanApply>().eq("status", "已批准"))) {
            if (la.getApprovedAmount() != null) totalLoanAmount = totalLoanAmount.add(la.getApprovedAmount());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("resourceCount", resourceMapper.selectCount(new QueryWrapper<Resource>().eq("status", "已收储")));
        data.put("resourceArea", totalArea);
        data.put("carbonStorage", totalCarbon);
        data.put("evaluationCount", evalMapper.selectCount(null));
        data.put("listingCount", listingMapper.selectCount(new QueryWrapper<Listing>().eq("status", "挂牌中")));
        data.put("demandCount", demandMapper.selectCount(new QueryWrapper<Demand>().eq("status", "征集中")));
        data.put("loanCount", loanMapper.selectCount(null));
        data.put("loanAmount", totalLoanAmount);
        data.put("consultCount", consultMapper.selectCount(null));
        data.put("userCount", userMapper.selectCount(null));
        data.put("financeProductCount", financeProductMapper.selectCount(new QueryWrapper<FinanceProduct>().eq("status", 1)));
        data.put("farmProductCount", farmProductMapper.selectCount(new QueryWrapper<FarmProduct>().eq("status", 1)));
        return R.ok().put("data", data);
    }

    @GetMapping("/resource-distribution")
    public R resourceDistribution() {
        Map<String, Integer> byType = new HashMap<>();
        for (Resource r : resourceMapper.selectList(new QueryWrapper<Resource>().eq("status", "已收储"))) {
            byType.merge(r.getResourceType(), 1, Integer::sum);
        }
        return R.ok().put("byType", byType);
    }

    @GetMapping("/trend")
    public R trend() {
        SimpleDateFormat sdf = new SimpleDateFormat("M/d");
        List<String> days = new ArrayList<>();
        List<Integer> resourceData = new ArrayList<>();
        List<Integer> listingData = new ArrayList<>();
        List<Integer> loanData = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        for (int i = 6; i >= 0; i--) {
            Calendar dayEnd = (Calendar) cal.clone();
            dayEnd.add(Calendar.DAY_OF_MONTH, -i);
            Calendar dayStart = (Calendar) dayEnd.clone();
            dayStart.set(Calendar.HOUR_OF_DAY, 0);
            dayStart.set(Calendar.MINUTE, 0);
            dayStart.set(Calendar.SECOND, 0);

            Date start = dayStart.getTime();
            Date end = dayEnd.getTime();
            days.add(sdf.format(start));

            resourceData.add(Math.toIntExact(resourceMapper.selectCount(
                new QueryWrapper<Resource>().between("created_time", start, end))));
            listingData.add(Math.toIntExact(listingMapper.selectCount(
                new QueryWrapper<Listing>().between("created_time", start, end))));
            loanData.add(Math.toIntExact(loanMapper.selectCount(
                new QueryWrapper<LoanApply>().between("created_time", start, end))));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("days", days);
        result.put("resource", resourceData);
        result.put("listing", listingData);
        result.put("loan", loanData);
        return R.ok().put("data", result);
    }
}
