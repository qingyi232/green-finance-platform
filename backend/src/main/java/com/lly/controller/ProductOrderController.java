package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.FarmProduct;
import com.lly.entity.ProductOrder;
import com.lly.mapper.FarmProductMapper;
import com.lly.mapper.ProductOrderMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/product-order")
public class ProductOrderController {
    @Autowired private ProductOrderMapper mapper;
    @Autowired private FarmProductMapper productMapper;

    @PostMapping("/save")
    public R save(@RequestBody ProductOrder o, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        FarmProduct p = productMapper.selectById(o.getProductId());
        if (p == null) return R.error("商品不存在");
        if (p.getStock() != null && p.getStock() < o.getQuantity()) return R.error("库存不足");

        o.setBuyerId(userId);
        o.setProductName(p.getName());
        o.setCover(p.getCover());
        o.setUnitPrice(p.getPrice());
        o.setTotalPrice(p.getPrice().multiply(BigDecimal.valueOf(o.getQuantity())));
        o.setOrderNo("ORD" + System.currentTimeMillis() + new Random().nextInt(100));
        o.setCreatedTime(new Date());
        if (o.getStatus() == null) o.setStatus("待发货");
        mapper.insert(o);

        // 减库存,加销量
        if (p.getStock() != null) p.setStock(p.getStock() - o.getQuantity());
        if (p.getSales() != null) p.setSales(p.getSales() + o.getQuantity());
        productMapper.updateById(p);

        return R.ok("订单已提交");
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest req,
                    @RequestParam(defaultValue = "1") Integer page,
                    @RequestParam(defaultValue = "10") Integer limit) {
        Long userId = (Long) req.getAttribute("userId");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit),
                new QueryWrapper<ProductOrder>().eq("buyer_id", userId).orderByDesc("created_time"))));
    }

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String status) {
        QueryWrapper<ProductOrder> w = new QueryWrapper<>();
        if (status != null && !status.isEmpty()) w.eq("status", status);
        w.orderByDesc("created_time");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @PutMapping("/update")
    public R update(@RequestBody ProductOrder o) { mapper.updateById(o); return R.ok(); }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
