package onePoint1;

public class testTemp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int count = 0;
		int total = 10000;
		for(int i = 0; i < total; i++){
			
			 if((Math.random() * 10) > 9){
				 count++;
			 }
		}
		System.out.println(1.0*count/total);
	}

}
