package com.aq.service.impl.manager;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.base.BaseValidate;
import com.aq.core.constant.AttentionWchatEnum;
import com.aq.core.constant.PageConstant;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.lock.RedisDistributionLock;
import com.aq.core.wechat.response.UserInfoResponse;
import com.aq.core.wechat.user.WheChatUserComponent;
import com.aq.facade.dto.UpdateManagerDTO;
import com.aq.facade.dto.manage.*;
import com.aq.facade.dto.share.dto.LoginPasswordDTO;
import com.aq.facade.entity.UserManager;
import com.aq.facade.entity.customer.Group;
import com.aq.facade.entity.manager.Balance;
import com.aq.facade.entity.manager.Manager;
import com.aq.facade.entity.permission.UserSecurity;
import com.aq.facade.enums.PwdOperTypeEnum;
import com.aq.facade.enums.customer.GroupEnum;
import com.aq.facade.enums.manager.ManagerEnum;
import com.aq.facade.enums.permission.SecurityBaseSettingEnum;
import com.aq.facade.exception.manage.ManageExceptionEnum;
import com.aq.facade.exception.permission.UserBizException;
import com.aq.facade.exception.permission.UserExceptionEnum;
import com.aq.facade.service.customer.ICustomerManageService;
import com.aq.facade.vo.ManagerDetailVO;
import com.aq.facade.vo.WechatBindFailVO;
import com.aq.facade.vo.manage.ManageInfoVO;
import com.aq.facade.vo.manage.ManagerListVO;
import com.aq.mapper.UserManagerMapper;
import com.aq.mapper.customer.GroupMapper;
import com.aq.mapper.manager.BalanceMapper;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: CustomerManageServiceImpl
 * @Description: 客户经理服务
 * @author: lijie
 * @date: 2018年2月9日 下午2:12:48
 */
@Slf4j
@Service(version = "1.0.0")
public class CustomerManagerServiceImpl implements ICustomerManageService {

    @Autowired
    private ManagerMapper managerMapper;

    @Autowired
    private BalanceMapper balanceMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private RedisDistributionLock distributionLock;

    @Autowired
    private UserManagerMapper userManagerMapper;

    @Autowired
    private WheChatUserComponent wheChatUserComponent;

    @Autowired
    private UserSecurityMapper userSecurityMapper;

