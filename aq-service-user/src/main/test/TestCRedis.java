import com.aq.AqServiceUserApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：
 * @author： 张霞
 * @createTime： 2018/03/13
 * @Copyright @2017 by zhangxia
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AqServiceUserApplication.class)
public class TestCRedis{

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void t1(){
        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
        stringStringValueOperations.set("testkey","testvalue");
        String testkey = stringStringValueOperations.get("testkey");
        log.info(testkey);
    }

    @Test
    public void t2() {
        for (int i = 0; i <= 2; i++) {
            JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) stringRedisTemplate.getConnectionFactory();
            jedisConnectionFactory.setDatabase(i);
            stringRedisTemplate.setConnectionFactory(jedisConnectionFactory);
            ValueOperations valueOperations = stringRedisTemplate.opsForValue();

            int test = (int) valueOperations.get("test");
            log.info(test+"");
        }
    }

    @Test
    public void t3() throws ParseException {
            log.info("测试执行");
        String DateStr1 = "2018-4-27 12:20:16";
        String DateStr2 = "2011-10-07 15:50:35";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime1 = dateFormat.parse(DateStr1);
        Date dateTime2 = dateFormat.parse(DateStr2);
        int i = dateTime1.compareTo(new Date());
        System.out.println(i < 0);

        BigDecimal rate1 = new BigDecimal("18.33333").divide(new BigDecimal("10"),4,BigDecimal.ROUND_HALF_UP);

        BigDecimal rate = new BigDecimal("17.3").divide(new BigDecimal("19.38"),4,BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1"));
        System.out.println(rate.doubleValue());
    }
}
