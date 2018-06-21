package com.kratos.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.beans.Expression;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 反射工具类
 *
 * @author tanghe
 * @created 2012-7-16 下午1:35:11
 * @since v1.0
 * @history
 * @see
 */
public class ReflectionUtils {

	private static Logger logger = LoggerFactory
			.getLogger(ReflectionUtils.class);

	/**
	 *
	 * 根据class名字，调用默认的无参构造函数实例化对象
	 *
	 * @param className
	 *            类名
	 * @return 实例
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:00
	 */
	public static Object newInstance(String className) {
		Assert.notNull(className, "ClassName must not be null");
		try {
			return Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Cannot create " + className + ": "
					+ e.getMessage());
		}
	}

	/**
	 * 实例化对象
	 *
	 * @param clazz
	 * @return
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static <T> T newInstance(Class<T> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Cannot create " + clazz.getName()
					+ ": " + e.getMessage());
		}
	}

	/**
	 *
	 * 根据class名字，调用默认的无参构造函数实例化对象
	 *
	 * @param className
	 *            类名
	 * @param args
	 *            构造函数的参数
	 * @return 实例
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static Object newInstance(String className, Object... args) {
		Assert.notNull(className, "ClassName must not be null");
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (Exception e) {
			throw new RuntimeException("Cannot find class with name"
					+ className + ": " + e.getMessage());
		}
		return newInstance(clazz, args);
	}

	/**
	 *
	 * 实例化对象。根据参数类型自动找到Constructor，并完成调用。
	 *
	 * @param clazz
	 *            类
	 * @param args
	 *            构造函数的参数
	 * @return 实例
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<T> clazz, Object... args) {
		Expression expression = new Expression(clazz, "new", args);
		try {
			return (T) expression.getValue();
		} catch (Exception e) {
			throw new RuntimeException("Cannot create " + clazz.getName()
					+ ": " + e.getMessage());
		}
	}

	/**
	 *
	 * 调用没有参数的方法,任何类型的访问域都可以调用
	 *
	 * @param method
	 *            方法
	 * @param target
	 *            方法所属的对象
	 * @return 方法调用的返回值
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static Object invokeMethod(Method method, Object target) {
		return invokeMethod(method, target, (Object[]) null);
	}

	/**
	 *
	 * 调用方法,任何类型的访问域都可以调用
	 *
	 * @param method
	 *            方法
	 * @param target
	 *            方法所属的对象
	 * @param args
	 *            参数可以是1至多个对象，或者对象数组
	 * @return 方法调用的返回值
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static Object invokeMethod(Method method, Object target,
			Object... args) {
		makeAccessible(method);
		try {
			return method.invoke(target, args);
		} catch (Exception ex) {
			handleReflectionException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}

	/**
	 *
	 * 调用对象的public方法
	 *
	 * @param target
	 * @param methodName
	 * @param arguments
	 * @return
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static Object invokePublicMethod(Object target, String methodName,
			Object[] arguments) {
		Expression expression = new Expression(target, methodName, arguments);
		try {
			return expression.getValue();
		} catch (Exception e) {
			handleReflectionException(e);
		}
		throw new IllegalStateException("Should never get here");
	}

	public static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * Make the given method accessible, explicitly setting it accessible if
	 * necessary. The <code>setAccessible(true)</code> method is only called
	 * when actually necessary, to avoid unnecessary conflicts with a JVM
	 * SecurityManager (if active).
	 *
	 * @param method
	 *            the method to make accessible
	 * @see Method#setAccessible
	 */
	public static void makeAccessible(Method method) {
		if (!Modifier.isPublic(method.getModifiers())
				|| !Modifier
						.isPublic(method.getDeclaringClass().getModifiers())) {
			method.setAccessible(true);
		}
	}

	/**
	 * Make the given constructor accessible, explicitly setting it accessible
	 * if necessary. The <code>setAccessible(true)</code> method is only called
	 * when actually necessary, to avoid unnecessary conflicts with a JVM
	 * SecurityManager (if active).
	 *
	 * @param ctor
	 *            the constructor to make accessible
	 * @see Constructor#setAccessible
	 */
	public static <T> void makeAccessible(Constructor<T> ctor) {
		if (!Modifier.isPublic(ctor.getModifiers())
				|| !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) {
			ctor.setAccessible(true);
		}
	}

	/**
	 * Handle the given reflection exception. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>
	 * Throws the underlying RuntimeException or Error in case of an
	 * InvocationTargetException with such a root cause. Throws an
	 * IllegalStateException with an appropriate message else.
	 *
	 * @param ex
	 *            the reflection exception to handle
	 */
	public static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException)
			throw new IllegalStateException("Method not found: "
					+ ex.getMessage());
		if (ex instanceof IllegalAccessException)
			throw new IllegalStateException("Could not access method: "
					+ ex.getMessage());
		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		if (ex instanceof RuntimeException)
			throw (RuntimeException) ex;
		handleUnexpectedException(ex);
	}

	/**
	 * Handle the given invocation target exception. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>
	 * Throws the underlying RuntimeException or Error in case of such a root
	 * cause. Throws an IllegalStateException else.
	 *
	 * @param ex
	 *            the invocation target exception to handle
	 */
	public static void handleInvocationTargetException(
			InvocationTargetException ex) {
		rethrowRuntimeException(ex.getTargetException());
	}

	/**
	 * Rethrow the given {@link Throwable exception}, which is presumably the
	 * <em>target exception</em> of an {@link InvocationTargetException}. Should
	 * only be called if no checked exception is expected to be thrown by the
	 * target method.
	 * <p>
	 * Rethrows the underlying exception cast to an {@link RuntimeException} or
	 * {@link Error} if appropriate; otherwise, throws an
	 * {@link IllegalStateException}.
	 *
	 * @param ex
	 *            the exception to rethrow
	 * @throws RuntimeException
	 *             the rethrown exception
	 */
	public static void rethrowRuntimeException(Throwable ex) {
		if (ex instanceof RuntimeException)
			throw (RuntimeException) ex;
		if (ex instanceof Error)
			throw (Error) ex;
		handleUnexpectedException(ex);
	}

	/**
	 * Throws an IllegalStateException with the given exception as root cause.
	 *
	 * @param ex
	 *            the unexpected exception
	 */
	private static void handleUnexpectedException(Throwable ex) {
		// Needs to avoid the chained constructor for JDK 1.4 compatibility.
		IllegalStateException isex = new IllegalStateException(
				"Unexpected exception thrown");
		isex.initCause(ex);
		throw isex;
	}

	/**
	 *
	 * 调用'读'方法.
	 *
	 * @param target
	 *            JavaBean
	 * @param propertyName
	 *            属性名
	 * @return 属性值
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static Object invokeReadMethod(Object target, String propertyName) {
		Method method = getReadMethod(target.getClass(), propertyName);
		Assert.notNull(method, "Could not find ReadMethod of field["
				+ propertyName + "] on target [" + target.getClass().getName()
				+ "]");
		return invokeMethod(method, target);
	}

	/**
	 *
	 * 调用'写'方法.使用value的Class来查找写方法.
	 *
	 * @param target
	 *            JavaBean
	 * @param propertyName
	 *            属性名
	 * @param value
	 *            属性值
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static void invokeWriteMethod(Object target, String propertyName,
			Object value) {
		String setterMethodName = "set" + StringUtils.capitalize(propertyName);
		invokePublicMethod(target, setterMethodName, new Object[] { value });
	}

	/**
	 *
	 * 从class以及其父类中,根据属性名获取JavaBean的'读'方法。<br/>
	 * 如果属性类型是boolean，则获取的方法形式是is+属性名，否则是"get"+属性名的形式。
	 *
	 * @param clazz
	 * @param propertyName
	 * @return
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static Method getReadMethod(Class<?> clazz, String propertyName) {
		PropertyDescriptor pdescriptor = getPropertyDescriptor(clazz,
				propertyName);
		return pdescriptor == null ? null : pdescriptor.getReadMethod();
	}

	/**
	 *
	 * 从class以及其父类中,根据属性名获取JavaBean的写方法。<br/>
	 * 获取的写方法名字是"set"+属性名的形式。
	 *
	 * @param clazz
	 *            类
	 * @param propertyName
	 *            属性名
	 * @return JavaBean 的写方法
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static Method getWriteMethod(Class<?> clazz, String propertyName) {
		PropertyDescriptor pdescriptor = getPropertyDescriptor(clazz,
				propertyName);
		return pdescriptor == null ? null : pdescriptor.getWriteMethod();
	}

	/**
	 *
	 * 获取给定class以及其父类中的全部属性描述信息
	 *
	 * @param clazz
	 *            类
	 * @return 全部属性描述信息
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
		return BeanUtils.getPropertyDescriptors(clazz);
	}

	/**
	 *
	 * 根据属性名，获取给定class以及其父类中的属性描述信息
	 *
	 * @param clazz
	 *            类
	 * @param propertyName
	 *            属性名
	 * @return 属性描述信息
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz,
			String propertyName) {
		return BeanUtils.getPropertyDescriptor(clazz, propertyName);
	}

	/**
	 *
	 * 从class以及其父类中获取指定名字的Field，包括公共、保护、默认（包）访问和私有字段。
	 *
	 * @param clazz
	 *            类
	 * @param fieldName
	 *            属性名
	 * @return
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static Field getDeclaredField(Class<?> clazz, String fieldName) {
		return org.springframework.util.ReflectionUtils.findField(clazz, fieldName);
	}

	/**
	 * 从class以及其父类中获取指定名字的Field，包括公共、保护、默认（包）访问和私有字段。
	 *
	 *
	 * @param clazz
	 *            类
	 * @param fieldName
	 *            属性名
	 * @param type
	 *            属性类型
	 * @return
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static Field getDeclaredField(Class<?> clazz, String fieldName,
			Class<?> type) {
		return org.springframework.util.ReflectionUtils.findField(clazz, fieldName, type);
	}

	/**
	 *
	 * 获取所有的属性，包括自己以及父类中 公共、保护、默认（包）访问和私有字段。
	 *
	 * @param clazz
	 *            类
	 * @return
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static Field[] getDeclaredFields(final Class<?> clazz) {
		Assert.notNull(clazz, "clazz不能为空");
		List<Field> fields = new ArrayList<Field>();
		for (Class<?> type = clazz; type != Object.class; type = type
				.getSuperclass()) {
			fields.addAll(Arrays.asList(type.getDeclaredFields()));
		}
		return fields.toArray(new Field[fields.size()]);
	}

	/**
	 *
	 * 获取目标对象上指定属性对象的值
	 *
	 * @param field
	 *            属性对象
	 * @param target
	 *            目标对象
	 * @return 目标对象上指定属性对象的值
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static Object getFieldValue(Object target, Field field) {
		makeAccessible(field);
		return org.springframework.util.ReflectionUtils.getField(field, target);
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object object,
			final String fieldName) {
		Field field = getDeclaredField(object.getClass(), fieldName);
		Assert.notNull(field, "Could not find field [" + fieldName
				+ "] on target [" + object + "]");
		return getFieldValue(object, field);
	}

	/**
	 *
	 * 获取目标对象上指定属性对象的值
	 *
	 * @param field
	 *            属性对象
	 * @param target
	 *            目标对象
	 * @return 目标对象上指定属性对象的值
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static void setFieldValue(Field field, Object target, Object value) {
		makeAccessible(field);
		org.springframework.util.ReflectionUtils.setField(field, target, value);
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 *
	 * @param object
	 * @param fieldName
	 * @param value
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static void setFieldValue(final Object object,
			final String fieldName, final Object value) {
		Field field = getDeclaredField(object.getClass(), fieldName);
		Assert.notNull(field, "Could not find field [" + fieldName
				+ "] on target [" + object + "]");
		setFieldValue(field, object, value);
	}


	/**
	 * 根据名字、参数类型从class以及其父类中获取相应的方法，包括公共、保护、默认（包）访问和私有方法。
	 *
	 * @param clazz
	 *            类
	 * @param methodName
	 *            方法名
	 * @param parmTypes
	 *            参数类型
	 * @return
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static Method getDeclaredMethod(Class<?> clazz, String methodName,
			Class<?>[] paramTypes) {
		return org.springframework.util.ReflectionUtils.findMethod(clazz, methodName, paramTypes);
	}

	/**
	 * 根据名字，从class以及其父类中获取相应的方法，包括公共、保护、默认（包）访问和私有方法。
	 *
	 * @param clazz
	 *            类
	 * @param methodName
	 *            方法名
	 * @return
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	public static Method getDeclaredMethod(Class<?> clazz, String methodName) {
		return org.springframework.util.ReflectionUtils.findMethod(clazz, methodName, (Class<?>[]) null);
	}

	/**
	 * 通过反射,获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao
	 * extends HibernateDao<User>
	 *
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be
	 *         determined
	 * @author tanghe
	 * @created 2012-7-16 下午1:36:23
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenricType(final Class<?> clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
	 *
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz
	 *            clazz The class to introspect
	 * @param index
	 *            the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be
	 *         determined
	 * @author tanghe
	 * @created 2012-7-16 下午1:39:28
	 */
	@SuppressWarnings("rawtypes")
	public static Class getSuperClassGenricType(final Class clazz,
			final int index) {
		Type genType = null;
		Class<?> parentClass = clazz;
		do {
			genType = parentClass.getGenericSuperclass();
			if (genType instanceof ParameterizedType) {
				break;
			}
			parentClass = parentClass.getSuperclass();
			if (parentClass.equals(Object.class)) {
				logger.warn(clazz.getSimpleName()
						+ "'s superclass not ParameterizedType");
				return Object.class;
			}
		} while (true);

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of "
					+ clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName()
					+ " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}

	public static Class<?> getFiledGenricType(final Field field, final int index) {

		Type genType = field.getGenericType();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(field.getName() + "'s field not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + field.getName()
					+ "'s Parameterized Type: " + params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class<?>)) {
			logger.warn(field.getName()
					+ " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class<?>) params[index];
	}

	/**
	 * 从指定类中查找Annotation，可以选择是否从父类、父接口中查找
	 *
	 * @param <A>
	 * @param sourceClass
	 * @param annotationClass
	 * @param containSuperClass
	 *            是否从基类中查找
	 * @param containSuperInterface
	 *            是否从接口中查找
	 * @return
	 * @author tanghe
	 * @created 2012-7-16 下午1:39:28
	 *
	 */
	public static <A extends Annotation> A getAnnotation(Class<?> sourceClass,
			Class<A> annotationClass, boolean containSuperClass,
			boolean containSuperInterface) {
		if (sourceClass == null) {
			return null;
		}
		A annotation = sourceClass.getAnnotation(annotationClass);
		if (annotation == null && containSuperClass) {
			annotation = getAnnotation(sourceClass.getSuperclass(),
					annotationClass, true, containSuperInterface);
		}
		if (annotation == null && containSuperInterface) {
			for (Class<?> clazz : sourceClass.getInterfaces()) {
				annotation = getAnnotation(clazz, annotationClass,
						containSuperClass, false);
				if (annotation != null) {
					return annotation;
				}
			}
		}
		return annotation;
	}

	/**
	 * 获取类的基类、接口中是superClazz子类或实现的所有Class集合
	 *
	 * @param clazz
	 * @param superClazz
	 * @return
	 * @author tanghe
	 * @created 2012-7-16 下午1:39:28
	 */
	public static List<Class<?>> getSuperClassAndInterface(Class<?> clazz,
			Class<?> superClazz) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		Class<?> parentClass = clazz;
		do {
			if (superClazz.isAssignableFrom(parentClass)) {
				result.add(parentClass);
				parentClass = parentClass.getSuperclass();
			} else {
				break;
			}
			if (superClazz.isInterface()) {
				for (Class<?> interfaceClass : parentClass.getInterfaces()) {
					if (superClazz.isAssignableFrom(interfaceClass)) {
						result.add(interfaceClass);
					}
				}
			}

		} while (true);
		return result;
	}

}
