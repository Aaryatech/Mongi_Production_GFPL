package com.ats.mongi_production.model;

public class AllMenu {

    private int menuId;
    private String menuTitle;
    private String menuDesc;
    private String menuImage;
    private String selectedMenuImage;
    private String isSameDayApplicable;
    private int mainCatId;
    private int delStatus;
    private boolean checkedStatus;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }

    public String getSelectedMenuImage() {
        return selectedMenuImage;
    }

    public void setSelectedMenuImage(String selectedMenuImage) {
        this.selectedMenuImage = selectedMenuImage;
    }

    public String getIsSameDayApplicable() {
        return isSameDayApplicable;
    }

    public void setIsSameDayApplicable(String isSameDayApplicable) {
        this.isSameDayApplicable = isSameDayApplicable;
    }

    public int getMainCatId() {
        return mainCatId;
    }

    public void setMainCatId(int mainCatId) {
        this.mainCatId = mainCatId;
    }

    public int getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(int delStatus) {
        this.delStatus = delStatus;
    }

    public boolean isCheckedStatus() {
        return checkedStatus;
    }

    public void setCheckedStatus(boolean checkedStatus) {
        this.checkedStatus = checkedStatus;
    }

    @Override
    public String toString() {
        return "AllMenu{" +
                "menuId=" + menuId +
                ", menuTitle='" + menuTitle + '\'' +
                ", menuDesc='" + menuDesc + '\'' +
                ", menuImage='" + menuImage + '\'' +
                ", selectedMenuImage='" + selectedMenuImage + '\'' +
                ", isSameDayApplicable='" + isSameDayApplicable + '\'' +
                ", mainCatId=" + mainCatId +
                ", delStatus=" + delStatus +
                ", checkedStatus=" + checkedStatus +
                '}';
    }
}
