package cn.mrsunflower.sinafinancecashfei.pojo;

import lombok.*;

import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tb_user")
public class User {
    // 用户名
    private Long uid;
    //密码，加密存储
    private String password;
    // 注册手机号
    private String phone;
    //  注册邮箱
    private String email;
    //创建时间
    private Date created;
    // 修改时间
    private Date updated;
    //  昵称
    private String nick_name;
    //  真实姓名
    private String name;
    //   使用状态（1正常 0非正常）
    private String status;
    // QQ号码
    private String qq;
    //   手机是否验证 （0否  1是）
    private String is_mobile_check;
    //邮箱是否检测（0否  1是）
    private String is_email_check;
    //性别，1男，0女
    private String sex;
    //最后登录时间
    private Date last_login_time;
}
