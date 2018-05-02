package com.aq.service.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.facade.dto.TrendToolDTO;
import com.aq.facade.service.IToolCacheService;
import com.aq.service.IToolImportService;
import com.aq.util.excel.ExcelImportUtil;
import com.aq.util.result.Result;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: ToolImportServiceImpl
 * @Description: TODO
 * @author: lijie
 * @date: 2018年4月16日 下午12:39:21
 */
@Slf4j
@Service
public class ToolImportServiceImpl implements IToolImportService {

	@Reference(version = "1.0.0", check = false)
    private IToolCacheService toolCacheService;
	
	@Override
	public List<TrendToolDTO> getTrendToolImportDatas(HttpServletRequest request) {
		// 最后返回结果
		final List<TrendToolDTO> result = new LinkedList<TrendToolDTO>();
		// 解析后的结果
		final List<TrendToolDTO> analysis = new LinkedList<TrendToolDTO>();
		try {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Map<String, MultipartFile> multipartFiles = multiRequest.getFileMap();
			if (null != multipartFiles) {
				InputStream in;
				String filePath;
				for (Map.Entry<String, MultipartFile> entry : multipartFiles.entrySet()) {
					in = entry.getValue().getInputStream();
					filePath = entry.getValue().getOriginalFilename();
					if (ExcelImportUtil.isExcelXls(filePath) || ExcelImportUtil.isExcelxlsx(filePath)) {
						analysis.addAll(ExcelImportUtil.readExcel(TrendToolDTO.class, new String[] { "stockCode" }, in,
								filePath, true));
					} else {
						analysis.addAll(readEbk(in));
					}
				}
			}
			log.info("读取股票解析后的数据={}", JSON.toJSONString(analysis));
			if(null != analysis && !analysis.isEmpty()){
				List<Object> codes = new ArrayList<>();
				for(TrendToolDTO dto : analysis){
					codes.add(dto.getStockCode());
				}
				Result results = toolCacheService.getCacheStockInfoByCodes(codes);
				if(results.isSuccess()){
					@SuppressWarnings("unchecked")
					Map<String, String> map = (Map<String, String>)results.getData();
					String name;
					for(TrendToolDTO dto : analysis){
						name = map.get(dto.getStockCode());
						if(StringUtils.isNotBlank(name)){
							dto.setStockName(name);
							result.add(dto);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("数据导入异常", e);
		}
		return result;
	}

	private List<TrendToolDTO> readEbk(InputStream in) throws Exception {
		List<TrendToolDTO> result = new LinkedList<TrendToolDTO>();
		BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(in), "utf-8"));
		String line;
		TrendToolDTO dto;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (StringUtils.isNotBlank(line)) {
				dto = new TrendToolDTO();
				dto.setStockCode(line);
				result.add(dto);
			}
		}
		br.close();
		return result;
	}
}
