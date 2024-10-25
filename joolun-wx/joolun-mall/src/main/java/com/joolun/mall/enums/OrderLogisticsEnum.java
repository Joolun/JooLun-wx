package com.joolun.mall.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 快递单当前状态，包括-1错误，0在途，1揽收，2疑难，3签收，4退签，5派件，6退回，7转投 等7个状态
 */
public enum OrderLogisticsEnum implements IEnum<String> {

	STATUS_ER("ER","错误"),
	STATUS_0("0","在途"),
	STATUS_1("1","揽收中"),
	STATUS_2("2","疑难"),
	STATUS_3("3","已签收"),
	STATUS_4("4","退签"),
	STATUS_5("5","派件中"),
	STATUS_6("6","退回"),
	STATUS_7("7","转投"),

	LOGISTICS_TIANTIAN("tiantian","天天快递"),
	LOGISTICS_HUITONGKUAIDI("huitongkuaidi","百世快递"),
	LOGISTICS_YUNDA("yunda","韵达快递"),
	LOGISTICS_YUANTONG("yuantong","圆通速递"),
	LOGISTICS_DEBANGWULIU("debangwuliu","德邦"),
	LOGISTICS_EMS("ems","EMS"),
	LOGISTICS_SHUNFENG("shunfeng","顺丰速运"),
	LOGISTICS_ZHONGTONG("zhongtong","中通快递"),
	LOGISTICS_SHENTONG("shentong","申通快递"),
	;
	OrderLogisticsEnum(final String value, final String desc) {
		this.value = value;
		this.desc = desc;
	}
	public static String LOGISTICS_PREFIX = "LOGISTICS";
	public static String STATUS_PREFIX = "STATUS";
	private String value;
	private String desc;

	@Override
	public String getValue() {
		return this.value;
	}

	@JsonValue
	public String getDesc(){
		return this.desc;
	}

	/**
	 * 获取所有字典
	 * @param prefix
	 * @return
	 */
	public static List<Map<String,String>> queryAll(String prefix) {
		List<Map<String,String>> list = new ArrayList<>();
		for (OrderLogisticsEnum t : OrderLogisticsEnum.values()) {
			if(t.name().contains(prefix)){
				Map<String,String> mp = new HashMap();
				mp.put("value",t.getValue());
				mp.put("label",t.getDesc());
				list.add(mp);
			}
		}
		return list;
	}
}
