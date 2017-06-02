package cn.longhaiyan.account.domain;

import cn.longhaiyan.account.enums.AccountPayStatusEnum;
import cn.longhaiyan.admin.enums.PayTypeEnum;
import cn.longhaiyan.user.bean.UserInfoBean;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by chenxb on 17-5-15.
 */
@Entity
@Table(name = "tb_account_pay")
public class AccountPay {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "createTime", updatable = false)
    private Date createTime;
    @Column(name = "modifyTime")
    private Date modifyTime;
    private int payer;
    private int money;
    private int type;
    @Transient
    private String typeStr;
    private int status;
    private String remark = "";
    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE}, optional = true)
    @JoinColumn(name = "accountId")
    private Account account;

    @Transient
    private UserInfoBean payerInfo;
    @Transient
    private UserInfoBean userInfo;

    public AccountPay() {

    }

    public AccountPay(Account account, int payer, int money, int type) {
        this.type = type;
        this.account = account;
        this.payer = payer;
        this.money = money;
        this.status = AccountPayStatusEnum.SUCCESS.getCode();
        this.createTime = new Date();
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

    public int getPayer() {
        return payer;
    }

    public void setPayer(int payer) {
        this.payer = payer;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeStr() {
        return PayTypeEnum.getValue(this.type);
    }

    public UserInfoBean getPayerInfo() {
        return payerInfo;
    }

    public void setPayerInfo(UserInfoBean payerInfo) {
        this.payerInfo = payerInfo;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }
}
