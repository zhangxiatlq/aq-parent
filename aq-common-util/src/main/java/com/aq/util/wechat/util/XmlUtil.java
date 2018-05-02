package com.aq.util.wechat.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.aq.util.wechat.domain.Article;
import com.aq.util.wechat.domain.NewsMessage;
import com.aq.util.wechat.domain.TextMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;


/**
 * @version 1.0
 * @项目：aq项目
 * @describe：xml处理工具类
 * @author： 张霞
 * @createTime： 2018/03/05
 * @Copyright @2017 by zhangxia
 */
public class XmlUtil {
    /**
     * 
    * @Title: xmlToMap  
    * @Description: xml转map 
    * @param: @param request
    * @param: @return
    * @param: @throws IOException
    * @param: @throws DocumentException
    * @return Map<String,String>
    * @author lijie
    * @throws
     */
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException {
		request.setCharacterEncoding("UTF-8");
		HashMap<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();

		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);

		Element root = doc.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> list = (List<Element>) root.elements();

		for (Element e : list) {
			map.put(e.getName(), e.getText());
		}
		ins.close();
		return map;
	}
	/**
	 * 
	* @Title: textMessageToXml  
	* @Description: 文本消息对象转换成xml 
	* @param: @param textMessage
	* @param: @return
	* @return String
	* @author lijie
	* @throws
	 */
    public static String textMessageToXml(TextMessage textMessage) {
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }
 
    /**
     * 
    * @Title: newsMessageToXml  
    * @Description: 图文消息对象转换成xml 
    * @param: @param newsMessage
    * @param: @return
    * @return String
    * @author lijie
    * @throws
     */
    public static String newsMessageToXml(NewsMessage newsMessage) {
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", new Article().getClass());
        return xstream.toXML(newsMessage);
    }
	/**
	 * 对象到xml的处理
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});

}
