package Person.Role;

public class RoleFactory {

	public static Role roleFromString(String string){
		Class c;
		try {
			c = Class.forName(string);
			return (Role) c.newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
}
