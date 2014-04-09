package pt.isel.mpd14.probe;

import static pt.isel.mpd14.probe.util.SneakyUtils.throwAsRTException;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;

public abstract class FormatUtils<T> implements BindMember<T>{
	public Object format(AnnotatedElement member, Object o){
		Format a = member.getAnnotation(Format.class);
        if(a != null){
            try {
				o = (T)a.formatter().newInstance().format(o);
			} catch (InstantiationException | IllegalAccessException e) {
	            throwAsRTException(e);
			}
        }
        return o;
	}

}
