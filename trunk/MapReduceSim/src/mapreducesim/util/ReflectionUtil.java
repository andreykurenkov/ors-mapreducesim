package mapreducesim.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtil {

	public static <T> T attemptConstructorCallAndCast(Class<T> castTo, Class<?> attemptOn, Object... params)
			throws InstantiationException, InvocationTargetException, ClassCastException {
		Object object = attemptConstructorCall(attemptOn, params);
		if (object != null) {
			return castTo.cast(object);
		}
		return null;
	}

	public static Object attemptConstructorCall(Class<?> attemptOn, Object... params) throws InstantiationException,
			InvocationTargetException {
		Class<?>[] paramTypes = new Class<?>[params.length];
		for (int i = 0; i < params.length; i++) {
			paramTypes[i] = params[i].getClass();
		}
		Constructor<?> constructor = getMatchingConstructor(attemptOn, paramTypes);
		if (constructor != null) {
			try {
				return constructor.newInstance(params);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Constructor<?> getMatchingConstructor(Class<?> loadFrom, Class<?>... constructorParams) {
		Constructor<?>[] constructors = loadFrom.getConstructors();
		for (Constructor<?> constructor : constructors) {
			Class<?>[] params = constructor.getParameterTypes();
			if (params.length == constructorParams.length) {
				boolean valid = true;
				for (int i = 0; i < params.length; i++) {
					if (!params[i].isAssignableFrom(constructorParams[i])) {
						valid = false;
						break;
					}
				}
				if (!valid)
					continue;
				else {
					return constructor;
				}
			}
		}
		return null;
	}
}