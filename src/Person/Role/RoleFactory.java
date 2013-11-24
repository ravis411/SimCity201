package Person.Role;

public class RoleFactory {

	public static Role roleFromString(String string) throws Exception{
		Class c = Class.forName(string);
		return (Role) c.newInstance();
	}
	
}
