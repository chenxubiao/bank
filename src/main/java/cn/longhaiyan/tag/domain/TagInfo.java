package cn.longhaiyan.tag.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by chenxb on 17-4-1.
 */
@Entity
@Table(name = "tb_tag_meta")
public class TagInfo implements Serializable {
    private static final long serialVersionUID = -1903875172360553265L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @Column(name = "createTime", updatable = false)
    private Date createTime;
    @Column(name = "modifyTime")
    private Date modifyTime;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
