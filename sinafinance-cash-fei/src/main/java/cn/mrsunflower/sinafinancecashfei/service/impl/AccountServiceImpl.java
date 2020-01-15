package cn.mrsunflower.sinafinancecashfei.service.impl;

import cn.mrsunflower.sinafinancecashfei.mapper.AccountMapper;
import cn.mrsunflower.sinafinancecashfei.pojo.Account;
import cn.mrsunflower.sinafinancecashfei.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Autowired
    private AccountMapper accountMapper;


    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Account findById(String id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    @Override
    public BigDecimal findAccountByUid(long uid) {
        Example example = new Example(Account.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uid", uid);
        List<Account> accounts = accountMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(accounts) && accounts.size()>0){
            return accounts.get(0).getAccount();
        }
        return null;
    }


}
