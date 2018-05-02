package com.aq.service.impl.customer;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.facade.dto.customer.AddOrUpdateGroupDTO;
import com.aq.facade.dto.customer.DeleteGroupDTO;
import com.aq.facade.dto.customer.QueryGroupDTO;
import com.aq.facade.entity.customer.CustomerManager;
import com.aq.facade.entity.customer.Group;
import com.aq.facade.enums.customer.GroupEnum;
import com.aq.facade.exception.customer.CustomerExceptionEnum;
import com.aq.facade.exception.permission.UserBizException;
import com.aq.facade.service.customer.IGroupService;
import com.aq.facade.vo.customer.QueryGroupVO;
import com.aq.mapper.customer.CustomerManagerMapper;
import com.aq.mapper.customer.GroupMapper;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 客户分组 service.impl
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
@Slf4j
@Service(version = "1.0.0")
public class GroupServiceImpl implements IGroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private CustomerManagerMapper customerManagerMapper;

    @Override
    public Result getGroupList(QueryGroupDTO queryGroupDTO) {
        log.info("根据条件查询分组入参：queryGroupDTO={}", JSON.toJSONString(queryGroupDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            List<QueryGroupVO> list = groupMapper.selectGroupList(queryGroupDTO);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, list, list.size());
        } catch (Exception e) {
            log.error("根据条件查询分组异常：e={}", e);
        }
        log.info("根据条件查询分组返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result addGroup(AddOrUpdateGroupDTO addOrUpdateGroupDTO) {
        log.info("新增分组入参：addOrUpdateGroupDTO={}", JSON.toJSONString(addOrUpdateGroupDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        //参数校验
        String message = addOrUpdateGroupDTO.validateForm();
        if (StringUtils.isNotBlank(message)) {
            result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
            result.setMessage(message);
            return result;
        }
        try {
            Group group = new Group();
            //校验数量-不超过10个
            group.setCreaterId(addOrUpdateGroupDTO.getManagerId());
            group.setIsDelete(GroupEnum.NO_DELETE.getStatus());
            int count = groupMapper.selectCount(group);
            if (count >= 10) {
                result.setMessage(CustomerExceptionEnum.GROUP_NUM_EXIST.getMsg());
                return result;
            }
            //校验分组名称是否重复
            group.setGroupName(addOrUpdateGroupDTO.getGroupName());
            Group queryGroup = groupMapper.selectOne(group);
            if (null != queryGroup) {
                result.setMessage(CustomerExceptionEnum.GROUP_NAME_EXIST.getMsg());
                return result;
            }
            group.setCreateTime(new Date());
            group.setIsDefault(GroupEnum.NO_DEFAULT.getStatus());
            groupMapper.insert(group);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("新增分组返回值：result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("新增分组异常：e={}", e);
            throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateGroup(AddOrUpdateGroupDTO addOrUpdateGroupDTO) {
        log.info("修改分组入参：addOrUpdateGroupDTO={}", JSON.toJSONString(addOrUpdateGroupDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        //参数校验
        String message = addOrUpdateGroupDTO.validateForm();
        if (StringUtils.isNotBlank(message)) {
            result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
            result.setMessage(message);
            return result;
        }
        try {
            //校验分组名称是否重复
            Group group = new Group();
            group.setGroupName(addOrUpdateGroupDTO.getGroupName());
            group.setCreaterId(addOrUpdateGroupDTO.getManagerId());
            group.setIsDelete(GroupEnum.NO_DELETE.getStatus());
            Group queryGroup = groupMapper.selectOne(group);
            if (null != queryGroup && !queryGroup.getId().equals(addOrUpdateGroupDTO.getId())) {
                result.setMessage(CustomerExceptionEnum.GROUP_NAME_EXIST.getMsg());
                return result;
            }
            if (null != queryGroup && GroupEnum.YES_DEFAULT.getStatus().equals(queryGroup.getIsDefault())) {
                result.setMessage(CustomerExceptionEnum.DEFAULT_NOT_UPDATE.getMsg());
                return result;
            }
            group.setId(addOrUpdateGroupDTO.getId());
            group.setCreaterId(null);
            group.setUpdaterId(addOrUpdateGroupDTO.getManagerId());
            group.setUpdateTime(new Date());
            groupMapper.updateByPrimaryKeySelective(group);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("修改分组返回值：result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("修改分组异常：e={}", e);
            throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteGroup(DeleteGroupDTO deleteGroupDTO) {
        log.info("删除分组入参：deleteGroupDTO={}", JSON.toJSONString(deleteGroupDTO));
        Result result;
        //参数校验
        String message = deleteGroupDTO.validateForm();
        if (StringUtils.isNotBlank(message)) {
            result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
            result.setMessage(message);
            return result;
        }
        try {
            //不能删除的分组名称
            List<String> list = new ArrayList<>();
            Integer managerId = deleteGroupDTO.getManagerId();
            CustomerManager customerManager;
            Group group;
            int count;
            for (Integer groupId : deleteGroupDTO.getList()) {
                //默认分组不能删除
                group = groupMapper.selectByPrimaryKey(groupId);
                if (GroupEnum.YES_DEFAULT.getStatus().equals(group.getIsDefault())) {
                    list.add(group.getGroupName());
                    continue;
                }
                customerManager = new CustomerManager();
                customerManager.setGroupId(groupId);
                customerManager.setManagerId(managerId);
                count = customerManagerMapper.selectCount(customerManager);
                //如果该分组存在客户，不能删除，返回分组名称
                if (count > 0) {
                    list.add(group.getGroupName());
                    continue;
                }
                group = new Group();
                group.setId(groupId);
                group.setIsDelete(GroupEnum.YES_DELETE.getStatus());
                group.setUpdateTime(new Date());
                group.setUpdaterId(managerId);
                groupMapper.updateByPrimaryKeySelective(group);
            }
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, list);
            log.info("删除分组返回值：result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("删除分组异常：e={}", e);
            throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }


}
