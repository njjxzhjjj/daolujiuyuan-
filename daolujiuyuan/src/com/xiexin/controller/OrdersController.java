package com.xiexin.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiexin.bean.Orders;
import com.xiexin.bean.OrdersExample;
import com.xiexin.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")//跨域
@RestController
@RequestMapping("/api/orders")
public class OrdersController{
@Autowired(required = false)
private OrdersService ordersService;


//提交
    @RequestMapping("/addOrders") //  /api/orders/addOrders
    public Map addOrders(HttpServletRequest request, Orders orders){
        Map codeMap =new HashMap();
        System.out.println("访问成功");
        //手机号 lng lat contents address cost
        System.out.println("orders = " + orders);
        String  phoneNum = (String) request.getAttribute("phoneNum");
        System.out.println("phoneNum = " + phoneNum);
        orders.setPhone(phoneNum);

        //作业2:河南省，郑州市，管城回族区，瑞绣路，，改编成﹒河南省郑州市管城回族区瑞绣路
        //调用 service层 做添加
        orders.setCreatetime(new Date());
        orders.setStatus("已经接单");
        int i = ordersService.insertSelective(orders);
        if(i==1){
            codeMap.put("code",200);
            codeMap.put("msg","您的提交己经收到，请交心等待，我们将电话联系您。");
            return codeMap;
            //作业3:.当顾客，比较着急，连续点击提交，提交，提交，
            // 这个学名叫做ajax重复提交。
            // 第一次提交完毕，将提交按钮变为不可点击状态，并且提交2字变为已提交订单，请稍等（前端处理）
            // 第二第N次提交的时候(防黑客，点击速度比较快)，给他 返回 亲 ，我知道 你很着急，订单已经接受了（后端处理）
            //思路 先查询 该手机号 有没有订单状态， 保存到redis 有了就返回到前端，没有 再执行新增

        }else{
            codeMap.put("code",40001);
            codeMap.put("msg","由于网咯故障，未能添加成功，亲，你重新提交一下");
            return codeMap;
        }
    }


//增
// 后端订单增加 -- 针对layui的 针对前端传 json序列化的
@RequestMapping("/insert")
public Map insert(@RequestBody Orders orders){ // orders 对象传参, 规则: 前端属性要和后台的属性一致!!!
Map map = new HashMap();
int i =  ordersService.insertSelective(orders);
if(i>0){
map.put("code",200);
map.put("msg","添加成功");
return map;
}else{
map.put("code",400);
map.put("msg","添加失败,检查网络再来一次");
return map;
}
}


// 删
// 删除订单  根据 主键 id 删除
@RequestMapping("/deleteById")
public Map deleteById(@RequestParam(value = "id") Integer id) {
Map responseMap = new HashMap();
int i = ordersService.deleteByPrimaryKey(id);
if (i > 0) {
responseMap.put("code", 200);
responseMap.put("msg", "删除成功");
return responseMap;
} else {
responseMap.put("code", 400);
responseMap.put("msg", "删除失败");
return responseMap;
}
}

// 批量删除
@RequestMapping("/deleteBatch")
public Map deleteBatch(@RequestParam(value = "idList[]") List<Integer> idList) {
    for (Integer integer : idList) {
    this.deleteById(integer);
    }
    Map responseMap = new HashMap();
    responseMap.put("code", 200);
    responseMap.put("msg", "删除成功");
    return responseMap;
    }


// 改
    // 修改订单
    @RequestMapping("/update")
    public Map update(@RequestBody Orders orders) {
    Map map = new HashMap();
    int i = ordersService.updateByPrimaryKeySelective( orders);
    if (i > 0) {
    map.put("code", 200);
    map.put("msg", "修改成功");
    return map;
    } else {
    map.put("code", 400);
    map.put("msg", "修改失败,检查网络再来一次");
    return map;
    }
    }

// 查--未分页
    // 全查
    @RequestMapping("/selectAll")
    public Map selectAll(){
    List<Orders> orderss =  ordersService.selectByExample(null);
        Map responseMap = new HashMap();
        responseMap.put("code", 0);
        responseMap.put("msg", "查询成功");
        responseMap.put("orderss", orderss);
        return responseMap;
        }

// 查-- 查询+自身对象的查询 + 分页
// 分页查询
    @RequestMapping("/selectAllByPage")
    public Map selectAllByPage(Orders orders, @RequestParam(value = "page", defaultValue = "1", required = true) Integer page,
                               @RequestParam(value = "limit", required = true) Integer pageSize) {
    // 调用service 层   , 适用于 单表!!!
    PageHelper.startPage(page, pageSize);
    OrdersExample example = new OrdersExample();
    OrdersExample.Criteria criteria = example.createCriteria();

   /* if (null!=orders.getYYYYYYYY()&&!"".equals(orders.getYYYYYYY())){
         criteria.andPhoneEqualTo(orders.getPhone());   // 1
    }*/

    List<Orders> orderssList = ordersService.selectByExample(example);
        PageInfo pageInfo = new PageInfo(orderssList);
        long total = pageInfo.getTotal();
        Map responseMap = new HashMap();
        responseMap.put("code", 0);
        responseMap.put("msg", "查询成功");
        responseMap.put("pageInfo", pageInfo);
        responseMap.put("count", total);
        return responseMap;
        }




    }
