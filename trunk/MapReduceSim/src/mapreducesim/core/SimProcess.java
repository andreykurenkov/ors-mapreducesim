package mapreducesim.core;

import org.simgrid.msg.Host;
import org.simgrid.msg.Msg;
import org.simgrid.msg.MsgException;
import org.simgrid.msg.Process;
import org.simgrid.msg.Task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

public class SimProcess extends Process {
	private HashMap<Class<?>, Method> eventHandlers;
	private HashMap<Class<?>, Method> hierarchyLeafClasses;
	private HashMap<Class<?>, Method> finishEventHandlers;
	private HashMap<Class<?>, Method> finishHierarchyLeafClasses;

	protected boolean finish;

	@Override
	public void main(String[] arg0) throws MsgException {
		finish = false;
		while (!finish) {
			step();
		}

	}

	protected void step() throws MsgException {
		this.waitFor(MapReduceSimMain.simStep);
		Task received = Task.receive(this.getHost().getName());
		this.handleTask(received);
	}

	public SimProcess(Host host, String name) {
		super(host, name);
		eventHandlers = new HashMap<Class<?>, Method>();
		hierarchyLeafClasses = new HashMap<Class<?>, Method>();
		finishEventHandlers = new HashMap<Class<?>, Method>();
		finishHierarchyLeafClasses = new HashMap<Class<?>, Method>();
		this.name = name;
		for (Method method : getClass().getMethods()) {
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length == 1) {
				Class<?> parameter = parameterTypes[0];
				if (Task.class.isAssignableFrom(parameter)) {
					String methodName = method.getName();
					String className = parameter.getCanonicalName();
					className = className.substring(className.lastIndexOf('.') + 1);
					if (methodName.equals("handle" + className)) {
						Msg.debug("Module " + this.getClass().getName() + " has handler for " + parameter.getName());
						eventHandlers.put(parameter, method);
					} else if (methodName.equals("handle" + className + "Completion")) {
						Msg.debug("Module " + this.getClass().getName() + " has completion handler for "
								+ parameter.getName());
						finishEventHandlers.put(parameter, method);
					}
				}
			}
		}
		for (Class<?> eventClass : eventHandlers.keySet()) {
			boolean assignable = false;
			for (Class<?> otherEventClass : eventHandlers.keySet()) {
				if (eventClass != otherEventClass && eventClass.getClass().isAssignableFrom(otherEventClass)) {
					assignable = true;
					break;
				}
			}
			if (!assignable) {
				hierarchyLeafClasses.put(eventClass, eventHandlers.get(eventClass));
			}
		}
		for (Class<?> eventClass : finishEventHandlers.keySet()) {
			boolean assignable = false;
			for (Class<?> otherEventClass : finishEventHandlers.keySet()) {
				if (eventClass != otherEventClass && eventClass.getClass().isAssignableFrom(otherEventClass)) {
					assignable = true;
					break;
				}
			}
			if (!assignable) {
				finishHierarchyLeafClasses.put(eventClass, finishEventHandlers.get(eventClass));
			}
		}
	}

	private final Method getEventHandler(Task task, boolean completion) {
		HashMap<Class<?>, Method> handlers = completion ? finishEventHandlers : eventHandlers;
		HashMap<Class<?>, Method> hierarchy = completion ? finishHierarchyLeafClasses : hierarchyLeafClasses;
		Class<?> eventClass = task.getClass();
		if (handlers.containsKey(eventClass))
			return handlers.get(eventClass);
		Method method = null;
		for (Class<?> knownEventClass : hierarchy.keySet()) {
			if (task.getClass().isAssignableFrom(knownEventClass)) {
				Method eventMethod = handlers.get(knownEventClass);
				hierarchy.put(eventClass.getClass(), eventMethod);
				while (!handlers.containsKey(eventClass)) {
					handlers.put(eventClass, eventMethod);
					eventClass = eventClass.getSuperclass();
				}
			}
		}
		return method;
	}

	private final boolean genericHandler(Task task, boolean completion) {
		Method handler = getEventHandler(task, completion);
		if (handler != null) {
			try {
				handler.invoke(this, task);
				return true;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	protected final boolean handleTask(Task task) {
		return genericHandler(task, false);
	}

	protected final boolean handleTaskCompletion(Task task) {
		return genericHandler(task, true);
	}

}
