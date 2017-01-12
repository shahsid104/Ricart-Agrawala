
public class Container {
	int id;
	Container(int id){
		this.id=id;
	}
	@Override
	public boolean equals(Object obj) {
		if(((Container)obj).id==id)
			return true;
		return false;
	}
}
