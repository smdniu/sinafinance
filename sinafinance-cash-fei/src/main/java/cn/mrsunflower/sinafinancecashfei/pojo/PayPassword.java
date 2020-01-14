package cn.mrsunflower.sinafinancecashfei.pojo;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
/**
 * 支付密码实体类
 */
@Table(name = "tb_pay_password")
public class PayPassword {
    @Id
    Long id;
    Long uid;
    String password;
    @Column(name = "createTime")
    Date createTime;
    @Column(name = "lastUpdateTime")
    Date lastUpdateTime;
}
