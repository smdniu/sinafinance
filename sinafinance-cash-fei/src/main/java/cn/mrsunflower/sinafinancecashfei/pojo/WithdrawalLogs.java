package cn.mrsunflower.sinafinancecashfei.pojo;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
/**
 * 提现的日志
 */
@Table(name = "tb_withdrawal_logs")
public class WithdrawalLogs {
  //id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  //提现申请人
  @Column(name = "operater")
  private String operater;
  //提现订单号,系统自动生成
  @Column(name = "withdrawOrder")
  private String withdrawOrder;
  //备注
  @Column(name = "remark")
  private String remark;
  //提现的状态
  @Column(name = "status")
  private Long status;
}
