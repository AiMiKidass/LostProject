package com.example.alex.newtestproject.view;


import java.io.Serializable;

/**
 * 用户会话管理类
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class UserSessionInfoEntity implements Serializable {

    public static final String CONSTANT_IDUSER = "idUser";
    public static final String CONSTANT_USERNICKNAME = "userNickName";
    public static final String CONSTANT_USERNAMEFULL = "userNameFull";
    public static final String CONSTANT_USERNAMEBRIEF = "userNameBrief";
    public static final String CONSTANT_USERIDENTITY = "userIdentity";

    public static final String CONSTANT_USERTYPE = "userType";
    public static final String CONSTANT_USERDESCRIPTION = "userDescription";
    public static final String CONSTANT_USERAVATARICONIDFILELOGIC = "userAvatarIconIdFileLogic";
    public static final String CONSTANT_IDUNITLICENSELOGINPROJECT = "idUnitLicenseLoginProject";
    public static final String CONSTANT_USERMOBILECAPTCHASOURCE = "userMobileCaptchaSource";

    public static final String CONSTANT_USEREMAILCAPTCHASOURCE = "userEmailCaptchaSource";
    public static final String CONSTANT_SESSIONCHECKSUMCODE = "sessionChecksumCode";
    public static final String CONSTANT_IDSESSION = "idSession";
    public static final String CONSTANT_USERGRPINFOLIST = "userGrpInfoList";
    public static final String CONSTANT_USERMODULEPURVIEWINFOLIST = "userModulePurviewInfoList";

    public static final String CONSTANT_IDUSERUNIT = "idUserUnit";


    private int idUser;
    private String userNickName;
    private String userNameFull;
    private String userNameBrief;
    private String userIdentity;
    private String userType;
    private String userDescription;
    private String userAvatarIconIdFileLogic;
    private String idUnitLicenseLoginProject;
    private String userMobileCaptchaSource;
    private String userEmailCaptchaSource;
    private String sessionChecksumCode;
    private String idSession;

    private String userGrpInfoList;
    private String userModulePurviewInfoList;
    private int idUserUnit;

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserNameFull() {
        return userNameFull;
    }

    public void setUserNameFull(String userNameFull) {
        this.userNameFull = userNameFull;
    }

    public String getUserNameBrief() {
        return userNameBrief;
    }

    public void setUserNameBrief(String userNameBrief) {
        this.userNameBrief = userNameBrief;
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getUserMobileCaptchaSource() {
        return userMobileCaptchaSource;
    }

    public void setUserMobileCaptchaSource(String userMobileCaptchaSource) {
        this.userMobileCaptchaSource = userMobileCaptchaSource;
    }

    public void setUserEmailCaptchaSource(String userEmailCaptchaSource) {
        this.userEmailCaptchaSource = userEmailCaptchaSource;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserAvatarIconIdFileLogic(String userAvatarIconIdFileLogic) {
        this.userAvatarIconIdFileLogic = userAvatarIconIdFileLogic;
    }

    public String getUserAvatarIconIdFileLogic() {
        return userAvatarIconIdFileLogic;
    }

    public void setUserGrpInfoList(String userGrpInfoList) {
        this.userGrpInfoList = userGrpInfoList;
    }

    public String getUserGrpInfoList() {
        return userGrpInfoList;
    }

    public void setUserModulePurviewInfoList(String userModulePurviewInfoList) {
        this.userModulePurviewInfoList = userModulePurviewInfoList;
    }

    public String getUserModulePurviewInfoList() {
        return userModulePurviewInfoList;
    }

    public void setIdUserUnit(int idUserUnit) {
        this.idUserUnit = idUserUnit;
    }

    public int getIdUserUnit() {
        return idUserUnit;
    }

    public void setIdUnitLicenseLoginProject(String idUnitLicenseLoginProject) {
        this.idUnitLicenseLoginProject = idUnitLicenseLoginProject;
    }

    public String getIdUnitLicenseLoginProject() {
        return idUnitLicenseLoginProject;
    }


    public String getUserEmailCaptchaSource() {
        return userEmailCaptchaSource;
    }

    public void setSessionChecksumCode(String sessionChecksumCode) {
        this.sessionChecksumCode = sessionChecksumCode;
    }

    public String getSessionChecksumCode() {
        return sessionChecksumCode;
    }

    public String getIdSession() {
        return idSession;
    }

    /**
     * 有ID表示是登录,反之没登录
     *
     * @return
     */
    public boolean isAlive() {
        return getIdUser() != 0;
    }

}
