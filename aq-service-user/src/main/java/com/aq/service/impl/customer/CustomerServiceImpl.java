package com.aq.service.impl.customer;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.base.BaseValidate;
import com.aq.core.constant.AttentionWchatEnum;
import com.aq.core.constant.PageConstant;
import com.aq.core.constant.ToolCategoryEnum;
import com.aq.core.lock.RedisDistributionLock;
import com.aq.core.wechat.response.UserInfoResponse;
import com.aq.core.wechat.user.WheChatUserComponent;
import com.aq.facade.dto.AddAttentionRecordDTO;
import com.aq.facade.dto.SelectClientDTO;
import com.aq.facade.dto.UpdateClientDTO;
import com.aq.facade.dto.customer.*;
import com.aq.facade.dto.manage.SelectManagerDTO;
import com.aq.facade.dto.share.dto.LoginPasswordDTO;
import com.aq.facade.entity.AttentionWechatRecord;
import com.aq.facade.entity.StatisticsAttention;
import com.aq.facade.entity.customer.*;
import com.aq.facade.entity.manager.Manager;
import com.aq.facade.entity.permission.UserSecurity;
import com.aq.facade.enums.PwdOperTypeEnum;
import com.aq.facade.enums.customer.CustomerEnum;
import com.aq.facade.enums.customer.GroupEnum;
import com.aq.facade.enums.permission.SecurityBaseSettingEnum;
import com.aq.facade.exception.customer.CustomerExceptionEnum;
import com.aq.facade.exception.permission.UserBizException;
import com.aq.facade.exception.permission.UserExceptionEnum;
import com.aq.facade.service.customer.ICustomerService;
import com.aq.facade.vo.ClientDetailVO;
import com.aq.facade.vo.ClientListVO;
import com.aq.facade.vo.WechatBindFailVO;
import com.aq.facade.vo.customer.CustomerInfoVO;
import com.aq.facade.vo.customer.ImportCustomerVO;
import com.aq.facade.vo.customer.QueryCustomerVO;
import com.aq.facade.vo.statistics.CustomerStatisticsPreManagerListVO;
import com.aq.mapper.customer.*;
import com.aq.mapper.manager.ManagerMapper;
import com.aq.mapper.permission.UserSecurityMapper;
import com.aq.util.encrypt.SHA256Python;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.aq.util.string.StringTools;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * 客户service.impl
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
@Slf4j
@Service(version = "1.0.0")
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerManagerMapper customerManagerMapper;

    @Autowired
    private RedisDistributionLock distributionLock;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private ImportFailureRecordMapper importFailureRecordMapper;

    @Autowired
    private ToolbindnumMapper toolbindnumMapper;

    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private AttentionWechatRecordMapper attentionWechatRecordMapper;

    @Autowired
    private StatisticsAttentionMapper statisticsAttentionMapper;

    @Autowired
    private WheChatUserComponent wheChatUserComponent;

    @Autowired
    private UserSecurityMapper userSecurityMapper;

    @Override
    public Result getCustomerByPage(PageBean pageBean, QueryCustomerDTO queryCustomerDTO) {
        log.info("根据条件查询客户入参：pageBean={},queryCustomerDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(queryCustomerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            if (StringUtils.isBlank(queryCustomerDTO.getOrderType())) {
                queryCustomerDTO.setOrderType("desc");
            } else {
                queryCustomerDTO.setOrderType(queryCustomerDTO.getOrderType().toLowerCase());
            }

            if (null == queryCustomerDTO.getOrderByContent()) {
                queryCustomerDTO.setOrderByContent((byte) 1);
            }
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<QueryCustomerVO> list = customerMapper.selectCustomerList(queryCustomerDTO);
            PageInfo<QueryCustomerVO> pageInfo = new PageInfo<>(list);
            for (QueryCustomerVO queryCustomerVO : pageInfo.getList()) {
                queryCustomerVO.setIsBindWebChat(CustomerEnum.NO_BIND_WEB_CHAT.getStatus());
                if (StringUtils.isNotBlank(queryCustomerVO.getOpenId())) {
                    queryCustomerVO.setIsBindWebChat(CustomerEnum.YES_BIND_WEB_CHAT.getStatus());
                }
                if (null == queryCustomerVO.getStrategyNum()) {
                    queryCustomerVO.setStrategyNum(0);
                }
                if (null == queryCustomerVO.getToolNum()) {
                    queryCustomerVO.setToolNum(0);
                }
                if (null == queryCustomerVO.getAdviserNum()) {
                    queryCustomerVO.setAdviserNum(0);
                }
            }
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
        } catch (Exception e) {
            log.error("根据条件查询客户异常：e={}", e);
        }
        log.info("根据条件查询客户返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result bindCustomer(BindCustomerDTO bindCustomerDTO) {
        log.info("新增客户（绑定客户）入参, bindCustomerDTO={}", JSON.toJSONString(bindCustomerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        String message = bindCustomerDTO.validateForm();
        try {
            if (StringUtils.isNotBlank(message)) {
                result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
                result.setMessage(message);
                return result;
            }
            //校验手机号与姓名匹配
            Customer customer = new Customer();
            customer.setTelphone(bindCustomerDTO.getTelphone());
            customer.setRealName(bindCustomerDTO.getRealName());
            customer = customerMapper.selectOne(customer);
            if (null == customer) {
                result.setMessage("客户信息不一致");
                return result;
            }
            //查询是否存在数据
            CustomerManager customerManager = new CustomerManager();
            customerManager.setCustomerId(customer.getId());
            if (customerManagerMapper.selectCount(customerManager) > 0) {
                result.setMessage("此客户已被绑定");
                return result;
            }
            customerManager.setManagerId(bindCustomerDTO.getManagerId());
            customerManager.setGroupId(bindCustomerDTO.getGroupId());
            customerManager.setCreateTime(new Date());
            customerManagerMapper.insert(customerManager);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("新增客户（绑定客户）返回值：result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("新增客户（绑定客户）异常", e);
            throw new UserBizException(UserExceptionEnum.ADD_CUSTOM_ERROR);
        }
    }

    @Override
    public Result getToolCustomerByPage(PageBean pageBean, QueryCustomerDTO queryCustomerDTO) {
        log.info("根据条件查询客户入参：pageBean={},queryCustomerDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(queryCustomerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            List<QueryCustomerVO> list = new ArrayList<>();
            if (PageConstant.PAGE_NUM.intValue() == pageBean.getPageNum()) {
                pageBean.setPageSize(pageBean.getPageSize() - 1);
                QueryCustomerVO vo = new QueryCustomerVO();
                list.add(managerMapper.selectToolPushNum(queryCustomerDTO.getManagerId(), queryCustomerDTO.getToolCategoryCode()));
            }
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<QueryCustomerVO> listCustomer = customerMapper.selectCustomerListTool(queryCustomerDTO);
            PageInfo<QueryCustomerVO> pageInfo = new PageInfo<>(listCustomer);
            list.addAll(pageInfo.getList());
            for (QueryCustomerVO vo : list) {
                if (StringUtils.isNotBlank(vo.getRealName()) && StringUtils.isNotBlank(vo.getTelphone())
                        && vo.getTelphone().length() == 11) {
                    vo.setRealName(vo.getRealName() + vo.getTelphone().substring(7));
                }
            }
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, list, pageInfo.getTotal());
        } catch (Exception e) {
            log.error("根据条件查询客户异常：e={}", e);
        }
        log.info("根据条件查询客户返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result importCustomer(ImportCustomerDTO importCustomerDTO) {
        log.info("导入客户入参, importCustomerDTO={}", JSON.toJSONString(importCustomerDTO));
        Result result;
        String message = importCustomerDTO.validateForm();
        try {
            if (StringUtils.isNotBlank(message)) {
                result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
                result.setMessage(message);
                return result;
            }
            //参数校验
            Map<String, Object> map = getImportData(importCustomerDTO);
            List<ImportFailureRecord> failureRecords = (List<ImportFailureRecord>) map.get("failure");
            List<CustomerManager> customerManagers = (List<CustomerManager>) map.get("success");
            //删除原来导入失败的记录
            ImportFailureRecord importFailureRecord = new ImportFailureRecord();
            importFailureRecord.setCreaterId(importCustomerDTO.getManagerId());
            importFailureRecordMapper.delete(importFailureRecord);
            //新增导入失败的记录
            if (CollectionUtils.isNotEmpty(failureRecords)) {
                importFailureRecordMapper.insertList(failureRecords);
            }
            //新增导入数据
            if (CollectionUtils.isNotEmpty(customerManagers)) {
                customerManagerMapper.insertList(customerManagers);
            }
            ImportCustomerVO importCustomerVO = new ImportCustomerVO();
            importCustomerVO.setFailureNum(failureRecords.size());
            importCustomerVO.setSuccessNum(customerManagers.size());
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, importCustomerVO);
            log.info("导入客户返回值：result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("导入客户异常", e);
            throw new UserBizException(UserExceptionEnum.ADD_CUSTOM_ERROR);
        }
    }

    private Map<String, Object> getImportData(ImportCustomerDTO importCustomerDTO) throws InterruptedException {
        log.info("导入客户数据校验开始,time={}", System.currentTimeMillis());
        Map<String, Object> map = new HashMap<>(16);
        List<CustomerDTO> list = importCustomerDTO.getList();
        Integer managerId = importCustomerDTO.getManagerId();
        //查询默分组-我的好友
        Group group = new Group();
        group.setIsDelete(GroupEnum.NO_DELETE.getStatus());
        group.setIsDefault(GroupEnum.YES_DEFAULT.getStatus());
        group.setCreaterId(managerId);
        group = groupMapper.selectOne(group);
        //分组id
        Integer groupId = group.getId();
        //分组名称
        String groupName = group.getGroupName();
        String message, telphone;
        //失败的客户
        List<ImportFailureRecord> failureRecords = new ArrayList<>();
        //成功的客户
        List<CustomerManager> customerManagers = new ArrayList<>();
        CustomerManager customerManager;
        Customer customer;

        //TODO 查询导入客户信息
        Map<String, String> realName = new HashMap<>(16);
        Map<String, Integer> connectionMap = new HashMap<>(16);
        Map<String, Integer> customerMap = new HashMap<>(16);
        Map<String, Integer> needInsertMap = new HashMap<>(16);
        List<CustomerInfoVO> queryList = customerMapper.selectCustomerInfoList(list);
        for (CustomerInfoVO customerInfoVO : queryList) {
            realName.putIfAbsent(customerInfoVO.getTelphone(), customerInfoVO.getRealName());
            connectionMap.putIfAbsent(customerInfoVO.getTelphone(), customerInfoVO.getManagerId());
            customerMap.putIfAbsent(customerInfoVO.getTelphone(), customerInfoVO.getId());

        }
        //TODO 校验是否存在
        for (CustomerDTO customerDTO : list) {
            //校验参数是否
            message = customerDTO.validateForm();
            if (StringUtils.isNotBlank(message)) {
                failureRecords.add(getImportFailureRecord(customerDTO, groupName, message, managerId));
                continue;
            }
            //校验手机号或用户名是否存在
            telphone = customerDTO.getTelphone();
            if (realName.containsKey(telphone)) {
                //校验手机号与姓名匹配
                if (null == realName.get(telphone) || !customerDTO.getRealName().trim().equals(realName.get(telphone).trim())) {
                    message = "手机号与姓名不匹配";
                    failureRecords.add(getImportFailureRecord(customerDTO, groupName, message, managerId));
                    continue;
                }
                //校验是否存在
                if (null != connectionMap.get(telphone)) {
                    message = "此客户已被绑定";
                    failureRecords.add(getImportFailureRecord(customerDTO, groupName, message, managerId));
                    continue;
                }
                customerManager = new CustomerManager();
                customerManager.setCustomerId(customerMap.get(telphone));
                customerManager.setManagerId(managerId);
                customerManager.setGroupId(groupId);
                customerManager.setCreateTime(new Date());
                customerManagers.add(customerManager);
            } else {
                /**
                 * 用户名或者手机号不存在
                 *  1-注册客户
                 *  2-添加客户-客户经理关系
                 */
                if (needInsertMap.containsKey(telphone)) {
                    message = "此客户已被绑定";
                    failureRecords.add(getImportFailureRecord(customerDTO, groupName, message, managerId));
                    continue;
                }

                //新增客户
                int id = insert(managerId, telphone, customerDTO.getRealName());
                if (id == 0) {
                    message = "此客户已被绑定";
                    failureRecords.add(getImportFailureRecord(customerDTO, groupName, message, managerId));
                    continue;
                }
                needInsertMap.putIfAbsent(telphone, id);
                customerManager = new CustomerManager();
                customerManager.setCustomerId(id);
                customerManager.setManagerId(managerId);
                customerManager.setGroupId(groupId);
                customerManager.setCreateTime(new Date());
                customerManagers.add(customerManager);
            }
        }
        map.put("failure", failureRecords);
        map.put("success", customerManagers);
        log.info("导入客户数据校验结果，结束={},map={}", System.currentTimeMillis(), JSON.toJSONString(map));
        return map;
    }


    private Integer insert(Integer managerId, String telphone, String realName) {
        Integer id = 0;
        try {
            AddCustomerDTO addCustomerDTO = new AddCustomerDTO();
            addCustomerDTO.setCreaterId(managerId);
            addCustomerDTO.setTelphone(telphone);
            addCustomerDTO.setRealName(realName);
            //密码-手机号后6位
            addCustomerDTO.setPassword(telphone.substring(5));
            Customer customer = queryAddCustomer(addCustomerDTO);
            //注册客户
            customerMapper.insertUseGeneratedKeys(customer);
            //初始化客户工具推荐数
            initToolbindnum(managerId, customer.getId());
            id = customer.getId();
        } catch (Exception e) {
            log.error("新增客户失败，e={}", e);
        }
        return id;
    }

    private ImportFailureRecord getImportFailureRecord(CustomerDTO customerDTO, String groupName, String message, Integer managerId) {
        ImportFailureRecord importFailureRecord = new ImportFailureRecord();
        BeanUtils.copyProperties(customerDTO, importFailureRecord);
        importFailureRecord.setCreaterId(managerId);
        importFailureRecord.setReason(message);
        importFailureRecord.setCreateTime(new Date());
        importFailureRecord.setGroupName(groupName);
        return importFailureRecord;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateBindCustomer(UpdateBindCustomerDTO updateBindCustomerDTO) {
        log.info("修改客户入参, updateBindCustomerDTO={}", JSON.toJSONString(updateBindCustomerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        String message = updateBindCustomerDTO.validateForm();
        try {
            if (StringUtils.isNotBlank(message)) {
                result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
                result.setMessage(message);
                return result;
            }
            CustomerManager customerManager = new CustomerManager();
            customerManager.setCustomerId(updateBindCustomerDTO.getCustomerId());
            customerManager.setGroupId(updateBindCustomerDTO.getOldGroupId());
            customerManager.setManagerId(updateBindCustomerDTO.getManagerId());
            customerManagerMapper.delete(customerManager);
            customerManager.setGroupId(updateBindCustomerDTO.getNewGroupId());
            customerManager.setCreateTime(new Date());
            customerManagerMapper.insert(customerManager);
            ResultUtil.setResult(result, RespCode.Code.SUCCESS);
            log.info("修改客户返回值：result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("修改客户异常", e);
            throw new UserBizException(UserExceptionEnum.ADD_CUSTOM_ERROR);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result registerCustomer(AddCustomerDTO dto) {
        log.info("注册客户入参={}", JSON.toJSONString(dto));
        final Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        Customer customer;
        try {
            String errorStr = dto.validateForm();
            if (StringUtils.isNotBlank(errorStr)) {
                result.setMessage(errorStr);
                return result;
            }

            String lockKey = dto.getTelphone();
            distributionLock.lock(lockKey);
            try {
                errorStr = checkAddCustomerParam(dto);
                if (StringUtils.isNotBlank(errorStr)) {
                    result.setMessage(errorStr);
                    return result;
                }
                // 查询客户经理信息
                Manager qrecord = null;
                // 查询客户经理分组
                Group grecord = null;
                // 根据客户经理ID CODE 查询客户经理信息
                if (null != dto.getIdCode()) {
                    qrecord = queryManagerByIdCode(dto.getIdCode());
                    if (null == qrecord) {
                        result.setMessage("客户经理不存在");
                        return result;
                    }
                    grecord = queryDefaultGroup(qrecord.getId());
                    if (null == grecord) {
                        result.setMessage("客户经理分组信息不存在");
                        return result;
                    }
                }
                customer = queryAddCustomer(dto);
                customerMapper.insertUseGeneratedKeys(customer);
                // 关联客户经理信息
                if (null != qrecord && null != grecord) {
                    // 新增客户经理客户绑定关系
                    CustomerManager customerManager = new CustomerManager();
                    customerManager.setManagerId(qrecord.getId());
                    customerManager.setGroupId(grecord.getId());
                    customerManager.setCreateTime(new Date());
                    customerManager.setCustomerId(customer.getId());
                    customerManagerMapper.insert(customerManager);
                }
            } finally {
                distributionLock.unLock(lockKey);
            }
            if (null != customer) {
                // 初始化客户工具数据信息
                initToolbindnum(dto.getCreaterId(), customer.getId());
            }
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("注册客户异常", e);
            throw new UserBizException(UserExceptionEnum.ADD_CUSTOM_ERROR);
        }
    }

    /**
     * @param @param  idCode
     * @param @return 参数
     * @return Manager    返回类型
     * @throws
     * @Title: queryManagerByIdCode
     * @Description: 查询客户经理
     */
    private Manager queryManagerByIdCode(Integer idCode) {
        Manager qrecord = new Manager();
        qrecord.setIdCode(idCode);
        qrecord = managerMapper.selectOne(qrecord);
        return qrecord;
    }

    /**
     * @param @param  managerId
     * @param @return 参数
     * @return Group    返回类型
     * @throws
     * @Title: queryDefaultGroup
     * @Description: 查询客户经理默认分组
     */
    private Group queryDefaultGroup(Integer managerId) {
        Group grecord = new Group();
        grecord.setCreaterId(managerId);
        grecord.setIsDefault(GroupEnum.YES_DEFAULT.getStatus());
        grecord = groupMapper.selectOne(grecord);
        return grecord;
    }

    /**
     * @param @param createrId
     * @param @param num
     * @param @param toolCode
     * @param @param customerId    参数
     * @return void    返回类型
     * @throws
     * @Title: initToolbindnum
     * @Description: 初始化数据
     */
    private void initToolbindnum(Integer createrId, Integer customerId) {
        List<Toolbindnum> inserts = new ArrayList<>();
        ToolCategoryEnum[] types = ToolCategoryEnum.values();
        Toolbindnum insert;
        for (ToolCategoryEnum type : types) {
            insert = new Toolbindnum();
            insert.setCreaterId(createrId);
            insert.setCreateTime(new Date());
            insert.setCustomerId(customerId);
            // TODO:此处需要优化，需求未定
            insert.setNum(2);
            insert.setToolCode(type.getCode());
            inserts.add(insert);
        }
        if (!inserts.isEmpty()) {
            toolbindnumMapper.insertList(inserts);
        }
    }

    /**
     * @param dto
     * @return
     * @throws InterruptedException
     * @Title: checkCustomerExists
     * @author: lijie
     * @Description: 校验唯一数据
     * @return: String
     */
    private String checkAddCustomerParam(AddCustomerDTO dto) {
        String result = null;
        Customer check = new Customer();
        check.setTelphone(dto.getTelphone());
        check.setUsername(dto.getUsername());
        check.setOpenId(dto.getOpenId());
        List<Customer> list = customerMapper.checkCustomerExists(check);
        if (null != list && !list.isEmpty()) {
            for (Customer c : list) {
                if (StringUtils.isNotBlank(c.getOpenId())) {
                    result = "微信已被其它账户绑定";
                    break;
                }
                if (StringUtils.isNotBlank(c.getUsername())) {
                    result = "用户名已存在";
                    break;
                }
                if (StringUtils.isNotBlank(c.getTelphone())) {
                    result = "手机号已被注册";
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @param dto
     * @return
     * @Title: queryAddCustomer
     * @author: lijie
     * @Description: 新增或则注册客户数据
     * @return: Customer
     */
    private Customer queryAddCustomer(AddCustomerDTO dto) {
        Customer customer = new Customer();
        customer.setUsername(dto.getUsername());
        customer.setCreaterId(dto.getCreaterId());
        customer.setCreateTime(new Date());
        customer.setIsVIP(CustomerEnum.NOT_VIP.getStatus());
        customer.setRealName(dto.getRealName());
        customer.setTelphone(dto.getTelphone());
        customer.setPassword(SHA256Python.encode(dto.getPassword()));
        return customer;
    }

	@Override
	@Transactional
	public Result customerLogin(CustomerLoginDTO dto) {
		log.info("客户登录入参={}", JSON.toJSONString(dto));
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			String errorStr = dto.validateForm();
			if (StringUtils.isNotBlank(errorStr)) {
				result.setMessage(errorStr);
			} else {
				Customer check = new Customer();
				check.setTelphone(dto.getAccount());
				check.setUsername(dto.getAccount());
				List<Customer> list = customerMapper.checkCustomerExists(check);
				if (null == list || list.isEmpty()) {
					result.setMessage("账号不存在");
					return result;
				}
				if (list.size() > 1) {
					result.setMessage("客户信息异常");
					return result;
				}
				Customer info = list.get(0);
				if (!SHA256Python.checkPassword(dto.getPassword(), info.getPassword())) {
					result.setMessage("用户名或密码错误");
					return result;
				}
				Customer updateRecord = new Customer();
				updateRecord.setId(info.getId());
				updateRecord.setUpdaterId(info.getId());
				Date date = new Date();
				updateRecord.setUpdateTime(date);
				updateRecord.setLoginTime(date);
				// 修改客户数据
				updateCustomer(updateRecord);
				return ResultUtil.setResult(result, RespCode.Code.SUCCESS, info.getTelphone());
			}
		} catch (Exception e) {
			log.error("客户登录异常", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

    private void updateCustomer(Customer record) {

        customerMapper.updateByPrimaryKeySelective(record);
    }


    @Override
    public Result getCustomerByOpenId(String openId) {
        log.info("根据微信openId得到客户信息入参={}", openId);
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (StringUtils.isNotBlank(openId)) {
                CustomerInfoVO info = customerMapper.selectCustomerInfoByopenId(openId);
                if (null != info) {
                    return ResultUtil.setResult(result, RespCode.Code.SUCCESS, info);
                } else {
                    result.setMessage("根据openId未查询到客户信息");
                }
            }
        } catch (Exception e) {
            log.error("根据微信openId得到客户信息异常", e);
        }
        return result;
    }

    @Override
    public Result getCustomerByAccount(String account) {
        log.info("根据账号（手机号/用户名）获取客户信息入参={}", account);
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            Customer check = new Customer();
            check.setTelphone(account);
            check.setUsername(account);
            List<Customer> list = customerMapper.checkCustomerExists(check);
            if (null != list && !list.isEmpty()) {
                if (list.size() > 1) {
                    log.info("客户数据存在多条，请检查数据");
                    result.setMessage("客户数据异常");
                } else {
                    return ResultUtil.setResult(result, RespCode.Code.SUCCESS, list.get(0));
                }
            }
        } catch (Exception e) {
            log.error("根据手机号查询客户异常", e);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateCustomerLoginPwd(LoginPasswordDTO dto) {
        log.info("客户修改/找回密码入参={}", JSON.toJSONString(dto));
        try {
            final Result result = checkCustomer(dto, dto.getTelphone());
            if (!result.isSuccess()) {
                return result;
            }
            Customer record = (Customer) result.getData();
            PwdOperTypeEnum type = dto.getType();
            Customer update = new Customer();
            update.setPassword(SHA256Python.encode(dto.getNewPassword()));
            if (null == dto.getUpdaterId()) {
                update.setUpdaterId(record.getId());
            } else {
                update.setUpdaterId(dto.getUpdaterId());
            }
            update.setUpdateTime(new Date());
            switch (type) {
                case BACK:
                    customerMapper.updateByPrimaryKeySelective(update);
                    break;
                case UPDATE:
                    if (!SHA256Python.checkPassword(dto.getPassword(), record.getPassword())) {
                        ResultUtil.setResult(result, RespCode.Code.FAIL);
                        result.setMessage("旧密码错误");
                        return result;
                    }
                    customerMapper.updateByPrimaryKeySelective(update);
                    break;
                default:
                    break;
            }
            return ResultUtil.getResult(RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("修改/找回登录密码异常", e);
            throw new UserBizException(CustomerExceptionEnum.UPDATE_LOGINPWD_ERROR);
        }
    }

    /**
     * @param @param  dto
     * @param @param  telphone
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: checkCustomer
     * @Description: 校验客户
     */
    private Result checkCustomer(BaseValidate dto, String telphone) {
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        String errorStr = dto.validateForm();
        if (StringUtils.isNotBlank(errorStr)) {
            result.setMessage(errorStr);
            return result;
        }
        Customer record = new Customer();
        record.setTelphone(telphone);
        record = customerMapper.selectOne(record);
        if (null == record) {
            result.setMessage("客户不存在");
            return result;
        }
        return ResultUtil.setResult(result, RespCode.Code.SUCCESS, record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateCustomer(UpdateCustomerDTO dto) {
        log.info("修改客户入参={}", JSON.toJSONString(dto));
        final Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        try {
            String errorStr = dto.validateForm();
            if (StringUtils.isNotBlank(errorStr)) {
                result.setMessage(errorStr);
                return result;
            }
            Customer update = new Customer();
            BeanUtils.copyProperties(dto, update);
            customerMapper.updateByPrimaryKeySelective(update);
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("修改客户异常", e);
            throw new UserBizException(CustomerExceptionEnum.UPDATE_CUSTOMER_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateCustomerOpenId(String account, String openId) {
        log.info("修改客户微信openId入参account={},openId={}", account, openId);
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (StringUtils.isBlank(account)) {
                result.setMessage("用户名或则手机号不能为空");
                return result;
            }
            if (StringUtils.isBlank(openId)) {
                result.setMessage("微信openId不能为空");
                return result;
            }
            String lockKey = "customer_" + openId;
            distributionLock.lock(lockKey);
            try {
                // 校验opnenID如果是自己绑定则直接返回成功
                if (isBindOwn(account, openId)) {
                    return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
                }
                // 校验账号是否已经绑定过/绑定则不能绑定
                String bindOpenId = isBindAccount(account);
                if (StringUtils.isNotBlank(bindOpenId)) {
                    // 如果微信标识绑定了其它账户并校验是否关注
                    UserInfoResponse ui = wheChatUserComponent.getUserInfo(bindOpenId);
                    // 如果为关注则取消之前的openId
                    if (null != ui && !ui.isSubscribe()) {
                        // 修改账户的openId为空
                        customerMapper.updateOpenIdIsNull(account);
                    } else {
                        WechatBindFailVO vo = new WechatBindFailVO();
                        vo.setTelphone(StringTools.telphoneChange(account));
                        vo.setNickname(null == ui ? "" : ui.getNickname());
                        return ResultUtil.setResult(result, CustomerExceptionEnum.ACCOUNT_ALREADY_BIND, vo);
                    }
                }
                // 校验openId是否被其它绑定
                String telphone = isBind(openId);
                if (StringUtils.isNotBlank(telphone)) {
                    return ResultUtil.setResult(result, CustomerExceptionEnum.OPENID_EXISTS,
                            StringTools.telphoneChange(telphone));
                }
                Customer check = new Customer();
                check.setTelphone(account);
                check.setUsername(account);
                List<Customer> list = customerMapper.checkCustomerExists(check);
                if (null == list || list.isEmpty()) {
                    result.setMessage("客户数据不存在");
                    return result;
                }
                if (list.size() > 1) {
                    result.setMessage("客户数据异常");
                    return result;
                }
                Customer customer = list.get(0);
                Customer update = new Customer();
                update.setId(customer.getId());
                update.setOpenId(openId);
                update.setUpdaterId(customer.getId());
                update.setUpdateTime(new Date());
                customerMapper.updateByPrimaryKeySelective(update);

                return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
            } finally {
                distributionLock.unLock(lockKey);
            }
        } catch (Exception e) {
            log.error("修改客户微信openId异常", e);
            throw new UserBizException(CustomerExceptionEnum.UPDATE_CUSTOMER_OPENID_ERROR);
        }
    }

    /**
     * @param @param  telphone
     * @param @param  openId
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: isBindOwn
     * @Description:校验是否被自己绑定
     */
    private boolean isBindOwn(final String telphone, final String openId) {
        Customer selectOne = new Customer();
        selectOne.setTelphone(telphone);
        selectOne.setOpenId(openId);
        selectOne = customerMapper.selectOne(selectOne);
        return null != selectOne;
    }

    /**
     * @return boolean
     * @throws
     * @Title: isBindAccount
     * @Description: 校验账号是否已经绑定过微信
     * @param: @param telphone
     * @param: @return
     * @author lijie
     */
    private String isBindAccount(final String telphone) {
        Customer selectOne = new Customer();
        selectOne.setTelphone(telphone);
        selectOne = customerMapper.selectOne(selectOne);
        return null == selectOne ? null : selectOne.getOpenId();
    }

    /**
     * @param @param  openId
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: isBind
     * @Description: 校验openID是否被绑定
     */
    private String isBind(final String openId) {
        String result = null;
        Customer select = new Customer();
        select.setOpenId(openId);
        List<Customer> selects = customerMapper.select(select);
        if (CollectionUtils.isNotEmpty(selects)) {
            result = selects.get(0).getTelphone();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result timingUpdateCustomerVip() {
        log.info("修改vip到期状态start");
        List<Customer> list = customerMapper.selectExpireTime();
        if (null != list && !list.isEmpty()) {
            List<Integer> customerIds = new ArrayList<>();
            for (Customer c : list) {
                customerIds.add(c.getId());
            }
            // TODO:修改数量
            toolbindnumMapper.updateToolBindNumByCusId(customerIds, 2);
        }
        return ResultUtil.getResult(RespCode.Code.SUCCESS, customerMapper.updateVipExpire());
    }

	@Override
	@Transactional
	public Result checkOpenId(String openId) {
		log.info("校验 openId 是否被注册入参={}", openId);
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL, false);
		try {
			Customer check = new Customer();
			check.setOpenId(openId);
			check = customerMapper.selectOne(check);
			if (null != check) {
				Customer updateRecord = new Customer();
				updateRecord.setId(check.getId());
				updateRecord.setLoginTime(new Date());
				// 修改数据
				updateCustomer(updateRecord);
				return ResultUtil.setResult(result, RespCode.Code.SUCCESS, true);
			}
		} catch (Exception e) {
			log.error("校验 openId 是否被注册异常", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

    @Override
    public Result getAllOpend() {
        List<String> rlist = new ArrayList<>();
        List<Customer> list = customerMapper.selectAll();
        if (null != list && !list.isEmpty()) {
            for (Customer c : list) {
                if (StringUtils.isNotBlank(c.getOpenId())) {
                    rlist.add(c.getOpenId());
                }
            }
        }
        return ResultUtil.getResult(RespCode.Code.SUCCESS, rlist);
    }

    /**
     * @Creater: zhangxia
     * @description：后台翻页获取客户列表
     * @methodName: getClientListByPage
     * @params: [pageBean, selectClientDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 16:48 2018-2-28
     */
    @Override
    public Result getCustomerListByPage(PageBean pageBean, SelectClientDTO selectClientDTO) {

        log.info("获取客户列表入参参数pageBean={},selectClientDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(selectClientDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {

            UserSecurity security = new UserSecurity();
            security.setUserId(selectClientDTO.getUserId());
            security.setSecurityId(SecurityBaseSettingEnum.ALL_USER_DATA.getId());
            //查看是否有查看所有用户权限
            if (userSecurityMapper.selectCount(security) > 0) {
                selectClientDTO.setUserId(null);
            }
            if (pageBean != null) {
                pageBean.setPageSize(pageBean.getPageSize() == 0 ? PageConstant.PAGE_SIZE : pageBean.getPageSize());
                pageBean.setPageNum(pageBean.getPageNum() == 0 ? PageConstant.PAGE_NUM : pageBean.getPageNum());
                PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            }
            List<ClientListVO> clientListVOS = customerMapper.getClientListByPage(selectClientDTO);
            security.setSecurityId(SecurityBaseSettingEnum.CUSTOMER_TEL_PHONE.getId());

            if (!(userSecurityMapper.selectCount(security) > 0)) {
                //不是超级管理员需要隐藏电话号码
                for (ClientListVO clientListVO :
                        clientListVOS) {
                    clientListVO.setTelphone(StringTools.telphoneChange(clientListVO.getTelphone()));
                }
            }
            PageInfo<ClientListVO> pageInfo = new PageInfo<>(clientListVOS);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
        } catch (Exception e) {
            log.info("查询异常e={}", e);
            result.setMessage("查询异常");
        }
        log.info("获取客户列表处理结果result={}", JSON.toJSONString(result));
        return result;
    }


    /**
     * @Creater: zhangxia
     * @description：后台获取客户详情信息
     * @methodName: getCustomerDetail
     * @params: [selectClientDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 10:20 2018-3-1
     */
    @Override
    public Result getCustomerDetail(SelectClientDTO selectClientDTO) {

        log.info("获取客户详情信息入参参数selectClientDTO = {}", JSON.toJSONString(selectClientDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            ClientDetailVO clientDetailVO = customerMapper.getClientDetail(selectClientDTO);
            UserSecurity security = new UserSecurity();
            security.setSecurityId(SecurityBaseSettingEnum.CUSTOMER_TEL_PHONE.getId());
            security.setUserId(selectClientDTO.getUserId());
            if (!(userSecurityMapper.selectCount(security) > 0)) {
                clientDetailVO.setTelphone(StringTools.telphoneChange(clientDetailVO.getTelphone()));
            }
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, clientDetailVO);
        } catch (Exception e) {
            log.info("查询客户详情异常e={}", e);
        }
        log.info("获取客户详情信息处理结果 = {}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param updateClientDTO
     * @author: 张霞
     * @description：客户编辑信息
     * @createDate: 11:13 2018/1/21
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @Transactional
    @Override
    public Result editClient(UpdateClientDTO updateClientDTO) {
        log.info("客户编辑信息入参参数updateClientDTO = {}", JSON.toJSONString(updateClientDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            //第一步：更新用户信息
            Customer customer = customerMapper.selectByPrimaryKey(updateClientDTO.getId());
            if (Objects.isNull(customer)) {
                log.info("编辑客户时，非法操作，没有相关用户存在authUser表中");
                result = ResultUtil.getResult(UserExceptionEnum.SELECT_DATA_NOT_EXIST);
                return result;
            }
            customer.setAssets(updateClientDTO.getAssets());
            customer.setRealName(updateClientDTO.getRealName());
            customer.setRemark(updateClientDTO.getRemark());

            if (customerMapper.updateByPrimaryKeySelective(customer) > 0) {
                result = updateCustomerManagerRelation(updateClientDTO);
            } else {
                log.info("编辑客户信息时，更新用户资金的时候失败");
            }
        } catch (Exception e) {
            log.info("客户编辑service处理异常，e={}", e);
            throw new UserBizException(UserExceptionEnum.UPDATE_USER_EXCEPTION);
        }
        log.info("客户编辑service处理结果result={}", JSON.toJSONString(result));
        return result;
    }


    /**
     * @param updateClientDTO
     * @author: 张霞
     * @description：客户重置密码
     * @createDate: 14:55 2018/1/22
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @Override
    public Result resetPWD(UpdateClientDTO updateClientDTO) {
        log.info("客户重置密码入参参数updateClientDTO={}", JSON.toJSONString(updateClientDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            Customer customer = customerMapper.selectByPrimaryKey(updateClientDTO.getId());
            if (Objects.isNull(customer)) {
                result = ResultUtil.getResult(UserExceptionEnum.SELECT_DATA_NOT_EXIST);
            } else {
                customer.setPassword(SHA256Python.encode(StringUtils.substring(customer.getTelphone(), updateClientDTO.getTelphone().length() - 6)));
                if (customerMapper.updateByPrimaryKeySelective(customer) > 0) {
                    result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                } else {
                    log.info("客户重置密码失败");
                    result.setMessage("客户重置密码失败");
                }
            }

        } catch (Exception e) {
            log.info("客户重置密码异常e={}", e);
        }
        log.info("客户重置密码处理结果result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @author: zhangxia
     * @desc: 定时记录每天微信的关注客户数据
     * @params: [addAttentionRecordDTO]
     * @methodName:recordAttention
     * @date: 2018/3/6 0006 下午 16:01
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result recordAttention(AddAttentionRecordDTO addAttentionRecordDTO) {

        log.info("记录客户关注公众号的记录service入参参数addAttentionRecordDTO={}", JSON.toJSONString(addAttentionRecordDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        List<AttentionWechatRecord> list = new ArrayList<>();
        try {
            for (String openId :
                    addAttentionRecordDTO.getData().getOpenid()) {
                AttentionWechatRecord attentionWechatRecord = new AttentionWechatRecord();
                attentionWechatRecord.setCreateTime(new Date());
                attentionWechatRecord.setOpenId(openId);
                list.add(attentionWechatRecord);
                if (list.size() >= addAttentionRecordDTO.getData().getOpenid().size() / 2) {
                    if (attentionWechatRecordMapper.insertList(list) > 0) {
                        log.info("批量插入关注数量成功list={}", JSON.toJSONString(list));
                        list.clear();
                    }
                }
            }
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.info("批量插入关注记录异常e={}", e);
        }
        log.info("批量插入关注记录处理结果result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @author: zhangxia
     * @desc:定时器每天更新微信关注客户状态
     * @params: []
     * @methodName:updateCustomerAttention
     * @date: 2018/3/6 0006 下午 12:45
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result updateCustomerAttention() {
        log.info("定时器每天更新微信关注客户状态");
        int cancelNum = 0;
        int againNum = 0;
        try {
            cancelNum = customerManagerMapper.cancelAttentionUpdate();
            againNum = customerManagerMapper.againAttentionUpdate();
            log.info("通过openid比对更新取消关注数量cancelNum={}", cancelNum);
            log.info("通过openid比对更新再次关注数量againNum={}", againNum);
        } catch (Exception e) {
            log.info("通过openid比对更新关注处理异常e={}", e);
        }
        return ResultUtil.getResult(RespCode.Code.SUCCESS);
    }

    /**
     * @author: zhangxia
     * @desc: 每天定时统计每个客户经理下客户昨天的关注微信情况记录
     * @params: []
     * @methodName:statisticsAttentionRecord
     * @date: 2018/3/6 0006 下午 16:54
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result statisticsAttentionRecord() {
        log.info("每天定时统计每个客户经理下客户昨天的关注微信情况记录开始={}", JSON.toJSONString(new Date()));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            List<CustomerStatisticsPreManagerListVO> customerStatisticsPreManagerListVOS = customerManagerMapper.getCustomerStatisticsOfManagerList(new SelectManagerDTO());
            List<StatisticsAttention> statisticsAttentions = new ArrayList<>();
            for (CustomerStatisticsPreManagerListVO manager :
                    customerStatisticsPreManagerListVOS) {
                StatisticsAttention statisticsAttention = new StatisticsAttention();
                statisticsAttention.setAttentionNum(manager.getOpenNum());
                statisticsAttention.setCancelAttentionNum(manager.getCancelOpenNum());
                statisticsAttention.setCreateTime(new Date());
                statisticsAttention.setManagerId(manager.getId());
                statisticsAttentions.add(statisticsAttention);
            }
            log.info("需要插入记录条数statisticsAttentions.size()={}", statisticsAttentions.size());
            if (statisticsAttentions.size() > 0) {
                int num = statisticsAttentionMapper.insertList(statisticsAttentions);
                log.info("插入记录成功条数num={}", num);
                result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.info("插入记录条数处理异常e={}", e);
        }
        log.info("每天定时统计每个客户经理下客户昨天的关注微信情况记录处理结果result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param updateClientDTO
     * @author: 张霞
     * @description：更新客户和客户经理关联表
     * @createDate: 19:14 2018/1/21
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @Transactional
    private Result updateCustomerManagerRelation(UpdateClientDTO updateClientDTO) {
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        //基本信息成功，然后更新关联关系表
        //1、查询客户经理默认分组id
        Group group = new Group();
        group.setIsDefault(GroupEnum.YES_DEFAULT.getStatus());
        group.setCreaterId(updateClientDTO.getManagerId());
        try {
            group = groupMapper.selectOne(group);
            if (Objects.isNull(group)) {
                log.info("获取客户经理分组没有数据,数据不完善有误");
                throw new RuntimeException("获取客户经理分组没有数据,数据不完善有误");
            }
        } catch (Exception e) {
            log.info("获取客户经理分组异常e={}", e);
            throw new RuntimeException("编辑客户信息时，获取客户经理分组异常");

        }
        CustomerManager customerManager = new CustomerManager();
        customerManager.setCustomerId(updateClientDTO.getId());
        List<CustomerManager> customerManagers = customerManagerMapper.select(customerManager);
        if (customerManagers.size() == 0) {
            if (Objects.nonNull(updateClientDTO.getManagerId())) {
                //没有关联关系，新添加关联关系
                log.info("需要新添加客户和客户经理之间关联关系，客户id={}，客户经理id={}", updateClientDTO.getId(), updateClientDTO.getManagerId());
                customerManager = new CustomerManager();
                customerManager.setCustomerId(updateClientDTO.getId());
                customerManager.setManagerId(updateClientDTO.getManagerId());
                customerManager.setCreateTime(new Date());
                customerManager.setGroupId(group.getId());
                if (customerManagerMapper.insert(customerManager) > 0) {
                    log.info("添加客户和客户经理之间关联关系成功");
                    result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                } else {
                    log.info("添加客户和客户经理之间关联关系失败");
                    throw new UserBizException(UserExceptionEnum.UPDATE_CLIENT_ADD_RELATION_FAIL);
                }
            } else {
                result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            }
        } else {
            log.info("客户和客户经理有关系数据managerUserRelation={}", JSON.toJSONString(customerManager));
            if (customerManagers.get(0).getManagerId().intValue() == updateClientDTO.getManagerId().intValue()) {
                //没有更改关联关系
                log.info("没有更改关联关系");
                result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            } else {
                //更换了关联关系
                //1、删除关联关系
                CustomerManager cm = new CustomerManager();
                cm.setCustomerId(updateClientDTO.getId());
                if (customerManagerMapper.delete(cm) > 0) {
                    log.info("删除客户和客户经理关联关系成功");
                } else {
                    log.info("删除客户和客户经理关联关系失败");
                    throw new RuntimeException("编辑客户信息时，需要更改客户和客户经理关联关系时，删除原先关系失败");
                }
                //2、添加新的关联关系
                customerManager.setGroupId(group.getId());
                customerManager.setCreateTime(new Date());
                customerManager.setManagerId(updateClientDTO.getManagerId());
                if (customerManagerMapper.insert(customerManager) > 0) {
                    log.info("添加新的关联关系成功，customerManager={}", JSON.toJSONString(customerManager));
                    result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                } else {
                    log.info("添加新的关联关系失败");
                    throw new RuntimeException("编辑客户信息时，更新客户和客户经理关联关系表失败");
                }

            }
            log.info("更新客户和客户经理关联表处理结果result={}", JSON.toJSONString(result));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateWeChatAttention(String openId, Byte status) {
        log.info("操作微信是否关注入参openId={}", openId, status);
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (StringUtils.isBlank(openId)) {
                result.setMessage("openId不能为空");
                return result;
            }
            if (null == status) {
                result.setMessage("修改状态不能为空");
                return result;
            }
            Customer select = new Customer();
            select.setOpenId(openId);
            Customer selectOne = customerMapper.selectOne(select);
            if (null == selectOne) {
                result.setMessage("当前openId未绑定不需要更新关注状态");
                return result;
            }
            Customer update = new Customer();
            update.setId(selectOne.getId());
            update.setIsCancelAttention(status);
            if (AttentionWchatEnum.NOT_ATTENTION_WCHAT_ENUM.getCode().equals(status)) {
                update.setCancelAttentionTime(new Date());
            } else {
                update.setCreateAttentionTime(new Date());
            }
            customerMapper.updateByPrimaryKeySelective(update);
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("申请工具异常", e);
            throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }
}
