package model;

/**
 * Created by Pavan on 4/8/16.
 */
public class SelectPrivilege {

    private String privilege;
    private Boolean isSelected;

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
