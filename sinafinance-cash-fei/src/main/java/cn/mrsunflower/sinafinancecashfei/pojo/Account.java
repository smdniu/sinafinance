package cn.mrsunflower.sinafinancecashfei.pojo;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
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
    private Long id;
    @Column(name = "uid")
    private Long uid;
    @Column(name = "account")
    private BigDecimal account;
    @Column(name = "createTime")
    private Date createTime;
    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;
}