	@Override
	public Result login(LoginDTO dto) {
		log.info("客户经理登录入参={}", JSON.toJSONString(dto));
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			String errorStr = dto.validateForm();
			if (StringUtils.isNotBlank(errorStr)) {
				result.setMessage(errorStr);
			} else {
				Manager check = new Manager();
				check.setTelphone(dto.getAccount());
				check.setUsername(dto.getAccount());
				List<Manager> list = managerMapper.checkManageExists(check);
				if (null == list || list.isEmpty()) {
					result.setMessage("账号不存在");
					return result;
				}
				if (list.size() > 1) {
					result.setMessage("客户经理信息异常");
					return result;
				}
				Manager info = list.get(0);
				if (!SHA256Python.checkPassword(dto.getPassword(), info.getPassword())) {
					result.setMessage("用户名或密码错误");
					return result;
				}
				// 修改登录时间
				Manager updateRecord = new Manager();
				updateRecord.setId(info.getId());
				updateRecord.setUpdaterId(info.getId());
				updateRecord.setUpdateTime(new Date());
				updateRecord.setLoginTime(new Date());
				updateManager(updateRecord);

				ManageInfoVO rinfo = new ManageInfoVO();
				BeanUtils.copyProperties(info, rinfo);
				return ResultUtil.setResult(result, RespCode.Code.SUCCESS, rinfo);
			}
		} catch (Exception e) {
			log.error("客户经理登录异常", e);
		}
		return result;
	}

    @Transactional(rollbackFor = Exception.class)
    private void updateManager(Manager updateRecord) {
        managerMapper.updateByPrimaryKeySelective(updateRecord);
    }

    private String checkAddCustomerParam(RegisterBaseDTO dto) {
        String result = null;
        Manager check = new Manager();
        check.setTelphone(dto.getTelphone());
        check.setUsername(dto.getUsername());
        List<Manager> list = managerMapper.checkManageExists(check);
        if (null != list && !list.isEmpty()) {
            for (Manager c : list) {
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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result registerManage(RegisterDTO dto) {
		log.info("客户经理注册入参={}", JSON.toJSONString(dto));
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			String errorStr = dto.validateForm();
			if (StringUtils.isNotBlank(errorStr)) {
				result.setMessage(errorStr);
				return result;
			}
			String lockKey = dto.getTelphone();
			try {
				distributionLock.lock(lockKey);
				errorStr = checkAddCustomerParam(dto);
				if (StringUtils.isNotBlank(errorStr)) {
					result.setMessage(errorStr);
					return result;
				} else {
					Manager manager = queryAddManage(dto, null);
					managerMapper.insertUseGeneratedKeys(manager);
					// 添加相关初始化值
					addRegistCommonContent(manager, null, null);
					return ResultUtil.setResult(result, RespCode.Code.SUCCESS, manager.getId());
				}
			} finally {
				distributionLock.unLock(lockKey);
			}
		} catch (Exception e) {
			log.error("客户经理注册异常", e);
			throw new UserBizException(ManageExceptionEnum.ADD_MANAGE_ERROR);
		}
	}

    /**
     * @Creater: zhangxia
     * @description：后台添加客户经理
     * @methodName: webRegisterManager
     * @params: [webRegisterDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 11:22 2018-2-26
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result webRegisterManager(WebRegisterDTO webRegisterDTO) {
        log.info("后台添加客户经理入参参数webRegisterDTO={}", JSON.toJSONString(webRegisterDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);

        try {
            webRegisterDTO.setPassword(webRegisterDTO.getTelphone().substring(webRegisterDTO.getTelphone().length() - 6, webRegisterDTO.getTelphone().length()));//默认手机手机后6位
            String msg = webRegisterDTO.validateForm();
            if (StringUtils.isBlank(msg)) {
                String lockKey = webRegisterDTO.getTelphone();

                try {
                    distributionLock.lock(lockKey);
                    msg = checkAddCustomerParam(webRegisterDTO);
                    if (StringUtils.isBlank(msg)) {

                        Manager manager = queryAddManage(webRegisterDTO,webRegisterDTO.getMoney());
                        manager.setManagerDivideScale(webRegisterDTO.getManagerDivideScale());
                        manager.setIsEmployee(webRegisterDTO.getIsEmployee());
                        manager.setRemark(webRegisterDTO.getRemark());
                        managerMapper.insertUseGeneratedKeys(manager);
                        result.setData(manager.getId());
                        //添加相关初始化值
                        addRegistCommonContent(manager, null,webRegisterDTO.getUserId());
                        result = ResultUtil.setResult(result, RespCode.Code.SUCCESS);
                    } else {
                        result.setMessage(msg);
                    }
                } finally {
                    distributionLock.unLock(lockKey);
                }

            } else {
                result.setMessage(msg);
            }

        } catch (Exception e) {
            log.info("后台添加客户经理处理结果异常e={}", e);
            throw new RuntimeException("后台添加客户经理处理结果异常");
        }
        log.info("后台添加客户经理处理结果result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @param dto
     * @return
     * @Title: queryAddManage
     * @author: lijie
     * @Description: 封装客户经理数据
     * @return: Manager
     */
    private Manager queryAddManage(RegisterBaseDTO dto,BigDecimal assets) {
        Manager manager = new Manager();
        manager.setCreaterId(Objects.isNull(dto.getCreaterId()) ? 0 : dto.getCreaterId());
        manager.setPassword(SHA256Python.encode(dto.getPassword()));
        manager.setCreateTime(new Date());
        manager.setIsAuthentication(ManagerEnum.NOT_AUTHEN.getStatus());
        manager.setIsBindBank(ManagerEnum.NOT_BINDBANK.getStatus());
        manager.setRealName(dto.getRealNmae());
        manager.setTelphone(dto.getTelphone());
        manager.setUsername(dto.getUsername());
        manager.setAssets(assets);
        // idCode
        Integer idCode = managerMapper.selectMaxIdCode();
        if (null == idCode) {
            manager.setIdCode(2008000001);
        } else {
            manager.setIdCode(++idCode);
        }
        return manager;
    }

    /**
     * @Creater: zhangxia
     * @description：添加客户经理初始化公共数据
     * @methodName: addRegistCommonContent
     * @params: [manager]
     * @return: void
     * @createTime: 11:37 2018-2-26
     */
    private void addRegistCommonContent(Manager manager, Integer money,Integer userId) {
        /**
         * mdy 郑朋 2018-2-10
         */
        // 初始化余额
        Balance balance = new Balance();
        balance.setManagerId(manager.getId());
        balance.setMoney(new BigDecimal(Objects.isNull(money) ? 0 : money));
        balance.setCreaterId(manager.getId());
        balance.setCreateTime(new Date());
        balance.setRoleType(RoleCodeEnum.MANAGER.getCode());
        balance.setTotalRevenue(new BigDecimal(0));
        balance.setTotalSettlement(new BigDecimal(0));
        balanceMapper.insert(balance);
        // 初始化默认分组
        Group group = new Group();
        group.setIsDefault(GroupEnum.YES_DEFAULT.getStatus());
        group.setIsDelete(GroupEnum.NO_DELETE.getStatus());
        group.setCreaterId(manager.getId());
        group.setGroupName(GroupEnum.DEFALUT_NAME.getDesc());
        group.setCreateTime(new Date());
        groupMapper.insert(group);

        //添加客户经理和员工的关联关系--后台添加客户经理必须要添加维护人员
        if (Objects.nonNull(userId)){
            UserManager userManager = new UserManager();
            userManager.setManagerId(manager.getId());
            userManager.setUserId(userId);
            userManager.setCreaterId(manager.getCreaterId());
            userManager.setCreateTime(new Date());
            userManagerMapper.insert(userManager);
        }

    }

	@Override
	public Result getManageById(Integer id) {
		log.info("根据ID查询客户经理入参={}", id);
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			Manager manager = managerMapper.selectByPrimaryKey(id);
			if (null != manager) {
				return ResultUtil.setResult(result, RespCode.Code.SUCCESS, manager);
			}
		} catch (Exception e) {
			log.error("根据ID查询客户经理异常", e);
		}
		return result;
	}

	@Override
	public Result getManageByAccount(String account) {
		log.info("根据手机号或则用户名查询客户经理入参={}", account);
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			Manager check = new Manager();
			check.setTelphone(account);
			check.setUsername(account);
			List<Manager> list = managerMapper.checkManageExists(check);
			if (null != list && !list.isEmpty()) {
				if (list.size() > 1) {
					log.info("客户经理数据存在多条，请检查数据");
					result.setMessage("客户经理数据异常");
				} else {
					return ResultUtil.setResult(result, RespCode.Code.SUCCESS, list.get(0));
				}
			}
		} catch (Exception e) {
			log.error("根据手机号或则用户名查询客户经理异常", e);
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result updateLoginPwd(LoginPasswordDTO dto) {
		log.info("修改/找回登录密码入参={}", JSON.toJSONString(dto));
		try {
			final Result result = checkManage(dto, dto.getTelphone());
			if (!result.isSuccess()) {
				return result;
			}
			Manager record = (Manager) result.getData();
			PwdOperTypeEnum type = dto.getType();
			Manager update = new Manager();
			update.setPassword(SHA256Python.encode(dto.getNewPassword()));
			update.setId(record.getId());
			if (null == dto.getUpdaterId()) {
				update.setUpdaterId(record.getId());
			} else {
				update.setUpdaterId(dto.getUpdaterId());
			}
			update.setUpdateTime(new Date());
			switch (type) {
			case BACK:
				managerMapper.updateByPrimaryKeySelective(update);
				break;
			case UPDATE:
				if (!SHA256Python.checkPassword(dto.getPassword(), record.getPassword())) {
					ResultUtil.setResult(result, RespCode.Code.FAIL);
					result.setMessage("旧密码错误");
					return result;
				}
				managerMapper.updateByPrimaryKeySelective(update);
				break;
			default:
				break;
			}
			return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
		} catch (Exception e) {
			log.error("修改登录密码异常", e);
			throw new UserBizException(ManageExceptionEnum.UPDATE_MANAGELOGINPWD_ERROR);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result updatePayPwd(PayPwdDTO dto) {
		log.info("设置/修改支付密码入参={}", JSON.toJSONString(dto));
		try {
			final Result result = checkManage(dto, dto.getTelphone());
			if (!result.isSuccess()) {
				return result;
			}
			Manager info = (Manager) result.getData();
			Manager update = new Manager();
			update.setId(info.getId());
			update.setUpdaterId(dto.getOperId());
			update.setUpdateTime(new Date());
			update.setPayPassword(SHA256Python.encode(dto.getPayPwd()));
			managerMapper.updateByPrimaryKeySelective(update);
			return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
		} catch (Exception e) {
			log.error("设置/修改支付密码异常", e);
			throw new UserBizException(ManageExceptionEnum.UPDATE_MANAGEPAYPWD_ERROR);
		}
	}

    /**
     * @param @param  dto
     * @param @param  telphone
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: checkManage
     * @Description: 校验客户经理数据
     */
	private Result checkManage(BaseValidate dto, String telphone) {
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		String errorStr = dto.validateForm();
		if (StringUtils.isNotBlank(errorStr)) {
			result.setMessage(errorStr);
			return result;
		}
		Manager record = new Manager();
		record.setTelphone(telphone);
		record = managerMapper.selectOne(record);
		if (null == record) {
			result.setMessage("客户经理不存在");
			return result;
		}
		return ResultUtil.setResult(result, RespCode.Code.SUCCESS, record);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result updateCustomerManageOpenId(String account, String openId) {
		log.info("修改客户经理微信openId入参account={},openId={}", account, openId);
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
			String lockKey = "manage_" + openId;
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
						managerMapper.updateOpenIdIsNull(account);
					} else {
						WechatBindFailVO vo = new WechatBindFailVO();
						vo.setTelphone(StringTools.telphoneChange(account));
						vo.setNickname(null == ui ? "" : ui.getNickname());
						return ResultUtil.setResult(result, ManageExceptionEnum.ACCOUNT_ALREADY_BIND, vo);
					}
				}
				// 校验openId是否被其它绑定
				String telphone = isBind(openId);
				if (StringUtils.isNotBlank(telphone)) {
					return ResultUtil.setResult(result, ManageExceptionEnum.OPENID_EXISTS,
							StringTools.telphoneChange(telphone));
				}
				Manager check = new Manager();
				check.setTelphone(account);
				check.setUsername(account);
				List<Manager> list = managerMapper.checkManageExists(check);
				if (null == list || list.isEmpty()) {
					result.setMessage("客户经理数据不存在");
					return result;
				}
				if (list.size() > 1) {
					result.setMessage("客户经理数据异常");
					return result;
				}
				Manager customer = list.get(0);
				Manager update = new Manager();
				update.setId(customer.getId());
				update.setOpenId(openId);
				update.setUpdaterId(customer.getId());
				update.setUpdateTime(new Date());
				managerMapper.updateByPrimaryKeySelective(update);
				return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
			} finally {
				distributionLock.unLock(lockKey);
			}
		} catch (Exception e) {
			log.error("修改客户经理微信openId异常", e);
			throw new UserBizException(ManageExceptionEnum.UPDATE_CUSTOMER_OPENID_ERROR);
		}
	}
    /**
     *
    * @Title: isBindAccount
    * @Description: 校验账号是否已经绑定过微信
    * @param: @param telphone
    * @param: @return
    * @return boolean
    * @author lijie
    * @throws
     */
	private String isBindAccount(final String telphone) {
		Manager selectOne = new Manager();
		selectOne.setTelphone(telphone);
		selectOne = managerMapper.selectOne(selectOne);
		return null == selectOne ? null : selectOne.getOpenId();
	}
    /**
     * 
    * @Title: isBindOwn  
    * @Description:校验是否被自己绑定  
    * @param @param telphone
    * @param @param openId
    * @param @return    参数  
    * @return boolean    返回类型  
    * @throws
     */
	private boolean isBindOwn(final String telphone, final String openId) {
		Manager selectOne = new Manager();
		selectOne.setTelphone(telphone);
		selectOne.setOpenId(openId);
		selectOne = managerMapper.selectOne(selectOne);
		return null != selectOne;
	}
    /**
     * 
    * @Title: isBind  
    * @Description: 校验openID是否被绑定  
    * @param @param openId
    * @param @return    参数  
    * @return boolean    返回类型  
    * @throws
     */
	private String isBind(final String openId) {
		String result = null;
		Manager select = new Manager();
		select.setOpenId(openId);
		List<Manager> selects = managerMapper.select(select);
		if (CollectionUtils.isNotEmpty(selects)) {
			result = selects.get(0).getTelphone();
		}
		return result;
	}

    /**
     * @Creater: zhangxia
     * @description：翻页获取客户经理列表
     * @methodName: getManagerListByPage
     * @params: [pageBean, selectManagerDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 14:23 2018-2-26
     */
    @Override
    public Result getManagerListByPage(PageBean pageBean, SelectManagerDTO selectManagerDTO) {
        log.info("客户经理后台列表查询入参参数pageBean = {},selectManagerDTO = {}", JSON.toJSONString(pageBean), JSON.toJSONString(selectManagerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            UserSecurity security = new UserSecurity();
            security.setUserId(selectManagerDTO.getUserId());
            //查看是否是所有客户经理
            security.setSecurityId(SecurityBaseSettingEnum.ALL_MANAGER.getId());
            if (userSecurityMapper.selectCount(security)>0){
                //有查看所有客户经理的权限
                selectManagerDTO.setUserId(null);
            }
            if (pageBean != null) {
                pageBean.setPageSize(pageBean.getPageSize() == 0 ? PageConstant.PAGE_SIZE : pageBean.getPageSize());
                pageBean.setPageNum(pageBean.getPageNum() == 0 ? PageConstant.PAGE_NUM : pageBean.getPageNum());
                PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            }
            List<ManagerListVO> managerListVOS = managerMapper.getManagerListByPage(selectManagerDTO);
            security.setSecurityId(SecurityBaseSettingEnum.MANAGER_TEL_PHONE.getId());
            if (!(userSecurityMapper.selectCount(security)>0)){
                log.info("员工id={} 不具备查看手机号的权限",security.getUserId());
                for (ManagerListVO managerListVO :
                        managerListVOS) {
                    managerListVO.setTelphone(StringTools.telphoneChange(managerListVO.getTelphone()));
                }
            }else {
                log.info("员工id={} 具备查看手机号的权限",security.getUserId());
            }
            PageInfo<ManagerListVO> pageInfo = new PageInfo<>(managerListVOS);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());

        } catch (Exception e) {
            log.info("查询异常e={}", e);
            result.setMessage("客户经理查询异常");
        }
        log.info("获取客户经理列表处理结果result={}", JSON.toJSONString(result));
        return result;
    }


    /**
     * @Creater: zhangxia
     * @description：后台获取客户经理的详细信息
     * @methodName: getManagerDetail
     * @params: [selectManagerDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 15:01 2018-2-26
     */
    @Override
    public Result getManagerDetail(SelectManagerDTO selectManagerDTO) {
        log.info("后台获取客户经理的详细信息入参参数selectManagerDTO={}", JSON.toJSONString(selectManagerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (Objects.isNull(selectManagerDTO.getId()) || selectManagerDTO.getId().intValue() == 0) {
                result.setMessage("参数不能为空");
            } else {
                ManagerDetailVO managerDetail = managerMapper.getManagerDetail(selectManagerDTO);
                UserSecurity security = new UserSecurity();
                security.setSecurityId(SecurityBaseSettingEnum.MANAGER_TEL_PHONE.getId());
                security.setUserId(selectManagerDTO.getUserId());
                if (!(userSecurityMapper.selectCount(security)>0)){
                    managerDetail.setTelphone(StringTools.telphoneChange(managerDetail.getTelphone()));
                }
                result = ResultUtil.getResult(RespCode.Code.SUCCESS, managerDetail);
            }

        } catch (Exception e) {
            log.info("后台获取客户经理的详细异常e={}", e);
        }
        log.info("后台获取客户经理的详细信息处理结果result={}", JSON.toJSONString(selectManagerDTO));
        return result;
    }


    /**
     * @Creater: zhangxia
     * @description：客户经理重置密码
     * @methodName: resetPWD
     * @params: [updateManagerDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 9:42 2018-2-28
     */
    @Override
    public Result resetPWD(UpdateManagerDTO updateManagerDTO) {

        log.info("客户经理重置密码入参参数updateManagerDTO={}", JSON.toJSONString(updateManagerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            Manager manager = managerMapper.selectByPrimaryKey(updateManagerDTO.getId());
            if (Objects.isNull(manager)) {
                result = ResultUtil.getResult(UserExceptionEnum.SELECT_DATA_NOT_EXIST);
            } else {
                manager.setPassword(SHA256Python.encode(StringUtils.substring(manager.getTelphone(), updateManagerDTO.getTelphone().length() - 6)));
                if (managerMapper.updateByPrimaryKeySelective(manager) > 0) {
                    result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                } else {
                    log.info("客户经理重置密码失败");
                    result.setMessage("客户经理重置密码失败");
                }
            }

        } catch (Exception e) {
            log.info("客户经理重置密码异常e={}", e);
        }
        log.info("客户经理重置密码处理结果result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @Creater: zhangxia
     * @description：后台编辑客户经理信息
     * @methodName: editManager
     * @params: [updateManagerDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 10:58 2018-2-28
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result editManager(UpdateManagerDTO updateManagerDTO) {

        log.info("后台编辑客户经理入参参数updateManagerDTO = {}", JSON.toJSONString(updateManagerDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            //更新姓名和资产
            Manager manager = managerMapper.selectByPrimaryKey(updateManagerDTO.getId());

            if (Objects.isNull(manager)) {
                log.info("传入参数有误，没有查到客户经理基本信息原始数据");
                result = ResultUtil.getResult(UserExceptionEnum.SELECT_DATA_NOT_EXIST);
            } else {
                manager.setRealName(updateManagerDTO.getName());
                manager.setAssets(updateManagerDTO.getAccout());
                manager.setRemark(updateManagerDTO.getRemark());
                manager.setIsEmployee(updateManagerDTO.getEditIsEmployee());
                if (managerMapper.updateByPrimaryKeySelective(manager) > 0) {
                    //更新userInfo数据完成后，更新与维护人员关联关系表
                    UserManager userManager = new UserManager();
                    userManager.setManagerId(updateManagerDTO.getId());
                    userManager = userManagerMapper.selectOne(userManager);
                    if (Objects.isNull(userManager)) {
                        if (Objects.nonNull(updateManagerDTO.getEmployeeId())) {
                            //插入信息
                            userManager = new UserManager();
                            userManager.setManagerId(updateManagerDTO.getId());
                            userManager.setUserId(updateManagerDTO.getEmployeeId());
                            userManager.setCreateTime(new Date());
                            userManager.setCreaterId(updateManagerDTO.getUpdaterId());
                            if (userManagerMapper.insert(userManager) > 0) {
                                //添加成功
                                result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                            } else {
                                log.info("添加客户经理和维护人员关联关系失败");
                                throw new UserBizException(UserExceptionEnum.UPDATE_MANAGER_ADD_RELATION_FAIL);
                            }
                        } else {
                            //添加成功
                            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                        }

                    } else {
                        userManager.setUserId(updateManagerDTO.getEmployeeId());
                        if (userManagerMapper.updateByPrimaryKeySelective(userManager) > 0) {
                            //更新成功
                            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                        } else {
                            log.info("更新客户经理和维护人员关联关系失败");
                            throw new UserBizException(UserExceptionEnum.UPDATE_MANAGER_UPDATE_RELATION_FAIL);
                        }
                    }
                } else {
                    log.info("更新客户经理基本信息失败");
                }
            }
        } catch (Exception e) {
            log.info("添加客户经理异常e={}，message={}", e, e.getMessage());
            throw new UserBizException(UserExceptionEnum.UPDATE_MANAGER_EXCEPTION);
        }
        log.info("后台编辑客户经理入参参数updateManagerDTO = {}", JSON.toJSONString(updateManagerDTO));
        return result;
    }

    @Override
    public Result getAllOpend() {
        List<String> rlist = new ArrayList<>();
        List<Manager> list = managerMapper.selectAll();
        if (null != list && !list.isEmpty()) {
            for (Manager c : list) {
                if (StringUtils.isNotBlank(c.getOpenId())) {
                    rlist.add(c.getOpenId());
                }
            }
        }
        return ResultUtil.getResult(RespCode.Code.SUCCESS, rlist);
    }

    /**
     * @author: 张霞
     * @description：获取所有客户经理
     * @createDate: 21:13 2018/1/22
     * @param
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    @Override
    public Result getAllManager() {
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            List<ManagerListVO> allManager = managerMapper.getAllManager();
            result = ResultUtil.getResult(RespCode.Code.SUCCESS,allManager);
        }catch (Exception e){
            log.info("获取所有客户经理异常e={}",e);
        }
        log.info("获取所有客户经理处理结果result={}",JSON.toJSONString(result));
        return result;
    }

	@Override
	public Result getManagerByOpenId(String openId) {
		log.info("根据微信openId得到客户经理信息入参={}", openId);
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			if (StringUtils.isNotBlank(openId)) {
				Manager record = new Manager();
				record.setOpenId(openId);
				record = managerMapper.selectOne(record);
				if (null != record) {
					return ResultUtil.setResult(result, RespCode.Code.SUCCESS, record);
				} else {
					result.setMessage("根据openId未查询到客户经理信息");
				}
			}
		} catch (Exception e) {
			log.error("根据openId查询客户经理信息异常", e);
		}
		return result;
	}


	@Override
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
			Manager select = new Manager();
			select.setOpenId(openId);
			Manager selectOne = managerMapper.selectOne(select);
			if (null == selectOne) {
				result.setMessage("当前openId未绑定不需要更新关注状态");
				return result;
			}
			Manager update = new Manager();
			update.setId(selectOne.getId());
			update.setIsCancelAttention(status);
			if (AttentionWchatEnum.NOT_ATTENTION_WCHAT_ENUM.getCode().equals(status)) {
				update.setCancelAttentionTime(new Date());
			} else {
				update.setCreateAttentionTime(new Date());
			}
			managerMapper.updateByPrimaryKeySelective(update);

			return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
		} catch (Exception e) {
			log.error("申请工具异常", e);
			throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
		}
	}


    /**
     * @author: zhangxia
     * @desc: vip分成比例修改
     * @params: [dto]
     * @methodName:updateDivideScalse
     * @date: 2018/3/23 0023 上午 11:27
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result updateDivideScalse(UpdateManagerDTO dto) {
        log.info("客户经理vip分成比例修改 入参参数dto={}",JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (Objects.isNull(dto.getId())||Objects.isNull(dto.getManagerDivideScale()) || (dto.getManagerDivideScale()>1||dto.getManagerDivideScale()<0)){
                result.setMessage("请求参数有误");
            }else {
                Manager manager = managerMapper.selectByPrimaryKey(dto.getId());
                manager.setManagerDivideScale(dto.getManagerDivideScale());
                if (managerMapper.updateByPrimaryKeySelective(manager)>0){
                    log.info("客户经理vip分成比例修改 更新成功");
                    result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                }
            }
        }catch (Exception e){
            log.info("客户经理vip分成比例修改 处理异常e={}",e);
        }
        log.info("客户经理vip分成比例修改 处理结果result={}",JSON.toJSONString(result));
        return result;
    }
}
