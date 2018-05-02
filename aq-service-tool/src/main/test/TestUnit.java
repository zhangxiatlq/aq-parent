import com.alibaba.dubbo.config.annotation.Reference;
import com.aq.AqServiceToolApplication;
import com.aq.service.impl.ToolServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;

/**
 * @author:zhangxia
 * @createTime: 2018/4/26 18:54
 * @version:1.0
 * @desc:
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@Configurable
@ComponentScan(basePackages = "com.aq.*")
@SpringBootTest(classes = AqServiceToolApplication.class)
public class TestUnit {

    @Autowired
    private ToolServiceImpl service;

    @Test
    public void test222(){

        try {
            service.timingCountToolIncomeRate(true);
        } catch (ParseException e) {
            log.info("e=={}",e);
        }
    }
}
