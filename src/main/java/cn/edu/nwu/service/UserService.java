package cn.edu.nwu.service;

import cn.edu.nwu.dao.AccountRepository;
import cn.edu.nwu.domain.Account;
import cn.edu.nwu.domain.ChangePasswordVO;
import cn.edu.nwu.domain.User;
import cn.edu.nwu.dao.UserRepository;
import cn.edu.nwu.util.CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CryptoUtil cryptoUtil;

    public User login(User user) {
        User u =this.getInfo(user.getCard()) ;
        if (u == null||!cryptoUtil.match(user.getPassword(), u.getPassword())) {
            return null;
        }
        return u;
    }
    @CacheEvict(value = "user",key="#a0.card",beforeInvocation = true)
    public boolean modify(User user) {
        User u =this.getInfo(user.getCard());
        if(u==null){
            return false;
        }
        userRepository.save(user);
        return true;
    }

    public String userAdd(User user) {
        User u = userRepository.findByIdcard(user.getIdcard());
        if (u != null) {
            return "already";
        }
        user.setPassword(cryptoUtil.encrypt(user.getPassword()));
        String card = System.currentTimeMillis() + "";
        user.setCard(card);
        user.setAdmin(0);
        user.setState(1);
        userRepository.save(user);
        accountRepository.save(new Account(card,new BigDecimal(0)));
        return card;
    }

    @Cacheable(value = "user")
    public User getInfo(String card) {
        System.out.println("查询数据库");
        return userRepository.findById(card).orElse(null);
    }

    @CacheEvict(value = "user",key="#a0.card",beforeInvocation = true)
    public boolean changePassword(ChangePasswordVO changePasswordVO){
        User u1=this.getInfo(changePasswordVO.getCard());
        if(u1==null){
            return false;
        }
        u1.setPassword(cryptoUtil.encrypt(changePasswordVO.getNewPass()));
        userRepository.save(u1);
        return true;
    }

    public Page<User> getUsers(int pageNum, int pageSize) {
        User user=new User();
        user.setAdmin(0);
        Example<User> example = Example.of(user);
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, Sort.Direction.DESC, "card");
        Page<User> all = userRepository.findAll(example,pageRequest);
        return all;
    }

    public boolean stateSwitch(String card, int newState) {
        User u1=userRepository.findById(card).orElse(null);
        if(u1==null){
            return false;
        }
        u1.setState(newState);
        userRepository.save(u1);
        return true;
    }
}
