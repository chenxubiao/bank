package cn.longhaiyan.account.domain;

import cn.longhaiyan.account.enums.AccountLogTypeEnum;
import cn.longhaiyan.common.utils.TimeUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by chenxb on 17-5-13.
 */


/**
 * many端
 *
 * 在one-to-many双向关联中，多的一方为关系维护端，关系维护端负责外键记录的更新
 * 关系被维护端是没有权力更新外键记录的
 *
 * @author jiqinlin
 *
 */
@Entity
@Table(name = "tb_account_log")
public class AccountLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "createTime", updatable = false)
    private Date createTime;
    @Column(name = "modifyTime")
    private Date modifyTime;
    private int userId;
    private int type;
    private int money;
    private int projectId;
    @Transient
    private String message;
    private int balance;    //余额
    private String remark = "";
    // optional=true：可选，表示此对象可以没有，可以为null；false表示必须存在
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, optional = true)
    @JoinColumn(name = "accountId")
    private Account account;

    public AccountLog() {
    }

    public AccountLog(int userId, int type, int money, int projectId, String remark, Account account) {
        this.userId = userId;
        this.type = type;
        this.money = money;
        this.projectId = projectId;
        this.remark = remark;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getMessage() {
        if (AccountLogTypeEnum.ADD_LOGIN.getCode() == this.type) {
            message = "您的账户"
                    + TimeUtil.format(this.createTime, TimeUtil.DATE_FORMAT_CHINESE)
                    + "连续登录" + this.projectId
                    + "天收入时间币"
                    + this.money
                    + "枚，账户余额"
                    + this.balance
                    + "枚。「时间银行」";

        } else if (AccountLogTypeEnum.ADD_REGESTER.getCode() == this.type) {
            message = "恭喜您"
                    + TimeUtil.format(this.createTime, TimeUtil.DATE_FORMAT_CHINESE)
                    + "加入「时间银行」，系统已为您自动开通账户，注册收入时间币"
                    + money
                    + "枚，账户余额"
                    + balance
                    + "枚。「时间银行」";

        } else if (AccountLogTypeEnum.DEL_TASK_PUB.getCode() == this.type) {
            message = "您的账户"
                    + TimeUtil.format(this.createTime, TimeUtil.DATE_FORMAT_CHINESE)
                    + "发布需求" + this.remark
                    + "，托管时间币"
                    + this.money
                    + "枚，账户余额"
                    + this.balance
                    + "枚。「时间银行」";
        } else if (AccountLogTypeEnum.DEL_TASK_URGENT.getCode() == this.type) {
            message = "您的账户"
                    + TimeUtil.format(this.createTime, TimeUtil.DATE_FORMAT_CHINESE)
                    + "发布需求" + this.remark
                    + "，公益资助时间币"
                    + this.money
                    + "枚，账户余额"
                    + this.balance
                    + "枚。「时间银行」";
        }
        return message;
    }
}
