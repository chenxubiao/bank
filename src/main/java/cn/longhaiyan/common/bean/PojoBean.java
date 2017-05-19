package cn.longhaiyan.common.bean;

/**
 * Created by chenxb on 17-5-19.
 */
public class PojoBean {
    private int id;
    private String value;

    public PojoBean() {
    }

    public PojoBean(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
