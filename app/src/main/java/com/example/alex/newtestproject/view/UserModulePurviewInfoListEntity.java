package com.example.alex.newtestproject.view;

/**
 * 用户授权模块
 */
public class UserModulePurviewInfoListEntity {
    private int idModule;
    private String userPurviewFeature;
    private String moduleUri;
    private String moduleNameEn;
    private String moduleNameCn;

    public int getIdModule() {
        return idModule;
    }

    public void setIdModule(int idModule) {
        this.idModule = idModule;
    }

    public String getUserPurviewFeature() {
        return userPurviewFeature;
    }

    public void setUserPurviewFeature(String userPurviewFeature) {
        this.userPurviewFeature = userPurviewFeature;
    }

    public String getModuleUri() {
        return moduleUri;
    }

    public void setModuleUri(String moduleUri) {
        this.moduleUri = moduleUri;
    }

    public String getModuleNameEn() {
        return moduleNameEn == null ? "" : moduleNameEn;
    }

    public void setModuleNameEn(String moduleNameEn) {
        this.moduleNameEn = moduleNameEn;
    }

    public String getModuleNameCn() {
        return moduleNameCn;
    }

    public void setModuleNameCn(String moduleNameCn) {
        this.moduleNameCn = moduleNameCn;
    }
}
