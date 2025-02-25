package org.example.meetlearning.converter;

import org.example.meetlearning.dao.entity.User;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.util.MD5Util;
import org.example.meetlearning.vo.user.UserInfoRespVo;
import org.example.meetlearning.vo.user.UserManageOperaReqVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.UUID;

@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    default User toCreateManage(UserManageOperaReqVo reqVo) {
        User user = new User();
        user.setDeleted(false);
        user.setIsManager(true);
        user.setCreator("SYSTEM");
        user.setCreateName("SYSTEM");
        user.setCreateTime(new Date());
        user.setRecordId(UUID.randomUUID().toString());
        user.setAccountCode(reqVo.getAccountCode());
        user.setPassword(MD5Util.md5("MD5", reqVo.getPassword()));
        user.setType(RoleEnum.MANAGER.name());
        user.setName(reqVo.getName());
        user.setEnName(reqVo.getEnName());
        user.setEmail(reqVo.getEmail());
        user.setEnabled(true);
        user.setRemark("后台系统添加");
        return user;
    }

    default UserInfoRespVo toUserInfoRespVo(User user) {
        UserInfoRespVo respVo = new UserInfoRespVo();
        respVo.setRecordId(user.getRecordId());
        respVo.setName(user.getName());
        respVo.setEnName(user.getEnName());
        respVo.setAvatarUrl("");
        respVo.setEmail(user.getEmail());
        RoleEnum roleEnum = RoleEnum.valueOf(user.getType());
        respVo.setMenus(roleEnum.getMenus());
        return respVo;
    }


}
