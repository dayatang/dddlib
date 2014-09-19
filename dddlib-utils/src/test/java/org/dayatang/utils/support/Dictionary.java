package org.dayatang.utils.support;

public class Dictionary {

    private static final long serialVersionUID = 5429887402331650527L;

    private Long id;

    private String code;

    private String text;

    private DictionaryCategory category;

    private int sortOrder;

    private boolean disabled = false;

    private String description;

    private String parentCode;

    public Dictionary() {
    }

    public Dictionary(String code, String text, DictionaryCategory category) {
        this.code = code;
        this.text = text;
        //this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the category
     */
    public DictionaryCategory getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(DictionaryCategory category) {
        this.category = category;
    }

    /**
     * @return the sortOrder
     */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
     * @param sortOrder the sortOrder to set
     */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * @return the disabled
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the parentCode
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * @param parentCode the parentCode to set
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public void disable() {
        setDisabled(true);
    }

}
