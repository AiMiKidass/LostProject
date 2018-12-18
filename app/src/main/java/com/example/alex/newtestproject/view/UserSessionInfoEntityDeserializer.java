package com.example.alex.newtestproject.view;

import com.example.alex.newtestproject.utils.LogUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 解决此类问题:
 * 1.字段修改(新增删除)
 * 2.字段返回值修改类型 如果返回string 修改后返回集合等 就会报错
 *
 * 说明:
 * 这只一种手动修改
 */
public class UserSessionInfoEntityDeserializer implements JsonDeserializer<UserSessionInfoEntity> {

    @Override
    public UserSessionInfoEntity deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {

        final JsonObject rootJsonObject = json.getAsJsonObject();
        final JsonObject jsonObject = rootJsonObject.getAsJsonObject("data");

        final String CONSTANT_IDUSER = jsonObject.get(UserSessionInfoEntity.CONSTANT_IDUSER).getAsString();
        final String CONSTANT_USERNICKNAME = jsonObject.get(UserSessionInfoEntity.CONSTANT_USERNICKNAME).getAsString();
        final String CONSTANT_USERNAMEFULL = jsonObject.get(UserSessionInfoEntity.CONSTANT_USERNAMEFULL).getAsString();
        final String CONSTANT_USERNAMEBRIEF = jsonObject.get(UserSessionInfoEntity.CONSTANT_USERNAMEBRIEF).getAsString();
        final String CONSTANT_USERIDENTITY = jsonObject.get(UserSessionInfoEntity.CONSTANT_USERIDENTITY).getAsString();

        final String CONSTANT_USERTYPE = jsonObject.get(UserSessionInfoEntity.CONSTANT_USERTYPE).getAsString();
        final String CONSTANT_USERDESCRIPTION = jsonObject.get(UserSessionInfoEntity.CONSTANT_USERDESCRIPTION).getAsString();
        final String CONSTANT_USERAVATARICONIDFILELOGIC = jsonObject.get(UserSessionInfoEntity.CONSTANT_USERAVATARICONIDFILELOGIC).getAsString();
        final String CONSTANT_IDUNITLICENSELOGINPROJECT = jsonObject.get(UserSessionInfoEntity.CONSTANT_IDUNITLICENSELOGINPROJECT).getAsString();
        final String CONSTANT_USERMOBILECAPTCHASOURCE = jsonObject.get(UserSessionInfoEntity.CONSTANT_USERMOBILECAPTCHASOURCE).getAsString();

        final String CONSTANT_USEREMAILCAPTCHASOURCE = jsonObject.get(UserSessionInfoEntity.CONSTANT_USEREMAILCAPTCHASOURCE).getAsString();
        final String CONSTANT_SESSIONCHECKSUMCODE = jsonObject.get(UserSessionInfoEntity.CONSTANT_SESSIONCHECKSUMCODE).getAsString();
        final String CONSTANT_IDSESSION = jsonObject.get(UserSessionInfoEntity.CONSTANT_IDSESSION).getAsString();
        final String CONSTANT_IDUSERUNIT = jsonObject.get(UserSessionInfoEntity.CONSTANT_IDUSERUNIT).getAsString();


//        final String CONSTANT_USERGRPINFOLIST = jsonObject.get(UserSessionInfoEntity.CONSTANT_USERGRPINFOLIST).getAsString();
//        final String CONSTANT_USERMODULEPURVIEWINFOLIST = jsonObject.get(UserSessionInfoEntity.CONSTANT_USERMODULEPURVIEWINFOLIST).getAsString();


//        final JsonArray jsonAuthorsArray = jsonObject.get("authors").getAsJsonArray();
//        final String[] authors = new String[jsonAuthorsArray.size()];
//        for (int i = 0; i < authors.length; i++) {
//            final JsonElement jsonAuthor = jsonAuthorsArray.get(i);
//            authors[i] = jsonAuthor.getAsString();
//        }


        List<UserGrpInfoList> userGrpInfoList = new ArrayList<>();
        try {
            final JsonArray userInfoArray = jsonObject.get(UserSessionInfoEntity.CONSTANT_USERGRPINFOLIST).getAsJsonArray();
            for (int i = 0; i < userInfoArray.size(); i++) {
                JsonElement jsonElement = userInfoArray.get(i);
                JsonObject tempJsonObject = jsonElement.getAsJsonObject();

                UserGrpInfoList temp = new UserGrpInfoList();
                String userGrpNameCn2 = tempJsonObject.get("userGrpNameCn").getAsString();
                String userGrpNameCn = tempJsonObject.getAsJsonObject().get("userGrpNameCn").getAsString();
                userGrpInfoList.add(temp);
            }
        } catch (Exception e) {
            LogUtils.d("getMessage" + e.getMessage());
        }

        userGrpInfoList.size();

        final UserSessionInfoEntity entity = new UserSessionInfoEntity();
        entity.setIdSession(CONSTANT_IDUSER);
        entity.setUserNickName(CONSTANT_USERNICKNAME);
        entity.setUserNameFull(CONSTANT_USERNAMEFULL);
        entity.setUserNameBrief(CONSTANT_USERNAMEBRIEF);
        return entity;
    }
}

