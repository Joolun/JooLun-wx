package com.joolun.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joolun.mall.entity.MemberPointLog;
import com.joolun.mall.mapper.MemberPointLogMapper;
import com.joolun.mall.service.MemberPointLogService;
import org.springframework.stereotype.Service;

@Service
public class MemberPointLogServiceImpl extends ServiceImpl<MemberPointLogMapper, MemberPointLog>
		implements MemberPointLogService {
}
