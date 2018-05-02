package com.aq.service;

import com.aq.facade.vo.customer.ImportFailureRecordVO;
import com.aq.util.container.ContainerUtil;
import com.aq.util.excel.ExcelBean;
import com.aq.util.excel.ExcelExportConfig;
import com.aq.util.excel.ExcelUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * 导入失败客户记录service
 *
 * @author 郑朋
 * @create 2018/2/10 0010
 **/
@Service
public class FailureCustomerService {

    /**
     * 时间转换
     */
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 客户导入失败导出excel
     *
     * @param list
     * @return com.aq.util.excel.ExcelBean
     * @author 郑朋
     * @create 2018/2/10 0010
     */
    public ExcelBean excelBean(List<ImportFailureRecordVO> list) {

        List<Object[]> sheetData = ContainerUtil.aList();
        String[] tableHeads = {"编号", "导入时间", "客户分组", "客户姓名", "客户手机号", "原因"};
        String name = "导入失败的客户列表";
        ExcelBean excelBean = ExcelUtil.getExcelHeadBean(tableHeads, name, sheetData);
        HashMap<Integer, ExcelBean> sheetsMap = new HashMap<>(16);
        Object[] content;
        ImportFailureRecordVO pageListVO;
        int length = list.size();
        int olength = tableHeads.length;
        int k;
        for (int j = 0; j < length; j++) {
            pageListVO = list.get(j);
            k = 0;
            content = new Object[olength];
            content[k++] = j + 1;
            content[k++] = SIMPLE_DATE_FORMAT.format(pageListVO.getCreateTime());
            content[k++] = pageListVO.getGroupName();
            content[k++] = pageListVO.getRealName();
            content[k++] = pageListVO.getTelphone();
            content[k++] = pageListVO.getReason();
            sheetData.add(content);
            if ((j + 1) % ExcelExportConfig.MAX_SIZE == 0 || (j + 1) == length) {
                int sheetNum = j / ExcelExportConfig.MAX_SIZE;
                ExcelBean littleExcelBean = new ExcelBean();
                littleExcelBean.setNum(sheetNum);
                littleExcelBean.setName(name);
                littleExcelBean.setSheetName(name + (sheetNum * ExcelExportConfig.MAX_SIZE + 1) + "-" + (j + 1) + "条");
                littleExcelBean.setHeaderCenter(name);
                littleExcelBean.setTableHeader(tableHeads);
                littleExcelBean.setColWidth(ExcelUtil.getColWidth(tableHeads.length));
                littleExcelBean.setSheetData(sheetData);
                sheetsMap.put(sheetNum, littleExcelBean);
                sheetData = ContainerUtil.aList();
            }
        }
        excelBean.setSheets(sheetsMap);
        return excelBean;
    }
}
