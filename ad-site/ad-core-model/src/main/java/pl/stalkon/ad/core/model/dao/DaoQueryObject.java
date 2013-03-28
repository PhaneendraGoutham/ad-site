package pl.stalkon.ad.core.model.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.internal.util.ReflectionHelper;

public class DaoQueryObject {
	
	enum CompareType{MORE,LESS,EQUAL}
	
	public String name;
	public Object value;
	public CompareType type;
	
	public DaoQueryObject(String name, Object value, CompareType type) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
	}
	
	public DaoQueryObject(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
		this.type = CompareType.EQUAL;
	}
	
	
	public void addCriteria(Criteria criteria, String alias, Class<?> clazz){
		if(!ReflectionHelper.containsDeclaredField(clazz, name))
			return;
		alias = alias != ""? alias + ".": "";
		if(type==CompareType.EQUAL)
			criteria.add(Restrictions.eq(alias + name, value));
		else if(type==CompareType.LESS){
			criteria.add(Restrictions.le(alias + name, value));
		}else{
			criteria.add(Restrictions.ge(alias + name, value));
		}
	}
}
