package com.sinafinance.pojo;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
/**
 * 用户绑定的银行
 */
@Table(name = "tb_withdrawal_bank")
public class WithdrawalBank {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  //用户ID
  @Column(name = "uid")
  private Long uid;
  //中文名
  @Column(name = "cnname")
  private String cnname;
  //卡的缩写 例如ICBC
  @Column(name = "bankCode")
  private String bankCode;
  //银行名
  @Column(name = "bankName")
  private String bankName;
  //卡号
  @Column(name = "bankNumber")
  private String bankNumber;
  //排序用,从小到大
  @Column(name = "sequence")
  private Long sequence;
  //创建时间
  @Column(name = "createTime")
  private java.sql.Timestamp createTime;
}
