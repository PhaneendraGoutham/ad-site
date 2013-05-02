package pl.stalkon.ad.core.model.dto;

import java.util.ArrayList;
import java.util.List;

import pl.styall.library.core.model.CommonEntity;

public class CheckboxListWrapper<T extends CommonEntity> {
	private T object;
	private boolean checked;

	public CheckboxListWrapper(T object, boolean checked) {
		super();
		this.object = object;
		this.checked = checked;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public static <T extends CommonEntity> List<CheckboxListWrapper<T>> getList(
			List<T> objects, List<Long> checkedIds) {

		List<CheckboxListWrapper<T>> list = new ArrayList<CheckboxListWrapper<T>>(
				objects.size());
		for (T object : objects) {
			if (checkedIds == null || !checkedIds.contains(object.getId())) {
				list.add(new CheckboxListWrapper<T>(object, false));
			} else {
				list.add(new CheckboxListWrapper<T>(object, true));
			}
		}
		return list;
	}

}
