package binder;

import java.lang.reflect.InvocationTargetException;

public class BinderPropsAndFields extends AbstractBinder {

    private AbstractBinder binderF = new BinderFields();
    private AbstractBinder binderP = new BinderProps();

    
    @Override
    <T> boolean bindMember(T target, String key, Object value) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if(!binderP.bindMember(target, key, value))
            return binderF.bindMember(target, key, value);
        return true;
    }

}
