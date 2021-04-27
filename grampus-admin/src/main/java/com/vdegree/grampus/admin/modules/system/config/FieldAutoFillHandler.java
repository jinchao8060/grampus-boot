package com.vdegree.grampus.admin.modules.system.config;

import com.google.common.collect.Maps;
import com.vdegree.grampus.admin.modules.system.security.utils.SecurityUtils;
import com.vdegree.grampus.common.core.utils.ReflectUtil;
import com.vdegree.grampus.common.mybatis.annotation.FieldFill;
import com.vdegree.grampus.common.mybatis.annotation.TableField;
import com.vdegree.grampus.common.mybatis.handler.FieldFillHandler;
import com.vdegree.grampus.common.mybatis.interceptor.TableFieldObject;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Title: 字段自动填充处理器
 * Company: v-degree
 *
 * @author Beck
 * @date 2021-04-26
 */
@Component
public class FieldAutoFillHandler implements FieldFillHandler {
	private final static String CREATE_BY = "createBy";
	private final static String CREATE_DATE = "createDate";
	private final static String UPDATE_BY = "updateBy";
	private final static String UPDATE_DATE = "updateDate";

	@Override
	public void fill(TableFieldObject tableFieldObject) {
		Long currentUserId = Objects.requireNonNull(SecurityUtils.getUserDetails()).getId();
		Date currentDate = new Date();

		SqlCommandType sqlCommandType = tableFieldObject.getSqlCommandType();
		Object paramObj = tableFieldObject.getParamObj();
		List<Field> fields = tableFieldObject.getFields();
		Map<FieldFill, List<Field>> fillFieldMap = fields.stream()
				.collect(Collectors.groupingBy(field -> field.getAnnotation(TableField.class).fill()));

		boolean isInsertSql = SqlCommandType.INSERT.equals(sqlCommandType);
		boolean isUpdateSql = SqlCommandType.UPDATE.equals(sqlCommandType);
		for (Map.Entry<FieldFill, List<Field>> fillFieldEntry : fillFieldMap.entrySet()) {
			FieldFill fill = fillFieldEntry.getKey();
			boolean withInsertFill = FieldFill.INSERT.equals(fill) || FieldFill.INSERT_UPDATE.equals(fill);
			boolean withUpdateFill = FieldFill.UPDATE.equals(fill) || FieldFill.INSERT_UPDATE.equals(fill);
			for (Field field : fillFieldEntry.getValue()) {
				field.setAccessible(true);
				if (isInsertSql && withInsertFill) {
					// INSERT SQL TODO 没有设置值才自动填充
					this.insertFillIfNull(fill, paramObj, field, currentUserId, currentDate);
				} else if (isUpdateSql && withUpdateFill) {
					// UPDATE SQL
					this.updateFillIfNull(fill, paramObj, field, currentUserId, currentDate);
				}
			}
		}
	}

	private void insertFillIfNull(FieldFill fill, Object paramObj, Field field, Long currentUserId, Date currentDate) {
		if (!FieldFill.INSERT.equals(fill) && !FieldFill.INSERT_UPDATE.equals(fill)) {
			return;
		}
		if (CREATE_BY.equals(field.getName()) || UPDATE_BY.equals(field.getName())) {
			ReflectUtil.setField(field, paramObj, currentUserId);
		} else if (CREATE_DATE.equals(field.getName()) || UPDATE_DATE.equals(field.getName())) {
			ReflectUtil.setField(field, paramObj, currentDate);
		}
	}

	private void updateFillIfNull(FieldFill fill, Object paramObj, Field field, Long currentUserId, Date currentDate) {
		if (!FieldFill.UPDATE.equals(fill) && !FieldFill.INSERT_UPDATE.equals(fill)) {
			return;
		}
		if (UPDATE_BY.equals(field.getName())) {
			ReflectUtil.setField(field, paramObj, currentUserId);
		} else if (UPDATE_DATE.equals(field.getName())) {
			ReflectUtil.setField(field, paramObj, currentDate);
		}
	}
}
