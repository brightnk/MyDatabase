import org.json.JSONObject;

public class MyJson implements Comparable<MyJson>{
	String data;
	static String typeCmp =  "String";
	static String fieldNameCmp;
	 
	public MyJson(String record){
		this.data = record;
	}
	
	
	

	@Override
	public int compareTo(MyJson o) {
		try{
			JSONObject myJson = new JSONObject(data);
			JSONObject oJson =  new JSONObject(o.data);
			
			switch(typeCmp){
			
			case "STRING": return myJson.getString(fieldNameCmp).compareTo(oJson.getString(fieldNameCmp));
				
			case "INT": return  myJson.getInt(fieldNameCmp) - oJson.getInt(fieldNameCmp);
			case "DOUBLE": if(myJson.getDouble(fieldNameCmp) - oJson.getDouble(fieldNameCmp)>0) return 1;
							else if(myJson.getDouble(fieldNameCmp) - oJson.getDouble(fieldNameCmp)<0) return -1;
							else return 0;
			
			default: return 0;	
			
			}
		
		
		}catch(Exception e){
			
			e.printStackTrace();
			return 0;
		}

}

}