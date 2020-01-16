package com.sinafinance.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
/**
 * 提现信息
 */
@Table(name = "tb_withdrawal_info")
public class WithdrawalInfo {

    @Id
    private Long id;
    //提现申请人ID
    @Column(name = "uid")
    private String uid;
    //提现订单号,系统自动生成的
    @Column(name = "withdrawOrder")
    private String withdrawOrder;
    //用户对应的卡的编号
    @Column(name = "withdrawBankId")
    private String withdrawBankId;
    //提现手续费
    @Column(name = "withdrawCharge")
    private BigDecimal withdrawCharge;
    //实际提现金额
    @Column(name = "withdrawRealityTotal")
    private BigDecimal withdrawRealityTotal;
    //申请提现的金额
    @Column(name = "withdrawApplyTotal")
    private BigDecimal withdrawApplyTotal;
    //申请提现的时间
    @Column(name = "withdrawApplyTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date withdrawApplyTime;
    /**
     * 提现状态:
     *    1：发起提现
     *    2：处理中
     *    3：到账
     *    4：提现失败
     *    5：退回成功
     *    -1审批不通过.
     */
    private Long status;
    //创建时间
    @Column(name = "createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    //最后一次修改时间
    @Column(name = "lastUpdateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastUpdateTime;
    //预计到账时间
    @Column(name = "expectTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date expectTime;

}
