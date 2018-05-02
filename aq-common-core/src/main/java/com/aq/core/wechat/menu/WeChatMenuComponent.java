package com.aq.core.wechat.menu;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aq.core.wechat.WeChatConfig;
import com.aq.core.wechat.WeChatMenuData;
import com.aq.core.wechat.constant.TagEnum;
import com.aq.core.wechat.constant.WheChatConstant;
import com.aq.core.wechat.constant.WheChatMenuEnum;
import com.aq.core.wechat.util.WeChatRquestUtil;
import com.aq.core.wechat.util.WeChatSignatureUtil;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @ClassName: WeChatMenuComponent
 * @Description: 微信菜单组件
 * @author: lijie
 * @date: 2018年3月14日 下午3:57:42
 */
@Slf4j
@Component
public class WeChatMenuComponent {
	/**
	 * 
	* @Title: getMenus  
	* @Description: TODO 
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	public Result getMenus() {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			Map<String, Object> params = new HashMap<>(1);
			String response = WeChatRquestUtil.sendGetByToken(WeChatConfig.GET_MENU, params);
			log.info("查询创建的菜单返回数据={}", response);
			result.setData(JSONObject.parseObject(response));
			ResultUtil.setResult(result, RespCode.Code.SUCCESS);
		} catch (Exception e) {
			log.error("查询创建的菜单数据异常", e);
		}
		return result;
	}
	/**
	 * 
	* @Title: getTags  
	* @Description: 查询公众号已创建的标签 
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	public Result getTags() {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			Map<String, Object> params = new HashMap<>(1);
			String response = WeChatRquestUtil.sendGetByToken(WeChatConfig.GET_TAGS, params);
			log.info("查询公众号已创建的标签 返回数据={}", response);
			result.setData(JSONObject.parseObject(response));
			ResultUtil.setResult(result, RespCode.Code.SUCCESS);
		} catch (Exception e) {
			log.error("查询公众号已创建的标签 数据异常", e);
		}
		return result;
	}
	/**
	 * 
	* @Title: createTag  
	* @Description: 为公众号创建标签 
	* @param: @param tagName
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	public Result createTag(String tagName) {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			JSONObject json = new JSONObject();
			JSONObject tag = new JSONObject();
			tag.put("name", tagName);
			json.put("tag", tag);
			String response = WeChatRquestUtil.sendPostJsonByToken(WeChatConfig.CREATE_TAG, json.toJSONString());
			log.info("为公众号创建标签 返回数据={}", response);
			result.setData(JSONObject.parseObject(response));
			ResultUtil.setResult(result, RespCode.Code.SUCCESS);
		} catch (Exception e) {
			log.error("为公众号创建标签 数据异常", e);
		}
		return result;
	}
	/**
	 * @throws Exception
	 *
	* @Title: createMaterial
	* @Description: 创建素材
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	public Result createMaterial() throws Exception{
		String json = "{\"articles\": [{\"title\": \"测试\"," + "\"thumb_media_id\": \"xDIQIsQhJoPdrCd5w21M2Fmr7kco3zG8JSmQ869dv7A\","
				+ "\"author\": \"AUTHOR\"," + "\"show_cover_pic\": 0," + "\"content\": \"CONTENT\","
				+ "\"content_source_url\": \"http://www.baidu.com\"},]}";
		String response = WeChatRquestUtil.sendPostJsonByToken(WeChatConfig.ADD_MATERIAL, json);
		return ResultUtil.getResult(RespCode.Code.SUCCESS, response);
	}


	public Result uploadMaterials() {
		String path = "D:" + File.separator + "image" + File.separator + "1.jpg";
		try {
			addMaterialEver(path, "image", WeChatSignatureUtil.getAccessToken());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultUtil.getResult(RespCode.Code.SUCCESS);
	}
	/**
	 * 上传其他永久素材(图片素材的上限为5000，其他类型为1000)
	 *
	 * @return
	 * @throws Exception
	 */
	public static JSONObject addMaterialEver(String fileurl, String type, String token) {
		try {
			File file = new File(fileurl);
			// 上传素材
			String path = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + token + "&type="
					+ type;
			String result = connectHttpsByPost(path, null, file);
			result = result.replaceAll("[\\\\]", "");
			System.out.println("result:" + result);
			JSONObject resultJSON = JSONObject.parseObject(result);
			if (resultJSON != null) {
				if (resultJSON.get("media_id") != null) {
					System.out.println("上传" + type + "永久素材成功");
					return resultJSON;
				} else {
					System.out.println("上传" + type + "永久素材失败");
				}
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String connectHttpsByPost(String path, String KK, File file)
			throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		URL urlObj = new URL(path);
		String result = null;
		// 连接
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); // post方式不能使用缓存
		// 设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");
		// 设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		// 请求正文信息
		// 第一部分：
		StringBuilder sb = new StringBuilder();
		sb.append("--"); // 必须多两道线
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"" + file.length() + "\";filename=\""
				+ file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		byte[] head = sb.toString().getBytes("utf-8");
		// 获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		// 输出表头
		out.write(head);
		// 文件正文部分
		// 把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();
		// 结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
		out.write(foot);
		out.flush();
		out.close();
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			System.out.println("发送POST请求出现异常！" + e);
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return result;
	}
	/**
	 *
	* @Title: delMenu  
	* @Description: 删除菜单/同时会删除自定义菜单 
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	public Result delMenu() {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			Map<String, Object> params = new HashMap<>(1);
			String response = WeChatRquestUtil.sendGetByToken(WeChatConfig.DEL_MENU, params);
			log.info("删除菜单/同时会删除自定义菜单 返回数据={}", response);
			JSONObject jsonObject = JSON.parseObject(response);
			String errCode = jsonObject.getString("errcode");
			if (WheChatConstant.SUCCESS_STATE.equals(errCode)) {
				result = ResultUtil.getResult(RespCode.Code.SUCCESS, jsonObject);
			} else {
				result.setMessage(jsonObject.getString("errmsg"));
			}
		} catch (Exception e) {
			log.error("删除菜单/同时会删除自定义菜单 数据异常", e);
		}
		return result;
	}
	/**
	 * 
	* @Title: deleteConditionalMenu  
	* @Description: 删除个性化菜单 
	* @param: @param menuId
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	public Result deleteConditionalMenu(final String menuId) {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			JSONObject json = new JSONObject();
			json.put("menuid", menuId);
			String response = WeChatRquestUtil.sendPostJsonByToken(WeChatConfig.DEL_CONDITIONAL_MENU,
					JSON.toJSONString(json));
			log.info("删除个性化菜单返回数据={}", response);
			JSONObject jsonObject = JSON.parseObject(response);
			String errCode = jsonObject.getString("errcode");
			if (WheChatConstant.SUCCESS_STATE.equals(errCode)) {
				result = ResultUtil.getResult(RespCode.Code.SUCCESS, jsonObject);
			} else {
				result.setMessage(jsonObject.getString("errmsg"));
			}
		} catch (Exception e) {
			log.error("删除个性化菜单 异常", e);
		}
		return result;
	}
	/**
	 * @throws Exception 
	* @Title: createIndividuationMenu  
	* @Description: 创建个性化菜单 
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	@Deprecated
	public Result createConditionalMenu() {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			JSONObject json = queryMenuData();
			JSONObject matchrule = new JSONObject();
			matchrule.put("tag_id", TagEnum.FOLLOW_CUSTOMER_TAG.getTagId());
			matchrule.put("language", "zh_CN");
			json.put("matchrule", matchrule);
			String response = WeChatRquestUtil.sendPostJsonByToken(WeChatConfig.CONDITIONAL_MENU,
					JSON.toJSONString(json));
			log.info("创建个性化菜单返回数据={}", response);
			result = ResultUtil.getResult(RespCode.Code.SUCCESS, JSONObject.parseObject(response));
		} catch (Exception e) {
			log.error("创建个性化菜单 异常", e);
		}
		return result;
	}
	/**
	 * 
	* @Title: queryMenuData
	* @Description: 封装菜单数据
	* @param: @param isConditional
	* @param: @return
	* @return JSONObject
	* @author lijie
	* @throws
	 */
	private JSONObject queryMenuData() {
		List<WeChatMenuData> list = WheChatMenuEnum.MENU.menus();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		if (CollectionUtils.isNotEmpty(list)) {
			int size = list.size();
			MenuData md;
			WeChatMenuData menu;
			for (int i = 0; i < size; i++) {
				menu = list.get(i);
				md = initMenuData(menu);
				md.setSub_button(nextMenus(menu.getSubmenus()));
				array.add(md);
			}
		}
		json.put("button", array);
		return json;
	}
	/**
	 *
	* @Title: nextMenus
	* @Description: 下一集菜单
	* @param: @param nexts
	* @param: @return
	* @return JSONArray
	* @author lijie
	* @throws
	 */
	private JSONArray nextMenus(List<WeChatMenuData> nexts) {
		JSONArray array = new JSONArray();
		if (CollectionUtils.isNotEmpty(nexts)) {
			int size = nexts.size();
			MenuData md;
			WeChatMenuData menu;
			for (int i = 0; i < size; i++) {
				menu = nexts.get(i);
				md = initMenuData(menu);
				md.setSub_button(nextMenus(menu.getSubmenus()));
				array.add(md);
			}
		}
		return array;
	}


	private MenuData initMenuData(WeChatMenuData menu) {
		MenuData result = new MenuData();
		BeanUtils.copyProperties(menu, result);
		if ("media_id".equals(menu.getType())) {
			result.setMedia_id(menu.getOption());
		} else if ("view".equals(menu.getType())) {
			result.setUrl(menu.getOption());
		}
		return result;
	}
	/**
	 *
	* @Title: createMenu
	* @Description: 创建菜单 
	* @param: @return
	* @param: @throws Exception
	* @return Result
	* @author lijie
	* @throws
	 */
	public Result createMenu() {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			JSONObject json = queryMenuData();
			String response = WeChatRquestUtil.sendPostJsonByToken(WeChatConfig.CREATE_MENU, JSON.toJSONString(json));
			log.info("创建菜单返回数据={}", response);
			JSONObject jsonObject = JSON.parseObject(response);
			String errCode = jsonObject.getString("errcode");
			if (WheChatConstant.SUCCESS_STATE.equals(errCode)) {
				result = ResultUtil.getResult(RespCode.Code.SUCCESS, jsonObject);
			} else {
				result.setMessage(jsonObject.getString("errmsg"));
			}
		} catch (Exception e) {
			log.error("创建菜单异常", e);
		}
		return result;
	}

	@Data
	protected class MenuData {
		/**
		 * 菜单名称
		 */
		private String name;
		/**
		 * 类型
		 */
		private String type;
		/**
		 * 菜单key
		 */
		private String key;
		/**
		 * 跳转url
		 */
		private String url;
		/**
		 *
		 */
		private String media_id;
		/**
		 * 子菜单
		 */
		private JSONArray sub_button;
	}

}
