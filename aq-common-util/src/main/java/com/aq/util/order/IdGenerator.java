package com.aq.util.order;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IdGenerator {

	/** 用ip地址最后几个字节标示 **/
	private static long workerId;
	/** 可配置在properties中,启动时加载,此处默认先写成0 **/
	private static long datacenterId = 0L;
	private static long sequence = 0L;
	/** 节点ID长度 **/
	private static long workerIdBits = 8L;
	/** 序列号12位 **/
	private static long sequenceBits = 12L;
	/** 机器节点左移12位 **/
	private static long workerIdShift = sequenceBits;
	/** 数据中心节点左移14位 **/
	private static long dataCenterIdShift = sequenceBits + workerIdBits;
	/** 4095 **/
	private static long sequenceMask = -1L ^ (-1L << sequenceBits);
	private static long lastTimestamp = -1L;

	public IdGenerator() {
		workerId = 0x000000FF & getLastIP();
	}

	/**
	 * 获取唯一订单号
	 * 
	 * @param prefix
	 * @return
	 * @author Mr.Chang
	 */
	public static synchronized String nextId(String prefix) {
		// 获取当前毫秒数
		long timestamp = timeGen();
		// 如果服务器时间有问题(时钟后退) 报错。
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}
		// 如果上次生成时间和当前时间相同,在同一毫秒内
		if (lastTimestamp == timestamp) {
			// sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
			sequence = (sequence + 1) & sequenceMask;
			// 判断是否溢出,也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
			if (sequence == 0) {
				// 自旋等待到下一毫秒
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			// 如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
			sequence = 0L;
		}
		lastTimestamp = timestamp;
		long suffix = (datacenterId << dataCenterIdShift) | (workerId << workerIdShift) | sequence;
		String datePrefix = DateFormatUtils.format(timestamp, "yyyyMMddHHMMssSSS");
		return new StringBuilder(prefix).append(suffix).append(datePrefix).toString();
	}

	/**
	 * 获取充值订单号
	 * 
	 * @param prefix
	 * @return
	 * @author Mr.Chang
	 */
	public synchronized String nextIdWithoutSuffix(String prefix) {
		// 获取当前毫秒数
		long timestamp = timeGen();
		// 如果服务器时间有问题(时钟后退) 报错。
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}
		// 如果上次生成时间和当前时间相同,在同一毫秒内
		if (lastTimestamp == timestamp) {
			// sequence自增，因为sequence只有12bit，所以和sequenceMask相与一下，去掉高位
			sequence = (sequence + 1) & sequenceMask;
			// 判断是否溢出,也就是每毫秒内超过4095，当为4096时，与sequenceMask相与，sequence就等于0
			if (sequence == 0) {
				// 自旋等待到下一毫秒
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			// 如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加
			sequence = 0L;
		}
		lastTimestamp = timestamp;
		String datePrefix = DateFormatUtils.format(timestamp, "yyyyMMddHHMMssSSS");
		return prefix + datePrefix;
	}

	protected static long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	/**
	 * 获取当前时间戳
	 * 
	 * @return
	 * @author Mr.chang
	 */
	protected static long timeGen() {
		return System.currentTimeMillis();
	}

	/**
	 * 获取IP
	 * 
	 * @return
	 */
	private byte getLastIP() {
		byte lastIp = 0;
		try {
			InetAddress ip = InetAddress.getLocalHost();
			byte[] ipByte = ip.getAddress();
			lastIp = ipByte[ipByte.length - 1];
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return lastIp;
	}

	public static String getBizeCode(String orderNo) {
		return StringUtils.substring(orderNo, 0, 5);
	}
}
