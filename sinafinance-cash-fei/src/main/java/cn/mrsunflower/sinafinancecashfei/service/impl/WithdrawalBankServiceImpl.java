package cn.mrsunflower.sinafinancecashfei.service.impl;

import cn.mrsunflower.sinafinancecashfei.mapper.WithdrawalBankMapper;
import cn.mrsunflower.sinafinancecashfei.pojo.WithdrawalBank;
import cn.mrsunflower.sinafinancecashfei.service.WithdrawalBankService;
import lombok.With;
import org.apache.http.impl.conn.Wire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@SuppressWarnings("ALL")
@Service
public class WithdrawalBankServiceImpl implements WithdrawalBankService {
    @Autowired
    private WithdrawalBankMapper withdrawalBankMapper;

    //根据uid与bankCode查询提现银行
    public WithdrawalBank getWithdrawalBankByUidAndBankCode(Long uid, String bankCode) {
        Example example = new Example(WithdrawalBank.class);
        example.createCriteria().andEqualTo("uid", uid).andEqualTo("bankCode", bankCode);
        List<WithdrawalBank> withdrawalBanks = withdrawalBankMapper.selectByExample(example);
        if (withdrawalBanks.size() == 0) {
            return null;
        }
        return withdrawalBanks.get(0);

    }


    public int insertWithdrawalBank(WithdrawalBank withdrawalBank) {
        int insert = withdrawalBankMapper.insert(withdrawalBank);
        return insert;
    }

    public List<WithdrawalBank> getWithdrawalBankByUid(Long userId) {
        Example example = new Example(WithdrawalBank.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria uid = criteria.andEqualTo("uid", userId);
        List<WithdrawalBank> withdrawalBanks = withdrawalBankMapper.selectByExample(example);
        return withdrawalBanks;
    }
}
