package tech.labs.rucker.llamachat;

/**
 * Created by Carlos on 2/8/2018.
 */

public class ListItem {
    private String head;
    private String desc;


    public ListItem(String head, String desc) {
        this.head = head;
        this.desc = desc;
    }

    public String getHead() {
        return head;
    }
    public String getDesc() {
        return desc;
    }
}
