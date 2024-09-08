package cn.redcoral.messageplus.data.service.impl;

import cn.redcoral.messageplus.data.entity.po.UserBlacklistPo;
import cn.redcoral.messageplus.data.mapper.UserBlacklistMapper;
import cn.redcoral.messageplus.data.service.UserBlacklistService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mo
 **/
@Service
public class UserBlacklistServiceImpl implements UserBlacklistService {

    @Autowired
    private UserBlacklistMapper userBlacklistMapper;

    @Override
    public boolean black(String id1, String id2) {
        LambdaQueryWrapper<UserBlacklistPo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserBlacklistPo::getId1, id1).eq(UserBlacklistPo::getId2, id2);
        UserBlacklistPo userBlacklistPo = userBlacklistMapper.selectOne(lqw);
        // 已拉黑
        if (userBlacklistPo != null) return false;
        // 未拉黑
        else {
            userBlacklistPo = new UserBlacklistPo();
            userBlacklistPo.setId1(id1);
            userBlacklistPo.setId2(id2);
            return userBlacklistMapper.insert(userBlacklistPo)>=1;
        }
    }

    @Override
    public boolean noBlack(String id1, String id2) {
        LambdaQueryWrapper<UserBlacklistPo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserBlacklistPo::getId1, id1).eq(UserBlacklistPo::getId2, id2);
        return userBlacklistMapper.delete(lqw)>=1;
    }

    @Override
    public boolean whetherPulledBlack(String id1, String id2) {
        LambdaQueryWrapper<UserBlacklistPo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserBlacklistPo::getId1, id2).eq(UserBlacklistPo::getId2, id1);
        return userBlacklistMapper.selectCount(lqw)==1;
    }
}
