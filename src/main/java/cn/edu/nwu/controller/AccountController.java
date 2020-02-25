package cn.edu.nwu.controller;


import cn.edu.nwu.domain.Account;
import cn.edu.nwu.domain.Log;
import cn.edu.nwu.domain.TransferVO;
import cn.edu.nwu.service.AccountExcelView;
import cn.edu.nwu.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/account" )
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping("/transfer")
    public boolean transfer(@RequestBody TransferVO transferVO){
        int type=transferVO.getType();
        BigDecimal amount = transferVO.getAmount();
        String oppositecard=transferVO.getOppositecard();
        String card=transferVO.getCard();

        Date date = new Date();
        if(type==2){
            Log transferFrom = new Log(date,amount,null,oppositecard,card,2);
            Log transferTo = new Log(date,amount,null,card,oppositecard,3);
            return accountService.transfer(transferFrom,transferTo);
        }

        if(type==0||type==1){
            Log log = new Log(date,amount,null,null,card,type);
            return accountService.transfer(log,null);
        }
        return false;
    }

    @GetMapping("")
    public Account getAccount(@RequestParam("card") String card){

        String s = accountService.getAccount(card).stripTrailingZeros().toPlainString();
        return new Account(card,s);
    }
    @GetMapping("/list")
    public Page<Log> getLog(@RequestParam("card") String card, @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize){

        return accountService.getLog(card,pageNum,pageSize);
    }
    @RequestMapping("/excel")
    public ModelAndView download(String card, HttpServletRequest request){
        List<Log> logs = accountService.getAllLog(card);//内容
        AccountExcelView excelView = new AccountExcelView();
        Map<String, Object> map = new HashMap<>();
        map.put("logs", logs);
        return new ModelAndView(excelView,map);
    }

}
