import com.aq.AqServiceStrategyApplication;
import com.aq.facade.dto.RenewStrategyDTO;
import com.aq.mapper.StrategyPurchaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@Configurable
@ComponentScan(basePackages = "com.aq.*")
@SpringBootTest(classes = AqServiceStrategyApplication.class)
public class TestUpdate {

    @Autowired
    StrategyPurchaseMapper strategyPurchaseMapper;

    @Test
    public void tt(){
        List<RenewStrategyDTO> renewStrategyDTOS = new ArrayList<>();
        //id=1 --2018-03-14 11:13:16
        RenewStrategyDTO renewStrategyDTO1 = new RenewStrategyDTO();
        renewStrategyDTO1.setPurchaserId(1);
        renewStrategyDTO1.setStrategyId(1);
        renewStrategyDTO1.setPurchaserType((byte)3);

        renewStrategyDTOS.add(renewStrategyDTO1);

        //id=2 --2018-03-15 11:13:16
        RenewStrategyDTO renewStrategyDTO2 = new RenewStrategyDTO();
        renewStrategyDTO2.setPurchaserId(2);
        renewStrategyDTO2.setStrategyId(2);
        renewStrategyDTO2.setPurchaserType((byte)3);
        renewStrategyDTOS.add(renewStrategyDTO2);
        //id=3 -- 2018-04-25 11:13:16
        RenewStrategyDTO renewStrategyDTO3 = new RenewStrategyDTO();
        renewStrategyDTO3.setPurchaserId(186);
        renewStrategyDTO3.setStrategyId(2);
        renewStrategyDTO3.setPurchaserType((byte)3);
        renewStrategyDTOS.add(renewStrategyDTO3);

        int s = strategyPurchaseMapper.updateRenewStrategyPurchase(renewStrategyDTOS);
        log.info("批量更新结果：{}",s);




    }
}
