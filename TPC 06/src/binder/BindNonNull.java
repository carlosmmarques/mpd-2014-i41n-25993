
package binder;

/**
 * The Method included in the following Class as copied from Miguel Gamboa's 
 * implementation of BindFieldNonNull
 */
public abstract class BindNonNull implements BindMember{

	private BindMember bindMember;
	
	public BindMember getBindMember() {
		return bindMember;
	}

	public void setBindMember(BindMember bindMember) {
		this.bindMember = bindMember;
	}

	/**
	 * bind - Default implementation - Acts on Non Null Values Only
	 * 
	 * @param target
	 * @param name
	 * @param v
	 * @return
	 */
    public <T> boolean bind(T target, String name, Object v) {
        if(v == null)
            return false;
        
        return bindMember.bind(target, name, v);

    }
    

}
