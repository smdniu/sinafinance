package cn.mrsunflower.sinafinancecashfei.pojo;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
/**
 * 帐户信息表
 */
@Table(name = "tb_account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "uid")
    private String uid;
    @Column(name = "account")
    private BigDecimal account;
    @Column(name = "state")
    private String state;//normal 1:正常  freeze 2:冻结
    @Column(name = "createTime")
    private Date createTime;
    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;
}
